package main;

public enum Neighborhood {
	MOORE("Moore Neighborhood"), VON_NEUMAN("Von Neuman Neighborhood");
	
	private final String name;
	
	private Neighborhood(String s) {
		name = s;
	}

	@Override
    public String toString() {
       return this.name;
    }
}