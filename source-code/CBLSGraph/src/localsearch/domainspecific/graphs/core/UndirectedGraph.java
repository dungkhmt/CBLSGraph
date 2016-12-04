/*
 * authors: DungPQ
 * date 12/7/2015
 */
package localsearch.domainspecific.graphs.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.io.File;

import localsearch.domainspecific.graphs.utils.Utility;

public class UndirectedGraph extends Graph {
	private HashMap<Node, HashSet<Edge>> Adj;
	public UndirectedGraph(){
		Adj = new HashMap<Node, HashSet<Edge>>();
	}
	public HashSet<Edge> getAdj(Node v){
		return Adj.get(v);
	}
	public int getNbrNodes(){
		return nodes.size();
	}
	@Override
	public void addNode(Node v){
		super.addNode(v);
	}
	@Override
	public void addEdge(Edge e){
		super.addEdge(e);
		Node u = e.getBegin();
		Node v = e.getEnd();
		if(Adj.get(u) == null) Adj.put(u, new HashSet<Edge>());
		if(Adj.get(v) == null) Adj.put(v, new HashSet<Edge>());
		
		Adj.get(u).add(e);
		Adj.get(v).add(e);
	}
	public Node addNodeByID(int vid){
		return super.addNodeByID(vid);
	}
	public Edge addEdgeByID(int eid, int uid, int vid){
		Edge e = super.addEdgeByID(eid, uid, vid);
		Node u = e.getBegin();
		Node v = e.getEnd();
		if(Adj.get(u) == null) Adj.put(u, new HashSet<Edge>());
		if(Adj.get(v) == null) Adj.put(v, new HashSet<Edge>());
		
		Adj.get(u).add(e);
		Adj.get(v).add(e);
		return e;
	}
	public Edge edge(Node u, Node v){
		for(Edge e : Adj.get(u)){
			Node o = e.otherNode(u);
			if(o == v) return e;
		}
		return null;
	}
	public boolean contains(Node v){
		return nodes.contains(v);
	}
	public boolean contains(Edge e){
		return edges.contains(e);
	}
	public String name(){ return "UndirectedGraph";}
	public void print(){
		System.out.println(name() + "::print, nodes = " + Utility.setNode2String(nodes) + ", edges = " + Utility.setEdge2String(edges));
		for(Node v : nodes){
			System.out.print("Node " + v.getID() + " : ");
			for(Edge e : Adj.get(v)){
				System.out.print("(" + e.getBegin().getID() + "," + e.getEnd().getID() + ") ");
			}
			System.out.println();
		}
	}
	public HashMap<Node, HashSet<Edge>> getAdj(){
		return Adj;
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
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}

