package localsearch.domainspecific.graphs.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.io.File;

public class UndirectedGraph extends Graph {

	public UndirectedGraph() {

	}
	
	private void updateEdge(Edge e) {
		Node u = e.getBegin();
		Node v = e.getEnd();
		if (Adj.get(u) == null) {
			Adj.put(u, new HashSet<Edge>());
		}
		if (Adj.get(v) == null) {
			Adj.put(v, new HashSet<Edge>());
		}
		Adj.get(u).add(e);
		Adj.get(v).add(e);
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

	public String name() { 
		return "UndirectedGraph";
	}
	
	public void print(){
		//System.out.println(name() + "::print, nodes = " + Utility.setNode2String(nodes) + ", edges = " + Utility.setEdge2String(edges));
		for(Node v : nodes){
			System.out.print("Node " + v.getID() + " : ");
			for(Edge e : getAdj(v)){
				System.out.print("(" + e.getBegin().getID() + "," + e.getEnd().getID() + ") ");
			}
			for (Edge e : getAdj(v)) {
				Node u = e.otherNode(v);
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

