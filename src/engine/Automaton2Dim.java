package engine;

/**
 * The class representing two dimensional game.
 * @author Maciej Znamirowski
 */
public abstract class Automaton2Dim extends Automaton {
	protected int width;
	protected int height;

	/**
	 * 
	 * @param cellStateFactory Factory used for initializing states of all cells in game.
	 * @param cellNeighborhood Neighborhood used for determining neighbors of cells. 
	 * @param width Nubmer of horizontally located cells.
	 * @param height Number of vertically located cells.
	 */
	public Automaton2Dim(CellStateFactory cellStateFactory, CellNeighborhood cellNeighborhood, int width, int height) {
		super(cellStateFactory, cellNeighborhood);
		this.width = width;
		this.height = height;
		super.initCells(); //TODO: nieelegancko - to powinno byæ gdzieœ w Automatonie, ale nie mo¿e byæ w konstruktorze, bo trzeba wczeœniej ustawiæ wymiary mapy
	}

	@Override
	protected boolean hasNextCoordinates(CellCoordinates cellCoordinates) {
		Coords2D coords = (Coords2D)cellCoordinates;
		return (coords.x < width - 1) || (coords.y < height - 1);
	}

	@Override
	protected CellCoordinates initialCoordinates() {
		return new Coords2D(-1, 0);
	}

	@Override
	protected CellCoordinates nextCoordinates(CellCoordinates cellCoordinates) {
		Coords2D coords = (Coords2D)cellCoordinates;

		if (coords.x == -1)
			return new Coords2D(0, 0);
		
		int newX = (coords.x + 1) % width;
		int newY = newX == 0 ? coords.y + 1 : coords.y;
		
		return new Coords2D(newX, newY);
	}
}
