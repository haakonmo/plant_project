package plant_project;

import java.awt.Color;

public class Gene {
	private static final char[] ORIENTATIONS = new char[] {
		'n', 's', 'e', 'w'
	};
	private static final String[] MUTATIONS = new String[] {
		"G", "xG", "[G]", "[xG]",
		"F", "xF", "[F]", "[xF]",
		"[X]", "xX",
	};
	private int angle;
	private LRule[] rules;
	private Color stemColor;
	private Color flowerColor;

	private int reproductionCount;
	private int maturityAge;

	public int getAngle() {
		return angle;
	}

	public LRule[] getRules() {
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

	public Gene(int angle, LRule[] rules, Color stemColor, Color flowerColor,
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
		// MUTATION
		if (i > 0 && i <= Main.mutation) {
			LRule[] newRules = rules.clone();
			// pick random rule to mutate
			int mutationRuleIndex = (int) (Math.random() * (newRules.length - 1));
			newRules[mutationRuleIndex] = mutate(newRules[mutationRuleIndex]);
			System.out.println("old: " + rules[mutationRuleIndex]);
			System.out.println("new: " + newRules[mutationRuleIndex]);
			Color stemColor = new Color((float) Math.random(),
					(float) Math.random(), (float) Math.random());
			Color flowerColor = new Color((float) Math.random(),
					(float) Math.random(), (float) Math.random());
			return new Gene(angle, newRules, stemColor, flowerColor,
					reproductionCount, maturityAge);
		} else {
			// NO MUTATION
			return new Gene(angle, rules, stemColor, flowerColor,
					reproductionCount, maturityAge);
		}
	}

	public static final int ADD = 1;
	public static final int DELETE = 0;

	private LRule mutate(LRule rule) {
		// SWAP ADD or DELETE
		String lString = rule.replace;
		final int type = (int) (Math.random()*2);
		System.out.println(type);
		int index = (int) (Math.random() * (lString.length() - 1));
		String mutation = MUTATIONS[(int) (Math.random() * (MUTATIONS.length - 1))];
		switch (type) {
		case ADD:
			lString = lString.substring(0, index) + mutation
					+ lString.substring(index);
			break;
		case DELETE:
			char c = lString.charAt(index);
			if (c == '[' || c == ']') {
				break;
//				// delete one sequence, i.e. [....]
//				int indexEnd = index;
//				int parantheses = 0;
//				while (c != ']' && parantheses > 0) {
//					if(c == '[')
//						parantheses++;
//					else if(c == ']')
//						parantheses--;
//					c = lString.charAt(++indexEnd);
//				}
//				if (indexEnd == lString.length() - 1)
//					lString = lString.substring(0, index);
//				else
//					lString = lString.substring(0, index)
//							+ lString.substring(indexEnd + 1);
//			} else if (c != ']') {
//				int indexStart = index;
//				int parantheses = 0;
//				while (c != '[') {
//					if(c == ']')
//						parantheses++;
//					else if(c == '[')
//						parantheses--;
//					c = lString.charAt(--indexStart);
//				}
//				if (index == lString.length() - 1)
//					lString = lString.substring(0, indexStart);
//				else
//					lString = lString.substring(0, indexStart)
//							+ lString.substring(index + 1);
			} else {
				if (index == lString.length() - 1)
					lString = lString.substring(0, index);
				else
					lString = lString.substring(0, index)
							+ lString.substring(index + 1);
			}
			break;
		}
		String replace = lString.replace("x", String.valueOf(ORIENTATIONS[(int) (Math
						.random() * (ORIENTATIONS.length - 1))]));
		return new LRule(rule.prob, rule.find, replace);
	}
}
