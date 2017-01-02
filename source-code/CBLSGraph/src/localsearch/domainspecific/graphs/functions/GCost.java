package localsearch.domainspecific.graphs.functions;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

import java.util.HashSet;

/*
 * 
 */

public class GCost implements GFunction {

	private double _value;
	private LSGraphManager _mgr;
	private VarGraph[] _varGraphs;
	private VarGraph _vg;
	private int _idxWeight;
	private int _id;

	public GCost(VarGraph[] vg, int idxWeight) {
		_varGraphs = vg;
		_idxWeight = idxWeight;
		_mgr = vg[0].getLSGraphManager();
		_mgr.post(this);
	}

	public GCost(VarGraph vg, int idxWeight) {
		_vg = vg;
		_idxWeight = idxWeight;
		_mgr = vg.getLSGraphManager();
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
		_id = id;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return _id;
	}

	@Override
	public void initPropagate() {
		_value = 0;
		for (VarGraph vg : _varGraphs) {
			HashSet<Edge> edge = vg.getEdges();
			for (Edge e : edge) {
				_value = _value + e.getWeight(_idxWeight);
			}
		}

	}

	@Override
	public void propagateAddEdge(VarGraph vt, Edge e) {
		_value += e.getWeight(_idxWeight);

	}

	@Override
	public void propagateRemoveEdge(VarGraph vt, Edge e) {
		_value -= e.getWeight(_idxWeight);

	}

	@Override
	public void propagateReplaceEdge(VarGraph vt, Edge eo, Edge ei) {
		_value = _value + ei.getWeight(_idxWeight) - eo.getWeight(_idxWeight);

	}

	@Override
	public double getAddEdgeDelta(VarGraph vt, Edge e) {
		return e.getWeight(_idxWeight);
	}

	@Override
	public double getRemoveEdgeDelta(VarGraph vt, Edge e) {
		return - e.getWeight(_idxWeight);
	}

	@Override
	public double getReplaceEdgeDelta(VarGraph vt, Edge ei, Edge eo) {
		return ei.getWeight(_idxWeight) - eo.getWeight(_idxWeight);
	}

	@Override
	public double getValue() {
		// TODO Auto-generated method stub
		return _value;
	}

}
