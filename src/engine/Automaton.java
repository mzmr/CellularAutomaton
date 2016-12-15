package engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The base class for all games containing all base methods providing full functionability.
 * However, games shouldn't directly extend this class but through intermaediary classes, e.g. {@link Automaton1Dim}, {@link Automaton2Dim}.
 * @author Maciej Znamirowski
 */
public abstract class Automaton {
	Map<CellCoordinates, CellState> cells;
	private CellNeighborhood neighborsStrategy;
	private CellStateFactory stateFactory;
	
	/**
	 * @param cellStateFactory The factory used to initialize cells at the game start.
	 * @param cellNeighborhood The neighborhood strategy.
	 */
	public Automaton(CellStateFactory cellStateFactory, CellNeighborhood cellNeighborhood) {
		stateFactory = cellStateFactory;
		neighborsStrategy = cellNeighborhood;
	}
	
	/**
	 * Processes the Automaton object resultng in new state of the game map.
	 * @return New state of the game map.
	 */
	public Automaton nextState() {
		Automaton newAutomaton = newInstance(stateFactory, neighborsStrategy);
		newAutomaton.initCells();
		CellIterator oldCellIter = cellIterator();
		CellIterator newCellIter = newAutomaton.cellIterator();
		
		while (oldCellIter.hasNext()) {
			Cell cell = oldCellIter.next();
			Set<CellCoordinates> neighborsCoords = neighborsStrategy.cellNeighbors(cell.coords);
			Set<Cell> neighbors = mapCoordinates(neighborsCoords);
			CellState newState = nextCellState(cell, neighbors);
			newCellIter.next();
			newCellIter.setState(newState);
		}
		
		return newAutomaton;
	}
	
	/**
	 * Insert a {@code structure} structure into the game map.
	 * @param structure Structure to insert.
	 */
	public void insertStructure(Map<? extends CellCoordinates, ? extends CellState> structure) {
		cells.putAll(structure);
	}
	
	/**
	 * Creates a new instance of {@link CellIterator}.
	 * @return Created iterator.
	 */
	public CellIterator cellIterator() {
		return new CellIterator(initialCoordinates());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cells == null) ? 0 : cells.hashCode());
		result = prime * result + ((neighborsStrategy == null) ? 0 : neighborsStrategy.hashCode());
		result = prime * result + ((stateFactory == null) ? 0 : stateFactory.hashCode());
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
		Automaton other = (Automaton) obj;
		if (cells == null) {
			if (other.cells != null)
				return false;
		} else if (!cells.equals(other.cells))
			return false;
		if (neighborsStrategy == null) {
			if (other.neighborsStrategy != null)
				return false;
		} else if (!neighborsStrategy.equals(other.neighborsStrategy))
			return false;
		if (stateFactory == null) {
			if (other.stateFactory != null)
				return false;
		} else if (!stateFactory.equals(other.stateFactory))
			return false;
		return true;
	}
	
	protected abstract Automaton newInstance(CellStateFactory cellStateFactory, CellNeighborhood cellNeighborhood);
	protected abstract boolean hasNextCoordinates(CellCoordinates cellCoordinates);
	protected abstract CellCoordinates initialCoordinates();
	protected abstract CellCoordinates nextCoordinates(CellCoordinates cellCoordinates);
	protected abstract CellState nextCellState(Cell currentCell, Set<Cell> neighborsStates);
	
	private Set<Cell> mapCoordinates(Set<CellCoordinates> cellsCoordinates) {
		Set<Cell> groupOfCells = new HashSet<Cell>();
		
		for (CellCoordinates coords : cellsCoordinates) {
			CellState state = cells.get(coords);
			groupOfCells.add(new Cell(state, coords));
		}
		
		return groupOfCells;
	}
	
	protected void initCells() {
		cells = new HashMap<>();
		CellCoordinates coords = initialCoordinates();
		
		while (hasNextCoordinates(coords)) {
			coords = nextCoordinates(coords);
			CellState state = stateFactory.initialState(coords);
			cells.put(coords, state);
		}
	}
	
	/**
	 * This class is an interator, used for iterating through the cells in Automaton.
	 * @author Maciej Znamirowski
	 */
	public class CellIterator {
		private CellCoordinates currentCoords;
		
		/**
		 * @param initialCoordinates Coordinates from which the iterator will start iterating.
		 */
		public CellIterator(CellCoordinates initialCoordinates) {
			currentCoords = initialCoordinates;
		}
		
		/**
		 * Checks if there are next coordinates in the map.
		 * @return True if there are next coordinates, false otherwise.
		 */
		public boolean hasNext() {
			return hasNextCoordinates(currentCoords);
		}
		
		/**
		 * Iterates to the next cell on the map.
		 * @return Next cell read by the CellIterator.
		 */
		public Cell next() {
			currentCoords = nextCoordinates(currentCoords);
			CellState state = cells.get(currentCoords);
			return new Cell(state, currentCoords);
		}
		
		/**
		 * Sets new state for the cell beeing currently pointed by the CellIterator.
		 * @param cellState New cell state which has to be set.
		 */
		public void setState(CellState cellState) {
			cells.put(currentCoords, cellState);
		}
	}
}
