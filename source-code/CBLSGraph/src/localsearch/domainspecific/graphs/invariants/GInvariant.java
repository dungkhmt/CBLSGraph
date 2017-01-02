/*
 * authors: DungPQ
 * date 12/7/2015
 */

package localsearch.domainspecific.graphs.invariants;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;

public interface GInvariant {
	public VarGraph[] getVarGraphs();
	public LSGraphManager getLSGraphManager();
	public void setID(int id);
	public int getID();
	public void initPropagate();
	public void propagateAddEdge(VarGraph vt, Edge e);
	public void propagateRemoveEdge(VarGraph vt, Edge e);
	public void propagateReplaceEdge(VarGraph vt, Edge eo, Edge ei);
}
