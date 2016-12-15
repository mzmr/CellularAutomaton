package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import engine.AntState;
import engine.BinaryState;
import engine.CellCoordinates;
import engine.CellState;
import engine.Coords1D;
import engine.Coords2D;
import engine.LangtonCell;
import engine.QuadState;
import engine.WireElectronState;

public class Board extends JPanel {
	private static final long serialVersionUID = 5869120770900201207L;
	
	private CellState[][] cells;
	private int xCells;
	private int yCells;
	private boolean isQuadState;
	
	private ColorManager colorManager;
	private Game currentGame;
	
	public Board(int widthInCells, int heightInCells, Game game, boolean isQuadState) {
		applyNewSettings(widthInCells, heightInCells, game, isQuadState);
	}
	
	public void applyNewSettings(int widthInCells, int heightInCells, Game game, boolean isQuadState) {
		colorManager = new ColorManager(game, isQuadState);
		currentGame = game;
		this.isQuadState = isQuadState;
		this.resizeAndCleanBoardSizeInCellsTo(widthInCells, heightInCells);
		repaint();
	}
	
	@Override 
	public void paintComponent(Graphics g) {
		Graphics2D canvas = (Graphics2D)g;
		
		canvas.setColor(new Color(240, 240, 240));
		canvas.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		for (int x = 0; x < xCells; x++) {
			for (int y = 0; y < yCells; y++) {
				Color fillColor = getColorOfCell(x,y);
				drawCell(canvas, x, y,fillColor);
			}
		}	
	}

	private Color getColorOfCell(int x, int y) {
		return colorManager.getColorOfCell(cells[x][y]);
	}

	private void drawCell(Graphics2D canvas, int x, int y, Color fillColor) {		
		int size = physicalCellSize();
		
		int physicalX = size * x;
		int physicalY = size * y;
		canvas.setColor(fillColor);
		canvas.fillRect(physicalX, physicalY, size, size);
		canvas.setColor(colorManager.getBorderColor());
		canvas.drawRect(physicalX, physicalY, size, size);
	}
	
	private CellState[][] resizeAndCleanBoardSizeInCellsTo(int xCells, int yCells) {
		this.xCells = xCells;
		this.yCells = yCells;
		
		CellState[][] oldCells = cells;
		cells = new CellState[xCells][yCells];
		
		CellState emptyState = getEmptyState();
		
		for(int i = 0; i < xCells; i++)
			for(int j = 0; j < yCells; j++)
				cells[i][j] = emptyState;
		
		return oldCells;
	}
	
	public void resizeBoardSizeInCellsTo(int xCells, int yCells) {
		CellState[][] oldCells = resizeAndCleanBoardSizeInCellsTo(xCells, yCells);
		int oldX = oldCells.length;
		int oldY = oldCells[0].length;
		
		int smallerX = oldX > xCells ? xCells : oldX;
		int smallerY = oldY > yCells ? yCells : oldY;
		
		for (int i = 0; i < smallerX; i++)
			for (int j = 0; j < smallerY; j++)
				cells[i][j] = oldCells[i][j];
	}
	
	private CellState getEmptyState() {
		switch(currentGame) {
		case ONE_DIM:
			return BinaryState.DEAD;
		case GAME_OF_LIFE:
			return isQuadState ? QuadState.DEAD : BinaryState.DEAD;
		case LANGTON_ANT:
			return new LangtonCell(BinaryState.DEAD, AntState.NONE);
		case WIRE_WORLD:
			return WireElectronState.VOID;
		default:
			throw new GUIException("Error");
		}
	}
	
	public void setCellTo(int xCell, int yCell, CellState state) {
		this.cells[xCell][yCell] = state;
	}
	
	public MouseListener createMouseListener(final Presenter p) {
		return new MouseAdapter() {
			@Override
		    public void mousePressed(MouseEvent event) {
				int logicalX = toLogicalXCell(event.getX());
				int logicalY = toLogicalYCell(event.getY());
				
				if (logicalX >= xCells || logicalY >= yCells)
					return;
				
				if (p.addingNewStructure) {
					p.addNewStructure(logicalX, logicalY);
					repaint();
					return;
				}
				
				CellState newState = nextCellState(logicalX,logicalY);
				CellCoordinates coords;
				
				if (currentGame == Game.ONE_DIM)
					coords = new Coords1D(logicalX);
				else
					coords = new Coords2D(logicalX, logicalY);

				p.changeAutomatCellState(coords, newState);
				repaint();
			}
		};
	}
	
	private CellState nextCellState(int logicalX, int logicalY) {
		CellState[] states = getAvailableStates();
		
		for (int i = 0; i < states.length; i++) {		
			if (states[i].equals(cells[logicalX][logicalY])) {
				cells[logicalX][logicalY] = (i + 1 == states.length) ? states[0] : states[i + 1];
				break;
			}
		}
		
		return cells[logicalX][logicalY];
	}
	
	private CellState[] getAvailableStates() {
		switch (currentGame) {
		case GAME_OF_LIFE:
			return isQuadState ? QuadState.values() : BinaryState.values();
		case LANGTON_ANT:
			return getLangtonValues();
		case WIRE_WORLD:
			return WireElectronState.values();
		case ONE_DIM:
			return BinaryState.values();
		default:
			throw new GUIException("This exception shouldn't really be thrown.");
		}
	}
	
	private LangtonCell[] getLangtonValues() {
		LangtonCell[] values = new LangtonCell[10];
		int i = 0;
		for (AntState state : AntState.values())
			values[i++] = new LangtonCell(BinaryState.DEAD, state);
		
		for (AntState state : AntState.values())
			values[i++] = new LangtonCell(BinaryState.ALIVE, state);
		
		return values;
	}
	
	private int toLogicalXCell(int x) {
		int physicalCellWidth = physicalCellSize();
		return x/physicalCellWidth;
	}

	private int physicalCellSize() {
		int cellWidth = this.getWidth() / xCells;
		int cellHeight = this.getHeight() / yCells;
		return Math.min(cellWidth, cellHeight);
	}
	
	private int toLogicalYCell(int y) {
		int physicalCellHeight = physicalCellSize();
		return y/physicalCellHeight;
	}
}
