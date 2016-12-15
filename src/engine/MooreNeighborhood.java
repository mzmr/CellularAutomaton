package engine;

import java.util.HashSet;
import java.util.Set;

/**
 * Specify which cells are neighbors of the given cell according to rules of Moore Neighborhood.
 * @author Maciej Znamirowski
 * @see {@link VonNeumanNeighborhood}
 */
public class MooreNeighborhood implements CellNeighborhood {
	private int neighborhoodRadius;
	private boolean isMapWrapped;
	private int mapWidth;
	private int mapHeight;
	
	/**
	 * @param mapWidth Nubmer of horizontally located cells.
	 * @param mapHeight Nubmer of vertically located cells.
	 * @param neighborhoodRadius Specify how far from the given cell can be other to be considered a neighbor.
	 * @param isMapWrapped If true, right edge of the game map is connected with left, and bottom edge with top one.
	 */
	public MooreNeighborhood(int mapWidth, int mapHeight, int neighborhoodRadius, boolean isMapWrapped) {
		this.neighborhoodRadius = neighborhoodRadius;
		this.isMapWrapped = isMapWrapped;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
	}
	
	@Override
	public Set<CellCoordinates> cellNeighbors(CellCoordinates cellCoords) {
		Set<CellCoordinates> neighbors = new HashSet<>();
		Coords2D coords = (Coords2D)cellCoords;
		int minX = coords.x - neighborhoodRadius;
		int maxX = coords.x + neighborhoodRadius;
		int minY = coords.y - neighborhoodRadius;
		int maxY = coords.y + neighborhoodRadius;
		
		for (int x = minX; x <= maxX; x++)
			for (int y = minY; y <= maxY; y++)
				if (canAddCoords(x, y, coords)) {
					int newX = (mapWidth + x) % mapWidth;
					int newY = (mapHeight + y) % mapHeight;
					neighbors.add(new Coords2D(newX, newY));
				}
				
		return neighbors;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isMapWrapped ? 1231 : 1237);
		result = prime * result + mapHeight;
		result = prime * result + mapWidth;
		result = prime * result + neighborhoodRadius;
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
		MooreNeighborhood other = (MooreNeighborhood) obj;
		if (isMapWrapped != other.isMapWrapped)
			return false;
		if (mapHeight != other.mapHeight)
			return false;
		if (mapWidth != other.mapWidth)
			return false;
		if (neighborhoodRadius != other.neighborhoodRadius)
			return false;
		return true;
	}

	private boolean canAddCoords(int x, int y, Coords2D coords) {
		return isNotTheCenterCell(x, y, coords) && (isMapWrapped || doesntStickOutOfTheMap(x, y));
	}
	
	private boolean isNotTheCenterCell(int x, int y, Coords2D centerCoords) {
		return (x != centerCoords.x) || (y != centerCoords.y);
	}
	
	private boolean doesntStickOutOfTheMap(int x, int y) {
		return (x >= 0) && (x < mapWidth) && (y >= 0) && (y < mapHeight);
	}
}
