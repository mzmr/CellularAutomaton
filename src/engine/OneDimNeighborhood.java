package engine;

import java.util.HashSet;
import java.util.Set;

/**
 * Specify which cells are neighbors of the given cell according to rules of One Dimension Game.
 * @author Maciej Znamirowski
 */
public class OneDimNeighborhood implements CellNeighborhood {
	private int mapSize;
	
	/**
	 * @param mapSize Number of cells in a row.
	 */
	public OneDimNeighborhood(int mapSize) {
		this.mapSize = mapSize;
	}
	
	@Override
	public Set<CellCoordinates> cellNeighbors(CellCoordinates cellCoords) {
		Set<CellCoordinates> neighbors = new HashSet<>();
		Coords1D coords = (Coords1D)cellCoords;
		int minX = coords.x - 1;
		int maxX = coords.x + 1;
		
		for (int x = minX; x <= maxX; x++)
			if (x >= 0 && x < mapSize)
				neighbors.add(new Coords1D(x));
		
		return neighbors;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mapSize;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OneDimNeighborhood other = (OneDimNeighborhood) obj;
		if (mapSize != other.mapSize)
			return false;
		return true;
	}
}
