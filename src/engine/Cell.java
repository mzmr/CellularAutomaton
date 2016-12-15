package engine;

/**
 * Represents a cell.
 * <p>
 * Consist of state and coordinates of the cell. This class is used in variety of cellural games.
 * @author Maciej Znamirowski
 */
public class Cell {
	/**
	 * The state of the cell.
	 */
	public CellState state;
	
	/**
	 * The coordinates of the cell.
	 */
	public CellCoordinates coords;
	
	/**
	 * @param state State of the created cell.
	 * @param coords Coordinates of the created cell.
	 */
	public Cell(CellState state, CellCoordinates coords) {
		this.state = state;
		this.coords = coords;
	}
	
	@Override
	public String toString() {
		return "{" + state + " - " + coords + "}";
	}
}
