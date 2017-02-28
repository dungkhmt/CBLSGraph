package localsearch.domainspecific.graphs.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.invariants.NearestCommonAncestor;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;
import localsearch.domainspecific.graphs.invariants.InsertableEdgesVarRootedTree;

public class VarRootedTree extends VarTree {
	
	private Node root; 
	private HashMap<Node, Node> fatherNode;
	private HashMap<Node, Edge> fatherEdge;
	private NearestCommonAncestor NCA;

	private HashMap<Node, HashMap<Node, Edge>> euv;

	public VarRootedTree(LSGraphManager mgr, UndirectedGraph lub, Node root){
		super(mgr, lub);
		this.root = root;
		super.addNode(root);//nodes.add(root);
		fatherNode = new HashMap<Node, Node>();
		fatherEdge = new HashMap<Node, Edge>();
		NCA = new NearestCommonAncestor(this);

		euv = new HashMap<Node, HashMap<Node, Edge>>();
		for (Edge e : lub.getEdges()) {
			if (!euv.containsKey(e.getBegin())) {
				euv.put(e.getBegin(), new HashMap<Node, Edge>());
			}
			if (!euv.containsKey(e.getEnd())) {
				euv.put(e.getEnd(), new HashMap<Node, Edge>());
			}
			euv.get(e.getBegin()).put(e.getEnd(), e);
			euv.get(e.getEnd()).put(e.getBegin(), e);
		}
		initTree();
	}

	private void initTree() {

	}

	public Edge getEdge(Node u, Node v) {
		return euv.get(u).get(v);
	}

	public Node nca(Node u, Node v) {
		if (!contains(u) || ! contains(v)) {
			return null;
		}
		return NCA.nca(u, v);
	}
	
	public Node root() {
		return root;
	}
	
	public Node getFatherNode(Node u) {
		return fatherNode.get(u);
	}
	
	public Edge getFatherEdge(Node u) {
		return fatherEdge.get(u);
	}
	
	public boolean dominate(Node u, Node v) {
		return nca(u, v) == u;
	}
	
	public NearestCommonAncestor getNCA() {
		return NCA;
	}
	
	public String name() { 
		return "VarRootedTree";
	}
	
	public void print() {
		//System.out.println(name() + "::print, nodes = " + Utility.setNode2String(nodes) + ", edges = " + Utility.setEdge2String(edges));
		for(Node v : nodes){
			System.out.print(name() + "::print, node " + v.getID() + " : ");
			if(Adj.get(v) != null)for(Edge e : Adj.get(v)){
				System.out.print(e.toString() + ", ");
			}
			System.out.println();
		}
		for(Node v : nodes){
			for(Node u : nodes){
				if(u.getID() > v.getID()){
					Node n = nca(u,v);
					System.out.println(name() + "::print, nca(" + v.getID() + "," + u.getID() + ") = " + n.getID());
				}
			}
		}
	}

	public boolean removeEdge(Edge e){
		Node fv = e.getBegin();
		Node cv = e.getEnd();
		if(fatherNode.get(cv) != fv){
			fv = e.getEnd();
			cv = e.getBegin();
		}
		super.removeEdge(e);
		if (nodes.isEmpty()) {
			nodes.add(root);
		}
		fatherNode.remove(cv);
		fatherEdge.remove(cv);
		return true;
	}

	public boolean addEdge(Edge e){
		Node leaf = e.getBegin();
		Node other = e.getEnd();
		if(contains(leaf)){
			leaf = e.getEnd();
			other = e.getBegin();
		}
		super.addEdge(e);
		fatherNode.put(leaf, other);
		fatherEdge.put(leaf, e);
		return true;
	}
	
	private void reverseFather(Node v1, Node v2){
		Edge tmp = fatherEdge.get(v1);
		while (v1 != v2) {
			Node fv = tmp.otherNode(v1);
			Edge next = fatherEdge.get(fv);
			fatherNode.put(fv, v1);
			fatherEdge.put(fv, tmp);
			tmp = next;
			v1 = fv;
		}
	}
	
	public boolean replaceEdge(Edge eo, Edge ei){
		Node uo = eo.getBegin();
		Node vo = eo.getEnd();
		Node ui = ei.getBegin();
		Node vi = ei.getEnd();
		if (fatherNode.get(uo) == vo) {
			uo = eo.getEnd();
			vo = eo.getBegin();
		}
		Node x = nca(ui, vo);
		Node y = nca(vi, vo);
		if (x == vo) {
			ui = ei.getEnd();
			vi = ei.getBegin();
		}
		super.addEdge(ei);
		super.removeEdge(eo);
		reverseFather(vi, vo);
		fatherNode.put(vi, ui);
		fatherEdge.put(vi, ei);
		return true;
	}

	public void removeEdgePropagate(Edge e){
		if (!contains(e)) {
			System.out.println(name() + "::removeEdge(" + e.getBegin().getID() + "," + e.getEnd().getID() + ") -> exception: this edge does not belong to the tree");
			System.exit(-1);
		}

		Node fv = e.getBegin();
		Node cv = e.getEnd();
		if(fatherNode.get(cv) != fv){
			fv = e.getEnd();
			cv = e.getBegin();
		}
		if(Adj.get(cv).size() != 1){
			System.out.println(name() + "::removeEdge(" + fv.getID() + "," + cv.getID() + ") -> exception: this edge is not a leaf of the tree");
			System.exit(-1);
		}
		removeEdge(e);
		//mgr.removeEdge(this, e);
		mgr.removeEdgeVarRootedTree(this, e);
	}
	
	public void addEdgePropagate(Edge e){
		if (contains(e)) {
			System.out.println(name() + "::addEdge" + e.toString() + " this edge does belong to the tree");
			System.exit(-1);
		}
		Node leaf = e.getBegin();
		Node other = e.getEnd();
		if(contains(leaf)){
			leaf = e.getEnd();
			other = e.getBegin();
		}
		if (!contains(other)) {
			System.out.println(name() + "::addEdge" + e.toString() + " exception: two endpoints are not belongs to the tree, this will create two connected components");
			System.exit(-1);
		}
		if(contains(leaf)){
			System.out.println(name() + "::addEdge" + e.toString() + " exception: two endpoints are belongs to the tree, this will create a cycle");
			System.exit(-1);
		}
		addEdge(e);
		//mgr.addEdge(this, e);
		mgr.addEdgeVarRootedTree(this, e);
	}
	
	public void replaceEdgePropagate(Edge eo, Edge ei){
		if (!(contains(eo) && !contains(ei))) {
			System.out.println(name() + "::replaceEdge" + ei.toString() + " this edge does belong to the tree, or " + eo + " this edge does not belong to the tree");
			System.exit(-1);
		}
		Node uo = eo.getBegin();
		Node vo = eo.getEnd();
		Node ui = ei.getBegin();
		Node vi = ei.getEnd();
		if (!contains(uo) || !contains(ui) || !contains(vo) || !contains(vi)) {
			System.out.println(name() + "::replaceEdge " + "(" + eo + ", " + ei + ") " + " there is some nodes that do not belong the tree");
			System.exit(-1);
		}
		if (fatherNode.get(uo) == vo) {
			uo = eo.getEnd();
			vo = eo.getBegin();
		}
		Node x = nca(ui, vo);
		Node y = nca(vi, vo);
		if ((x == vo ? 1 : 0) + (y == vo ? 1 : 0) != 1) {
			System.out.println(name() + "::replaceEdge " + "(" + eo + ", " + ei + ") " + " this will create two connected components and a cycle");
			System.exit(-1);
		}
		replaceEdge(eo, ei);
		//mgr.replaceEdge(this, eo, ei);
		mgr.replaceEdgeVarRootedTree(this, ei, eo);
	}

	public void nodeOptVarRootedTreePropagate(VarRootedTree vt, Node v, Node u) {

	}

	public void subTreeOptVarRootedTreePropagate(VarRootedTree vt, Node v, Node u) {

	}

	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(new File("data/g.txt"));
			HashMap<Integer, Node> m = new HashMap<Integer, Node>();
			UndirectedGraph ug = new UndirectedGraph();
			while(in.hasNext()){
				int vid = in.nextInt();
				if(vid == -1) break;
				Node v = new Node(vid);
				m.put(vid, v);
			}
			int eid = -1;
			while(in.hasNext()){
				int uid = in.nextInt();
				if(uid == -1) break;
				int vid = in.nextInt();
				eid++;
				ug.addEdgeByID(eid,uid,vid);
			}
			in.close();
			//ug.print();
			
			LSGraphManager mgr = new LSGraphManager();
			Node r = ug.getNodeByID(1);
			VarRootedTree t = new VarRootedTree(mgr,ug,r);
			InsertableEdgesVarRootedTree I = new InsertableEdgesVarRootedTree(t);
			
			mgr.close();
			
			
			long start = System.currentTimeMillis();
			for (int i = 1; i < m.size(); i++) {
				//System.out.print(i + "\n");
				for (Edge e : ug.getEdges()) {
					t.addEdge(e);
				}
			}
			t.print();
			//PrinterOutput fo = new PrinterOutput("NCA.txt");
			
			for(Node v : t.getNodes()) {
				for(Node u : t.getNodes()) { 
					System.out.println(v.getID() + " " + u.getID() + " " + t.nca(u, v).getID());
				}
			}
			
			long finish = System.currentTimeMillis();
			//fo.close();
			System.out.println((finish - start));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
