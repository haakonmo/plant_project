package plant_project;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static ArrayList<Plant> plants = new ArrayList<Plant>();

	public static void main(String[] args) {
		
		//init
		Drawer drawer = new Drawer();
		plants.add(new Plant(0, 0, new Gene(15, new String[] {"Fn[[X]sX]eF[sFX]wX", "FF"}, new Color(0.0f, 0.8f, 0.2f))));
		plants.add(new Plant(1, 0, new Gene(15, new String[] {"Fs[[X]eX]nF[wFX]wX", "FF"}, new Color(0.2f, 0.2f, 0.2f))));
		
		Scanner sc = new Scanner(System.in);
		while (!sc.nextLine().equals("q")) {
			for (Plant plant: plants) {
				plant.grow();
			}
			System.out.println("tick");
			drawer.draw(plants);
		}
	}

}
