package localsearch.domainspecific.graphs.functions;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class DiameterTree implements GFunction {

	public DiameterTree(VarRootedTree vt){
		
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
	public void propagateAddEdge(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propagateRemoveEdge(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void propagateReplaceEdge(VarRootedTree vt, Edge eo, Edge ei) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getAddEdgeDelta(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRemoveEdgeDelta(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getReplaceEdgeDelta(VarRootedTree vt, Edge ei, Edge eo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getValue() {
		// TODO Auto-generated method stub
		return 0;
	}

}
