package main;

public enum Game {
	GAME_OF_LIFE("Game of Life"), LANGTON_ANT("Langton Ant"), WIRE_WORLD("Wire World"), ONE_DIM("One Dimension Game");
	
	private final String name;
	
	private Game(String s) {
		name = s;
	}

	@Override
    public String toString() {
       return this.name;
    }
}