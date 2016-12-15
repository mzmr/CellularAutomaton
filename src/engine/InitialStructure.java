package engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class is responsible for creating new structures for games {@link GameOfLife} and {@link WireWorld}.
 * @author Maciej Znamirowski
 */
public class InitialStructure {
	public interface Structure {}
	
	public enum GOLStructure implements Structure {
		STILL, OSCILLATOR, SPACESHIP, GUN;
		
		public enum Still implements Structure {
			BLOCK, BEEHIVE, LOAF, BOAT
		}
		
		public enum Oscillator implements Structure {
			BLINKER, TOAD, BEACON, PULSAR, PENTADECATHLON
		}
		
		public enum Spaceship implements Structure {
			GLIDER, LIGHTWEIGHT
		}
		
		public enum Gun implements Structure {
			GOSPER_GLIDER_GUN
		}
	}
	
	public enum WWStructure implements Structure {
		CLOCK, GATE, OTHER;
		
		public enum Clock implements Structure {
			FAST1T, MEDIUM2T, SLOW4T
		}
		
		public enum Gate implements Structure {
			NOT, OR, XOR, AND
		}
		
		public enum Other implements Structure {
			DIODE, REFLECTOR
		}
	}
	
	public static boolean isQuadState = false;
	
	public static Map<CellCoordinates, CellState> getStructure(Structure s) {
		if (s.equals(GOLStructure.Still.BLOCK))
			return Still.block();
		if (s.equals(GOLStructure.Still.BEEHIVE))
			return Still.beehive();
		if (s.equals(GOLStructure.Still.LOAF))
			return Still.loaf();
		if (s.equals(GOLStructure.Still.BOAT))
			return Still.boat();
		
		if (s.equals(GOLStructure.Oscillator.BLINKER))
			return Oscillator.blinker();
		if (s.equals(GOLStructure.Oscillator.TOAD))
			return Oscillator.toad();
		if (s.equals(GOLStructure.Oscillator.BEACON))
			return Oscillator.beacon();
		if (s.equals(GOLStructure.Oscillator.PULSAR))
			return Oscillator.pulsar();
		if (s.equals(GOLStructure.Oscillator.PENTADECATHLON))
			return Oscillator.pentadecathlon();
		
		if (s.equals(GOLStructure.Spaceship.GLIDER))
			return SpaceShip.glider();
		if (s.equals(GOLStructure.Spaceship.LIGHTWEIGHT))
			return SpaceShip.lightweight();
		
		if (s.equals(GOLStructure.Gun.GOSPER_GLIDER_GUN))
			return Gun.gosperGliderGun();
		
		if (s.equals(WWStructure.Clock.FAST1T))
			return Clock.fast1T();
		if (s.equals(WWStructure.Clock.MEDIUM2T))
			return Clock.medium2T();
		if (s.equals(WWStructure.Clock.SLOW4T))
			return Clock.slow4T();
		
		if (s.equals(WWStructure.Gate.NOT))
			return Gate.not();
		if (s.equals(WWStructure.Gate.OR))
			return Gate.or();
		if (s.equals(WWStructure.Gate.XOR))
			return Gate.xor();
		if (s.equals(WWStructure.Gate.AND))
			return Gate.and();
		
		if (s.equals(WWStructure.Other.DIODE))
			return Other.diode();
		if (s.equals(WWStructure.Other.REFLECTOR))
			return Other.reflector();
		
		throw new AutomatonException("You're looking for some really rare structure we haven't got here. :(");
	}
	
	private static Map<CellCoordinates, CellState> convertArrayToMap(int[][] cellStates) {
		int height = cellStates.length;
		int width = cellStates[0].length;
		Map<CellCoordinates, CellState> states = new HashMap<>(width*height);
		
		CellState dead = getDeadState();
		CellState alive = null;
		CellState[] aliveStates = getAliveStates();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				alive = getAliveState(aliveStates);
				CellState state = cellStates[i][j] == 0 ? dead : alive;
				states.put(new Coords2D(j, i), state);
			}
		}
		
		return states;
	}
	
	private static Map<CellCoordinates, CellState> convertArrayToWireMap(int[][] cellStates) {
		int height = cellStates.length;
		int width = cellStates[0].length;
		Map<CellCoordinates, CellState> states = new HashMap<>(width*height);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				CellState state = null;
				
				switch(cellStates[i][j]) {
				case 0:
					state = WireElectronState.VOID;
					break;
				case 1:
					state = WireElectronState.WIRE;
					break;
				case 2:
					state = WireElectronState.ELECTRON_HEAD;
					break;
				case 3:
					state = WireElectronState.ELECTRON_TAIL;
					break;
				default:
					throw new AutomatonException("The value of cell should be in range 0-3.");
				}
				
				states.put(new Coords2D(j, i), state);
			}
		}
		
		return states;
	}
	
	private static CellState getDeadState() {
		return isQuadState ? QuadState.DEAD : BinaryState.DEAD;
	}
	
	private static CellState getAliveState(CellState[] aliveStates) {
		return isQuadState ? aliveStates[new Random().nextInt(aliveStates.length)] : BinaryState.ALIVE;
	}
	
	private static CellState[] getAliveStates() {
		if (isQuadState) {
			CellState[] aliveStates;
			
			aliveStates = new CellState[QuadState.values().length];
			int i = 0;
			for (CellState s : QuadState.values()) {
				if (s == QuadState.DEAD)
					continue;
				aliveStates[i++] = s;
			}
		}
		
		return null;
	}
	
	private static class Still {
		public static Map<CellCoordinates, CellState> block() {
			int[][] block = {{1, 1},
							 {1, 1}};
			return convertArrayToMap(block);
		}
		
		public static Map<CellCoordinates, CellState> beehive() {
			int[][] beehive = {{0, 1, 1, 0},
							   {1, 0, 0, 1},
							   {0, 1, 1, 0}};
			return convertArrayToMap(beehive);
		}
		
		public static Map<CellCoordinates, CellState> loaf() {
			int[][] loaf = {{0, 1, 1, 0},
							{1, 0, 0, 1},
							{0, 1, 0, 1},
							{0, 0, 1, 0}};
			return convertArrayToMap(loaf);
		}
		
		public static Map<CellCoordinates, CellState> boat() {
			int[][] boat = {{1, 1, 0},
							{1, 0, 1},
							{0, 1, 0}};
			return convertArrayToMap(boat);
		}
	}
	
	private static class Oscillator {
		public static Map<CellCoordinates, CellState> blinker() {
			int[][] blinker = {{1, 1, 1}};
			return convertArrayToMap(blinker);
		}
		
		public static Map<CellCoordinates, CellState> toad() {
			int[][] toad = {{0, 0, 1, 0},
							{1, 0, 0, 1},
							{1, 0, 0, 1},
							{0, 1, 0, 0}};
			return convertArrayToMap(toad);
		}
		
		public static Map<CellCoordinates, CellState> beacon() {
			int[][] beacon = {{1, 1, 0, 0},
							  {1, 1, 0, 0},
							  {0, 0, 1, 1},
							  {0, 0, 1, 1}};
			return convertArrayToMap(beacon);
		}
		
		public static Map<CellCoordinates, CellState> pulsar() {
			int[][] pulsar = {{0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0},
							  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
							  {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1},
							  {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1},
							  {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1},
							  {0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0},
							  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
							  {0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0},
							  {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1},
							  {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1},
							  {1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1},
							  {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
							  {0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0}};
			return convertArrayToMap(pulsar);
		}
		
		public static Map<CellCoordinates, CellState> pentadecathlon() {
			int[][] pentadecathlon = {{0, 1, 1, 1, 0},
									  {1, 0, 0, 0, 1},
									  {1, 0, 0, 0, 1},
									  {0, 1, 1, 1, 0},
									  {0, 0, 0, 0, 0},
									  {0, 0, 0, 0, 0},
									  {0, 0, 0, 0, 0},
									  {0, 0, 0, 0, 0},
									  {0, 1, 1, 1, 0},
									  {1, 0, 0, 0, 1},
									  {1, 0, 0, 0, 1},
									  {0, 1, 1, 1, 0}};
			return convertArrayToMap(pentadecathlon);
		}
	}
	
	private static class SpaceShip {
		public static Map<CellCoordinates, CellState> glider() {
			int[][] glider = {{0, 0, 1},
							  {1, 0, 1},
							  {0, 1, 1}};
			return convertArrayToMap(glider);
		}
		
		public static Map<CellCoordinates, CellState> lightweight() {
			int[][] lightweight = {{0, 1, 1, 0, 0},
								   {1, 1, 1, 1, 0},
								   {1, 1, 0, 1, 1},
								   {0, 0, 1, 1, 0}};
			return convertArrayToMap(lightweight);
		}
	}
	
	private static class Gun {
		public static Map<CellCoordinates, CellState> gosperGliderGun() {
			int[][] gosperGliderGun = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
									   {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
									   {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
									   {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
									   {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
									   {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
									   {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
									   {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
									   {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
			return convertArrayToMap(gosperGliderGun);
		}
	}
	
	private static class Clock {
		public static Map<CellCoordinates, CellState> fast1T() {
			int[][] diode = {{0, 1, 1, 1, 1},
							 {3, 2, 0, 0, 0}};
			return convertArrayToWireMap(diode);
		}
		
		public static Map<CellCoordinates, CellState> medium2T() {
			int[][] reflector = {{0, 2, 3, 0, 0, 0},
								 {1, 0, 0, 1, 1, 1},
								 {0, 1, 1, 0, 0, 0}};
			return convertArrayToWireMap(reflector);
		}
		
		public static Map<CellCoordinates, CellState> slow4T() {
			int[][] diode = {{0, 1, 2, 3, 0, 0, 0, 0},
							 {1, 0, 0, 0, 1, 0, 0, 0},
							 {1, 0, 0, 0, 1, 1, 1, 1},
							 {1, 0, 0, 0, 1, 0, 0, 0},
							 {0, 1, 1, 1, 0, 0, 0, 0}};
			return convertArrayToWireMap(diode);
		}
	}
	
	private static class Other {
		public static Map<CellCoordinates, CellState> diode() {
			int[][] diode = {{0, 0, 0, 1, 1, 0, 0, 0},
							 {1, 1, 1, 1, 0, 1, 1, 1},
							 {0, 0, 0, 1, 1, 0, 0, 0}};
			return convertArrayToWireMap(diode);
		}
		
		public static Map<CellCoordinates, CellState> reflector() {
			int[][] reflector = {{0, 0, 0, 0, 0, 1, 1, 0},
								 {0, 0, 0, 0, 1, 0, 0, 1},
								 {1, 1, 1, 1, 1, 1, 0, 1},
								 {0, 0, 0, 0, 1, 0, 0, 1},
								 {0, 0, 0, 1, 0, 1, 0, 1},
								 {0, 0, 0, 1, 1, 1, 0, 1},
								 {0, 0, 0, 0, 1, 0, 0, 1},
								 {0, 0, 0, 0, 0, 1, 1, 0}};
			return convertArrayToWireMap(reflector);
		}
	}

	private static class Gate {
		public static Map<CellCoordinates, CellState> or() {
			int[][] or = {{1, 1, 1, 0, 0, 0, 0},
						  {0, 0, 0, 1, 0, 0, 0},
						  {0, 0, 1, 1, 1, 1, 1},
						  {0, 0, 0, 1, 0, 0, 0},
						  {1, 1, 1, 0, 0, 0, 0}};
			return convertArrayToWireMap(or);
		}
		
		public static Map<CellCoordinates, CellState> xor() {
			int[][] xor = {{1, 1, 1, 0, 0, 0, 0, 0, 0},
						   {0, 0, 0, 1, 0, 0, 0, 0, 0},
						   {0, 0, 1, 1, 1, 1, 0, 0, 0},
						   {0, 0, 1, 0, 0, 1, 1, 1, 1},
						   {0, 0, 1, 1, 1, 1, 0, 0, 0},
						   {0, 0, 0, 1, 0, 0, 0, 0, 0},
						   {1, 1, 1, 0, 0, 0, 0, 0, 0}};
			return convertArrayToWireMap(xor);
		}
		
		public static Map<CellCoordinates, CellState> and() {
			int[][] and = {{0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0},
						   {1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 1},
						   {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0},
						   {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
						   {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
						   {0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
						   {1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1},
						   {0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0},
						   {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}};
			return convertArrayToWireMap(and);
		}
		
		public static Map<CellCoordinates, CellState> not() {
			int[][] not = {{0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
						   {1, 1, 1, 0, 0, 1, 0, 1, 1, 1},
						   {0, 0, 0, 0, 2, 2, 2, 0, 0, 0},
						   {0, 0, 0, 3, 0, 3, 3, 0, 0, 0},
						   {0, 0, 0, 1, 2, 1, 0, 0, 0, 0}};
			return convertArrayToWireMap(not);
		}
	}
}
