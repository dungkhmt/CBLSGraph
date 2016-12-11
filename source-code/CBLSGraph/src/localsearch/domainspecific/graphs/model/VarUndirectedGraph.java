package localsearch.domainspecific.graphs.model;

import localsearch.domainspecific.graphs.core.Edge;

import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;

import java.util.HashMap;
import java.util.HashSet;

public class VarUndirectedGraph extends VarGraph {
	
	protected UndirectedGraph lub;
	protected HashMap<Node, HashSet<Edge>> Adj;
	
	public VarUndirectedGraph(LSGraphManager mgr, UndirectedGraph lub){
		super(mgr, lub);
		this.lub = lub;
		Adj = new HashMap<Node, HashSet<Edge>>();
	}
	
	public UndirectedGraph getLUB(){
		return lub;
	}

	public HashSet<Edge> getAdj(Node u) {
		return Adj.get(u);
	}

	public HashMap<Node, HashSet<Edge>> getAdj(){
		return Adj;
	}
	
	public boolean addEdge(Edge e){
		super.addEdge(e);
		Node u = e.getBegin();
		Node v = e.getEnd();
		if (Adj.get(u) == null) {
			Adj.put(u, new HashSet<Edge>());
		}
		if (Adj.get(v) == null) {
			Adj.put(v, new HashSet<Edge>());
		}
		Adj.get(u).add(e);
		Adj.get(v).add(e);
		return true;
	}
	
	public boolean removeEdge(Edge e){
		super.removeEdge(e);
		Node u = e.getBegin();
		Node v = e.getEnd();
		Adj.get(u).remove(e);
		Adj.get(v).remove(e);
		if (Adj.get(u).size() == 0) {
			removeNode(u);
			Adj.remove(u);
		}
		if (Adj.get(v).size() == 0) {
			removeNode(v);
			Adj.remove(v);
		}
		return true;
	}
}
