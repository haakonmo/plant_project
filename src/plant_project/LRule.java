package plant_project;

public class LRule {
	public double prob;
	public char   find;
	public String replace;

	public LRule(String str) {
		String[] parts = str.split(":");

		this.prob    = Double.parseDouble(parts[0]);
		this.find    = parts[1].charAt(0);
		this.replace = parts[2];
	}

	public LRule(double probability, char find, String replace) {
		this.prob    = probability;
		this.find    = find;
		this.replace = replace;
	}

	public String toString() {
		return String.format("[%4.2f] %s -> %s",
			this.prob, this.find, this.replace);
	}
}

