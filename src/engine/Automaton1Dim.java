package engine;

/**
 * The class representing one dimensional game.
 * @author Maciej Znamirowski
 */
public abstract class Automaton1Dim extends Automaton {
	protected int size;
	
	/**
	 * @param cellStateFactory Factory used for initializing states of all cells in Wire World game.
	 * @param cellNeighborhood Neighborhood used for determining neighbors of cells.
	 * @param size Number of cells.
	 */
	public Automaton1Dim(CellStateFactory cellStateFactory, CellNeighborhood cellNeighborhood, int size) {
		super(cellStateFactory, cellNeighborhood);
		this.size = size;
		super.initCells();
	}

	@Override
	protected boolean hasNextCoordinates(CellCoordinates cellCoordinates) {
		Coords1D coords = (Coords1D)cellCoordinates;
		return coords.x < size - 1;
	}

	@Override
	protected CellCoordinates initialCoordinates() {
		return new Coords1D(-1);
	}

	@Override
	protected CellCoordinates nextCoordinates(CellCoordinates cellCoordinates) {
		Coords1D coords = (Coords1D)cellCoordinates;
		return new Coords1D(coords.x + 1);
	}
}
