package plant_project;

import java.util.ArrayList;

public class Plant {
	public static final int MAX_ITERATION = 5;
	public static final int MAX_AGE = 7;
	public static final float MAX_REPRODUCTION_RADIUS = 2;

	private float x, y;
	private int age = 0;
	private String lString = Character.toString(LSystem.AXIOM);
	private Gene gene;

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getAge() {
		return age;
	}

	public String getlString() {
		return lString;
	}

	public Gene getGene() {
		return gene;
	}

	public Plant(float x, float y, Gene gene) {
		super();
		this.x = x;
		this.y = y;
		this.gene = gene;
	}

	/**
	 * Grows plant and returns seeds(child plants)
	 * @return seeds
	 */
	public ArrayList<Plant> grow() {
		ArrayList<Plant> children = new ArrayList<Plant>();
		if (++age < MAX_ITERATION) {
			lString = LSystem.iterate(lString, gene.getRules());
		}
		if (age == gene.getMaturityAge()) {
			for (int i = 0; i <  gene.getReproductionCount(); i++) {
				float newX = x + (float)Math.random()*MAX_REPRODUCTION_RADIUS - MAX_REPRODUCTION_RADIUS/2;
				float newY = y + (float)Math.random()*MAX_REPRODUCTION_RADIUS - MAX_REPRODUCTION_RADIUS/2;
				if(newX < Drawer.EAST_BOUND && newX > Drawer.WEST_BOUND && newY < Drawer.NORTH_BOUND && newY > Drawer.SOUTH_BOUND)
					children.add(new Plant(newX, newY, gene.clone()));
			}
		}
		return children;
	}
}
