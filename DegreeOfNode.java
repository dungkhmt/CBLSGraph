package localsearch.domainspecific.graphs.functions;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.invariants.NodeDegree;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class DegreeOfNode implements GFunction {

	private LSGraphManager _mgr;
	private Node _node;
	private NodeDegree _nodeDegree;
	private VarGraph[] _varGraphs;
	private VarGraph _vg;
	private  double _value;
	private int _id;

	public DegreeOfNode(VarGraph vg, Node u, NodeDegree nd) {
		_vg = vg;
		_varGraphs = new VarGraph[1];
		_varGraphs[0] = vg;
		_mgr = vg.getLSGraphManager();
		_nodeDegree = nd;
		_node = u;
		_mgr.post(this);
	}
	@Override
	public VarGraph[] getVarGraphs() {
		// TODO Auto-generated method stub
		return _varGraphs;
	}

	@Override
	public LSGraphManager getLSGraphManager() {
		return _mgr;
	}

	@Override
	public void setID(int id) {
		_id = id;
	}

	@Override
	public int getID() {
		return _id;
	}

	@Override
	public void initPropagate() {
		_value = _nodeDegree.getDegree(_node);

	}

	@Override
	public void propagateAddEdge(VarRootedTree vt, Edge e) {
		if (e.contains(_node)) _value++;

	}

	@Override
	public void propagateRemoveEdge(VarRootedTree vt, Edge e) {
		if (e.contains(_node)) _value--;

	}

	@Override
	public void propagateReplaceEdge(VarRootedTree vt, Edge eo, Edge ei) {
		if (ei.contains(_node)) _value++;
		if (eo.contains(_node)) _value--;

	}

	@Override
	public double getAddEdgeDelta(VarRootedTree vt, Edge e) {
		if (e.contains(_node)) return 1;
		return 0;
	}

	@Override
	public double getRemoveEdgeDelta(VarRootedTree vt, Edge e) {
		if (e.contains(_node)) return -1;
		return 0;
	}

	@Override
	public double getReplaceEdgeDelta(VarRootedTree vt, Edge ei, Edge eo) {
		int tmp = 0;
		if (ei.contains(_node)) tmp++;
		if (eo.contains(_node)) tmp--;
		return tmp;
	}

	@Override
	public double getValue() {
		// TODO Auto-generated method stub
		return _value;
	}

}
