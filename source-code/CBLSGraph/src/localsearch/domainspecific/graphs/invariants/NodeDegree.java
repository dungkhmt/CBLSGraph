package localsearch.domainspecific.graphs.invariants;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;

import java.util.HashMap;

public class NodeDegree implements GInvariant {
	//data structure maintaining the degree of every node of vg

	private LSGraphManager _mgr;
	private VarGraph _vg;
	private VarGraph[] _varGraphs;
	private int _id;
	private HashMap<Node, Integer> _degrees;

	public NodeDegree(VarGraph vg){
		this._vg = vg;
		this._mgr = vg.getLSGraphManager();
		_varGraphs = new VarGraph[1];
		_varGraphs[0] = vg;
		_mgr.post(this);
	}

	@Override
	public VarGraph[] getVarGraphs() {
		// TODO Auto-generated method stub
		return _varGraphs;
	}

	@Override
	public LSGraphManager getLSGraphManager() {
		// TODO Auto-generated method stub
		return _mgr;
	}

	@Override
	public void setID(int id) {
		// TODO Auto-generated method stub
		this._id = id;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return _id;
	}

	@Override
	public void initPropagate() {
		// TODO Auto-generated method stub
		_degrees = new HashMap<Node, Integer>();
		for (Node u : _vg.getLUB().getNodes()) {
			if (_vg.getAdj(u) == null) _degrees.put(u, 0);
			else _degrees.put(u, _vg.getAdj(u).size());
		}

	}

	@Override
	public void propagateAddEdge(VarGraph vt, Edge e) {
		for (Node u : vt.getLUB().getNodes()) {
			if (e.contains(u)) _degrees.replace(u, _degrees.get(u) +1);
		}

	}

	@Override
	public void propagateRemoveEdge(VarGraph vt, Edge e) {
		for (Node u : vt.getLUB().getNodes()) {
			if (e.contains(u)) _degrees.replace(u, _degrees.get(u) -1);
		}

	}

	@Override
	public void propagateReplaceEdge(VarGraph vt, Edge eo, Edge ei) {
		for (Node u : vt.getLUB().getNodes()) {
			int tmp = _degrees.get(u);
			if (ei.contains(u)) tmp++;
			if (eo.contains(u)) tmp--;
			_degrees.replace(u, tmp);
		}

	}

	public int getDegree(Node u) {
		if (_degrees.containsKey(u)) return _degrees.get(u);
		return 0;
	}

}
