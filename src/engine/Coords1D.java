package engine;

/**
 * Represents cell coordinates in one dimension.
 * @author Maciej Znamirowski
 * @see {@link Coords2D}
 */
public class Coords1D implements CellCoordinates {
	/**
	 * Cell coordinate.
	 */
	public int x;

	/**
	 * @param x Value of the x coordinate.
	 */
	public Coords1D(int x) {
		this.x = x;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
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
		Coords1D other = (Coords1D) obj;
		if (x != other.x)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "<" + x + ">";
	}
}
