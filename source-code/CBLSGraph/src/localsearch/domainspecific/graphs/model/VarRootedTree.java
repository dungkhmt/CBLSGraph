/*
 * authors: DungPQ
 * date 12/7/2015
 */

package localsearch.domainspecific.graphs.model;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;
import localsearch.domainspecific.graphs.invariants.InsertableEdgesVarRootedTree;
import localsearch.domainspecific.graphs.invariants.RemovableEdgesVarRootedTree;
import localsearch.domainspecific.graphs.utils.Utility;

public class VarRootedTree extends VarUndirectedGraph {
	private LSGraphManager mgr;
	private UndirectedGraph lub;
	private Node root;
	private HashMap<Node, Node> father;
	private Random R = new Random();
	
	public VarRootedTree(LSGraphManager mgr, UndirectedGraph lub, Node root){
		super(mgr,lub);
		this.mgr = mgr;
		this.lub = lub;
		this.root = root;
		father = new HashMap<Node, Node>();
		
		UndirectedGraph t = Utility.genRandomRootedTree(lub,root,lub.getNbrNodes()-1);
		allocateNCA();
		initComputation(root, t.getNodes(), t.getEdges(), t.getAdj());
	}
	private void allocateNCA(){
	}
	private void initComputation(Node root, HashSet<Node> nodes, HashSet<Edge> edges, HashMap<Node, HashSet<Edge>> Adj){
		initComputationTreeDataStructure(nodes, edges);
		initComputationNCAandFather(root,nodes,edges,Adj);
	}
	private void initComputationTreeDataStructure(HashSet<Node> nodes, HashSet<Edge> edges){
		this.nodes.clear();
		this.edges.clear();
		this.Adj.clear();
		if(nodes == null || nodes.size() == 0)
			return;
		
		for(Node nod : nodes){
			this.nodes.add(nod);
			
		}
		for(Edge e : edges){
			this.edges.add(e);
			Node u = e.getBegin();
			Node v = e.getEnd();
			if(this.Adj.get(u) == null) this.Adj.put(u, new HashSet<Edge>());
			if(this.Adj.get(v) == null) this.Adj.put(v, new HashSet<Edge>());
			
			this.Adj.get(u).add(e);
			this.Adj.get(v).add(e);
		}
			
	}
	private void initComputationNCAandFather(Node root, HashSet<Node> nodes, HashSet<Edge> edges, HashMap<Node, HashSet<Edge>> Adj){
		if(nodes == null || nodes.size() == 0){
			return;
		}
	}
	public Node nca(Node u, Node v){
		//TODO
		return null;
	}
	public Node root(){
		return this.root;
	}
	public Node getFatherNode(Node u){
		//TODO
		return father.get(u);
	}
	public Edge getFatherEdge(Node u){
		//TODO
		Node v = father.get(u);
		return lub.edge(u, v);
	}
	public boolean dominate(Node u, Node v){
		return nca(u,v) == u;
	}
	public String name(){ return "VarRootedTree";}
	public void print(){
		System.out.println(name() + "::print, nodes = " + Utility.setNode2String(nodes) + ", edges = " + Utility.setEdge2String(edges));
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
	private void updateRMQWhenRemove(Node cv){
	}
	
	public boolean removeEdge(Edge e){
		if(!contains(e)) return false;
		
		Node fv = e.getBegin();
		Node cv = e.getEnd();
		if(!contains(e)){
			System.out.println(name() + "::removeEdge(" + fv.getID() + "," + cv.getID() + ") -> exception: this edge does not belong to the tree");
			System.exit(-1);
		}
		if(father.get(cv) != fv){
			fv = e.getEnd();
			cv = e.getBegin();
		}
		if(Adj.get(cv).size() != 1){
			System.out.println(name() + "::removeEdge(" + fv.getID() + "," + cv.getID() + ") -> exception: this edge is not a leaf of the tree");
			System.exit(-1);	
		}
		edges.remove(e);
		Adj.get(fv).remove(e);
		Adj.get(cv).remove(e);
		nodes.remove(cv);
		updateRMQWhenRemove(cv);	
		return true;
	}
	public boolean isNull(){
		return edges.size() == 0;
	}
	private void updateRMQWhenAdd(Node leaf, Node other){
		//range R = _lub.getVerticesRange();
	}

	public boolean addEdge(Edge e){
		if(contains(e)) return false;
		
		Node leaf = e.getBegin();
		Node other = e.getEnd();
		if(contains(leaf)){
			leaf = e.getEnd();
			other = e.getBegin();
		}
		if(isNull()){
			edges.add(e);
			nodes.add(leaf);
			nodes.add(other);
			if(Adj.get(leaf) == null) Adj.put(leaf, new HashSet<Edge>());
			if(Adj.get(other) == null) Adj.put(other, new HashSet<Edge>());
			Adj.get(leaf).add(e);
			Adj.get(other).add(e);
			father.put(leaf, other);
			initComputationNCAandFather(root, nodes, edges, Adj);
			return true;
		}
		if(contains(leaf) && contains(other)){
			System.out.println(name() + "::addEdge" + e.toString() + " exception: two endpoints are belongs to the tree, this will create a cycle");
			print();
			System.exit(-1);
		}
		if(!contains(leaf) && !contains(other)){
			System.out.println(name() + "::addEdge" + e.toString() + " exception: two endpoints are not belongs to the tree, this will create two connected components");
			System.exit(-1);
		}
		
		edges.add(e);
		nodes.add(leaf);
		if(Adj.get(leaf) == null) Adj.put(leaf, new HashSet<Edge>());
		if(Adj.get(other) == null) Adj.put(other, new HashSet<Edge>());
		Adj.get(leaf).add(e);
		Adj.get(other).add(e);
		father.put(leaf, other);
	
		updateRMQWhenAdd(leaf,other);
		return true;
	}
	private void updateRMQWhenReplace(Node u1, Node v1, Node u2, Node v2){
	}
	private void reverseFather(Node v1, Node v2){
		Node p = v1;
		Node fp = father.get(p);
		while(p != v2){
			Node tmp = fp;
			fp = father.get(fp);
			father.put(tmp,p);
			p = tmp;
		}
	}
	
	public boolean replaceEdge(Edge eo, Edge ei){
		if(!(contains(eo) && !contains(ei))) return false;
		
		Node u1 = ei.getBegin();
		Node v1 = ei.getEnd();
		Node u2 = eo.getBegin();
		Node v2 = eo.getEnd();
		Node r = nca(u1,v1);
		if(dominate(v2, u2)){
			u2 = eo.getEnd();
			v2 = eo.getBegin();
		}
		if(dominate(v2, u1)){
			u1 = ei.getEnd();
			v1 = ei.getBegin();
		}
		reverseFather(v1, v2);
		father.put(v1, u1);
		edges.remove(eo);
		Adj.get(u2).remove(eo);
		Adj.get(v2).remove(eo);
		edges.add(ei);
		Adj.get(u1).add(ei);
		Adj.get(v1).add(ei);
		updateRMQWhenReplace(u1,v1,u2,v2);
		return true;
	}

	public void removeEdgePropagate(Edge e){
		//TODO
		//if(removeEdge(e))
		mgr.removeEdge(this, e);
		removeEdge(e);
	}
	public void addEdgePropagate(Edge e){
		//TODO
		//if(addEdge(e))
		mgr.addEdge(this, e);
		addEdge(e);
	}
	public void replaceEdgePropagate(Edge eo, Edge ei){
		//TODO
		//if(replaceEdge(eo,ei))
		mgr.replaceEdge(this, eo, ei);
		replaceEdge(eo, ei);
	}
	
	public static void main(String[] args){
		try{
			Scanner in = new Scanner(new File("data\\graphs\\g.txt"));
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
			ug.print();
			
			
			LSGraphManager mgr = new LSGraphManager();
			Node r = ug.getNodeByID(1);
			VarRootedTree t = new VarRootedTree(mgr,ug,r);
			InsertableEdgesVarRootedTree I = new InsertableEdgesVarRootedTree(t);
			RemovableEdgesVarRootedTree R = new RemovableEdgesVarRootedTree(t);
			mgr.close();
			
			t.print();
			System.out.println("Init I = " + Utility.setEdge2String(I.getEdges()) + ", R = " + 
			Utility.setEdge2String(R.getEdges()));
			
		}catch(Exception ex){
			ex.printStackTrace();
		}

		
	}
}
