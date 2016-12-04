/*
 * authors: DungPQ
 * date 12/7/2015
 */

package localsearch.domainspecific.graphs.model;

import java.util.HashMap;
import java.util.HashSet;

import localsearch.domainspecific.graphs.core.BasicGraphElement;
import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;


public class VarGraph extends BasicGraphElement {
	protected LSGraphManager mgr;
	protected HashSet<Node> nodes;
	protected HashSet<Edge> edges;
	protected UndirectedGraph lub;
	//protected HashMap<Integer, Node> mID2Node;
	//protected HashMap<Integer, Edge> mID2Edge;
	public VarGraph(LSGraphManager mgr, UndirectedGraph lub){
		super(0);
		nodes = new HashSet<Node>();
		edges = new HashSet<Edge>();
		this.mgr = mgr;
		this.lub = lub;
	}
	public UndirectedGraph getLUB(){
		return lub;
	}
	public boolean addEdge(Edge e){
		edges.add(e);
		Node u = e.getBegin();
		Node v = e.getEnd();
		addNode(u);
		addNode(v);
		return true;
	}
	public void addNode(Node v){
		nodes.add(v);
	}
	public boolean removeEdge(Edge e){
		edges.remove(e);
		return true;
	}
	public boolean contains(Node v){
		return nodes.contains(v);
	}
	public boolean contains(Edge e){
		return edges.contains(e);
	}
	public HashSet<Edge> getEdges(){
		return edges;
	}
	public HashSet<Node> getNodes(){
		return nodes;
	}
	public LSGraphManager getLSGraphManager(){
		return this.mgr;
	}
}
