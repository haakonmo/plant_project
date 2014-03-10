package plant_project;

import java.awt.Color;

public class Gene {
	private static final String[] MUTATIONS = new String[] {"F[XXnF]", "[sF]", "F[nXwX]", "sF"};
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
		int i = (int) (Math.random() * 100);
		if (i > 0 && i <= Main.mutation) {
			// TODO mutate
			System.out.println("Mutation happened");
			String mutation = MUTATIONS[(int)(Math.random()*MUTATIONS.length)];
			int index 		= (int)(Math.random()*this.rules.length);
			String rule 	= rules[index];
			int ruleIndex	= (int)(Math.random()*rule.length());
			char c 			= rule.charAt(ruleIndex);
			while (c == '[' || c == ']'){
				ruleIndex	= (int)(Math.random()*rule.length());
				c 			= rule.charAt(ruleIndex);
			}
			rule 			= rule.substring(0,ruleIndex)+mutation+rule.substring(ruleIndex+1, rule.length());
			String[] newRules 	= rules;
			newRules[index] 	= rule;
			Color newStemColor 	= new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
			Color newFlowerColor = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
			return new Gene(angle, newRules, newStemColor, newFlowerColor, reproductionCount, maturityAge);
		}else{
			return new Gene(angle, rules, stemColor, flowerColor, reproductionCount, maturityAge);
		}
	}
}
