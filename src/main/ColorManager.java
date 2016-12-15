package main;

import java.awt.Color;

import engine.AntState;
import engine.BinaryState;
import engine.CellState;
import engine.LangtonCell;
import engine.QuadState;
import engine.WireElectronState;

public class ColorManager {
	private Game currentGame;
	private boolean isQuadState;
	
	private Color borderColor = new Color(235, 235, 235);
	
	private Color binaryAliveColor = new Color(110, 110, 110);
	private Color binaryDeadColor = new Color(255, 255, 255);
	
	private Color quadRedColor = new Color(255, 71, 51);
	private Color quadGreenColor = new Color(121, 227, 0);
	private Color quadBlueColor = new Color(89, 77, 250);
	private Color quadYellowColor = new Color(255, 241, 51);
	private Color quadDeadColor = new Color(255, 255, 255);
	
	private Color wireBorderColor = new Color(128, 128, 128);
	private Color wireWireColor = new Color(255, 241, 51);
	private Color wireHeadColor = new Color(89, 77, 250);
	private Color wireTailColor = new Color(255, 71, 51);
	private Color wireVoidColor = new Color(110, 110, 110);
	
	private Color langtonAntColor = new Color(255, 71, 51);
	
	public ColorManager(Game game, boolean isQuadState) {
		currentGame = game;
		this.isQuadState = isQuadState;
	}
	
	public Color getColorOfCell(CellState state) {
		switch(currentGame) {
		case ONE_DIM:
		case GAME_OF_LIFE:
			return getGameOfLifeCellColor(state);
		case LANGTON_ANT:
			return getLangtonAntCellColor(state);
		case WIRE_WORLD:
			return getWireWorldCellColor(state);
		default:
			throw new GUIException("Error");
		}
	}
	
	private Color getGameOfLifeCellColor(CellState state) {
		if (isQuadState)
			return getGOLQuadCellColor(state);
		else
			return getGOLBinaryCellColor(state);
	}
	
	private Color getGOLBinaryCellColor(CellState state) {
		BinaryState s = (BinaryState)state;
		return s == BinaryState.ALIVE ? binaryAliveColor : binaryDeadColor;
	}
	
	private Color getGOLQuadCellColor(CellState state) {
		QuadState s = (QuadState)state;

		switch(s) {
		case BLUE:
			return quadBlueColor;
		case GREEN:
			return quadGreenColor;
		case RED:
			return quadRedColor;
		case YELLOW:
			return quadYellowColor;
		case DEAD:
			return quadDeadColor;
		default:
			throw new GUIException("Error");
		}
	}

	private Color getLangtonAntCellColor(CellState state) {
		LangtonCell lc = (LangtonCell)state;
		
		if (lc.antState != AntState.NONE)
			return langtonAntColor;
		
		return (lc.cellState == BinaryState.ALIVE) ? binaryAliveColor : binaryDeadColor;
	}

	private Color getWireWorldCellColor(CellState state) {
		WireElectronState s = (WireElectronState)state;
		
		switch(s) {
		case ELECTRON_HEAD:
			return wireHeadColor;
		case ELECTRON_TAIL:
			return wireTailColor;
		case WIRE:
			return wireWireColor;
		case VOID:
			return wireVoidColor;
		default:
			throw new GUIException("Error");
		}
	}

	public Color getBorderColor() {
		return (currentGame == Game.WIRE_WORLD) ? wireBorderColor : borderColor;
	}
}
