package localsearch.domainspecific.graphs.model;

import localsearch.domainspecific.graphs.core.Edge;

import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;

import java.util.HashMap;
import java.util.HashSet;

public class VarUndirectedGraph extends VarGraph {

	public VarUndirectedGraph(LSGraphManager mgr, UndirectedGraph lub){
		super(mgr, lub);
		this.lub = lub;

	}

	public boolean addEdge(Edge e){
		//System.out.println("VarUndirectedG");
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
//		if (Adj.get(u).size() == 0) {
//			removeNode(u);
//			Adj.remove(u);
//		}
//		if (Adj.get(v).size() == 0) {
//			removeNode(v);
//			Adj.remove(v);
//		}
		return true;
	}

	public void removeEdgePropagate(Edge e){
		removeEdge(e);
		//super.removeEdgePropagate(e);
		mgr.removeEdge(this, e);
	}

	public void addEdgePropagate(Edge e){
		addEdge(e);
		//super.addEdgePropagate(e);
		mgr.addEdge(this, e);
	}

	public void replaceEdgePropagate(Edge eo, Edge ei){
		//replaceEdge(eo, ei);
		removeEdge(eo);
		addEdge(ei);
		//super.replaceEdgePropagate(eo, ei);
		mgr.replaceEdge(this, eo, ei);
	}
}
