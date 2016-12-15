package engine;

import java.util.Set;

/**
 * Represents the Langton Ant game.
 * @author Maciej Znamirowski
 * @see {@link GameOfLife}, {@link WireWorld}, {@link OneDimensionGame}
 */
public class LangtonAnt extends Automaton2Dim {
	/**
	 * @param cellStateFactory Factory used for initializing states of all cells in Langton Ant game.
	 * @param cellNeighborhood Neighborhood used for determining neighbors of cells.
	 * @param width Nubmer of horizontally located cells.
	 * @param height Number of vertically located cells.
	 * @see {@link GeneralStateFactory}, {@link UniformStateFactory}, {@link MooreNeighborhood}, {@link VonNeumanNeighborhood}
	 */
	public LangtonAnt(CellStateFactory cellStateFactory, CellNeighborhood cellNeighborhood, int width, int height) {
		super(cellStateFactory, cellNeighborhood, width, height);
	}
	
	@Override
	public String toString() {
		return "Langton Ant";
	}

	@Override
	protected Automaton newInstance(CellStateFactory cellStateFactory, CellNeighborhood cellNeighborhood) {
		return new LangtonAnt(cellStateFactory, cellNeighborhood, width, height);
	}
	
	@Override
	protected CellState nextCellState(Cell currentCell, Set<Cell> neighborsStates) {
		AntState newAntState = ((LangtonCell)currentCell.state).antState;
		BinaryState newCellState = ((LangtonCell)currentCell.state).cellState;
		
		if (newAntState != AntState.NONE) {
			newCellState = switchCellState(newCellState);
			newAntState = AntState.NONE;
		}
		
		for (Cell c :neighborsStates) {
			Coords2D myCoords = (Coords2D)currentCell.coords;
			Coords2D neighborsCoords = (Coords2D)c.coords;
			LangtonCell neighbor = (LangtonCell)c.state;
			
			if (isOnMyTop(myCoords, neighborsCoords) && isGoingToGoSouth(neighbor))
				return new LangtonCell(newCellState, AntState.SOUTH);
			else if (isOnMyBottom(myCoords, neighborsCoords) && isGoingToGoNorth(neighbor))
				return new LangtonCell(newCellState, AntState.NORTH);
			else if (isOnMyLeft(myCoords, neighborsCoords) && isGoingToGoEast(neighbor))
				return new LangtonCell(newCellState, AntState.EAST);
			else if (isOnMyRight(myCoords, neighborsCoords) && isGoingToGoWest(neighbor))
				return new LangtonCell(newCellState, AntState.WEST);
		}
		
		return new LangtonCell(newCellState, newAntState);
	}
	
	private BinaryState switchCellState(BinaryState cellState) {
		return cellState == BinaryState.ALIVE ? BinaryState.DEAD : BinaryState.ALIVE;
	}

	private boolean isGoingToGoWest(LangtonCell cell) {
		return ((cell.antState == AntState.NORTH) && (cell.cellState == BinaryState.ALIVE)) ||
				((cell.antState == AntState.SOUTH) && (cell.cellState == BinaryState.DEAD));
	}
	
	private boolean isGoingToGoEast(LangtonCell cell) {
		return ((cell.antState == AntState.NORTH) && (cell.cellState == BinaryState.DEAD)) ||
				((cell.antState == AntState.SOUTH) && (cell.cellState == BinaryState.ALIVE));
	}
	
	private boolean isGoingToGoSouth(LangtonCell cell) {
		return ((cell.antState == AntState.WEST) && (cell.cellState == BinaryState.ALIVE)) ||
				((cell.antState == AntState.EAST) && (cell.cellState == BinaryState.DEAD));
	}
	
	private boolean isGoingToGoNorth(LangtonCell cell) {
		return ((cell.antState == AntState.WEST) && (cell.cellState == BinaryState.DEAD)) ||
				((cell.antState == AntState.EAST) && (cell.cellState == BinaryState.ALIVE));
	}
	
	private boolean isOnMyRight(Coords2D myCoords, Coords2D neighborsCoords) {
		int x = (myCoords.x + 1 >= width) ? 0 : myCoords.x + 1;
		return (x == neighborsCoords.x) && (myCoords.y == neighborsCoords.y);
	}
	
	private boolean isOnMyLeft(Coords2D myCoords, Coords2D neighborsCoords) {
		int x = (myCoords.x - 1 < 0) ? width - 1 : myCoords.x - 1;
		return (x == neighborsCoords.x) && (myCoords.y == neighborsCoords.y);
	}
	
	private boolean isOnMyTop(Coords2D myCoords, Coords2D neighborsCoords) {
		int y = (myCoords.y - 1 < 0) ? height - 1 : myCoords.y - 1;
		return (myCoords.x == neighborsCoords.x) && (y == neighborsCoords.y);
	}
	
	private boolean isOnMyBottom(Coords2D myCoords, Coords2D neighborsCoords) {
		int y = (myCoords.y + 1 >= height) ? 0 : myCoords.y + 1;
		return (myCoords.x == neighborsCoords.x) && (y == neighborsCoords.y);
	}
}
