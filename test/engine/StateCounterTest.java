package engine;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

public class StateCounterTest {
	private StateCounter counter;
	
	public void initBinary() {
		counter = new StateCounter(false);
		CellState[] states = {BinaryState.ALIVE, BinaryState.DEAD, BinaryState.DEAD, BinaryState.ALIVE,
							  BinaryState.DEAD, BinaryState.DEAD, BinaryState.DEAD, BinaryState.ALIVE, BinaryState.DEAD};
		
		for (CellState s : states)
			counter.addState(s);
	}
	
	public void initQuad() {
		counter = new StateCounter(true);
		CellState[] states = {QuadState.DEAD, QuadState.RED, QuadState.DEAD, QuadState.RED, QuadState.RED, QuadState.BLUE,
							  QuadState.GREEN, QuadState.DEAD, QuadState.GREEN, QuadState.YELLOW, QuadState.DEAD, QuadState.DEAD,
							  QuadState.BLUE, QuadState.RED, QuadState.DEAD, QuadState.DEAD, QuadState.DEAD, QuadState.GREEN};
		
		for (CellState s : states)
			counter.addState(s);
	}
	
	@Test(expected=AutomatonException.class)
	public void exceptionTest() {
		initBinary();
		counter.getAbsentAliveStates();
	}
	
	@Test
	public void binaryTest1() {
		initBinary();
		assertEquals(3, counter.getAliveCount());
	}
	
	@Test
	public void binaryTest2() {
		initBinary();
		assertEquals(BinaryState.ALIVE, counter.getMostPlentiful());
	}
	
	@Test
	public void quadTest1() {
		initQuad();
		assertEquals(10, counter.getAliveCount());
	}
	
	@Test
	public void quadTest2() {
		initQuad();
		assertEquals(QuadState.RED, counter.getMostPlentiful());
	}
	
	@Test
	public void quadTest3() {
		StateCounter counter = new StateCounter(true);
		Set<CellState> absentStates = counter.getAbsentAliveStates();
		
		assertEquals(4, absentStates.size());
	}
	
	@Test
	public void quadTest4() {
		initQuad();
		assertEquals(0, counter.getAbsentAliveStates().size());
	}
}
