																																																						package engine;

import java.util.Set;

/**
 * lalalSpecify which cells are neighbors of the given cell according to rules of Von Neuman Neighborhood.
 * @author Maciej
 *
 */
public interface CellNeighborhood {
	/**
	 * Determine neighbors of the given cell {@code cell}.
	 * @param cell The cell which neighbors will be determined.
	 * @return A set containing all neighbors of cell {@code cell}.
	 */
	Set<CellCoordinates> cellNeighbors(CellCoordinates cell);
	
	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);
}
