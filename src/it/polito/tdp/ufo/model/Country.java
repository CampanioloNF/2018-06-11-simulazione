package it.polito.tdp.ufo.model;

public class Country implements Comparable<Country>{

	private String state;

	public Country(String state) {
		super();
		this.state = state;
	}

	public String getState() {
		return state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		Country other = (Country) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	@Override
	public int compareTo(Country c) {
		// TODO Auto-generated method stub
		return this.state.compareTo(c.state);
	}
	
	public String toString() {
		return this.state;
	}
}
