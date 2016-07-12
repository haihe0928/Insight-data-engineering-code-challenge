package venmo;

public class VerticeDegree implements Comparable<VerticeDegree> {
	private String name;
	private int degree;
	public VerticeDegree(String name, int degree) {
		this.name = name;
		this.degree = degree;
	}
	public String getName() {
		return name;
	}
	public int getDegree() {
		return degree;
	}
	
	public void setDegree(int deg) {
		this.degree = deg;
	}
	
	@Override
	public int compareTo(VerticeDegree another) {
		if (this.getDegree() == another.getDegree()) {
			return this.getName().compareTo(another.getName());
		}
		return this.getDegree() < another.getDegree() ? -1 : 1;
	}
}
