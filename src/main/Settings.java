package main;

import java.util.Arrays;

import javax.swing.JCheckBox;

import engine.GameOfLifeRules;
import engine.OneDimensionRules;

public class Settings {
	public Game gameType = Game.GAME_OF_LIFE;
	public Neighborhood neighborhoodType = Neighborhood.MOORE;
	public int neighborhoodRadius = 1;
	public int mapHeight = 40;
	public int mapWidth = 60;
	public GameOfLifeRules rulesGOL = new GameOfLifeRules(Arrays.asList(2, 3), Arrays.asList(3));
	public OneDimensionRules rulesODG = new OneDimensionRules(30);
	public boolean isQuadState = false;
	public boolean isMapWrapped = false;
	public JCheckBox chkIsMapWrapped;
	public JCheckBox chkIsQuadState;
}
