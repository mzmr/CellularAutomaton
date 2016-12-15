package main;

import java.util.HashMap;
import java.util.Map;

import engine.Automaton;
import engine.Cell;
import engine.CellCoordinates;
import engine.CellState;
import engine.Coords1D;
import engine.Coords2D;
import engine.InitialStructure;

public class Presenter {
	
	private MainWindow view;
	private Automaton currentAutomaton;
	private int step;
	private int mapWidth;
	private int mapHeight;
	protected boolean addingNewStructure;
	
	public Presenter(MainWindow view, Automaton automaton, int mapWidth, int mapHeight) {
		this.view = view;
		currentAutomaton = automaton;
		step = 0;
		this.mapHeight = mapHeight;
		this.mapWidth = mapWidth;
		addingNewStructure = false;
	}

	public void nextClicked() {
		if (step >= mapHeight)
			return;
		
		currentAutomaton = currentAutomaton.nextState();
		updateView();
	}
	
	public void changeAutomatCellState(CellCoordinates coords, CellState state) {
		Map<CellCoordinates, CellState> cell = new HashMap<>(1);
		cell.put(coords, state);
		currentAutomaton.insertStructure(cell);
		
		view.setNewCellText(state);
	}

	public void updateView() {
		Automaton.CellIterator iter = currentAutomaton.cellIterator();
		boolean isOneDim = view.settings.gameType == Game.ONE_DIM;
		
		while(iter.hasNext()) {
			Cell c = iter.next();
			int coordY = isOneDim ? step : ((Coords2D)c.coords).y;
			int coordX = isOneDim ? ((Coords1D)c.coords).x : ((Coords2D)c.coords).x;
			view.changeCell(coordX, coordY, c.state);
		}
		
		if (isOneDim)
			step++;
		
		view.resizeBoardSizeInCellsTo(mapWidth, mapHeight);
	}

	public void addNewStructure(int logicalX, int logicalY) {
		Map<CellCoordinates, CellState> baseStruct = InitialStructure.getStructure(view.getNewStructureType());
		Map<CellCoordinates, CellState> preparedStruct = new HashMap<>();
		
		for (Map.Entry<CellCoordinates, CellState> entry : baseStruct.entrySet()) {
			Coords2D baseCoords = (Coords2D)entry.getKey();
			CellState state = entry.getValue();
			int newX = baseCoords.x + logicalX;
			int newY = baseCoords.y + logicalY;
			
			if (newX >= mapWidth || newY >= mapHeight)
				break;
			
		    CellCoordinates preparedCoords = new Coords2D(newX, newY);
		    preparedStruct.put(preparedCoords, state);
		}
		
		currentAutomaton.insertStructure(preparedStruct);
		
		setAddingNewStructure(false);
		updateView();
	}

	public void setAddingNewStructure(boolean adding) {
		addingNewStructure = adding;
	}

	public boolean getAddingNewStructure() {
		return addingNewStructure;
	}
}
