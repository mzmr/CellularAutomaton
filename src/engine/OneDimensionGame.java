package engine;

import java.util.Set;

/**
 * Represents the One Dimension Game game.
 * @author Maciej Znamirowski
 * @see {@link GameOfLife}, {@link LangtonAnt}, {@link WireWorld}
 */
public class OneDimensionGame extends Automaton1Dim {
	private OneDimensionRules rules;
	
	/**
	 * @param cellStateFactory Factory used for initializing states of all cells in One Dimension Game game.
	 * @param cellNeighborhood Neighborhood used for determining neighbors of cells.
	 * @param rules Rules specifying the process of the game simulation.
	 * @param size Number of cells.
	 */
	public OneDimensionGame(CellStateFactory cellStateFactory, CellNeighborhood cellNeighborhood, OneDimensionRules rules, int size) {
		super(cellStateFactory, cellNeighborhood, size);
		this.rules = rules;
	}
	
	@Override
	public String toString() {
		return "One Dimension Game";
	}

	@Override
	protected Automaton newInstance(CellStateFactory cellStateFactory, CellNeighborhood cellNeighborhood) {
		return new OneDimensionGame(cellStateFactory, cellNeighborhood, rules, size);
	}

	@Override
	protected CellState nextCellState(Cell currentCell, Set<Cell> neighbors) {	
		return rules.getState(((Coords1D)currentCell.coords).x, neighbors);
	}

}
