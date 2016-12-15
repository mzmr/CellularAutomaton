package engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The helper class used for counting states of the neighbors of the specyfied cell.
 * @author Maciej Znamirowski
 */
public class StateCounter {
	private Map<CellState, Integer> statesCount;
	private boolean isQuadState;
	
	/**
	 * @param isQuadState If true, the StateCounter will be prepared for working with 
	 * {@link QuadState} states of cells, otherwise with {@link BinaryState} states.
	 */
	public StateCounter(boolean isQuadState) {
		statesCount = new HashMap<>();
		this.isQuadState = isQuadState;
	}
	
	/**
	 * Adds another state to the StateCounter.
	 * @param state The cell state which has to be added.
	 */
	public void addState(CellState state) {
		statesCount.put(state, statesCount.getOrDefault(state, 0) + 1);
	}
	
	/**
	 * Computes the number of alive cells saved in counter.
	 * @return Number of alive cells.
	 */
	public int getAliveCount() {
		if (statesCount.isEmpty())
			return 0;

		int aliveCount = 0;
		
		if (isQuadState) {
			aliveCount += statesCount.getOrDefault(QuadState.BLUE, 0);
			aliveCount += statesCount.getOrDefault(QuadState.GREEN, 0);
			aliveCount += statesCount.getOrDefault(QuadState.RED, 0);
			aliveCount += statesCount.getOrDefault(QuadState.YELLOW, 0);
		} else
			aliveCount = statesCount.getOrDefault(BinaryState.ALIVE, 0);
		
		return aliveCount;	
	}
	
	/** 
	 * @return The most plentiful alive cell state.
	 */
	public CellState getMostPlentiful() {
		CellState mostPlentiful = null;
		
		if (isQuadState) {
			int biggestCount = -1;
			
			for (CellState s : QuadState.values()) {
				if (s == QuadState.DEAD)
					continue;
				
				int count = statesCount.getOrDefault(s, 0);
				if (count > biggestCount) {
					mostPlentiful = s;
					biggestCount = count;
				}
			}
			
		} else {
			mostPlentiful = BinaryState.ALIVE;
		}
		
		if (mostPlentiful == null)
			throw new AutomatonException("GameOfLife - StateCounter - getMostPlentiful - It should never be null.");
		
		return mostPlentiful;
	}
	
	/**
	 * @return The set of cell states that haven't been added to the StateCounter.
	 */
	public Set<CellState> getAbsentAliveStates() {
		if (!isQuadState)
			throw new AutomatonException("GameOfLife - StateCounter - getAbsentAliveStates - You can't call this method until you are in a QuadLife mode.");
		
		Set<CellState> absent = new HashSet<>();
		for(CellState s : QuadState.values())
			if (s != QuadState.DEAD && !statesCount.containsKey(s))
				absent.add(s);
		
		return absent;
	}
}
