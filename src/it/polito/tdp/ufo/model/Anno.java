package it.polito.tdp.ufo.model;

public class Anno {

	private int anno;
	private int na; //numero avvistamenti
	
	public Anno(int anno, int na) {
		super();
		this.anno = anno;
		this.na = na;
	}

	public int getAnno() {
		return anno;
	}

	public int getNa() {
		return na;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + anno;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Anno other = (Anno) obj;
		if (anno != other.anno)
			return false;
		return true;
	}
	
	public String toString() {
		return ""+anno+ " ("+na+")";
	}
}
