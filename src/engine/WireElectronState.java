package engine;

/**
 * Contains possible states of cell in Wire World game.
 * @author Maciej Znamirowski
 */
public enum WireElectronState implements CellState {
	VOID, WIRE, ELECTRON_HEAD, ELECTRON_TAIL;
}
