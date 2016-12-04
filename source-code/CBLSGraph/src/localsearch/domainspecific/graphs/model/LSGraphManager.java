/*
 * authors: DungPQ
 * date 12/7/2015
 */

package localsearch.domainspecific.graphs.model;

import localsearch.domainspecific.graphs.core.Edge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;


class Compare implements Comparator<GraphInvariant> {
	
	@Override
	public int compare(GraphInvariant o1, GraphInvariant o2) {
		return o1.getID() - o2.getID();
	}
	
}
public class LSGraphManager {
	private ArrayList<VarGraph> varGraphs;
	private ArrayList<GraphInvariant> graphInvariants;
	private HashMap<VarGraph, TreeSet<GraphInvariant>> _map;
	private int n;
	private boolean closed;
	
	public LSGraphManager(){
		n = 0;
		closed = false;
		varGraphs = new ArrayList<VarGraph>();
		graphInvariants = new ArrayList<GraphInvariant>();
		
	}
	public void post(VarGraph vg){
		vg.setID(n++);
		varGraphs.add(vg);
	}
	public void post(GraphInvariant gi){
		gi.setID(n++);
		graphInvariants.add(gi);
	}
	
	public void addEdge(VarRootedTree vt, Edge e){
		TreeSet<GraphInvariant> s = _map.get(vt);
		for(GraphInvariant gi : s){
			gi.propagateAddEdge(vt, e);
		}
	}
	public void removeEdge(VarRootedTree vt, Edge e){
		TreeSet<GraphInvariant> s = _map.get(vt);
		for(GraphInvariant gi : s){
			gi.propagateRemoveEdge(vt, e);
		}
	}
	public void replaceEdge(VarRootedTree vt, Edge eo, Edge ei){
		TreeSet<GraphInvariant> s = _map.get(vt);
		for(GraphInvariant gi : s){
			gi.propagateReplaceEdge(vt, eo, ei);
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
		
		_map = new HashMap<VarGraph, TreeSet<GraphInvariant>>();
		
		for(int i = 0; i < graphInvariants.size(); i++){
			GraphInvariant gi = graphInvariants.get(i); 
			VarGraph[] s = gi.getVarGraphs();
			if (s != null) 
				for (int j = 0; j < s.length; j++) {
					VarGraph vg = s[j];
					if (_map.get(vg) == null) 
						_map.put(vg,new TreeSet<GraphInvariant>(new Compare()));
					_map.get(vg).add(gi);
				}
		}	
		initPropagate();
	}
	
}
