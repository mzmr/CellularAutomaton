package engine;

import java.util.Set;

/**
 * Represents the Game of Life game.
 * @author Maciej Znamirowski
 * @see {@link WireWorld}, {@link LangtonAnt}, {@link OneDimensionGame}
 */
public class GameOfLife extends Automaton2Dim {
	private GameOfLifeRules rules;
	private boolean isQuadState;

	/**
	 * @param cellStateFactory Factory used for initializing states of all cells in Game of life game.
	 * @param cellNeighborhood Neighborhood used for determining neighbors of cells.
	 * @param rules
	 * @param width Nubmer of horizontally located cells.
	 * @param height Nubmer of vertically located cells.
	 * @param isQuadState If true, the game will use four different colours for alive cells states.
	 * @see {@link GeneralStateFactory}, {@link UniformStateFactory}, {@link MooreNeighborhood}, {@link VonNeumanNeighborhood}
	 */
	public GameOfLife(CellStateFactory cellStateFactory, CellNeighborhood cellNeighborhood, GameOfLifeRules rules, int width, int height, boolean isQuadState) {
		super(cellStateFactory, cellNeighborhood, width, height);
		this.rules = rules;
		this.isQuadState = isQuadState;
	}
	
	@Override
	public String toString() {
		return "Game of Life";
	}

	@Override
	protected Automaton newInstance(CellStateFactory cellStateFactory, CellNeighborhood cellNeighborhood) {
		return new GameOfLife(cellStateFactory, cellNeighborhood, rules, width, height, isQuadState);
	}
	
	@Override
	protected CellState nextCellState(Cell currentCell, Set<Cell> neighborsStates) {
		StateCounter states = countNeighborsStates(neighborsStates);
		return getNextStateOfCell(currentCell, states);
	}
	
	private StateCounter countNeighborsStates(Set<Cell> cells) {
		StateCounter counter = new StateCounter(isQuadState);
		
		for (Cell c : cells)
			counter.addState(c.state);
		
		return counter;
	}
	
	private CellState getNextStateOfCell(Cell currentCell, StateCounter neighborsStates) {
		if (isAlive(currentCell))
			return getNextStateOfLivingCell(currentCell, neighborsStates);
		else
			return getNextStateOfDeadCell(currentCell, neighborsStates);
	}
	
	private boolean isAlive(Cell cell) {
		return !(cell.state == QuadState.DEAD || cell.state == BinaryState.DEAD);
	}
	
	private CellState getNextStateOfLivingCell(Cell currentCell, StateCounter neighborsStates) {
		if (rules.willStayAlive(neighborsStates.getAliveCount()))
			return currentCell.state;
		else
			return isQuadState ? QuadState.DEAD : BinaryState.DEAD;
	}

	private CellState getNextStateOfDeadCell(Cell currentCell, StateCounter neighborsStates) {
		if (rules.willStayDead(neighborsStates.getAliveCount()))
			return currentCell.state;
		else {
			if (isQuadState) {
				Set<CellState> absent = neighborsStates.getAbsentAliveStates();
				if (absent.size() == 1)
					return absent.iterator().next();
				else
					return neighborsStates.getMostPlentiful();
			} else {
				return BinaryState.ALIVE;
			}
		}
	}
}
