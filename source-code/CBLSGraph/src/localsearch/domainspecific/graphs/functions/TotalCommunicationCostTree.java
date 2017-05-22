package localsearch.domainspecific.graphs.functions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Graph;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;
import localsearch.domainspecific.graphs.invariants.InsertableEdgesVarRootedTree;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

public class TotalCommunicationCostTree implements GFunction {
	
	HashMap<Node, Integer> subTreeSize;
	HashMap<Node, Double> subTreeSumWeight;
	HashMap<Node, Double> subTreeSumWeightMulSiz;
	HashMap<Node, Double> tmpValue;
	HashMap<Node, Double> sumWeightFromRoot; // Sum weight from root to node x
	private LSGraphManager mgr;
	int value;
	int idxWeight;
	int id;
	VarRootedTree vt;
	public TotalCommunicationCostTree(VarRootedTree vt, int idxWeight){
		this.vt = vt;
		this.idxWeight = idxWeight;
		mgr = vt.getLSGraphManager(); 
		vt.getLSGraphManager().post(this);
	}
	
	void init(){
		value = 0;
		subTreeSize = new HashMap<Node, Integer>();
		subTreeSumWeight = new HashMap<Node, Double>();
		subTreeSumWeightMulSiz = new HashMap<Node, Double>();
		tmpValue = new HashMap<Node, Double>();
		sumWeightFromRoot = new HashMap<>();
		sumWeightFromRoot.put(vt.root(), 0.);
		dfs1(vt.root());
		tmpValue.put(vt.root(), 0.);
		dfs2(vt.root());
	}
	void dfs1(Node p){
		int n = vt.getLUB().getNbrNodes();
		int sts = 1;
		double stsw = 0;
		double stswms = 0;
		HashSet<Edge> adj = vt.getAdj(p);
		
		if(adj!=null){
			for(Edge e : adj){
				Node otherNode = e.otherNode(p);
				if(p == vt.root() || otherNode != vt.getFatherNode(p)){
					sumWeightFromRoot.put(otherNode, sumWeightFromRoot.get(p) + e.getWeight(idxWeight));
					
					dfs1(otherNode);
					int nChild = subTreeSize.get(otherNode);
					sts += nChild;
					stsw += subTreeSumWeight.get(otherNode);
					stswms += subTreeSumWeightMulSiz.get(otherNode);
					double w = e.getWeight(idxWeight);
					stsw += w;
					stswms += w*nChild;
					value += w * nChild*(n - nChild);
				}
			}
		}
		subTreeSize.put(p,  sts);
		subTreeSumWeight.put(p,  stsw);
		subTreeSumWeightMulSiz.put(p, stswms);
	}
	void dfs2(Node p){
		HashSet<Edge> adj = vt.getAdj(p);
		int n = vt.getLUB().getNbrNodes();
		if(adj != null){
			for(Edge e : adj){
				Node onode = e.otherNode(p);
				if(p == vt.root() || onode != vt.getFatherNode(p)){
					double v = tmpValue.get(p) + subTreeSumWeightMulSiz.get(p) - subTreeSumWeightMulSiz.get(onode) - e.getWeight(idxWeight)*subTreeSize.get(onode);
					v += e.getWeight(idxWeight)*(n - subTreeSize.get(onode));
					tmpValue.put(onode, v);
					dfs2(onode);
				}
			}
		}
	}
	
	@Override
	public VarGraph[] getVarGraphs() {
		return null;
	}
	@Override
	public LSGraphManager getLSGraphManager() {
		return this.mgr;
	}
	@Override
	public void setID(int id) {
		this.id = id;
	}
	@Override
	public int getID() {
		return this.id;
	}
	@Override
	public void initPropagate() {
		init();
	}
	@Override
	public void propagateAddEdge(VarGraph vt, Edge e) {
		initPropagate();
	}
	@Override
	public void propagateRemoveEdge(VarGraph vt, Edge e) {
		initPropagate();
	}
	@Override
	public void propagateReplaceEdge(VarGraph vt, Edge eo, Edge ei) {
		initPropagate();
	}
	@Override
	public double getAddEdgeDelta(VarGraph vt, Edge e) {
		return getAddEdgeVarRootedTree((VarRootedTree) vt, e);
	}
	@Override
	public double getRemoveEdgeDelta(VarGraph vt, Edge e) {
		return getAddEdgeVarRootedTree((VarRootedTree) vt, e);
	}
	@Override
	public double getReplaceEdgeDelta(VarGraph vt, Edge ei, Edge eo) {
		return getReplaceEdgeVarRootedTree((VarRootedTree) vt, ei, eo);
	}
	@Override
	public double getValue() {
		return value;
	}

	private double getCommunicationCostEdge(double weight, int subTreeSize, int numberOfNodes) {
		return weight * subTreeSize * (numberOfNodes - subTreeSize);
	}
	
	public static void main(String []args){
		try {
			Scanner in = new Scanner(new File("D:\\Project\\github\\CBLSGraph\\source-code\\CBLSGraph\\data\\g.txt"));
			HashMap<Integer, Node> m = new HashMap<>();
			UndirectedGraph ug = new UndirectedGraph();
			while(in.hasNext()){
				int vid = in.nextInt();
				if(vid == -1) break;
				Node v = new Node(vid);
				m.put(vid, v);
			}
			
			ArrayList<Edge> leofTree = new ArrayList<>();
			int eid = -1;
			while(in.hasNext()){
				int uid = in.nextInt();
				if(uid == -1) break;
				int vid = in.nextInt();
				eid++;
				Edge e = ug.addEdgeByID(eid,uid,vid);
				double []w = new double[1];
				w[0] = in.nextDouble();
				e.setWeights(w);
				int fl = in.nextInt();
				if(fl == 1){
					leofTree.add(e);
				}
			}
			in.close();
			
			LSGraphManager mgr = new LSGraphManager();
			Node r = ug.getNodeByID(1);
			VarRootedTree t = new VarRootedTree(mgr,ug,r);
			InsertableEdgesVarRootedTree I = new InsertableEdgesVarRootedTree(t);
			TotalCommunicationCostTree tcct = new TotalCommunicationCostTree(t, 0);
			mgr.close();
			
			for(Edge e : leofTree){
				t.addEdgePropagate(e);
				tcct.propagateAddEdge(t, e);
			}
			
			
			System.out.println(tcct.value);
			
//			int ei = 7;
//			int eo = 0;
			Node p = ug.getNodeByID(5);
			Node q = ug.getNodeByID(4);
			System.out.println("del " +tcct.getReplaceEdgeDelta(t, ug.getEdge(ug.getNodeByID(1), ug.getNodeByID(5)), ug.getEdge(ug.getNodeByID(4), ug.getNodeByID(5))));
			System.out.println("Subtree oprerator: " + p.getID() + " to " + q.getID());
			double subTreeOptTreeDelta = tcct.getSubTreeOptVarRootedTree(t, p ,q);
			System.out.println("Prediction: plus " + subTreeOptTreeDelta + " to be " + (tcct.value + subTreeOptTreeDelta));
			t.subTreeOptVarRootedTreePropagate(p, q);
			tcct.propagateSubTreeOptVarRootedTree(t, p, q);
			System.out.println("Result: " + tcct.getValue());

//			System.out.println("\n");

//			System.out.println("Node operator: " + p.getID() + " to " + q.getID());
//			double nodeOptTreeDelta = tcct.getNodeOptVarRootedTree(t, p, q);
//			System.out.println("Prediction: plus " + nodeOptTreeDelta + " to be " + (tcct.value + nodeOptTreeDelta));
//			t.nodeOptVarRootedTreePropagate(p, q);
//			tcct.propagateNodeOptVarRootedTree(t, p, q);
//			System.out.println("Result: " + tcct.getValue());

//			t.replaceEdgePropagate(ug.getEdgeByID(eo), ug.getEdgeByID(ei));
//			mgr.replaceEdge(t, ug.getEdgeByID(eo), ug.getEdgeByID(ei));
//			tcct.init();
			
//			System.out.println(tcct.value);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void propagateAddEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		initPropagate();
	}

	@Override
	public void propagateRemoveEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		initPropagate();
	}

	@Override
	public void propagateReplaceEdgeVarRootedTree(VarRootedTree vt, Edge ei,
			Edge eo) {
		initPropagate();
	}

	@Override
	public void propagateNodeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		initPropagate();
	}

	@Override
	public void propagateSubTreeOptVarRootedTree(VarRootedTree vt, Node v,
			Node u) {
		initPropagate();
	}

	@Override
	public double getAddEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		Node n1 = e.getBegin();
		Node n2 = e.getEnd();
		if(vt.contains(n2)){
			Node tm = n1;
			n1 = n2;
			n2 = tm;
		}
		return tmpValue.get(n1) + e.getWeight(idxWeight)*vt.getLUB().getNbrNodes();
	}

	@Override
	public double getRemoveEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		Node n1 = e.getBegin();
		Node n2 = e.getEnd();
		if(vt.nca(n1, n2) == n2){
			Node tm = n1;
			n1 = n2;
			n2 = tm;
		}
		return -tmpValue.get(n1) - e.getWeight(idxWeight)*(vt.getLUB().getNbrNodes()-1);
	}

	@Override
	public double getReplaceEdgeVarRootedTree(VarRootedTree vt, Edge ei, Edge eo) {
		Node p = eo.getBegin();
		Node q = eo.getEnd();
		if(p!=vt.root() && vt.getFatherNode(p) == q){
			Node tmp = p;
			p = q;
			q = tmp;
		}
		double delta = 0;
		int n = vt.getLUB().getNbrNodes();
//		System.out.println(q);
//		System.out.println(subTreeSize);
		int csz = subTreeSize.get(q);
		delta -= csz * tmpValue.get(q);
		delta -= (n - csz)* subTreeSumWeightMulSiz.get(q);
		System.out.println(delta);

		Node ei1 = ei.getBegin();
		Node ei2 = ei.getEnd();
		if(vt.nca(q, ei1) != q){
			Node tmp = ei1;
			ei1 = ei2;
			ei2 = tmp;
		}
		Node nca_q_ei2 = vt.nca(q, ei2);
		double ad1 = csz * (tmpValue.get(ei2) + subTreeSumWeightMulSiz.get(ei2) - subTreeSumWeightMulSiz.get(q)
				- csz *(sumWeightFromRoot.get(q) + sumWeightFromRoot.get(ei2) - 2*sumWeightFromRoot.get(nca_q_ei2) ));
		double ad2 = ei.getWeight(idxWeight)* csz * (n - csz);
		double ad3 = (n - csz) * (tmpValue.get(ei1) + subTreeSumWeightMulSiz.get(ei1) - tmpValue.get(q) -
				(sumWeightFromRoot.get(ei1) - sumWeightFromRoot.get(q))*(n - csz));
		/*System.out.println(ad1);
		System.out.println(ad2);
		System.out.println(ad3);*/
		delta += ad1 + ad2 + ad3;
		return delta;
	}

	private double getDeltaOnPath(Node source, Node des, Node nca, double numberOfMovingVertices) {
		return numberOfMovingVertices * (-tmpValue.get(source) + tmpValue.get(des)
			+ subTreeSumWeightMulSiz.get(des) - subTreeSumWeightMulSiz.get(source)
			- numberOfMovingVertices * (sumWeightFromRoot.get(source) + sumWeightFromRoot.get(des) - 2 * sumWeightFromRoot.get(nca)));
	}

	@Override
	public double getNodeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		Node fatherOfV = vt.getFatherNode(v);
		Node fatherOfU = vt.getFatherNode(u);
		Graph graph = vt.getLUB();
		int subTreeSizeV = subTreeSize.get(v);
		int subTreeSizeU = subTreeSize.get(u);
		double delta = 0;
		int n = vt.getNodes().size();
		Node ncaUAndV = vt.nca(u, v);

		double removeUAndFU = - getCommunicationCostEdge(vt.getFatherEdge(u).getWeight(idxWeight), subTreeSizeU, n);
		System.out.println("Remove edge between u(" + u.getID() + ") and father of u(" + fatherOfU.getID()+ "): " + removeUAndFU);

		double removeVAndFV = - getCommunicationCostEdge(vt.getFatherEdge(v).getWeight(idxWeight), subTreeSizeV, n);
		System.out.println("Remove edge between v(" + v.getID() + ") and father of v(" + fatherOfV.getID() + "): " + removeVAndFV);

		double addVAndFU = getCommunicationCostEdge(graph.getEdge(v, fatherOfU).getWeight(idxWeight),
				ncaUAndV == u ? subTreeSizeU : (subTreeSizeU + 1), n);
		System.out.println("Add edge between v(" + v.getID() + ") and father of u(" + fatherOfU.getID() + "): " + addVAndFU);

		double addUAndV = getCommunicationCostEdge(graph.getEdge(u, v).getWeight(idxWeight),
				ncaUAndV == u ? (subTreeSizeU - 1) : subTreeSizeU, n);
		System.out.println("Add edge between v(" + v.getID() + ") and u(" + u.getID() + "): " + addUAndV);

		double removeChildrenOfVAndV = 0;
		for (Edge edge : vt.getAdj(v)) {
			Node childOfV = edge.otherNode(v);
			if (childOfV == vt.getFatherNode(v)) continue;
			removeChildrenOfVAndV -= getCommunicationCostEdge(edge.getWeight(idxWeight), subTreeSize.get(childOfV), n);
			System.out.println("Remove edge between v(" + v.getID() + ") and child (" + childOfV + ")");
		};
		System.out.println("Total: " + removeChildrenOfVAndV);

		double addChildrenOfVAndFV = 0;
		for (Edge edge : vt.getAdj(v)) {
			Node childOfV = edge.otherNode(v);
			if (childOfV == vt.getFatherNode(v)) continue;
			addChildrenOfVAndFV += getCommunicationCostEdge(graph.getEdge(childOfV, fatherOfV).getWeight(idxWeight), subTreeSize.get(childOfV), n);
			System.out.println("Add edge between father of v(" + fatherOfV.getID() + ") and child of v(" + childOfV.getID() + ")");
		}
		System.out.println("Total: " + addChildrenOfVAndFV);

		delta = removeUAndFU + removeVAndFV + removeChildrenOfVAndV + addVAndFU + addUAndV + addChildrenOfVAndFV;

		if (ncaUAndV == u) {
			delta += getDeltaOnPath(fatherOfV, u , vt.nca(u, fatherOfV), 1);
		} else {
			delta += getDeltaOnPath(fatherOfV, fatherOfU, vt.nca(fatherOfU, fatherOfV), 1);
		}
		return delta;
	}

	@Override
	public double getSubTreeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		Node fatherOfV = vt.getFatherNode(v);
		Node fatherOfU = vt.getFatherNode(u);
		Graph graph = vt.getLUB();
		int subTreeSizeV = subTreeSize.get(v);
		int subTreeSizeU = subTreeSize.get(u);
		int n = graph.getNbrNodes();

		double removeVtoFatherV = - getCommunicationCostEdge(vt.getFatherEdge(v).getWeight(this.idxWeight), subTreeSizeV, n);
		System.out.println("remove edge between V(" + v.getID() +  ") and father of V(" + fatherOfV.getID() + "): " + removeVtoFatherV);

		double removeUtoFatherU = - getCommunicationCostEdge(vt.getFatherEdge(u).getWeight(this.idxWeight), subTreeSizeU, n);
		System.out.println("remove edge between U(" + u.getID() + ") and father of U(" + fatherOfU.getID() + "): " + removeUtoFatherU);

		if (vt.nca(u,v) == u) {
			subTreeSizeU -= subTreeSizeV;
		}
		double addVtoFatherU = + getCommunicationCostEdge(graph.getEdge(v, fatherOfU).getWeight(this.idxWeight), subTreeSizeU + subTreeSizeV, n);
		System.out.println("add edge between V(" + v.getID() +") and father of U(" + fatherOfU.getID() + "): " + addVtoFatherU);

		double addUtoV = + getCommunicationCostEdge(graph.getEdge(u, v).getWeight(this.idxWeight), subTreeSizeU, n);
		System.out.println("add edge between U(" + u.getID() + ") and V(" + v.getID() +"): " + addUtoV);

		double delta = removeUtoFatherU + removeVtoFatherV + addUtoV + addVtoFatherU;

		if (vt.nca(v,u) == u) {
			delta += getDeltaOnPath(fatherOfV, u, vt.nca(fatherOfV, u), subTreeSizeV);
		} else {
			delta += getDeltaOnPath(fatherOfV, fatherOfU, vt.nca(fatherOfU, fatherOfV), subTreeSizeV);
		}

		return delta;
	}
}
