
/*
 * authors: DungPQ
 * date 12/7/2015
 */
package localsearch.domainspecific.graphs.core;


public class BasicGraphElement{
	private int ID;
	private double[] weight;
	public BasicGraphElement(int id) {
		// TODO Auto-generated constructor stub
		this.ID = id;
	}
	public int getID(){
		return this.ID;
	}
	public void setID(int id){
		ID = id;
	}
	public double[] getWeight() {
		return weight;
	}
	public void setWeight(double[] weight) {
		this.weight = weight;
	}
}
