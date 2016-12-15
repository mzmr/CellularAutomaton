package engine;

/**
 * Factory which is creating only one specified kind of cell state.
 * @author Maciej Znamirowski
 */
public class UniformStateFactory implements CellStateFactory {
	private CellState state;
	
	/**
	 * @param state Cell state which will be produced by this factory.
	 */
	public UniformStateFactory(CellState state) {
		this.state = state;
	}
	
	@Override
	public CellState initialState(CellCoordinates cellCoordinates) {
		return state;
	}

}
