package engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Manages the rules in {@link OneDimensionGame} and cooperates with it.
 * @author Maciej Znamirowski
 */
public class OneDimensionRules {
	private Map<Integer, BinaryState> rules;
	
	/**
	 * @param ruleValue The value of the rule. Possible values are integers from 0 to 255.
	 */
	public OneDimensionRules(int ruleValue) {
		rules = new HashMap<>(8);
		
		for (int i = 0; i < 8; i++) {
			int bit = extractNthBit(ruleValue, i);
			rules.put(i, intToState(bit));
		}
	}
	
	/**
	 * Determines the next state of the cell with {@code centerX} coordinates and neighbors {@code neighbors}.
	 * @param centerX Coordinates of the cell.
	 * @param neighbors Neighbors of the cell.
	 * @return The {@link BinaryState} of the cell in next iteration of the game.
	 */
	public BinaryState getState(int centerX, Set<Cell> neighbors) {
		int cellNumber = 0;
		
		for (Cell cell : neighbors) {
			if (cell.state != BinaryState.ALIVE)
				continue;
			
			int cellX = ((Coords1D)cell.coords).x;
			
			if (cellX == centerX - 1)
				cellNumber += 4;
			else if (cellX == centerX)
				cellNumber += 2;
			else if (cellX == centerX + 1)
				cellNumber += 1;
		}

		return rules.get(cellNumber);
	}
	
	private int extractNthBit(int number, int n) {
		return (number >> n) & 1;
	}
	
	private BinaryState intToState(int value) {
		return value == 0 ? BinaryState.DEAD : BinaryState.ALIVE;
	}
}
