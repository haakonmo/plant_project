package plant_project;

import java.util.ArrayList;

import plant_project.LSystem;

public class Plant {
	public static final int MINUMUM_ITERATION = 4;
	public static final int MAX_ITERATION = 7;
	public static final int MAX_AGE = 8;
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
