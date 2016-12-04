/*
 * authors: DungPQ
 * date 12/7/2015
 */

package localsearch.domainspecific.graphs.core;

public class Edge extends BasicGraphElement {
	private Node begin;
	private Node end;
	public Edge(int id){
		super(id);
	}
	public Node getBegin() {
		return begin;
	}
	public void setBegin(Node begin) {
		this.begin = begin;
	}
	public Node getEnd() {
		return end;
	}
	public void setEnd(Node end) {
		this.end = end;
	}
	public Node otherNode(Node v){
		if(v == begin) return end; else if(v == end) return begin;
		return null;
	}
	public boolean contains(Node v){
		return begin == v || end == v;
	}
	public String toString(){
		return "(" + begin.getID() + "," + end.getID() + ")";
	}
}
