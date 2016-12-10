/*
 * authors: DungPQ
 * date 12/7/2015
 */
package localsearch.domainspecific.graphs.core;

public class Node extends BasicGraphElement {
	public Node(int id){
		super(id);
	}
	public String toString(){
		return "Node" + getID() + "";

	}
}
