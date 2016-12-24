package localsearch.domainspecific.graphs.functions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import localsearch.domainspecific.graphs.core.Edge;
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
	HashMap<Node, Double> sumWeightFromRoot;
	private LSGraphManager mgr;
	int value;
	int idxWeight;
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
				Node onode;
				if(e.getBegin() == p)
					onode = e.getEnd();
				else
					onode = e.getBegin();
				if(p == vt.root() || onode != vt.getFatherNode(p)){
					sumWeightFromRoot.put(onode, sumWeightFromRoot.get(p) + e.getWeight(idxWeight));
					
					dfs1(onode);
					int nchild = subTreeSize.get(onode);
					sts += nchild;
					stsw += subTreeSumWeight.get(onode);
					stswms += subTreeSumWeightMulSiz.get(onode);
					double w = e.getWeight(idxWeight);
					stsw += w;
					stswms += w*nchild;
					value += w * nchild*(n - nchild);
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
				Node onode;
				if(e.getBegin() == p)
					onode = e.getEnd();
				else
					onode = e.getBegin();
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
		init();
	}
	@Override
	public void propagateAddEdge(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		System.out.println("add edge");
		initPropagate();
	}
	@Override
	public void propagateRemoveEdge(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		initPropagate();
	}
	@Override
	public void propagateReplaceEdge(VarRootedTree vt, Edge eo, Edge ei) {
		// TODO Auto-generated method stub
		initPropagate();
	}
	@Override
	public double getAddEdgeDelta(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
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
	public double getRemoveEdgeDelta(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
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
	public double getReplaceEdgeDelta(VarRootedTree vt, Edge ei, Edge eo) {
		// TODO Auto-generated method stub
		Node p = eo.getBegin();
		Node q = eo.getEnd();
		if(p!=vt.root() && vt.getFatherNode(p) == q){
			Node tmp = p;
			p = q;
			q = tmp;
		}
		double delta = 0;
		int n = vt.getLUB().getNbrNodes();
		System.out.println(q);
		System.out.println(subTreeSize);
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
		double ad1 = csz * (tmpValue.get(ei2) + subTreeSumWeightMulSiz.get(ei2) - subTreeSumWeightMulSiz.get(q)- csz *(sumWeightFromRoot.get(q) + sumWeightFromRoot.get(ei2) - 2*sumWeightFromRoot.get(nca_q_ei2) ));
		double ad2 = ei.getWeight(idxWeight)* csz * (n - csz);
		double ad3 = (n - csz) * (tmpValue.get(ei1) + subTreeSumWeightMulSiz.get(ei1) - tmpValue.get(q) - (sumWeightFromRoot.get(ei1) - sumWeightFromRoot.get(q))*(n - csz));
		/*System.out.println(ad1);
		System.out.println(ad2);
		System.out.println(ad3);*/
		delta += ad1 + ad2 + ad3;
		return delta;
	}
	@Override
	public double getValue() {
		// TODO Auto-generated method stub
		return value;
	}
	
	public static void main(String []args){
		try {
			Scanner in = new Scanner(new File("g.txt"));
			HashMap<Integer, Node> m = new HashMap<Integer, Node>();
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
				e.setWeight(w);
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
				t.addEdge(e);
				tcct.propagateAddEdge(t, e);
			}
			
			
			System.out.println(tcct.value);
			
			int ei = 7;
			int eo = 0;
			System.out.println("del " +tcct.getReplaceEdgeDelta(t, ug.getEdgeByID(ei), ug.getEdgeByID(eo)));
			
			t.replaceEdgePropagate(ug.getEdgeByID(eo), ug.getEdgeByID(ei));
			mgr.replaceEdge(t, ug.getEdgeByID(eo), ug.getEdgeByID(ei));
			tcct.init();
			
			System.out.println(tcct.value);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
