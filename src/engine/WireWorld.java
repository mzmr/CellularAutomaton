package engine;

import java.util.Set;

/**
 * Represents the WireWorld game.
 * @author Maciej Znamirowski
 * @see {@link GameOfLife}, {@link LangtonAnt}, {@link OneDimensionGame}
 */
public class WireWorld extends Automaton2Dim {
	/**
	 * @param cellStateFactory Factory used for initializing states of all cells in Wire World game.
	 * @param cellNeighborhood Neighborhood used for determining neighbors of cells. 
	 * @param width Nubmer of horizontally located cells.
	 * @param height Number of vertically located cells.
	 * @see {@link GeneralStateFactory}, {@link UniformStateFactory}, {@link MooreNeighborhood}, {@link VonNeumanNeighborhood}
	 */
	public WireWorld(CellStateFactory cellStateFactory, CellNeighborhood cellNeighborhood, int width, int height) {
		super(cellStateFactory, cellNeighborhood, width, height);
	}
	
	@Override
	public String toString() {
		return "Wire World";
	}

	@Override
	protected Automaton newInstance(CellStateFactory cellStateFactory, CellNeighborhood cellNeighborhood) {
		return new WireWorld(cellStateFactory, cellNeighborhood, width, height);
	}
	
	@Override
	protected CellState nextCellState(Cell currentCell, Set<Cell> neighborsStates) {
		if (!(currentCell.state instanceof WireElectronState))
			throw new AutomatonException("WireWorld - nextCellState() - State is not an instance of WireElectronState");
		
		WireElectronState state = (WireElectronState)currentCell.state;
		
		if (state == WireElectronState.VOID)
			return WireElectronState.VOID;
		else if (state == WireElectronState.ELECTRON_HEAD)
			return WireElectronState.ELECTRON_TAIL;
		else if (state == WireElectronState.ELECTRON_TAIL)
			return WireElectronState.WIRE;
		else if (state == WireElectronState.WIRE) {
			int headsCount = countElectronHeads(neighborsStates);
			
			if (headsCount == 1 || headsCount == 2)
				return WireElectronState.ELECTRON_HEAD;
			else
				return WireElectronState.WIRE;
		}
		
		return currentCell.state;
	}
	
	private int countElectronHeads(Set<Cell> neighbors) {
		int counter = 0;
		
		for (Cell cell : neighbors) {
			WireElectronState state = (WireElectronState)cell.state;
			
			if (state == WireElectronState.ELECTRON_HEAD)
				counter++;
		}
		
		return counter;
	}
}
