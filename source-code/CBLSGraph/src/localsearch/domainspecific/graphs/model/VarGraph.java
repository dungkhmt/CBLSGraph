package localsearch.domainspecific.graphs.model;

import java.util.HashMap;
import java.util.HashSet;

import localsearch.domainspecific.graphs.core.*;

public class VarGraph extends BasicGraphElement {

	protected Graph lub;
	protected LSGraphManager mgr;
	protected HashSet<Node> nodes;
	protected HashSet<Edge> edges;
	protected HashMap<Node, HashSet<Edge>> Adj;
	//protected HashMap<Integer, Node> mID2Node;
	//protected HashMap<Integer, Edge> mID2Edge;
	
	public VarGraph(LSGraphManager mgr, Graph lub){
		super(0);
		nodes = new HashSet<Node>();
		edges = new HashSet<Edge>();
		Adj = new HashMap<Node, HashSet<Edge>>();
		this.lub = lub;
		this.mgr = mgr;
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
	
	public boolean removeNode(Node v) {
		nodes.remove(v);
		return true;
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

	public Graph getLUB() {
		return lub;
	}

	public HashSet<Edge> getAdj(Node u) {
		return Adj.get(u);
	}

	public HashMap<Node, HashSet<Edge>> getAdj(){
		return Adj;
	}
}
