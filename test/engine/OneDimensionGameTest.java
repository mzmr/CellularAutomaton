package engine;

import static org.junit.Assert.assertArrayEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OneDimensionGameTest {
	@Test
	public void nextStates1() {
		int[] cellStates = {0, 0, 0, 1, 0, 0, 0, 0};
					 
		int mapSize = cellStates.length;
		Map<CellCoordinates, CellState> states = convertArrayToMap(cellStates);
		OneDimensionRules rules = new OneDimensionRules(30);

		Automaton automaton = new OneDimensionGame(new GeneralStateFactory(states),
												   new OneDimNeighborhood(mapSize),
												   rules,
												   mapSize);
		
		int[] correctStates = {0, 0, 1, 1, 1, 0, 0, 0};
		
		int[] generatedStates = convertMapToArray(automaton.nextState().cells, mapSize);
		assertArrayEquals(correctStates, generatedStates);
	}

	@Test
	public void nextStates2() {
		int[] cellStates = {0, 0, 1, 1, 1, 0, 0, 0};
					 
		int mapSize = cellStates.length;
		Map<CellCoordinates, CellState> states = convertArrayToMap(cellStates);
		OneDimensionRules rules = new OneDimensionRules(30);

		Automaton automaton = new OneDimensionGame(new GeneralStateFactory(states),
												   new OneDimNeighborhood(mapSize),
												   rules,
												   mapSize);
		
		int[] correctStates = {0, 1, 1, 0, 0, 1, 0, 0};
		
		int[] generatedStates = convertMapToArray(automaton.nextState().cells, mapSize);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	@Test
	public void nextStates3() {
		int[] cellStates = {0, 0, 0, 0, 0, 0, 1, 0};
					 
		int mapSize = cellStates.length;
		Map<CellCoordinates, CellState> states = convertArrayToMap(cellStates);
		OneDimensionRules rules = new OneDimensionRules(110);

		Automaton automaton = new OneDimensionGame(new GeneralStateFactory(states),
												   new OneDimNeighborhood(mapSize),
												   rules,
												   mapSize);
		
		int[] correctStates = {0, 0, 0, 0, 0, 1, 1, 0};
		
		int[] generatedStates = convertMapToArray(automaton.nextState().cells, mapSize);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	@Test
	public void nextStates4() {
		int[] cellStates = {0, 1, 1, 0, 0, 0, 1, 0};
					 
		int mapSize = cellStates.length;
		Map<CellCoordinates, CellState> states = convertArrayToMap(cellStates);
		OneDimensionRules rules = new OneDimensionRules(110);

		Automaton automaton = new OneDimensionGame(new GeneralStateFactory(states),
												   new OneDimNeighborhood(mapSize),
												   rules,
												   mapSize);
		
		int[] correctStates = {1, 1, 1, 0, 0, 1, 1, 0};
		
		int[] generatedStates = convertMapToArray(automaton.nextState().cells, mapSize);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	private Map<CellCoordinates, CellState> convertArrayToMap(int[] cellStates) {
		int size = cellStates.length;
		Map<CellCoordinates, CellState> newStates = new HashMap<>(size);
		
		for (int i = 0; i < size; i++) {
			BinaryState cellState;;
			
			switch(cellStates[i]) {
			case 0:
				cellState = BinaryState.DEAD;
				break;
			case 1:
				cellState = BinaryState.ALIVE;
				break;
			default:
				throw new AutomatonException("OneDimensionTest - convertArrayToMap() - Wrong number of cellState in array. (" + cellStates[i] + ")");
			}
			
			newStates.put(new Coords1D(i), cellState);
		}

		return newStates;
	}
	
	private int[] convertMapToArray(Map<CellCoordinates, CellState> states, int size) {
		int[] newArray = new int[size];

		for (int i = 0; i < size; i++) {
			int stateNumber;
			BinaryState state = (BinaryState)states.get(new Coords1D(i));
			
			switch(state) {
			case DEAD:
				stateNumber = 0;
				break;
			case ALIVE:
				stateNumber = 1;
				break;
			default:
				throw new AutomatonException("OneDimesionGameTest - convertMapToArray() - Wrong cellState.");
			}
			
			newArray[i] = stateNumber;

		}
		
		return newArray;
	}
}
