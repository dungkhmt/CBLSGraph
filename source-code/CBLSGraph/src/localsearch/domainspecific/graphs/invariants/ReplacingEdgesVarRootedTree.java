package localsearch.domainspecific.graphs.invariants;

import java.util.HashMap;
import java.util.HashSet;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;
import localsearch.domainspecific.graphs.model.GraphInvariant;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class ReplacingEdgesVarRootedTree implements GraphInvariant {
	private LSGraphManager	mgr;
	private VarGraph[]		varGraphs;
	private VarRootedTree	vt;
	private HashSet<Edge>	replacingEdges;
	private int id;
	
	public ReplacingEdgesVarRootedTree(VarRootedTree vt) {
		// TODO Auto-generated constructor stub
		this.vt = vt;
		this.mgr = vt.getLSGraphManager();
		replacingEdges = new HashSet<Edge>();
		varGraphs = new VarGraph[1];
		varGraphs[0] = vt;
		mgr.post(this);
	}
	
	public String name() { 
		return "ReplacingEdgesVarRootedTree";
	}
	
	@Override
	public VarGraph[] getVarGraphs() {
		// TODO Auto-generated method stub
		return varGraphs;
	}

	@Override
	public LSGraphManager getLSGraphManager() {
		// TODO Auto-generated method stub
		return mgr;
	}

	@Override
	public void setID(int id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}

	public HashSet<Edge> getEdges(){
		return replacingEdges;
	}
	
	@Override
	public void initPropagate() {
		// TODO Auto-generated method stub
		UndirectedGraph lub = vt.getLUB();
		for (Node u : vt.getNodes()) {
			for (Edge e : lub.getAdj(u)) {
				Node v = e.otherNode(u);
				if (vt.contains(v) && !vt.contains(e)) {
					replacingEdges.add(e);
				}
			}
		}
	}

	@Override
	public void propagateAddEdge(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		System.out.println(name() + "::propagateAddEdge(" + e.toString() + ")");
		if (this.vt != vt) {
			return;
		}
		
		Node leaf = e.getBegin();
		Node other = e.getEnd();
		if (vt.contains(e.getEnd()) && vt.contains(e.getBegin())) {
			System.out.println(name() + "::propagateAddEdge" + e.toString() + " exception: two endpoints are belongs to the tree, this will create a cycle");
			System.exit(-1);
		}
		
		if (!vt.contains(e.getEnd()) && !vt.contains(e.getBegin())) {
			System.out.println(name() + "::propagateAddEdge" + e.toString() + " exception: two endpoints are not belongs to the tree, this will create two connected components");
			System.exit(-1);
		}
		
		if(vt.contains(leaf)){
			leaf = e.getEnd();
			other = e.getBegin();
		}
		
		UndirectedGraph lub = vt.getLUB();
		HashSet<Edge> incidents = lub.getAdj(leaf);
		int c = 0;
		for(Edge ei : incidents){
			Node v = ei.otherNode(leaf);
			if(vt.contains(v) && ei != e){
				replacingEdges.add(ei);
			}
		}
	}

	@Override
	public void propagateRemoveEdge(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		System.out.println(name() + "::propagateRemoveEdge(" + e.toString() + ")");
		if(this.vt != vt)
			return;
		Node fv = e.getBegin();
		Node cv = e.getEnd();
		
		if(!vt.contains(e)){
			System.out.println(name() + "::propagateRemoveEdge(" + fv.getID() + "," + cv.getID() + ") -> exception: this edge does not belong to the tree");
			System.exit(-1);
		}
		if(vt.getFatherNode(cv) != fv){
			fv = e.getEnd();
			cv = e.getBegin();
		}
		HashMap<Node, HashSet<Edge>> adjEdges = vt.getAdj();
		if(adjEdges.get(cv).size() != 1 && adjEdges.get(fv).size() != 1){
			System.out.println(name() + "::propagateRemoveEdge(" + fv.getID() + "," + cv.getID() + ") -> exception: this edge is not a leaf of the tree");
			System.exit(-1);		
		}
		
		UndirectedGraph lub = vt.getLUB();
		for(Edge ei : lub.getAdj().get(cv)){
			Node other = ei.otherNode(cv);
			if(vt.contains(other)){
				replacingEdges.remove(ei);
			}
		}

	}

	@Override
	public void propagateReplaceEdge(VarRootedTree vt, Edge eo, Edge ei) {
		// TODO Auto-generated method stub
		System.out.println(name() + "::propagateReplaceEdge(" + eo.toString() + "," + ei.toString() + ")");
		if(this.vt != vt)
			return;
		replacingEdges.remove(ei);
		replacingEdges.add(eo);

	}

}
