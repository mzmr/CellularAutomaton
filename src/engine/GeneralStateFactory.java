package engine;

import java.util.Map;

/**
 * Factory which is creating initial cell states according to the states given in GeneralStateFactory constructor.
 * @author Maciej Znamirowski
 */
public class GeneralStateFactory implements CellStateFactory {
	private Map<CellCoordinates, CellState> states;
	
	/**
	 * @param states States which will be used for initializing a game map.
	 */
	public GeneralStateFactory(Map<CellCoordinates, CellState> states) {
		this.states = states;
	}
	
	@Override
	public CellState initialState(CellCoordinates cellCoordinates) {
		return states.get(cellCoordinates);
	}
}
