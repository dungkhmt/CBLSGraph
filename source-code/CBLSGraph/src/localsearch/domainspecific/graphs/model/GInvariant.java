/*
 * authors: DungPQ
 * date 12/7/2015
 */

package localsearch.domainspecific.graphs.model;

import localsearch.domainspecific.graphs.core.Edge;

public interface GInvariant {
	public VarGraph[] getVarGraphs();
	public LSGraphManager getLSGraphManager();
	public void setID(int id);
	public int getID();
	public void initPropagate();
	public void propagateAddEdge(VarRootedTree vt, Edge e);
	public void propagateRemoveEdge(VarRootedTree vt, Edge e);
	public void propagateReplaceEdge(VarRootedTree vt, Edge eo, Edge ei);
}
