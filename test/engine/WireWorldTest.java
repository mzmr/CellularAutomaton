package engine;

import static org.junit.Assert.assertArrayEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class WireWorldTest {
	/*
		0 - VOID
		1 - WIRE
		2 - ELECTRON_HEAD
		3 - ELECTRON_TAIL
	*/
	
	@Test
	public void nextStates1() {
		int[][] cellStates = {{0, 0, 0, 0, 2},
					 		  {3, 2, 0, 0, 3},
					 		  {0, 1, 0, 1, 0},
					 		  {0, 0, 1, 0, 0}};
					 
		int mapWidth = cellStates[0].length;
		int mapHeight = cellStates.length;
		Map<CellCoordinates, CellState> states = convertArrayToMap(cellStates);

		Automaton automaton = new WireWorld(new GeneralStateFactory(states),
											 new MooreNeighborhood(mapWidth, mapHeight, 1, false),
											 mapWidth,
											 mapHeight);
		
		int[][] correctStates = {{0, 0, 0, 0, 3},
		 		  				 {1, 3, 0, 0, 1},
		 		  				 {0, 2, 0, 1, 0},
		 		  				 {0, 0, 1, 0, 0}};
		
		int[][] generatedStates = convertMapToArray(automaton.nextState().cells, mapWidth, mapHeight);
		assertArrayEquals(correctStates, generatedStates);
	}
	 
	@Test
	public void nextStates2() {
		int[][] cellStates = {{0, 2, 0, 3, 0, 0},
				  			  {0, 3, 0, 2, 0, 0},
				  			  {2, 1, 0, 1, 0, 3},
				  			  {0, 1, 0, 2, 0, 0},
				  			  {0, 1, 0, 3, 0, 1}};
					 
		int mapWidth = cellStates[0].length;
		int mapHeight = cellStates.length;
		Map<CellCoordinates, CellState> states = convertArrayToMap(cellStates);

		Automaton automaton = new WireWorld(new GeneralStateFactory(states),
											 new MooreNeighborhood(mapWidth, mapHeight, 1, true),
											 mapWidth,
											 mapHeight);
		
		int[][] correctStates = {{0, 3, 0, 1, 0, 0},
	  			  				 {0, 1, 0, 3, 0, 0},
	  			  				 {3, 2, 0, 2, 0, 1},
	  			  				 {0, 2, 0, 3, 0, 0},
	  			  				 {0, 2, 0, 1, 0, 1}};
		
		int[][] generatedStates = convertMapToArray(automaton.nextState().cells, mapWidth, mapHeight);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	private Map<CellCoordinates, CellState> convertArrayToMap(int[][] cellStates) {
		int height = cellStates.length;
		int width = cellStates[0].length;
		Map<CellCoordinates, CellState> newStates = new HashMap<>(width*height);
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				WireElectronState cellState;;
				
				switch(cellStates[j][i]) {
				case 0:
					cellState = WireElectronState.VOID;
					break;
				case 1:
					cellState = WireElectronState.WIRE;
					break;
				case 2:
					cellState = WireElectronState.ELECTRON_HEAD;
					break;
				case 3:
					cellState = WireElectronState.ELECTRON_TAIL;
					break;
				default:
					throw new AutomatonException("WireWorldTest - convertArrayToMap() - Wrong number of cellState in array. (" + cellStates[j][i] + ")");
				}
				
				newStates.put(new Coords2D(i, j), cellState);
			}
		}

		return newStates;
	}
	
	private int[][] convertMapToArray(Map<CellCoordinates, CellState> states, int width, int height) {
		int[][] newArray = new int[height][width];
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int stateNumber;
				WireElectronState state = (WireElectronState)states.get(new Coords2D(i, j));
				
				switch(state) {
				case VOID:
					stateNumber = 0;
					break;
				case WIRE:
					stateNumber = 1;
					break;
				case ELECTRON_HEAD:
					stateNumber = 2;
					break;
				case ELECTRON_TAIL:
					stateNumber = 3;
					break;
				default:
					throw new AutomatonException("WireWorldTest - convertMapToArray() - Wrong cellState.");
				}
				
				newArray[j][i] = stateNumber;
			}
		}
		
		return newArray;
	}
}
