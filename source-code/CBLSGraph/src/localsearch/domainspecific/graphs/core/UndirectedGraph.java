package localsearch.domainspecific.graphs.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.io.File;

public class UndirectedGraph extends Graph {
	
	private HashMap<Node, HashSet<Edge>> Adj;
	private HashMap<Node, HashMap<Node, Integer>> adjNode;
	
	public UndirectedGraph() {
		Adj = new HashMap<Node, HashSet<Edge>>();
		adjNode = new HashMap<Node, HashMap<Node, Integer>>();
	}
	
	private void updateEdge(Edge e) {
		Node u = e.getBegin();
		Node v = e.getEnd();
		if (Adj.get(u) == null) {
			Adj.put(u, new HashSet<Edge>());
			adjNode.put(u, new HashMap<Node, Integer>());
		}
		if (Adj.get(v) == null) {
			Adj.put(v, new HashSet<Edge>());
			adjNode.put(v, new HashMap<Node, Integer>());
		}
		Adj.get(u).add(e);
		Adj.get(v).add(e);
		adjNode.get(u).put(v, e.getID());
		adjNode.get(v).put(u, e.getID());
	}
	
	@Override
	public void addEdge(Edge e){
		super.addEdge(e);
		updateEdge(e);
	}
	
	public Edge addEdgeByID(int eid, int uid, int vid){
		Edge e = super.addEdgeByID(eid, uid, vid);
		updateEdge(e);
		return e;
	}
	
	public Edge edge(Node u, Node v){
		return getEdgeByID(adjNode.get(u).get(v));
	}
	
	public Set<Node> getAdjNode(Node u) {
		return adjNode.get(u).keySet(); 
	}
	
	public HashSet<Edge> getAdjEdge(Node u) {
		return Adj.get(u);
	}
	
	public HashMap<Node, HashSet<Edge>> getAdj(){
		return Adj;
	}
	
	public String name() { 
		return "UndirectedGraph";
	}
	
	public void print(){
		//System.out.println(name() + "::print, nodes = " + Utility.setNode2String(nodes) + ", edges = " + Utility.setEdge2String(edges));
		for(Node v : nodes){
			System.out.print("Node " + v.getID() + " : ");
			for(Edge e : getAdjEdge(v)){
				System.out.print("(" + e.getBegin().getID() + "," + e.getEnd().getID() + ") ");
			}
			for (Node u : getAdjNode(v)) {
				System.out.print(" " + u.getID());
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args){
		try{
			Scanner in = new Scanner(new File("g.txt"));
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
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}

