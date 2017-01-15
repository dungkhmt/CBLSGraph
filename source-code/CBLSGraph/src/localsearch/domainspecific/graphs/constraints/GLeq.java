package localsearch.domainspecific.graphs.constraints;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.functions.GFunction;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class GLeq implements GConstraint {
	
	private GConstraint _c;
	public GLeq(GFunction f1, GFunction f2){
		// f1 <= f2
		//_c = new GLeqFunctionFunction(f1, f2);
	}
	public GLeq(GFunction f, double v){
		//_c = new GLeqFunctionValue(f,v);
	}
	@Override
	public int getAddEdgeDelta(VarGraph vt, Edge e) {
		// TODO Auto-generated method stub
		return _c.getAddEdgeDelta(vt, e);
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
