package localsearch.domainspecific.graphs.constraints.Leq;

import localsearch.domainspecific.graphs.constraints.GConstraint;
import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.functions.GFunction;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class GLeqFunctionFunction implements GConstraint {
	private GFunction _f1;
	private GFunction _f2;
	private int _violations;
	public GLeqFunctionFunction(GFunction f1, GFunction f2){
		
	}
	
	@Override
	public int getAddEdgeDelta(VarGraph vt, Edge e) {
		// TODO Auto-generated method stub
		double d1 = _f1.getAddEdgeDelta(vt, e);
		double d2 = _f2.getAddEdgeDelta(vt, e);
		double nv1 = _f1.getValue() + d1;
		double nv2 = _f2.getValue() + d2;
		int newViolations = nv1 <= nv2 ? 0 : (int)(Math.ceil(nv1 - nv2));
		return newViolations - _violations;
	}
	public int getRemoveEdgeDelta(VarGraph vt, Edge e){
		return -1;
	}
	public int getReplaceEdgeDelta(VarGraph vt, Edge ei, Edge eo){
		return -1;
	}

	public int violations(){
		return _violations;
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
