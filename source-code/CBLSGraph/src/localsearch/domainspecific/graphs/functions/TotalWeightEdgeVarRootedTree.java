/*
 * start by dungkhmt@gmail.com
 */

package localsearch.domainspecific.graphs.functions;


import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class TotalWeightEdgeVarRootedTree implements GFunction {

	public TotalWeightEdgeVarRootedTree(VarRootedTree vt, int idxWeight){
		// total weight on edges of vt with respect to idxWeight
		
	}
	@Override
	public VarGraph[] getVarGraphs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LSGraphManager getLSGraphManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void initPropagate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void propagateAddEdge(VarGraph vt, Edge e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propagateRemoveEdge(VarGraph vt, Edge e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propagateReplaceEdge(VarGraph vt, Edge eo, Edge ei) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propagateAddEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propagateRemoveEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propagateReplaceEdgeVarRootedTree(VarRootedTree vt, Edge ei,
			Edge eo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propagateNodeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propagateSubTreeOptVarRootedTree(VarRootedTree vt, Node v,
			Node u) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAddEdgeDelta(VarGraph vt, Edge e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRemoveEdgeDelta(VarGraph vt, Edge e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getReplaceEdgeDelta(VarGraph vt, Edge ei, Edge eo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAddEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRemoveEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getReplaceEdgeVarRootedTree(VarRootedTree vt, Edge ei, Edge eo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getNodeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSubTreeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		// TODO Auto-generated method stub
		return 0;
	}

}
