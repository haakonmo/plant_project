package plant_project;

public class LSystem {
	public final static char VARIABLES[] = {'X', 'F'};
	public final static char AXIOM = 'X';
	public final static char POSITIVE = '+';
	public final static char NEGATIVE = '-';
	
	public static String iterate(String lString, String[] rules){
		char randomVariable = VARIABLES[(int)(Math.random()*VARIABLES.length)];
		switch (randomVariable) {
		case 'F':
			lString = lString.replace(Character.toString(VARIABLES[1]), rules[1]);
			break;
		case 'X':
			lString = lString.replace(Character.toString(VARIABLES[0]), rules[0]);
			break;
		default:
			break;
		}
		return lString;
	}
}
