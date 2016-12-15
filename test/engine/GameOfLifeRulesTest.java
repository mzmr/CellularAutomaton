package engine;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import engine.GameOfLifeRules;

import static org.junit.Assert.*;

public class GameOfLifeRulesTest {
	private GameOfLifeRules rules;
	
	@Before
	public void initRules() {
		rules = new GameOfLifeRules(Arrays.asList(2, 3), Arrays.asList(4));
	}
	
	@Test
	public void sustainingLife1() {
		assertFalse("Cell shoul become dead, but stays alive.", rules.willStayAlive(1));
	}

	@Test
	public void sustainingLife2() {
		assertTrue("Cell shoul stay alive, but becomes dead.", rules.willStayAlive(2));
	}
	
	@Test
	public void sustainingLife3() {
		assertTrue("Cell shoul stay alive, but becomes dead.", rules.willStayAlive(3));
	}
	
	@Test
	public void beginningLife1() {
		assertTrue("Cell shoul stay dead, but becomes alive.", rules.willStayDead(3));
	}
	
	@Test
	public void beginningLife2() {
		assertFalse("Cell shoul become alive, but stays dead.", rules.willStayDead(4));
	}
}
