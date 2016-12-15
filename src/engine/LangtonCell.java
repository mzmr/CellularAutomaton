package engine;

/**
 * Represents the cell state in Langton Ant game.
 * @author Maciej Znamirowski
 */
public class LangtonCell implements CellState {
//	private static int counter = 0;

	/**
	 * The state of the cell. Can be dead or alive.
	 */
	public BinaryState cellState;
	
//	public int antId;
	
	/**
	 * The state of the ant. Can be absent or turned north, east, south or west.
	 */
	public AntState antState;
	
	/**
	 * @param cellState The state of the cell.
	 * @param antState The state of the ant on this cell.
	 */
	public LangtonCell(BinaryState cellState, AntState antState) {
		this.cellState = cellState;
		this.antState = antState;
	}
	
	@Override
	public String toString() {
		return cellState + " " + antState;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + antId;
		result = prime * result + ((antState == null) ? 0 : antState.hashCode());
		result = prime * result + ((cellState == null) ? 0 : cellState.hashCode());
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
		LangtonCell other = (LangtonCell) obj;
//		if (antId != other.antId)
//			return false;
		if (antState != other.antState)
			return false;
		if (cellState != other.cellState)
			return false;
		return true;
	}
}
