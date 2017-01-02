package localsearch.domainspecific.graphs.constraints;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class TreeEdgeDisjoint implements GConstraint {
	
	public TreeEdgeDisjoint(VarRootedTree vt1, VarRootedTree vt2){
		
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
	public int violations() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public VarGraph[] getVarGraphs() {
		return new VarGraph[0];
	}

	@Override
	public LSGraphManager getLSGraphManager() {
		return null;
	}

	@Override
	public void setID(int id) {

	}

	@Override
	public int getID() {
		return 0;
	}

	@Override
	public void initPropagate() {

	}

	@Override
	public void propagateAddEdge(VarGraph vt, Edge e) {

	}

	@Override
	public void propagateRemoveEdge(VarGraph vt, Edge e) {

	}

	@Override
	public void propagateReplaceEdge(VarGraph vt, Edge eo, Edge ei) {

	}
}
