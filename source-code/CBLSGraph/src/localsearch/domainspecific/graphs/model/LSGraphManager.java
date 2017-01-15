/*
 * authors: DungPQ
 * date 12/7/2015
 */

package localsearch.domainspecific.graphs.model;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.invariants.GInvariant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class LSGraphManager {
	private ArrayList<VarGraph> varGraphs;
	private ArrayList<GInvariant> graphInvariants;
	private HashMap<VarGraph, TreeSet<GInvariant>> _map;
	private int n;
	private boolean closed;
	
	public LSGraphManager(){
		n = 0;
		closed = false;
		varGraphs = new ArrayList<VarGraph>();
		graphInvariants = new ArrayList<GInvariant>();
		
	}
	public void post(VarGraph vg){
		vg.setID(n++);
		varGraphs.add(vg);
	}
	public void post(GInvariant gi){
		gi.setID(n++);
		graphInvariants.add(gi);
	}
	
	public void addEdge(VarGraph vt, Edge e){
		TreeSet<GInvariant> s = _map.get(vt);
		for(GInvariant gi : s){
			gi.propagateAddEdge(vt, e);
		}
		
	}
	public void removeEdge(VarGraph vt, Edge e){
		TreeSet<GInvariant> s = _map.get(vt);
		for(GInvariant gi : s){
			gi.propagateRemoveEdge(vt, e);
		}
	}
	public void replaceEdge(VarGraph vt, Edge eo, Edge ei){
		TreeSet<GInvariant> s = _map.get(vt);
		for(GInvariant gi : s){
			gi.propagateReplaceEdge(vt, eo, ei);
		}
	}
	
	
	// add an edge to vt
	public void addEdgeVarRootedTree(VarRootedTree vt, Edge e){
		TreeSet<GInvariant> s = _map.get(vt);
		for(GInvariant gi : s){
			gi.propagateAddEdgeVarRootedTree(vt, e);
		}
		
	}
	
	// remove the edge from vt
	public void removeEdgeVarRootedTree(VarRootedTree vt, Edge e){
		TreeSet<GInvariant> s = _map.get(vt);
		for(GInvariant gi : s){
			gi.propagateRemoveEdgeVarRootedTree(vt, e);
		}

	}
	
	// add ei to vt will create a cycle, then remove edge edge eo of the cycle --> return a new VarRootedTree
	public void replaceEdgeVarRootedTree(VarRootedTree vt, Edge ei, Edge eo){
		TreeSet<GInvariant> s = _map.get(vt);
		for(GInvariant gi : s){
			gi.propagateReplaceEdgeVarRootedTree(vt, eo, ei);
		}

	}
	
	// all children of v will be marked as children of parent p(v) of v
	// v will be inserted between u and parent p(u) of u
	public void nodeOptVarRootedTree(VarRootedTree vt, Node v, Node u){
		TreeSet<GInvariant> s = _map.get(vt);
		for(GInvariant gi : s){
			gi.propagateNodeOptVarRootedTree(vt, v, u);
		}

	}
	
	
	// remove subtree rooted at v from vt, 
	// and re-insert this subtree between u and parent p(u) of u
	public void subTreeOptVarRootedTree(VarRootedTree vt, Node v, Node u){
		TreeSet<GInvariant> s = _map.get(vt);
		for(GInvariant gi : s){
			gi.propagateSubTreeOptVarRootedTree(vt, v, u);
		}
		
	}
	
	
	public boolean isClosed(){
		return this.closed;
	}
	private void initPropagate(){
		for(int i = 0; i < graphInvariants.size(); i++){
			graphInvariants.get(i).initPropagate();
		}
	}
	public void close() {
		if(isClosed()) return;
		closed = true;
		
		_map = new HashMap<VarGraph, TreeSet<GInvariant>>();
		
		for(int i = 0; i < graphInvariants.size(); i++){
			GInvariant gi = graphInvariants.get(i); 
			VarGraph[] s = gi.getVarGraphs();
			if (s != null) 
				for (int j = 0; j < s.length; j++) {
					VarGraph vg = s[j];
					if (_map.get(vg) == null) 
						_map.put(vg,new TreeSet<GInvariant>(new Compare()));
					_map.get(vg).add(gi);
				}
		}	
		initPropagate();
	}

	class Compare implements Comparator<GInvariant> {

		@Override
		public int compare(GInvariant o1, GInvariant o2) {
			return o1.getID() - o2.getID();
		}

	}
}
