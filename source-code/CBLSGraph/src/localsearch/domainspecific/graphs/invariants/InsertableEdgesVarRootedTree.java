package localsearch.domainspecific.graphs.invariants;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;
import localsearch.domainspecific.graphs.model.GraphInvariant;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

import java.util.HashMap;
import java.util.HashSet;

public class InsertableEdgesVarRootedTree  implements GraphInvariant {
	
	private LSGraphManager	mgr;
	private VarGraph[]		varGraphs;
	private VarRootedTree	vt;
	private HashSet<Edge>	insertableEdges;
	private HashMap<Node, HashSet<Edge>> insertableEdgesOfInternalNode;
	private HashMap<Node, HashSet<Edge>> insertableEdgesOfExternalNode;
	private int id;
	
	public InsertableEdgesVarRootedTree(VarRootedTree vt) {
		this.vt = vt;
		this.mgr = vt.getLSGraphManager();
		insertableEdges = new HashSet<Edge>();
		insertableEdgesOfInternalNode = new HashMap<Node, HashSet<Edge>>();
		insertableEdgesOfExternalNode = new HashMap<Node, HashSet<Edge>>();
		varGraphs = new VarGraph[1];
		varGraphs[0] = vt;
		mgr.post(this);
	}
	
	public String name() { 
		return "InsertableEdgesVarRootedTree";
	}
	
	public HashSet<Edge> getEdges() {
		return insertableEdges;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
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
	public void initPropagate(){
		System.out.println(name() + "::initPropagate");
		insertableEdges.clear();
		UndirectedGraph ug = vt.getLUB();
		for(Node v : vt.getNodes()) {
			insertableEdgesOfInternalNode.put(v, new HashSet<Edge>());
			HashSet<Edge> sv = insertableEdgesOfInternalNode.get(v);
			for(Edge e : ug.getAdj(v)) {
				Node u = e.otherNode(v);
				if(!vt.contains(u)) {
					sv.add(e);
					insertableEdges.add(e);
					if (!insertableEdgesOfExternalNode.containsKey(u)) {
						insertableEdgesOfExternalNode.put(u, new HashSet<Edge>());
					}
					insertableEdgesOfExternalNode.get(u).add(e);
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
		if (vt.contains(leaf)) {
			leaf = e.getEnd();
			other = e.getBegin();
		}
		UndirectedGraph lub = vt.getLUB();

		if (vt.contains(leaf) && vt.contains(other)) {
			System.out.println(name() + "::propagateAddEdge" + e.toString() + " exception: two endpoints are belongs to the tree, this will create a cycle");
			System.exit(-1);
		}
		if (!vt.contains(leaf) && !vt.contains(other)) {
			System.out.println(name() + "::propagateAddEdge" + e.toString() + " exception: two endpoints are not belongs to the tree, this will create two connected components");
			System.exit(-1);
		}
		
		for(Edge ei : insertableEdgesOfExternalNode.get(leaf)) {
			Node u = ei.otherNode(leaf);
			insertableEdges.remove(ei);
			insertableEdgesOfInternalNode.get(u).remove(ei);
		}
		insertableEdgesOfExternalNode.remove(leaf);
		
		insertableEdgesOfInternalNode.put(leaf, new HashSet<Edge>());
		HashSet<Edge> sv = insertableEdgesOfInternalNode.get(leaf);
		for (Edge ei : lub.getAdj(leaf)) {
			Node u = ei.otherNode(leaf);
			if (!vt.contains(u)) {
				sv.add(ei);
				insertableEdges.add(ei);
				if (!insertableEdgesOfExternalNode.containsKey(u)) {
					insertableEdgesOfExternalNode.put(u, new HashSet<Edge>());
				}
				insertableEdgesOfExternalNode.get(u).add(ei);
			}
		}
	}

	@Override
	public void propagateRemoveEdge(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		System.out.println(name() + "::propagateRemoveEdge(" + e.toString() + ")");
		if(this.vt != vt) {
			return;
		}
		
		Node fv = e.getBegin();
		Node cv = e.getEnd();
		if(!vt.contains(e)) {
			System.out.println(name() + "::propagateRemoveEdge(" + fv.getID() + "," + cv.getID() + ") -> exception: this edge does not belong to the tree");
			System.exit(-1);
		}
		
		UndirectedGraph lub = vt.getLUB();
		if(vt.getFatherNode(cv) != fv) {
			fv = e.getEnd();
			cv = e.getBegin();
		}

		if (vt.getAdj().get(cv).size() != 1) {
			System.out.println(name() + "::propagateRemoveEdge(" + fv.getID() + "," + cv.getID() + ") -> exception: this edge is not a leaf of the tree");
			System.exit(-1);		
		}
		
		for (Edge ei : insertableEdgesOfInternalNode.get(cv)) {
			Node u = ei.otherNode(cv);
			insertableEdges.remove(ei);
			insertableEdgesOfExternalNode.get(u).remove(ei);
		}
		insertableEdgesOfInternalNode.remove(cv);
		
		insertableEdgesOfExternalNode.put(cv, new HashSet<Edge>());
		HashSet<Edge> sv = insertableEdgesOfExternalNode.get(cv);
		for (Edge ei : lub.getAdj(cv)) {
			Node u = ei.otherNode(cv);
			//System.out.println(cv + " " + u);
			if (vt.contains(u)) {
				sv.add(ei);
				insertableEdges.add(ei);
				insertableEdgesOfInternalNode.get(u).add(ei);
			}
		}
	}

	@Override
	public void propagateReplaceEdge(VarRootedTree vt, Edge eo, Edge ei) {
		// TODO Auto-generated method stub
		// DO NOTHING
		System.out.println(name() + "::propagateReplaceEdge(" + eo.toString() + "," + ei.toString() + ")");
	}

}
