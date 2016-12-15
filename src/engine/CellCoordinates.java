package engine;

/**
 * Represents coordinates of the cell. Every cell coordinates class has to implement this interface.
 * @author Maciej Znamirowski
 */
public interface CellCoordinates {
	@Override
	public boolean equals(Object o);
	
	@Override
	public int hashCode();
}
