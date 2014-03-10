package plant_project;

public class LSystem {
	public final static char VARIABLES[] = {'X', 'F'};
	public final static char FLOWER = 'L';
	public final static char AXIOM = 'X';
	public final static char POSITIVE = '+';
	public final static char NEGATIVE = '-';
	
	public static String iterate(String lString, String[] rules){
		char randomVariable = VARIABLES[(int)(Math.random()*VARIABLES.length)];
		lString = lString.replace(Character.toString(randomVariable), rules[(int)Math.random()*rules.length]);
		return lString;
	}

}
