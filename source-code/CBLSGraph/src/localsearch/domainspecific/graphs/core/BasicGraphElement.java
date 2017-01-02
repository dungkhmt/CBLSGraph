/*
 * authors: DungPQ
 * date 12/7/2015
 */
package localsearch.domainspecific.graphs.core;

public class BasicGraphElement {
	private int ID;
	private double[] weight;

	public BasicGraphElement(int id) {
		// TODO Auto-generated constructor stub
		this.ID = id;
	}

	public int getID() {
		return this.ID;
	}

	public void setID(int id) {
		ID = id;
	}

	public double[] getWeights() {
		return weight;
	}

	public double getWeight() {
		return weight != null ? weight[0] : 1;
	}

	public double getWeight(int idx){
		return idx >= 0 && idx < weight.length ? weight[idx] : 1;
	}
	
	public void setWeights(double[] weight) {
		this.weight = weight;
	}

	public void setWeight(double w) {
		this.weight = new double[1];
		this.weight[0] = w;
	}
}
