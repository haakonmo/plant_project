package plant_project;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	/**
	 * Water value in percent
	 */
	public static int water = 100;

	/**
	 * Sun value in percent
	 */
	public static int sun = 100;

	/**
	 * Mutation probability
	 */
	public static int mutation = 1;

	/**
	 * Soil nutrition
	 */
	public static int nutrition = 10000;

	/**
	 * Plants in world
	 */
	public ArrayList<Plant> plants;

	/**
	 * Drawing window
	 */
	public Drawer drawer;

	/**
	 * Use input from GL
	 */
	public void keyPress(char c, String in)
	{
		switch (c) {
			case 'p':
				// plant p=(probability:find:replace; ...), reproducecount, maturityage
				if (in.length() < 3) {
					plants.add(new Plant(1, 0, new Gene(15,
						new LRule[] { new LRule(0.5, 'X', "Gs[[G]eX]nG[wGX]G"),
						              new LRule(0.5, 'G', "GG") },
						new Color(0.2f, 0.9f, 0.0f),
						Color.BLUE,
						2, Plant.MAX_ITERATION,
						0.01f, 0.1f)));
				} else {
					String[] plantElements = in.split("=")[1].split(";");
					String[] ruleStrings = plantElements[0].substring(1,
							plantElements[0].length()-2).split(",");
					LRule rules[] = new LRule[ruleStrings.length];
					for (int i = 0; i < ruleStrings.length; i++)
						rules[i] = new LRule(ruleStrings[i]);
					plants.add(new Plant(0, 0, new Gene(15,
						rules, Color.GREEN, Color.BLUE,
						Integer.parseInt(plantElements[1]),
						Integer.parseInt(plantElements[2]),
						0.01f, 0.1f)));
				}
				System.out.println("planted in 0, 0!");
				break;
			case 's':
				sun = Integer.parseInt(in.split("=")[1]);
				System.out.println(String.format("%d%% sun", sun));
				break;
			case 'w':
				water = Integer.parseInt(in.split("=")[1]);
				System.out.println(String.format("%d%% water", water));
				break;
			case 'm':
				mutation = Integer.parseInt(in.split("=")[1]);
				System.out.println(String.format("%d%% mutation probability",
							mutation));
				break;
			case 'n':
				nutrition = Integer.parseInt(in.split("=")[1]);
				System.out.println(String.format("%d%% nutrition ", nutrition));
				break;
			case 'q':
				this.drawer.quit(); // does not return
				break;
			default:
				ArrayList<Plant> newPlants = new ArrayList<Plant>(plants);
				int n = nutrition;
				int en;
				for (Plant plant : plants) {
					// plant dies
					if (plant.getAge() >= Plant.MAX_AGE)
						newPlants.remove(plant);
					else {
						// plant reproduces
						
						// if there's not enough nutrition, some plants die.
						en = plant.getlString().length();
						if (n <= en) {
							//Delete random plants
							//System.out.println("Deleted plant");
							int mod = (int)(Math.random()*5);
							for (int i=0; i < newPlants.size(); i++){
								if (mod>2 && (i%mod)== 0){
									Plant p = newPlants.get(i);
									n += p.getlString().length();
									newPlants.remove(i);
								}
							}
						}
						n -= plant.getlString().length();
						newPlants.addAll(plant.grow());
					}
				}
				plants = newPlants;
				//System.out.println("tick");
				drawer.draw(plants);
				break;
		}
	}

	/**
	 * Main constructor
	 */
	public Main() {
		this.plants = new ArrayList<Plant>();
		this.drawer = new Drawer(this);

		/* Wikipedia example L-System */
		//plants.add(new Plant(0, 0, new Gene(15,
		//	new LRule[] { new LRule(1, 'X', "Gn[[X]sX]sG[sGX]nX"),
		//	              new LRule(1, 'G', "GG") },
		//	new Color(0.0f, 0.8f, 0.2f),
		//	new Color(0.3f, 0.3f, 0.8f),
		//	2, Plant.MAX_ITERATION)));

		plants.add(new Plant(0, 0, new Gene(15,
			new LRule[] { new LRule(0.75, 'X', "Gn[[X]sXL]eG[sGXF]X"),
			              new LRule(0.75, 'G', "GG") },
			new Color(0.0f, 0.8f, 0.2f),
			new Color(0.3f, 0.3f, 0.8f),
			2, Plant.MAX_ITERATION, 0.01f, 0.1f)));

		plants.add(new Plant(1, 0, new Gene(15,
			new LRule[] { new LRule(0.75, 'X', "Gs[[GL]eX]nG[wGXL]G"),
			              new LRule(0.75, 'G', "GG") },
			new Color(0.2f, 0.9f, 0.9f),
			new Color(0.8f, 0.3f, 0.3f),
			2, Plant.MAX_ITERATION, 0.01f, 0.1f)));

		System.out.format("%-16s %3d%% (s=?)\n", "Sun:", sun);
		System.out.format("%-16s %3d%% (w=?)\n", "Water:", water);
		System.out.format("%-16s %3d%% (m=?)\n", "Mutation Prob:", mutation);
	}

	/**
	 * Main loop
	 */
	public void run() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			String in = sc.nextLine();
			char c = in.length() > 0 ? in.charAt(0) : ' ';
			keyPress(c, in);
		}
	}

	/**
	 * Main..
	 */
	public static void main(String[] args) {
		Main main = new Main();
		main.run();
	}
}
