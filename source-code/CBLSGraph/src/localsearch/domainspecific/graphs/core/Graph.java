package localsearch.domainspecific.graphs.core;

import java.util.HashSet;
import java.util.HashMap;
public class Graph {
	
	protected HashSet<Node> nodes;
	protected HashSet<Edge> edges;
	protected HashMap<Integer, Node> mID2Node;
	protected HashMap<Integer, Edge> mID2Edge;
	
	public Graph() {
		nodes = new HashSet<Node>();
		edges = new HashSet<Edge>();
		mID2Node = new HashMap<Integer, Node>();
		mID2Edge = new HashMap<Integer, Edge>();
	}
	
	public int getNbrNodes() {
		return nodes.size();
	}
	
	public boolean contains(Node v){
		return nodes.contains(v);
	}
	
	public boolean contains(Edge e){
		return edges.contains(e);
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
	
	public void addNode(Node node){
		nodes.add(node);
		mID2Node.put(node.getID(), node);
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
		if(v == null) {
			v = new Node(id);
			nodes.add(v);
			mID2Node.put(id, v);
		}
		return v;
	}
	
	public Edge addEdgeByID(int eid, int uid,  int vid){
		Edge e = mID2Edge.get(eid);
		if(e == null) {
			Node u = addNodeByID(uid);
			Node v = addNodeByID(vid);
			e = new Edge(eid);
			e.setBegin(u);
			e.setEnd(v);
			addEdge(e);
		}
		return e;
	}
	
	public Node getNodeByID(int id){
		return mID2Node.get(id);
	}
	
	public Edge getEdgeByID(int id) {
		return mID2Edge.get(id);
	}
}
