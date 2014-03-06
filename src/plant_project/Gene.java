package plant_project;

import java.awt.Color;

public class Gene {
	private int angle;
	private String[] rules;
	private Color stemColor;
	private Color flowerColor;

	private int reproductionCount;
	private int maturityAge;

	public int getAngle() {
		return angle;
	}

	public String[] getRules() {
		return rules;
	}

	public Color getStemColor() {
		return stemColor;
	}

	public Color getFlowerColor() {
		return flowerColor;
	}

	public int getReproductionCount() {
		return reproductionCount;
	}
	
	public int getMaturityAge() {
		return maturityAge;
	}

	public Gene(int angle, String[] rules, Color stemColor, Color flowerColor,
			int reproductionCount, int maturityAge) {
		super();
		this.angle = angle;
		this.rules = rules;
		this.stemColor = stemColor;
		this.flowerColor = flowerColor;
		this.reproductionCount = reproductionCount;
		this.maturityAge = maturityAge;
	}

	/**
	 * Returns the gene with probability of a mutation equal to the global
	 * mutation probability {@code Main.mutation}
	 */
	public Gene clone() {
		int i = (int) Math.random() * 100;
		if (i > 0 && i < Main.mutation) {
			// TODO mutate
		}
		return new Gene(angle, rules, stemColor, flowerColor, reproductionCount, maturityAge);
	}
}
