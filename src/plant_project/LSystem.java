package plant_project;

public class LSystem {
	public final static char AXIOM       = 'X';

	public final static char NOTHING     = 'X';
	public final static char GROW        = 'G';
	public final static char LEAF        = 'L';
	public final static char FLOWER      = 'F';
	public final static char NORTH       = 'n';
	public final static char EAST        = 'e';
	public final static char WEST        = 'w';
	public final static char SOUTH       = 's';
	public final static char PUSH        = '[';
	public final static char POP         = ']';

	public static String iterate(String lString, LRule[] rules) {
		StringBuilder output = new StringBuilder();

		for (int i = 0; i < lString.length(); i++) {
			boolean found = false;
			char chr = lString.charAt(i);
			for (LRule rule : rules) {
				if (chr == rule.find && Math.random() < rule.prob) {
					output.append(rule.replace);
					found = true;
					break;
				}
			}
			if (!found) {
				output.append(chr);
			}
		}

		return output.toString();
	}
}
