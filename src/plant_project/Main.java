package plant_project;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

public class Main {

	/**
	 * Maximum number of plants
	 */
	public static final int MAX_PLANTS = 30;

	/**
	 * Water value in percent
	 */
	public static int water = 50;

	/**
	 * Sun value in percent
	 */
	public static int sun = 50;

	/**
	 * Soil nutrition
	 */
	public static int nutrition = 50;

	/**
	 * Mutation probability
	 */
	public static int mutation = 50;

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
		Date   time;
		String path;
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
			case 'c':
				// take screen capture
				time = new Date();
				path = String.format(
					"img/screenshot-%tY%tm%td_%tH%tM%tS_%tL.png",
					time, time, time, time, time, time, time);
				this.drawer.capture(path);
				break;
			case 'r':
				// toggle video recording
				time = new Date();
				path = String.format(
					"img/video-%tY%tm%td_%tH%tM%tS.mp4",
					time, time, time, time, time, time);
				this.drawer.record(path);
				break;
			case 'f':
				// record frame
				this.drawer.addFrame("user");
				break;
			case 'x':
				// kill all plants and reset
				this.resetPlants();
				break;
			case ' ':
				// play/pause
				this.drawer.toggle();
				break;
			default:
				// System.out.println("tick");
				this.tick();
				this.drawer.addFrame("tick");
				break;
		}
	}

	public void tick() {
		// TODO - use an interator or something instead of
		//        doing an array copies of plants.
		ArrayList<Plant> tmpPlants;
		int doomed = 0;

		// Only run fitness for gorwing plants
		if (plants.get(0).getAge() >= Plant.MIN_ITERATION) {
			doomed = (plants.size() - MAX_PLANTS) / 2;

			// Run fitness function
			for (Plant plant : plants)
				plant.update(water, sun, nutrition);

			// Sort plants and remove unfit ones
			Collections.shuffle(plants);
			Collections.sort(plants);
			for (int i = 0; i < doomed; i++)
				plants.remove(0);
		}

		// Add offspring
		tmpPlants = new ArrayList<Plant>(plants);
		for (Plant plant : tmpPlants)
			plants.addAll(plant.grow());

		// Kill off old plants
		tmpPlants = new ArrayList<Plant>(plants);
		for (Plant plant : tmpPlants)
			if (plant.getAge() >= Plant.MAX_AGE)
				plants.remove(plant);

		// Debug
		System.out.println("tick - removed " + doomed);
	}

	public void resetPlants() {
		this.plants.clear();

		/* Wikipedia example L-System */
		//this.plants.add(new Plant(0, 0, new Gene(15,
		//	new LRule[] { new LRule(1, 'X', "Gn[[X]sX]sG[sGX]nX"),
		//	              new LRule(1, 'G', "GG") },
		//	new Color(0.0f, 0.8f, 0.2f),
		//	new Color(0.3f, 0.3f, 0.8f),
		//	2, Plant.MAX_ITERATION)));

		//this.plants.add(new Plant(0, 0, new Gene(15,
		//	new LRule[] { new LRule(0.75, 'X', "GGGG[nX][sX][eX][wX][X]"),
		//	              new LRule(0.75, 'G', "GG") },
		//	new Color(0.0f, 0.8f, 0.2f),
		//	new Color(0.3f, 0.3f, 0.8f),
		//	2, Plant.MAX_ITERATION, 0.01f, 0.1f)));

		this.plants.add(new Plant(0, 0, new Gene(15,
			new LRule[] { new LRule(0.75, 'X', "Gn[[X]sXL]eG[sGXF]X"),
			              new LRule(0.75, 'G', "GG") },
			new Color(0.0f, 0.8f, 0.2f),
			new Color(0.3f, 0.3f, 0.8f),
			2, Plant.MAX_ITERATION, 0.01f, 0.1f)));

		this.plants.add(new Plant(1, 0, new Gene(15,
			new LRule[] { new LRule(0.75, 'X', "Gs[[GL]eX]nG[wGXL]G"),
			              new LRule(0.75, 'G', "GG") },
			new Color(0.2f, 0.9f, 0.9f),
			new Color(0.8f, 0.3f, 0.3f),
			2, Plant.MAX_ITERATION, 0.01f, 0.1f)));
	}

	/**
	 * Main constructor
	 */
	public Main() {
		this.plants = new ArrayList<Plant>();
		this.drawer = new Drawer(this);
		this.resetPlants();
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
