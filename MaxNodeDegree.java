package localsearch.domainspecific.graphs.functions;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.invariants.NodeDegree;
import localsearch.domainspecific.graphs.invariants.Pair;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

import java.util.*;


public class MaxNodeDegree implements GFunction {

	private LSGraphManager _mgr;
	private VarGraph[] _varGraphs;
	private VarGraph _vg;
	private double _value;
	private HashSet<Node> _nodes;
	private NodeDegree _nodeDegree;
	private Node[] _maxNodes;
	private HashMap<Integer,Node> _x;
	private TreeSet<localsearch.domainspecific.graphs.invariants.Pair> _tree;
	private int _id;

	public MaxNodeDegree(VarGraph vg, HashSet<Node> S, NodeDegree nd){
		_vg = vg;
		_varGraphs = new VarGraph[1];
		_varGraphs[0] = vg;
		_mgr = vg.getLSGraphManager();
		_nodes = S;
		_nodeDegree = nd;
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
		_tree = new TreeSet<Pair>(new Comparator<Pair>() {
			@Override
			public int compare(Pair o1, Pair o2) {
				return o1.second != o2.second? o2.second - o1.second : o2.first - o1.first;
			}
		});
		//int i = 0;
		_x = new HashMap<>();
		for (Node u : _nodes) {
			_value = Math.max(_nodeDegree.getDegree(u),_value);
			boolean add = _tree.add(new Pair(u.getID(), _nodeDegree.getDegree(u)));
			_x.put(u.getID(),u);
		}
		//Iterator<Pair> itr = _tree.iterator();

		int szMaxNodes = 0;
		_maxNodes = new Node[3];
		for (Pair p : _tree) {
			if (szMaxNodes >= 3) break;
			_maxNodes[szMaxNodes++] = _x.get(p.first);
		}

	}

	private void updateNode(Node u, int i) {
		if (!_nodes.contains(u)) return;
		_tree.remove(new Pair(u.getID(),_nodeDegree.getDegree(u)-i));
		_tree.add(new Pair(u.getID(),_nodeDegree.getDegree(u)));
	}

	private void updateMaxNodes() {
		int count = 0;
		for (Pair p : _tree) {
			if (count >= 3) break;
			_maxNodes[count++] = _x.get(p.first);
		}
		_value = _tree.first().second;
	}

	@Override
	public void propagateAddEdge(VarRootedTree vt, Edge e) {
		updateNode(e.getBegin(),1);
		updateNode(e.getEnd(),1);
		updateMaxNodes();
	}

	@Override
	public void propagateRemoveEdge(VarRootedTree vt, Edge e) {
		updateNode(e.getBegin(),-1);
		updateNode(e.getEnd(),-1);
		updateMaxNodes();
	}

	@Override
	public void propagateReplaceEdge(VarRootedTree vt, Edge eo, Edge ei) {
		updateNode(eo.getBegin(),-1);
		updateNode(ei.getBegin(),1);
		updateNode(eo.getEnd(),-1);
		updateNode(ei.getEnd(),1);
		updateMaxNodes();
	}

	@Override
	public double getAddEdgeDelta(VarRootedTree vt, Edge e) {
		double tmp = _value;
		if (_nodes.contains(e.getBegin())) tmp = Math.max(tmp,_nodeDegree.getDegree(e.getBegin())+1);
		if (_nodes.contains(e.getEnd())) tmp = Math.max(tmp,_nodeDegree.getDegree(e.getEnd())+1);
		return tmp - _value;
	}

	@Override
	public double getRemoveEdgeDelta(VarRootedTree vt, Edge e) {
		double tmp = _value;
		int tt = 0;
		for (Node u : _maxNodes) {
			tt = _nodeDegree.getDegree(u);
			if (e.getBegin() == u) tt--;
			if (e.getEnd() == u) tt--;
			tmp = Math.max(tmp,tt);
		}
		return tmp - _value;

	}

	@Override
	public double getReplaceEdgeDelta(VarRootedTree vt, Edge ei, Edge eo) {
		double tmp = _value;
		int tt;
		for (Node u : _maxNodes) {
			tt = _nodeDegree.getDegree(u);
			if (eo.getBegin() == u || eo.getEnd() == u) tt--;
			if (ei.getBegin() == u || ei.getEnd() == u) tt++;
			tmp = Math.max(tmp,tt);
		}
		return tmp - _value;
	}

	@Override
	public double getValue() {
		// TODO Auto-generated method stub
		return _value;
	}

}
