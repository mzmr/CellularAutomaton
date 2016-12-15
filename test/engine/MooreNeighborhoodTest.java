package engine;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class MooreNeighborhoodTest {
	private static int mapWidth;
	private static int mapHeight;
	private MooreNeighborhood neighborhood;
	private Set<CellCoordinates> generatedCoords;
	private List<CellCoordinates> correctCoords;
	private int x;
	private int y;
	private int radius;
	private boolean wrappedMap;
	
	@BeforeClass
	public static void setUp() {
		mapWidth = 42;
		mapHeight = 71;
	}
	
	private void configureAndRunTest() {
		updateSettings();
		assertCollectionsAreEqual();
	}
	
	@Test
	public void neighborsTest1() {
		radius = 1;
		wrappedMap = false;
		x = 35;
		y = 50;
		correctCoords = Arrays.asList(c(x - 1, y - 1), c(x, y - 1), c(x + 1, y - 1),
									  c(x - 1, y),					c(x + 1, y),
									  c(x - 1, y + 1), c(x, y + 1), c(x + 1, y + 1));
		configureAndRunTest();
	}

	@Test
	public void neighborsTest2() {
		radius = 1;
		wrappedMap = false;
		x = 0;
		y = 0;
		correctCoords = Arrays.asList(			   c(x + 1, y),
									  c(x, y + 1), c(x + 1, y + 1));
		configureAndRunTest();
	}
	
	@Test
	public void neighborsTest3() {
		radius = 1;
		wrappedMap = false;
		x = mapWidth - 1;
		y = mapHeight - 1;
		correctCoords = Arrays.asList(c(x - 1, y - 1), c(x, y - 1),
									  c(x - 1, y));
		configureAndRunTest();
	}
	
	@Test
	public void neighborsTest4() {
		radius = 1;
		wrappedMap = true;
		x = 7;
		y = 1;
		correctCoords = Arrays.asList(c(x - 1, y - 1), c(x, y - 1), c(x + 1, y - 1),
									  c(x - 1, y),					c(x + 1, y),
									  c(x - 1, y + 1), c(x, y + 1), c(x + 1, y + 1));
		configureAndRunTest();
	}
	
	@Test
	public void neighborsTest5() {
		radius = 1;
		wrappedMap = true;
		x = 0;
		y = 0;
		correctCoords = Arrays.asList(c(mapWidth - 1, mapHeight - 1), c(x, mapHeight - 1), c(x + 1, mapHeight - 1),
									  c(mapWidth - 1, y),								   c(x + 1, y),
									  c(mapWidth - 1, y + 1),		  c(x, y + 1),		   c(x + 1, y + 1));
		configureAndRunTest();
	}
	
	@Test
	public void neighborsTest6() {
		radius = 1;
		wrappedMap = true;
		x = mapWidth - 1;
		y = mapHeight - 1;
		correctCoords = Arrays.asList(c(x - 1, y - 1), c(x, y - 1), c(0, y - 1),
									  c(x - 1, y),					c(0, y),
									  c(x - 1, 0),	   c(x, 0),		c(0, 0));
		configureAndRunTest();
	}
	
	@Test
	public void neighborsTest7() {
		radius = 2;
		wrappedMap = true;
		x = mapWidth - 1;
		y = mapHeight - 1;
		correctCoords = Arrays.asList(c(x - 2, y - 2), c(x - 1, y - 2), c(x, y - 2), c(0, y - 2), c(1, y - 2),
									  c(x - 2, y - 1), c(x - 1, y - 1), c(x, y - 1), c(0, y - 1), c(1, y - 1),
									  c(x - 2, y),	   c(x - 1, y),					 c(0, y),	  c(1, y),
									  c(x - 2, 0),	   c(x - 1, 0),		c(x, 0),	 c(0, 0),	  c(1, 0),
									  c(x - 2, 1),	   c(x - 1, 1),		c(x, 1),	 c(0, 1),	  c(1, 1));
		configureAndRunTest();
	}
	
	private void updateSettings() {
		neighborhood = new MooreNeighborhood(mapWidth, mapHeight, radius, wrappedMap);
		generatedCoords = neighborhood.cellNeighbors(c(x, y));
	}
	
	private void assertCollectionsAreEqual() {
		assertTrue("Collections should contain the same elements but contain different."
				+ "\nGenerated:\t" + generatedCoords
				+ "\nCorrect:\t" + correctCoords, generatedCoords.containsAll(correctCoords));
	}

	private Coords2D c(int x, int y) {
		return new Coords2D(x, y);
	}
}
