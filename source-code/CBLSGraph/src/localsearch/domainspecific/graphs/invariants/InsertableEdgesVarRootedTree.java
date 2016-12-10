package localsearch.domainspecific.graphs.invariants;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;
import localsearch.domainspecific.graphs.model.GraphInvariant;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

import java.util.HashSet;

public class InsertableEdgesVarRootedTree  implements GraphInvariant {
	private LSGraphManager	mgr;
	private VarGraph[]		varGraphs;
	private VarRootedTree	vt;
	private HashSet<Edge>	insertableEdges;
	private int id;
	
	public InsertableEdgesVarRootedTree(VarRootedTree vt){
		this.vt = vt;
		this.mgr = vt.getLSGraphManager();
		insertableEdges = new HashSet<Edge>();
		varGraphs = new VarGraph[1];
		varGraphs[0] = vt;
		mgr.post(this);
	}
	public String name(){ return "InsertableEdgesVarRootedTree";}
	public HashSet<Edge> getEdges(){
		return insertableEdges;
	}
	public void setID(int id){
		this.id = id;
	}
	public int getID(){
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
		for(Node v : vt.getNodes()){
			for(Edge e : vt.getAdj().get(v)){
				Node u = e.otherNode(v);
				if(!vt.contains(e) && !vt.contains(u))
					insertableEdges.add(e);
			}
		}
	}
	@Override
	public void propagateAddEdge(VarRootedTree vt, Edge e) {
	/*	// TODO Auto-generated method stub
		System.out.println(name() + "::propagateAddEdge(" + e.toString() + ")");
		if(this.vt != vt)
			return;
		
		Node leaf = e.getBegin();
		Node other = e.getEnd();
		if(vt.contains(leaf)){
			leaf = e.getEnd();
			other = e.getBegin();
		}
		UndirectedGraph lub = vt.getLUB();

		if(vt.contains(leaf) && vt.contains(other)){
			System.out.println(name() + "::propagateAddEdge" + e.toString() + " exception: two endpoints are belongs to the tree, this will create a cycle");
			System.exit(-1);
		}
		if(!vt.contains(leaf) && !vt.contains(other)){
			System.out.println(name() + "::propagateAddEdge" + e.toString() + " exception: two endpoints are not belongs to the tree, this will create two connected components");
			System.exit(-1);
		}
		insertableEdges.remove(e);
		HashSet<Edge> toRemove = new HashSet<Edge>();
		for(Edge ei : insertableEdges){
			if(ei.contains(leaf)){
				toRemove.add(ei);
			}
		}
		for(Edge ei : toRemove){
			insertableEdges.remove(ei);
		}
		HashSet<Edge> incidents = lub.getAdjEdge(leaf).getClass(leaf);
		for(Edge ei : incidents){
			if(ei != e && !vt.contains(ei.otherNode(leaf))){
				insertableEdges.add(ei);
			}
		}
		*/
	}

	@Override
	public void propagateRemoveEdge(VarRootedTree vt, Edge e) {
/*		// TODO Auto-generated method stub
		System.out.println(name() + "::propagateRemoveEdge(" + e.toString() + ")");
		if(this.vt != vt)
			return;
		Node fv = e.getBegin();
		Node cv = e.getEnd();
		Node _root = vt.root();
		UndirectedGraph lub = vt.getLUB();
		if(!vt.contains(e)){
			System.out.println(name() + "::propagateRemoveEdge(" + fv.getID() + "," + cv.getID() + ") -> exception: this edge does not belong to the tree");
			System.exit(-1);
		}
		if(vt.getFatherNode(cv) != fv){
			fv = e.getEnd();
			cv = e.getBegin();
		}

		if(vt.getAdj().get(cv).size() != 1 && vt.getAdj().get(fv).size() != 1){
			System.out.println(name() + "::propagateRemoveEdge(" + fv.getID() + "," + cv.getID() + ") -> exception: this edge is not a leaf of the tree");
			System.exit(-1);		
		}
		
		HashSet<Edge> toRemove = new HashSet<Edge>();
		for(Edge ei : insertableEdges){
			if(ei.getBegin() == cv || ei.getEnd() == cv)
				toRemove.add(ei);
		}
		for(Edge ei : toRemove){
			insertableEdges.remove(ei);
		}
		for(Edge ei : lub.getAdj(leaf).get(cv)){
			Node other = ei.otherNode(cv);
			if(vt.contains(other)){
				insertableEdges.add(ei);
			}
		}
*/
	}

	@Override
	public void propagateReplaceEdge(VarRootedTree vt, Edge eo, Edge ei) {
		// TODO Auto-generated method stub
		// DO NOTHING
		System.out.println(name() + "::propagateReplaceEdge(" + eo.toString() + "," + ei.toString() + ")");
	}

}
