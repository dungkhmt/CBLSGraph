package localsearch.domainspecific.graphs.utils;

import java.util.HashSet;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;

import java.util.Random;


public class Utility {
	public static final double EPSILON = 0.00001;
	
    public static void collectReplacedEdges(VarRootedTree vt, Edge e, HashSet<Edge> E) {
        Node u = e.getBegin();
        Node v = e.getEnd();
        Node uv = vt.nca(u, v);

        E.clear();
        Node x = u;
        while (x != uv) {
            Edge ei = vt.getFatherEdge(x);
            E.add(ei);
            x = vt.getFatherNode(x);
        }
        x = v;
        while (x != uv) {
            Edge ei = vt.getFatherEdge(x);
            E.add(ei);
            x = vt.getFatherNode(x);
        }
    }

    public static HashSet<Edge> collectReplacedEdges(VarRootedTree vt, Edge e) {
        Node u = e.getBegin();
        Node v = e.getEnd();
        Node uv = vt.nca(u, v);
        HashSet<Edge> E = new HashSet<Edge>();
        Node x = u;
        while (x != uv) {
            Edge ei = vt.getFatherEdge(x);
            E.add(ei);
            x = vt.getFatherNode(x);
        }
        x = v;
        while (x != uv) {
            Edge ei = vt.getFatherEdge(x);
            E.add(ei);
            x = vt.getFatherNode(x);
        }
        return E;
    }

    public static String setEdge2String(HashSet<Edge> E) {
        String s = "";
        for (Edge e : E) {
            s = s + e.toString() + ", ";
        }
        return s;
    }

    public static String setNode2String(HashSet<Node> N) {
        String s = "";
        for (Node n : N) {
            s = s + n.toString() + ", ";
        }
        return s;
    }

    public static Edge randomSelect(HashSet<Edge> E) {
        int sz = E.size();
        if (sz == 0) return null;
        Random R = new Random();
        int idx = R.nextInt(sz);
        int i = 0;
        for (Edge e : E) {
            if (i == idx) return e;
            i++;
        }
        return null;
    }

    public static String name() {
        return "Utility";
    }

    public static UndirectedGraph genRandomRootedTree(UndirectedGraph g, Node root, int nbEdges) {
        UndirectedGraph t = new UndirectedGraph();
        HashSet<Edge> candidates = new HashSet<Edge>();

        for (Edge e : g.getAdj(root)) {
            candidates.add(e);
        }
        for (int k = 0; k < nbEdges; k++) {
            Edge e = randomSelect(candidates);
            if (t.contains(e.getBegin()) && t.contains(e.getEnd())) {
                System.out.println(name() + "::genRandomTree, addEdge " + e.toString() + ", EXCEPTION???????");
                System.exit(-1);
            }
            Node v = e.getBegin();
            if (t.contains(v)) v = e.getEnd();

            t.addEdge(e);
            candidates.remove(e);

            //System.out.println(name() + "::genRandomTree, addEdge " + e.toString() + ", nodes = " + Utility.setNode2String(nodes) +
            //		", edges = " + Utility.setEdge2String(edges));
            //System.out.println(name() + "::genRandomTree, candidate = " + Utility.setEdge2String(candidates));


            //update candidate
            HashSet<Edge> R = new HashSet<Edge>();
            HashSet<Edge> A = new HashSet<Edge>();

            for (Edge ei : candidates) {
                //System.out.println(name() + "::genRandomTree, establish R, ei = " + ei.toString() + ", v = " + v.toString());
                if (ei.contains(v)) {
                    R.add(ei);
                    //System.out.println(name() + "::genRandomTree, establish R, ei = " + ei.toString() + " contains " + v.toString() + ", --> ADDED");
                }
            }
            for (Edge ei : g.getAdj().get(v)) {
                //System.out.println(name() + "::genRandomTree, establish A, ei = " + ei.toString());
                Node u = ei.otherNode(v);
                //if(candidates.contains(ei))
                //	R.add(ei);
                if (!t.contains(u)) {
                    A.add(ei);
                    //System.out.println(name() + "::genRandomTree, establish A, ei = " + ei.toString() + ", u = " + u.toString() + ", not in tree --> ADDED");
                }
            }
            for (Edge ei : R) {
                candidates.remove(ei);
            }
            for (Edge ei : A) {
                candidates.add(ei);
            }

            //System.out.println(name() + "::genRandomTree, final candidate = " + Utility.setEdge2String(candidates));
        }
        return t;
    }

    public static boolean equal(double a, double b){
		return Math.abs(a-b) < EPSILON;
	}

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
