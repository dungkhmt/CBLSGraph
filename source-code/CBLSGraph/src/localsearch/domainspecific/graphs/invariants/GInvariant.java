/*
 * authors: DungPQ
 * date 12/7/2015
 */

package localsearch.domainspecific.graphs.invariants;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public interface GInvariant {
	public VarGraph[] getVarGraphs();
	public LSGraphManager getLSGraphManager();
	public void setID(int id);
	public int getID();
	public void initPropagate();
	public void propagateAddEdge(VarGraph vt, Edge e);
	public void propagateRemoveEdge(VarGraph vt, Edge e);
	public void propagateReplaceEdge(VarGraph vt, Edge eo, Edge ei);
	
	// add an edge to vt
	public void propagateAddEdgeVarRootedTree(VarRootedTree vt, Edge e);
	
	// remove the edge from vt
	public void propagateRemoveEdgeVarRootedTree(VarRootedTree vt, Edge e);
	
	// add ei to vt will create a cycle, then remove edge edge eo of the cycle --> return a new VarRootedTree
	public void propagateReplaceEdgeVarRootedTree(VarRootedTree vt, Edge ei, Edge eo);
	
	// all children of v will be marked as children of parent p(v) of v
	// v will be inserted between u and parent p(u) of u
	public void propagateNodeOptVarRootedTree(VarRootedTree vt, Node v, Node u);
	
	
	// remove subtree rooted at v from vt, 
	// and re-insert this subtree between u and parent p(u) of u
	public void propagateSubTreeOptVarRootedTree(VarRootedTree vt, Node v, Node u);
	
	
}
