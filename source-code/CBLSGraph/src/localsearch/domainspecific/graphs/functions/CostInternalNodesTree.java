package localsearch.domainspecific.graphs.functions;

import localsearch.domainspecific.graphs.constraints.GConstraint;
import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class CostInternalNodesTree implements GConstraint {

	// TODO by Duy Hoang
	private int _idxWeight;
	private VarRootedTree _vt;
	
	public CostInternalNodesTree(VarRootedTree vt, int idxWeight){
		this._vt = vt;
		this._idxWeight = idxWeight;
	}
	public CostInternalNodesTree(VarRootedTree vt){
		this._vt = vt;
		this._idxWeight = 0;
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
	public int violations() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAddEdgeDelta(VarGraph vt, Edge e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRemoveEdgeDelta(VarGraph vt, Edge e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getReplaceEdgeDelta(VarGraph vt, Edge ei, Edge eo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAddEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRemoveEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getReplaceEdgeVarRootedTree(VarRootedTree vt, Edge ei, Edge eo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNodeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSubTreeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		// TODO Auto-generated method stub
		return 0;
	}

}
