package engine;

import java.util.List;

/**
 * Manages the rules in Game of Life game.
 * @author Maciej Znamirowski
 */
public class GameOfLifeRules {
	private List<Integer> sustainingLife;
	private List<Integer> beginningLife;
	
	/**
	 * @param sustainingLife List of numbers of neighbors which will allow the cell to continue its life.
	 * @param beginningLife List of numbers of neighbors which will take the cell back to life.
	 */
	public GameOfLifeRules(List<Integer> sustainingLife, List<Integer> beginningLife) {
		this.sustainingLife = sustainingLife;
		this.beginningLife = beginningLife;
	}
	
	/**
	 * Specify if the cell with {@code aliveNeighborsCount} alive neighbors will stay alive due to the current rules.
	 * @param aliveNeighborsCount Number of livinig neigbor cells.
	 * @return True if alive cell will stay alive.
	 */
	public boolean willStayAlive(int aliveNeighborsCount) {
		return sustainingLife.contains(new Integer(aliveNeighborsCount));
	}
	
	/**
	 * Specify if the cell with {@code aliveNeighborsCount} alive neighbors will become alive due to the current rules.
	 * @param aliveNeighborsCount Number of livinig neigbor cells.
	 * @return True if dead cell will become alive.
	 */
	public boolean willStayDead(int aliveNeighborsCount) {
		return !beginningLife.contains(new Integer(aliveNeighborsCount));
	}
}
