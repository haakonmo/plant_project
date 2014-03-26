package plant_project;

import java.util.ArrayList;

import plant_project.LSystem;

public class Plant implements Comparable<Plant> {
	public static final int MINUMUM_ITERATION = 4;
	public static final int MAX_ITERATION = 7;
	public static final int MAX_AGE = 8;
	public static final float MAX_REPRODUCTION_RADIUS = 2;

	private float x, y, a;
	private int age = 0;
	private String lString = Character.toString(LSystem.AXIOM);
	private Gene gene;
	private float fitness;

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getA() {
		return a;
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
		this.a = (int)(Math.random()*360);
		this.gene = gene;
	}

	/**
	 * Fitness function:
	 *
	 * Update plant fitness based on environmental parameters.
	 */
	public void update(float waterPct, float sunPct, float nutritionPct) {
		double total = 0;

		// Genetic pressure from different parameters
		//   (lots of sun == low pressure, etc)
		double water     = 1.0 - (waterPct     / 100.0);
		double sun       = 1.0 - (sunPct       / 100.0);
		double nutrition = 1.0 - (nutritionPct / 100.0);

		// Handy genetic properties
		double leafRatio    = gene.getLeafSize()  / Gene.MAX_LEAF_SIZE;
		double stemRatio    = gene.getStemWidth() / Gene.MAX_STEM_WIDTH;

		double stemCount    = lString.length(); // TODO
		double leafCount    = lString.length(); // TODO
		double flowerCount  = lString.length(); // TODO

		double storageSpace = stemRatio * stemCount;
		double surfaceArea  = leafRatio * leafCount;

		// Water
		total -= surfaceArea    * water;      // Large surface area looses water
		total += storageSpace/3 * water;      // Large stems can store water

		// Sun
		total += surfaceArea    * sun;        // Large leaves gather more light
		total += stemCount      * sun;        // Taller plants have an advantage

		// Nutrition
		total -= storageSpace/2 * nutrition;  // Large stems can store nutrients
		total -= stemCount/2    * nutrition;  // Large plants require more nutrients
		total -= leafCount/2    * nutrition;  // ..
		total -= flowerCount/2  * nutrition;  // ..

		// Other
		total += flowerCount;               // Flowers help with reproduction

		this.fitness = (float)total;
	}

	/**
	 * Compare based on fitness
	 */
	public int compareTo(Plant other) {
		return this.fitness > other.fitness ?  1 :
		       this.fitness < other.fitness ? -1 : 0;
	}

	/**
	 * Grows plant and returns seeds(child plants)
	 * @return seeds
	 */
	public ArrayList<Plant> grow() {
		//Sun
		//plants try to adapt and becomes smaller by 100% - sun (e.g. sun=65% --> 35% smaller)
		int s = Main.sun;
		shrinklString(s);

		//Water
		//plants try to adapt and becomes smaller by 100% - water (e.g. water=65% --> 35% smaller)
		int w = Main.water;
		shrinklString(w);

		ArrayList<Plant> children = new ArrayList<Plant>();
		++age;
		if (MINUMUM_ITERATION <= age && age <= MAX_ITERATION) {
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

	private void shrinklString(int percentToShrink){
		String templString = lString;
		if (percentToShrink != 100){
			float percentageToDelete = (100.0f-percentToShrink)/100.0f;
			int numberOfCharDeleted = 0;
			for (int i = 0; i < templString.length(); i++) {
				char c = templString.charAt(i);
				if (c == 'F'){
					double random = Math.random();
					boolean delete = random < Math.sqrt(percentageToDelete);
					if (delete){
						int index = i-numberOfCharDeleted;
						if (index!=0 && index!=lString.length()){
							lString = lString.substring(0,index)+lString.substring(index+1, lString.length());
							numberOfCharDeleted++;
						}
					}
				}
				if (c == 'L'){
					double random = Math.random();
					boolean delete = random < Math.sqrt(percentageToDelete);
					if (delete){
						int index = i-numberOfCharDeleted;
						if (index!=0 && index!=lString.length()){
							lString = lString.substring(0,index)+lString.substring(index+1, lString.length());
							numberOfCharDeleted++;
						}
					}
				}
			}
		}
	}
}
