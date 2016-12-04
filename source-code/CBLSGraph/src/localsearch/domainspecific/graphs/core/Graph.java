/*
 * authors: DungPQ
 * date 12/7/2015
 */
package localsearch.domainspecific.graphs.core;

import java.util.HashSet;
import java.util.HashMap;
public class Graph {
	protected HashSet<Node> nodes;
	protected HashSet<Edge> edges;
	protected HashMap<Integer, Node> mID2Node;
	protected HashMap<Integer, Edge> mID2Edge;
	public Graph(){
		super();
		nodes = new HashSet<Node>();
		edges = new HashSet<Edge>();
		mID2Node = new HashMap<Integer, Node>();
		mID2Edge = new HashMap<Integer, Edge>();
	}
	public HashSet<Node> getNodes() {
		return nodes;
	}
	public void setNodes(HashSet<Node> nodes) {
		this.nodes = nodes;
	}
	public HashSet<Edge> getEdges() {
		return edges;
	}
	public void setEdges(HashSet<Edge> edges) {
		this.edges = edges;
	}
	public void addNode(Node nod){
		nodes.add(nod);
		mID2Node.put(nod.getID(), nod);
	}
	public void addEdge(Edge e){
		edges.add(e);
		mID2Edge.put(e.getID(), e);
		
		Node u = e.getBegin();
		Node v = e.getEnd();
		nodes.add(u);
		nodes.add(v);
	}
	public Node addNodeByID(int id){
		Node v = mID2Node.get(id);
		if(v != null) return v;
		v = new Node(id);
		nodes.add(v);
		mID2Node.put(id, v);
		return v;
	}
	public Edge addEdgeByID(int eid, int uid,  int vid){
		Edge e = mID2Edge.get(eid);
		if(e != null) return e;
		Node u = addNodeByID(uid);
		Node v = addNodeByID(vid);
		e = new Edge(eid);
		e.setBegin(u);
		e.setEnd(v);
		return e;
	}
	public Node getNodeByID(int id){
		return mID2Node.get(id);
	}
}
