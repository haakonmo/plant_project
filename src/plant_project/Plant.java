package plant_project;


public class Plant {
	public int x, y;
	public int age = 0;
	public String lString = Character.toString(LSystem.AXIOM);
	public Gene gene;
	
	public Plant(int x, int y, Gene gene) {
		super();
		this.x = x;
		this.y = y;
		this.gene = gene;
	}
	
	public void grow() {
		lString = LSystem.iterate(lString, gene.rules);
		age++;
	}
}
