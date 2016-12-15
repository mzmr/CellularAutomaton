package engine;

import static org.junit.Assert.assertArrayEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class LangtonAntTest {
	/*
		0 - DEAD
		1 - ALIVE
		00 - NONE
		10 - NORTH
		20 - EAST
		30 - SOUTH
		40 - WEST
		
		31 - SOUTH ALIVE
		1 - NONE
		20 - EAST DEAD
		...
	 */

	@Test
	public void nextStates1() {
		int[][] cellStates = {{0,  0, 1, 0,  1},
					 		  {1,  1, 0, 0,  1},
					 		  {0, 21, 0, 1,  0},
					 		  {1,  0, 0, 0, 10}};
					 
		int mapWidth = cellStates[0].length;
		int mapHeight = cellStates.length;
		Map<CellCoordinates, CellState> states = convertArrayToMap(cellStates);

		Automaton automaton = new LangtonAnt(new GeneralStateFactory(states),
											 new MooreNeighborhood(mapWidth, mapHeight, 1, true),
											 mapWidth,
											 mapHeight);
		
		int[][] correctStates = {{ 0,  0, 1, 0, 1},
								 { 1, 11, 0, 0, 1},
								 { 0,  0, 0, 1, 0},
								 {21,  0, 0, 0, 1}};
		
		int[][] generatedStates = convertMapToArray(automaton.nextState().cells, mapWidth, mapHeight);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	@Test
	public void nextStates2() {
		int[][] cellStates = {{40,  0,  1, 0,  1},
					 		  { 1,  1,  0, 0,  1},
					 		  { 0, 31, 30, 1,  0},
					 		  { 1,  0,  0, 0, 10}};
					 
		int mapWidth = cellStates[0].length;
		int mapHeight = cellStates.length;
		Map<CellCoordinates, CellState> states = convertArrayToMap(cellStates);

		Automaton automaton = new LangtonAnt(new GeneralStateFactory(states),
											 new MooreNeighborhood(mapWidth, mapHeight, 1, true),
											 mapWidth,
											 mapHeight);
		
		int[][] correctStates = {{ 1,  0,  1, 0, 1},
								 { 1,  1,  0, 0, 1},
								 { 0, 40, 21, 1, 0},
								 {11,  0,  0, 0, 1}};
		
		int[][] generatedStates = convertMapToArray(automaton.nextState().cells, mapWidth, mapHeight);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	@Test
	public void insertingStructure() {
		int[][] cellStates = {{40,  0,  1, 0,  1},
					 		  { 1,  1,  0, 0,  1},
					 		  { 0, 31, 30, 1,  0},
					 		  { 1,  0,  0, 0, 10}};
					 
		int mapWidth = cellStates[0].length;
		int mapHeight = cellStates.length;
		Map<CellCoordinates, CellState> states = convertArrayToMap(cellStates);

		int[][] structure = {{1, 21},
				 			 {0,  0},
				 			 {30, 1}};
		
		Automaton automaton = new LangtonAnt(new GeneralStateFactory(states),
											 new MooreNeighborhood(mapWidth, mapHeight, 1, true),
											 mapWidth,
											 mapHeight);
		
		automaton.insertStructure(convertArrayToMap(structure));
		
		int[][] correctStates = {{ 1, 21,  1, 0,  1},
	 			 				 { 0,  0,  0, 0,  1},
	 			 				 {30,  1, 30, 1,  0},
	 			 				 { 1,  0,  0, 0, 10}};
		
		int[][] generatedStates = convertMapToArray(automaton.cells, mapWidth, mapHeight);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	private Map<CellCoordinates, CellState> convertArrayToMap(int[][] cellStates) {
		int height = cellStates.length;
		int width = cellStates[0].length;
		Map<CellCoordinates, CellState> newStates = new HashMap<>(width*height);
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				BinaryState cellState = (cellStates[j][i] % 2 == 1) ? BinaryState.ALIVE : BinaryState.DEAD;
				AntState antState;
				
				switch((int)(cellStates[j][i] / 10)) {
				case 0:
					antState = AntState.NONE;
					break;
				case 1:
					antState = AntState.NORTH;
					break;
				case 2:
					antState = AntState.EAST;
					break;
				case 3:
					antState = AntState.SOUTH;
					break;
				case 4:
					antState = AntState.WEST;
					break;
				default:
					throw new AutomatonException("LangtonAntTest - convertArrayToMap() - Wrong number of cellState in array. (" + cellStates[j][i] + ")");
				}
				
				newStates.put(new Coords2D(i, j), new LangtonCell(cellState, antState));
			}
		}

		return newStates;
	}
	
	private int[][] convertMapToArray(Map<CellCoordinates, CellState> states, int width, int height) {
		int[][] newArray = new int[height][width];
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int state = 0;
				LangtonCell cell = (LangtonCell)states.get(new Coords2D(i, j));
				BinaryState cellState = cell.cellState;
				AntState antState = cell.antState;
				
				state += (cellState == BinaryState.ALIVE) ? 1 : 0;
				
				if (antState == AntState.NORTH)
					state += 10;
				else if (antState == AntState.EAST)
					state += 20;
				else if (antState == AntState.SOUTH)
					state += 30;
				else if (antState == AntState.WEST)
					state += 40;
				
				newArray[j][i] = state;
			}
		}
		
		return newArray;
	}
}
