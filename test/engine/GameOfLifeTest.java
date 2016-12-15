			package engine;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class GameOfLifeTest {
	private int[][] cellStates;
	private Map<CellCoordinates, CellState> states;
	private int mapWidth;
	private int mapHeight;
	private boolean isQuadState;
	
	public void initBinaryStates() {
		cellStates = new int[][]{{0, 0, 1, 1},
			 					 {1, 0, 0, 0},
			 					 {0, 1, 0, 1},
			 					 {1, 0, 0, 0}};
			 					 
		mapWidth = cellStates[0].length;
		mapHeight = cellStates.length;
		isQuadState = false;
		
		states = convertArrayToMap(cellStates);
	}
	
	public void initQuadStates() {
		cellStates = new int[][]{{0, 0, 2, 1},
			 					 {3, 0, 0, 0},
			 					 {0, 4, 0, 4},
			 					 {3, 0, 0, 0}};
			 					 
		mapWidth = cellStates[0].length;
		mapHeight = cellStates.length;
		isQuadState = true;

		states = convertArrayToMap(cellStates);
	}

	@Test
	public void nextBinaryStates1() {
		initBinaryStates();
		int radius = 1;
		boolean wrappedMap = false;
		GameOfLifeRules rules = new GameOfLifeRules(Arrays.asList(2, 3), Arrays.asList(3));
		Map<CellCoordinates, CellState> aaa = new HashMap<>();
		aaa.put(new Coords2D(1,4), BinaryState.ALIVE);
		aaa.put(new Coords2D(1,3), BinaryState.DEAD);
		
		Automaton automaton = new GameOfLife(new GeneralStateFactory(aaa),
											 new MooreNeighborhood(mapWidth, mapHeight, radius, wrappedMap),
											 rules,
											 mapWidth,
											 mapHeight,
											 isQuadState);
		
		int[][] correctStates = {{0, 0, 0, 0},
								 {0, 1, 0, 1},
								 {1, 1, 0, 0},
								 {0, 0, 0, 0}};
		
		int[][] generatedStates = convertMapToArray(automaton.nextState().cells);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	@Test
	public void nextBinaryStates2() {
		initBinaryStates();
		int radius = 2;
		boolean wrappedMap = false;
		GameOfLifeRules rules = new GameOfLifeRules(Arrays.asList(2, 3), Arrays.asList(3));
		Automaton automaton = new GameOfLife(new GeneralStateFactory(states),
											 new MooreNeighborhood(mapWidth, mapHeight, radius, wrappedMap),
											 rules,
											 mapWidth,
											 mapHeight,
											 isQuadState);
		
		int[][] correctStates = {{1, 0, 0, 1},
								 {1, 0, 0, 0},
								 {0, 0, 0, 1},
								 {1, 0, 0, 0}};
		
		int[][] generatedStates = convertMapToArray(automaton.nextState().cells);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	@Test
	public void nextBinaryStates3() {
		initBinaryStates();
		int radius = 1;
		boolean wrappedMap = true;
		GameOfLifeRules rules = new GameOfLifeRules(Arrays.asList(2, 3), Arrays.asList(3));
		Automaton automaton = new GameOfLife(new GeneralStateFactory(states),
											 new MooreNeighborhood(mapWidth, mapHeight, radius, wrappedMap),
											 rules,
											 mapWidth,
											 mapHeight,
											 isQuadState);
		
		int[][] correctStates = {{1, 1, 0, 1},
								 {1, 1, 0, 0},
								 {0, 1, 0, 1},
								 {1, 1, 0, 0}};
		
		int[][] generatedStates = convertMapToArray(automaton.nextState().cells);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	@Test
	public void nextQuadStates1() {
		initQuadStates();
		int radius = 1;
		boolean wrappedMap = false;
		GameOfLifeRules rules = new GameOfLifeRules(Arrays.asList(2, 3), Arrays.asList(3));
		Automaton automaton = new GameOfLife(new GeneralStateFactory(states),
											 new MooreNeighborhood(mapWidth, mapHeight, radius, wrappedMap),
											 rules,
											 mapWidth,
											 mapHeight,
											 isQuadState);

		int[][] correctStates = {{0, 0, 0, 0},
								 {0, 1, 0, 3},
								 {3, 4, 0, 0},
								 {0, 0, 0, 0}};
		
		int[][] generatedStates = convertMapToArray(automaton.nextState().cells);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	@Test
	public void nextQuadStates2() {
		initQuadStates();
		int radius = 2;
		boolean wrappedMap = false;
		GameOfLifeRules rules = new GameOfLifeRules(Arrays.asList(2, 3), Arrays.asList(3));
		Automaton automaton = new GameOfLife(new GeneralStateFactory(states),
											 new MooreNeighborhood(mapWidth, mapHeight, radius, wrappedMap),
											 rules,
											 mapWidth,
											 mapHeight,
											 isQuadState);

		int[][] correctStates = {{1, 0, 0, 1},
				 				 {3, 0, 0, 0},
				 				 {0, 0, 0, 4},
				 				 {3, 0, 0, 0}};
		
		int[][] generatedStates = convertMapToArray(automaton.nextState().cells);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	@Test
	public void nextQuadStates3() {
		initQuadStates();
		int radius = 1;
		boolean wrappedMap = true;
		GameOfLifeRules rules = new GameOfLifeRules(Arrays.asList(2, 3), Arrays.asList(3));
		Automaton automaton = new GameOfLife(new GeneralStateFactory(states),
											 new MooreNeighborhood(mapWidth, mapHeight, radius, wrappedMap),
											 rules,
											 mapWidth,
											 mapHeight,
											 isQuadState);

		int[][] correctStates = {{3, 3, 0, 1},
								 {3, 1, 0, 0},
								 {0, 4, 0, 4},
								 {3, 1, 0, 0}};
		
		int[][] generatedStates = convertMapToArray(automaton.nextState().cells);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	@Test
	public void insertingBinaryStructure() {
		initBinaryStates();
		int radius = 1;
		boolean wrappedMap = true;
		GameOfLifeRules rules = new GameOfLifeRules(Arrays.asList(2, 3), Arrays.asList(3));
		int[][] structure = {{1, 1},
							 {1, 0},
							 {0, 1}};

		Automaton automaton = new GameOfLife(new GeneralStateFactory(states),
											 new MooreNeighborhood(mapWidth, mapHeight, radius, wrappedMap),
											 rules,
											 mapWidth,
											 mapHeight,
											 isQuadState);
		
		automaton.insertStructure(convertArrayToMap(structure));
		
		int[][] correctStates = {{1, 1, 1, 1},
								 {1, 0, 0, 0},
								 {0, 1, 0, 1},
								 {1, 0, 0, 0}};

		int[][] generatedStates = convertMapToArray(automaton.cells);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	@Test
	public void insertingQuadStructure() {
		initQuadStates();
		int radius = 2;
		boolean wrappedMap = false;
		GameOfLifeRules rules = new GameOfLifeRules(Arrays.asList(2, 3), Arrays.asList(3));
		int[][] structure = {{2, 2, 3},
							 {0, 4, 4}};

		Automaton automaton = new GameOfLife(new GeneralStateFactory(states),
											 new MooreNeighborhood(mapWidth, mapHeight, radius, wrappedMap),
											 rules,
											 mapWidth,
											 mapHeight,
											 isQuadState);
		
		automaton.insertStructure(convertArrayToMap(structure));

		int[][] correctStates = {{2, 2, 3, 1},
								 {0, 4, 4, 0},
								 {0, 4, 0, 4},
								 {3, 0, 0, 0}};

		int[][] generatedStates = convertMapToArray(automaton.cells);
		assertArrayEquals(correctStates, generatedStates);
	}
	
	private Map<CellCoordinates, CellState> convertArrayToMap(int[][] cellStates) {
		int height = cellStates.length;
		int width = cellStates[0].length;
		Map<CellCoordinates, CellState> states = new HashMap<>(width*height);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				CellState state;
				
				if (isQuadState) {
					switch (cellStates[i][j]) {
					case 0:
						state = QuadState.DEAD;
						break;
					case 1:
						state = QuadState.RED;
						break;
					case 2:
						state = QuadState.GREEN;
						break;
					case 3:
						state = QuadState.BLUE;
						break;
					case 4:
						state = QuadState.YELLOW;
						break;
					default:
						state = QuadState.DEAD;
					}
				} else
					state = cellStates[i][j] == 0 ? BinaryState.DEAD : BinaryState.ALIVE;
				states.put(new Coords2D(j, i), state);
			}
		}

		return states;
	}
	
	private int[][] convertMapToArray(Map<CellCoordinates, CellState> states) {
		int[][] array = new int[mapHeight][mapWidth];
		
		for (int i = 0; i < mapHeight; i++) {
			for (int j = 0; j < mapWidth; j++) {
				int state;
				CellState cellState = states.get(new Coords2D(j, i));
				
				if (isQuadState) {
					if (cellState == QuadState.DEAD)
						state = 0;
					else if (cellState == QuadState.RED)
						state = 1;
					else if (cellState == QuadState.GREEN)
						state = 2;
					else if (cellState == QuadState.BLUE)
						state = 3;
					else // YELLOW
						state = 4;
				} else
					state = cellState == BinaryState.DEAD ? 0 : 1;
				array[i][j] = state;
			}
		}
		
		return array;
	}
}
