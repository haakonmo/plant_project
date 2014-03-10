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
	public static int nutrition = 100;

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
			case 'p':// plant p=(rules seperated by ,), reproducecount,
				// maturityage
				if (in.length() < 3) {
					plants.add(new Plant(1, 0, new Gene(15, new String[] {
						"Fs[[F]eX]nF[wFX]F", "FF" }, new Color(0.2f, 0.9f,
							0.0f), Color.BLUE, 2, Plant.MAX_ITERATION)));
				} else {
					String[] plantElements = in.split("=")[1].split(";");
					String[] rules = plantElements[0].substring(1,
							plantElements[0].length() - 2).split(",");
					plants.add(new Plant(0, 0, new Gene(15, rules, Color.GREEN,
									Color.BLUE, Integer.parseInt(plantElements[1]),
									Integer.parseInt(plantElements[2]))));
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
					if (plant.getAge() >= plant.MAX_AGE)
						newPlants.remove(plant);
					else {
						// plant reproduces
						en = plant.getlString().length();
						if (n <= en) {
							//Delete random plants
							System.out.println("Deleted plant");
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
				System.out.println("tick");
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

		plants.add(new Plant(0, 0, new Gene(15,
			new String[] { "Fn[[X]sXL]eF[sFX]X", "FF" },
			new Color(0.0f, 0.8f, 0.2f),
			new Color(0.3f, 0.3f, 0.8f),
			2, Plant.MAX_ITERATION)));

		plants.add(new Plant(1, 0, new Gene(15,
			new String[] { "Fs[[FL]eX]nF[wFXL]F", "FF" },
			new Color(0.2f, 0.9f, 0.9f),
			new Color(0.8f, 0.3f, 0.3f),
			2, Plant.MAX_ITERATION)));

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
