package engine;

import java.util.HashSet;
import java.util.Set;

/**
 * Specify which cells are neighbors of the given cell according to rules of Von Neuman Neighborhood.
 * @author Maciej Znamirowski
 * @see {@link MooreNeighborhood}
 */
public class VonNeumanNeighborhood implements CellNeighborhood {
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
	public VonNeumanNeighborhood(int mapWidth, int mapHeight, int neighborhoodRadius, boolean isMapWrapped) {
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
		
		for (int s = 0; s < 2; s++) {
			int incrValue = (s == 0 ? -1 : 1);
			for (int x = coords.x, i = 0; ; x += incrValue, i++) {
				if (crossedNeighborhoodsBorder(x, minX, maxX))
					break;
				
				for (int y = minY + i; y <= maxY - i; y++)
					if (canAddCoords(x, y, coords))
						neighbors.add(new Coords2D(x, y));
			}
		}
		
		return neighbors;
	}
	
	private boolean crossedNeighborhoodsBorder(int x, int minX, int maxX) {
		return x < minX || x > maxX;
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
