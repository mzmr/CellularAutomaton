package engine;

/**
 * All state factories have to implement this interface.
 * @author Maciej Znamirowski
 */
public interface CellStateFactory {
	/**
	 * Returns the initial state of cell with coordinates {@code cellCoordinates}.
	 * @param cellCoordinates Coordinates of the cell.
	 * @return Initial cell state.
	 */
	CellState initialState(CellCoordinates cellCoordinates);
}
