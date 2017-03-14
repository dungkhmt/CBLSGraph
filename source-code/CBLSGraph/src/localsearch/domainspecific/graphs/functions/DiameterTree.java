package localsearch.domainspecific.graphs.functions;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Graph;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;
import localsearch.domainspecific.graphs.invariants.InsertableEdgesVarRootedTree;
import localsearch.domainspecific.graphs.invariants.RemovableEdgesVarRootedTree;
import localsearch.domainspecific.graphs.invariants.ReplacingEdgesVarRootedTree;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;
import localsearch.domainspecific.graphs.model.VarTree;
import localsearch.domainspecific.graphs.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class DiameterTree implements GFunction {

    private Node root;
    private VarTree vt;
    private LSGraphManager mgr;
    private VarGraph[] varGraphs;
    private int idxWeight;
    private int id;

    private int count;
    private double value;
    private HashMap<Node, Data> dataOfNodes;

    public DiameterTree(VarRootedTree vt) {
        this.vt = vt;
        idxWeight = -1;
        mgr = vt.getLSGraphManager();
        varGraphs = new VarGraph[1];
        varGraphs[0] = vt;
        mgr.post(this);
    }

    public DiameterTree(VarRootedTree vt, int idxWeight) {
        this.vt = vt;
        this.idxWeight = idxWeight;
        mgr = vt.getLSGraphManager();
        varGraphs = new VarGraph[1];
        varGraphs[0] = vt;
        mgr.post(this);
    }

    private double weightOfEdge(Edge e) {
        return idxWeight < 0 ? 1 : e.getWeight(idxWeight);
    }

    private Data dfs1(Node u, Node f) {
        Data du = new Data(u);
        du.L = ++count;
        for (Edge e : vt.getAdj(u)) {
            Node v = e.otherNode(u);
            if (v != f) {
                Data dv = dfs1(v, u);
                du.updateLongestPath(dv.kthLongestPath[0] + weightOfEdge(e), v);
                du.updateLongestDiameterA(dv.diameterA, v);
            }
        }
        du.diameterA = Math.max(du.diameterA, du.kthLongestPath[0] + du.kthLongestPath[1]);
        dataOfNodes.put(u, du);
        du.R = count;
        return du;
    }

    private void dfs2(Node u, Node f, Data du) {
        for (Edge e : vt.getAdj(u)) {
            Node v = e.otherNode(u);
            if (v != f) {
                Data dv = dataOfNodes.get(v);
                if (v != du.branchOfKthLongestPath[0]) {
                    dv.updateLongestPath(du.kthLongestPath[0] + weightOfEdge(e), u);
                } else {
                    dv.updateLongestPath(du.kthLongestPath[1] + weightOfEdge(e), u);
                }

                dv.updateLongestDiameterB(du);
                dfs2(v, u, dv);
            }
        }
    }

    private void dfs3(Node u, Node f, Data du) {
        for (Edge e : vt.getAdj(u)) {
            Node v = e.otherNode(u);
            if (v != f) {
                Data dv = dataOfNodes.get(v);
                dv.buildLCA(du, weightOfEdge(e));
                dfs3(v, u, dv);
            }
        }
    }

    private void initComputation() {
        if (!vt.isNull()) {
            count = 0;
            if (vt instanceof VarRootedTree) {
                root = ((VarRootedTree) vt).root();
            } else {
                root = vt.getNodes().iterator().next();
            }
            dfs1(root, null);
            dfs3(root, null, dataOfNodes.get(root));
            dfs2(root, null, dataOfNodes.get(root));
            value = dataOfNodes.get(root).diameterA;
        } else {
            value = 0;
        }
        //
    }

    @Override
    public VarGraph[] getVarGraphs() {
        // TODO Auto-generated method stub
        return varGraphs;
    }

    @Override
    public LSGraphManager getLSGraphManager() {
        // TODO Auto-generated method stub
        return mgr;
    }

    @Override
    public void setID(int id) {
        // TODO Auto-generated method stub
        this.id = id;
    }

    @Override
    public int getID() {
        // TODO Auto-generated method stub
        return id;
    }

    private Data getLCA(Data du, Data dv) {
        if (du.h > dv.h) {
            return getLCA(dv, du);
        }
        int f = dv.h - du.h;
        for (int i = dv.log2 - 1; i >= 0; i--) {
            if (((f >> i) & 1) == 1) {
                dv = dv.lca[i];
            }
        }
        if (du == dv) {
            return du;
        }
        for (int i = du.log2 - 1; i >= 0; i--) {
            if (du.lca[i] != dv.lca[i]) {
                du = du.lca[i];
                dv = dv.lca[i];
                i = du.log2;
            }
        }
        return du.lca[0];
    }

    @Override
    public void initPropagate() {
        // TODO Auto-generated method stub
        dataOfNodes = new HashMap<Node, Data>();
        initComputation();
    }

    @Override
    public void propagateAddEdge(VarGraph vt, Edge e) {
        // TODO Auto-generated method stub
        initComputation();
    }

    @Override
    public void propagateRemoveEdge(VarGraph vt, Edge e) {
        // TODO Auto-generated method stub
        initComputation();
    }

    @Override
    public void propagateReplaceEdge(VarGraph vt, Edge eo, Edge ei) {
        // TODO Auto-generated method stub
        initComputation();
    }

    @Override
    public double getAddEdgeDelta(VarGraph vt, Edge e) {
        // TODO Auto-generated method stub
        if (value == 0) {
            return weightOfEdge(e);
        }
        Node u = e.getBegin();
        if (!vt.contains(u)) {
            u = e.getEnd();
        }
        Data du = dataOfNodes.get(u);
        if (du.kthLongestPath[0] == value) {
            return weightOfEdge(e);
        } else {
            return 0;
        }
    }

    @Override
    public double getRemoveEdgeDelta(VarGraph vt, Edge e) {
        // TODO Auto-generated method stub
        Data du = dataOfNodes.get(e.getBegin());
        Data dv = dataOfNodes.get(e.getEnd());
        if (du.L > dv.L) {
            Data tmp = dv;
            dv = du;
            du = tmp;
        }
        return Math.max(dv.diameterA, dv.diameterB) - value;
    }

    @Override
    public double getReplaceEdgeDelta(VarGraph vt, Edge ei, Edge eo) {
        // TODO Auto-generated method stub
        Data tmp;
        Node uo = eo.getBegin();
        Node vo = eo.getEnd();
        Data duo = dataOfNodes.get(uo);
        Data dvo = dataOfNodes.get(vo);
        if (duo.L > dvo.L) {
            uo = eo.getEnd();
            vo = eo.getBegin();
            tmp = duo;
            duo = dvo;
            dvo = tmp;
        }
        double newVal = Math.max(dvo.diameterA, dvo.diameterB);

        Node ui = ei.getBegin();
        Node vi = ei.getEnd();
        Data dui = dataOfNodes.get(ui);
        Data dvi = dataOfNodes.get(vi);
        if (dvo.L <= dui.L && dvo.R >= dui.L) {
            ui = ei.getEnd();
            vi = ei.getBegin();
            tmp = dui;
            dui = dvi;
            dvi = tmp;
        }

        double valA = 0;
        tmp = dataOfNodes.get(dvi.branchOfKthLongestPath[0]);
        if (tmp.L < dvi.L) {
            valA = dvi.kthLongestPath[1];
        } else {
            valA = dvi.kthLongestPath[0];
        }
        valA = Math.max(valA, dvi.getLongestUpLCA(dvi.h - dvo.h));

        double valB = 0;
        if (dui.L <= duo.L && dui.R >= duo.L) {
            if (duo.h - dui.h > 0) {
                valB = dvo.getLongestDownLCA(duo.h - dui.h) - dui.depth;
            }
            tmp = dataOfNodes.get(dui.branchOfKthLongestPath[0]);
            if ((tmp.L < dui.L && tmp.R >= dui.L) || tmp.L > dvo.R || tmp.R < dvo.L) {
                valB = Math.max(valB, dui.kthLongestPath[0]);
            } else {
                valB = Math.max(valB, dui.kthLongestPath[1]);
            }
        } else {
            tmp = dataOfNodes.get(dui.branchOfKthLongestPath[0]);
            if (tmp.L < dui.L) {
                valB = dui.kthLongestPath[1];
            } else {
                valB = dui.kthLongestPath[0];
            }

            Data dp = getLCA(dui, duo);
            valB = Math.max(valB, dui.getLongestUpLCA(dui.h - dp.h - 1));
            if (duo.h - dp.h > 0) {
                valB = Math.max(valB, dui.depth + dvo.getLongestDownLCA(duo.h - dp.h) - 2 * dp.depth);
            }
            valB = Math.max(valB, dui.depth + duo.depth - 2 * dp.depth);
            //valB = Math.max(valB, dui.depth + dvo.getLongestDownLCA(duo.h - dp.h) - 2 * dp.depth);

            //Data droot = dataOfNodes.get(root);
            for (int i = 0; i < 3 && dp.branchOfKthLongestPath[i] != null; i++) {
                tmp = dataOfNodes.get(dp.branchOfKthLongestPath[i]);
                if ((tmp.L < dp.L && tmp.R >= dp.L) || (!((tmp.L <= dui.L && tmp.R >= dui.L) || ((tmp.L <= dvo.L && tmp.R >= dvo.L))))) {
                    valB = Math.max(valB, dui.depth + dp.kthLongestPath[i] - dp.depth);
                    break;
                }
            }
        }

        newVal = Math.max(newVal, valA + valB + weightOfEdge(ei));
        return newVal - value;
    }

    @Override
    public double getValue() {
        // TODO Auto-generated method stub
        return value;
    }

    private class Data {
        Node v;
        double diameterA;
        double diameterB;
        double[] kthLongestPath;
        double[] kthLongestDiameterA;
        Node[] branchOfKthLongestPath;
        Node[] branchOfKthLongestDiameterA;

        int h;
        int log2;
        double depth;
        double[] diameterLCA;
        double[] longestUpLCA;
        double[] longestDownLCA;
        Data[] lca;

        int L;
        int R;

        public Data(Node v) {
            this.v = v;
            diameterA = diameterB = 0;
            kthLongestPath = new double[4];
            kthLongestDiameterA = new double[2];
            branchOfKthLongestPath = new Node[4];
            branchOfKthLongestDiameterA = new Node[2];
            h = log2 = 0;
            depth = 0;
        }

        void updateLongestPath(double length, Node node) {
            for (int i = 0; i < 4; i++) {
                if (kthLongestPath[i] < length) {
                    for (int j = 3; j > i; j--) {
                        branchOfKthLongestPath[j] = branchOfKthLongestPath[j - 1];
                        kthLongestPath[j] = kthLongestPath[j - 1];
                    }
                    branchOfKthLongestPath[i] = node;
                    kthLongestPath[i] = length;
                    return;
                }
            }
        }

        void updateLongestDiameterA(double length, Node node) {
            diameterA = Math.max(diameterA, length);
            for (int i = 0; i < 2; i++) {
                if (kthLongestDiameterA[i] < length) {
                    for (int j = 1; j > i; j--) {
                        branchOfKthLongestDiameterA[j] = branchOfKthLongestDiameterA[j - 1];
                        kthLongestDiameterA[j] = kthLongestDiameterA[j - 1];
                    }
                    branchOfKthLongestDiameterA[i] = node;
                    kthLongestDiameterA[i] = length;
                    return;
                }
            }
        }

        void updateLongestDiameterB(Data du) {
            if (v == du.branchOfKthLongestPath[0]) {
                diameterB = Math.max(du.kthLongestPath[1] + du.kthLongestPath[2], du.diameterB);
            } else if (v == du.branchOfKthLongestPath[1]) {
                diameterB = Math.max(du.kthLongestPath[0] + du.kthLongestPath[2], du.diameterB);
            } else {
                diameterB = Math.max(du.kthLongestPath[0] + du.kthLongestPath[1], du.diameterB);
            }

            if (v != du.branchOfKthLongestDiameterA[0]) {
                diameterB = Math.max(diameterB, du.kthLongestDiameterA[0]);
            } else {
                diameterB = Math.max(diameterB, du.kthLongestDiameterA[1]);
            }
        }

        void buildLCA(Data du, double w) {
            h = du.h + 1;
            log2 = du.log2;
            if (h == (1 << log2)) {
                log2++;
            }
            diameterLCA = new double[log2];
            longestUpLCA = new double[log2];
            longestDownLCA = new double[log2];
            lca = new Data[log2];
            depth = du.depth + w;

            lca[0] = du;
            if (v != du.branchOfKthLongestPath[0]) {
                longestUpLCA[0] = du.kthLongestPath[0] - du.depth;
                longestDownLCA[0] = du.kthLongestPath[0] + du.depth;
                if (v != du.branchOfKthLongestPath[1]) {
                    diameterLCA[0] = du.kthLongestPath[0] + du.kthLongestPath[1];
                } else {
                    diameterLCA[0] = du.kthLongestPath[0] + du.kthLongestPath[2];
                }
            } else {
                longestUpLCA[0] = du.kthLongestPath[1] - du.depth;
                longestDownLCA[0] = du.kthLongestPath[1] + du.depth;
                diameterLCA[0] = du.kthLongestPath[1] + du.kthLongestPath[2];
            }
            if (du.branchOfKthLongestDiameterA[0] != v) {
                diameterLCA[0] = Math.max(diameterLCA[0], du.kthLongestDiameterA[0]);
            } else {
                diameterLCA[0] = Math.max(diameterLCA[0], du.kthLongestDiameterA[1]);
            }

            for (int i = 1; i < log2; i++) {
                lca[i] = lca[i - 1].lca[i - 1];

                longestUpLCA[i] = Math.max(longestUpLCA[i - 1], lca[i - 1].longestUpLCA[i - 1]);
                longestDownLCA[i] = Math.max(longestDownLCA[i - 1], lca[i - 1].longestDownLCA[i - 1]);

                diameterLCA[i] = Math.max(diameterLCA[i - 1], lca[i - 1].diameterLCA[i - 1]);
                diameterLCA[i] = Math.max(diameterLCA[i], longestDownLCA[i - 1] + lca[i - 1].longestUpLCA[i - 1]);
            }
        }

        Node getParent(int nbSteps) {
            Data tmp = this;
            for (int i = log2 - 1; i >= 0; i--) {
                if (((nbSteps >> i) & 1) > 0) {
                    tmp = tmp.lca[i];
                    if (tmp.log2 < i) {
                        i = tmp.log2;
                    }
                }
            }
            return tmp.v;
        }

        double getLongestUpLCA(int nbSteps) {
            double val = 0;
            Data tmp = this;
            for (int i = log2 - 1; i >= 0; i--) {
                if (((nbSteps >> i) & 1) > 0) {
                    val = Math.max(val, depth + tmp.longestUpLCA[i]);
                    tmp = tmp.lca[i];
                    if (tmp.log2 < i) {
                        i = tmp.log2;
                    }
                }
            }
            return val;
        }

        double getLongestDownLCA(int nbSteps) {
            double val = 0;
            Data tmp = this;
            for (int i = log2 - 1; i >= 0; i--) {
                if (((nbSteps >> i) & 1) > 0) {
                    val = Math.max(val, tmp.longestDownLCA[i]);
                    tmp = tmp.lca[i];
                    if (tmp.log2 < i) {
                        i = tmp.log2;
                    }
                }
            }
            return val;
        }

        double getLongestDiameterLCA(int nbSteps) {
            double val = 0;
            double maxDepth = 0;
            Data tmp = this;
            for (int i = log2 - 1; i >= 0; i--) {
                if (((nbSteps >> i) & 1) > 0) {
                    val = Math.max(val, tmp.diameterLCA[i]);
                    val = Math.max(val, tmp.longestUpLCA[i] + maxDepth);
                    maxDepth = Math.max(maxDepth, tmp.longestDownLCA[i]);
                    tmp = tmp.lca[i];
                    if (tmp.log2 < i) {
                        i = tmp.log2;
                    }
                }
            }
            return val;
        }

        double getLongestOfNotBranch(Node node) {
            for (int i = 0; i < 4; i++) {
                if (branchOfKthLongestPath[i] != node) {
                    return kthLongestPath[i];
                }
            }
            return 0;
        }

        double getLongestOfNotBranchs(Node node1, Node node2) {
            for (int i = 0; i < 4; i++) {
                if (branchOfKthLongestPath[i] != node1 && branchOfKthLongestPath[i] != node2) {
                    return kthLongestPath[i];
                }
            }
            return 0;
        }

        void print() {
            System.out.println("diameterA = " + diameterA + ", diameterB = " + diameterB);
            for (int i = 0; i < 3; i++) {
                if (branchOfKthLongestPath[i] != null) {
                    System.out.println((i + 1) + ": branch " + branchOfKthLongestPath[i] + ", length = " + kthLongestPath[i]);
                }
            }
        }
    }

    public void print() {
        System.out.println("Diameter:\n root = " + root);
        for (Node v : vt.getNodes()) {
            System.out.print("Node " + v + ": ");
            for (Edge e : vt.getAdj(v)) {
                System.out.print(e + ", ");
            }
            System.out.println();
        }
        for (Node v : vt.getNodes()) {
            Data dv = dataOfNodes.get(v);
            System.out.print("Data " + v + "\n");
            dv.print();
        }
    }

    public static void main(String[] args) {

        for (int nbTest = 0; nbTest < 10000; nbTest++) {
            UndirectedGraph lub = new UndirectedGraph();
            int n = 100;
            Random rand = new Random();
            for (int i = 1; i <= n; i++) {
                for (int j = i + 1; j <= n; j++) {
                    if (i != j) {
                        lub.addEdgeByID((i - 1) * n + j, i, j);
                        Edge e = lub.getEdgeByID((i - 1) * n + j);
                        e.setWeight(rand.nextInt(20) + 1);
                    }
                }
            }
            lub.print();

            LSGraphManager mgr = new LSGraphManager();
            VarRootedTree vt = new VarRootedTree(mgr, lub, lub.getNodeByID(1));
            DiameterTree diameterTree = new DiameterTree(vt, 0);


            for (int i = 1; i < n; i++) {
                Node u = lub.getNodeByID(i + 1);
                Node v = lub.getNodeByID(rand.nextInt(i) + 1);
                Edge e = lub.getEdge(u, v);
                vt.addEdge(e);
            }
            mgr.close();

            //

            for (int step = 1; step <= 1000; step++) {
                System.out.println("Steps = " + step);
                //vt.print();
                while (true) {
                    Node u = lub.getNodeByID(rand.nextInt(n) + 1);
                    Node v = lub.getNodeByID(rand.nextInt(n) + 1);
                    // NodeOptVarRootedTree
                    if (v != u && vt.getFatherNode(u) != v && v != vt.root() && u != vt.root()) {
                        double d = diameterTree.getNodeOptVarRootedTree(vt, v, u);
                        double oldV = diameterTree.getValue();
                        vt.nodeOptVarRootedTreePropagate(v, u);
                        double newV = diameterTree.getValue();
                        if (Math.abs(newV - (oldV + d)) > 1e-6) {
                            //                        vt.print();
                            //                        diameterTree.print();
                            System.out.println("u = " + u + ", v = " + v);
                            System.out.println("oldV = " + oldV + ", delta = " + d + ", newV = " + newV);
                            System.exit(-1);
                        }
                        break;
                    }


                    //                if (v != u && !vt.dominate(v, u) && v != vt.root() && u != vt.root()) {
                    //                    double d = diameterTree.getSubTreeOptVarRootedTree(vt, v, u);
                    //                    double oldV = diameterTree.getValue();
                    //                    vt.subTreeOptVarRootedTreePropagate(v, u);
                    //                    double newV = diameterTree.getValue();
                    //                    if (Math.abs(newV - (oldV + d)) > 1e-6) {
                    //                        //vt.print();
                    //                        //diameterTree.print();
                    //                        System.out.println("u = " + u + ", v = " + v);
                    //                        System.out.println("oldV = " + oldV + ", delta = " + d + ", newV = " + newV);
                    //                        System.exit(-1);
                    //                    }
                    //                    break;
                    //                }
                }

                System.out.println("Ok");
            }
        }

    }

	@Override
	public void propagateAddEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
        initComputation();
	}

	@Override
	public void propagateRemoveEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
		initComputation();
	}

	@Override
	public void propagateReplaceEdgeVarRootedTree(VarRootedTree vt, Edge ei,
			Edge eo) {
		// TODO Auto-generated method stub
        initComputation();
	}

	@Override
	public void propagateNodeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		// TODO Auto-generated method stub
        initComputation();
	}

	@Override
	public void propagateSubTreeOptVarRootedTree(VarRootedTree vt, Node v,
			Node u) {
		// TODO Auto-generated method stub
        initComputation();
	}

	@Override
	public double getAddEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
        if (this.vt != vt) {
            return 0;
        }
        if (value == 0) {
            return weightOfEdge(e);
        }
        Node u = e.getBegin();
        if (!vt.contains(u)) {
            u = e.getEnd();
        }
        Data du = dataOfNodes.get(u);
        if (du.kthLongestPath[0] == value) {
            return weightOfEdge(e);
        } else {
            return 0;
        }
	}

	@Override
	public double getRemoveEdgeVarRootedTree(VarRootedTree vt, Edge e) {
		// TODO Auto-generated method stub
        if (this.vt != vt) {
            return 0;
        }
        Data du = dataOfNodes.get(e.getBegin());
        Data dv = dataOfNodes.get(e.getEnd());
        if (du.L > dv.L) {
            Data tmp = dv;
            dv = du;
            du = tmp;
        }
        return Math.max(dv.diameterA, dv.diameterB) - value;
	}

	@Override
	public double getReplaceEdgeVarRootedTree(VarRootedTree vt, Edge ei, Edge eo) {
		// TODO Auto-generated method stub
        if (this.vt != vt) {
            return 0;
        }

        Data tmp;
        Node uo = eo.getBegin();
        Node vo = eo.getEnd();
        Data duo = dataOfNodes.get(uo);
        Data dvo = dataOfNodes.get(vo);
        if (duo.L > dvo.L) {
            uo = eo.getEnd();
            vo = eo.getBegin();
            tmp = duo;
            duo = dvo;
            dvo = tmp;
        }
        double newVal = Math.max(dvo.diameterA, dvo.diameterB);

        Node ui = ei.getBegin();
        Node vi = ei.getEnd();
        Data dui = dataOfNodes.get(ui);
        Data dvi = dataOfNodes.get(vi);
        if (dvo.L <= dui.L && dvo.R >= dui.L) {
            ui = ei.getEnd();
            vi = ei.getBegin();
            tmp = dui;
            dui = dvi;
            dvi = tmp;
        }

        double valA = 0;
        tmp = dataOfNodes.get(dvi.branchOfKthLongestPath[0]);
        if (tmp.L < dvi.L) {
            valA = dvi.kthLongestPath[1];
        } else {
            valA = dvi.kthLongestPath[0];
        }
        valA = Math.max(valA, dvi.getLongestUpLCA(dvi.h - dvo.h));

        double valB = 0;
        if (dui.L <= duo.L && dui.R >= duo.L) {
            if (duo.h - dui.h > 0) {
                valB = dvo.getLongestDownLCA(duo.h - dui.h) - dui.depth;
            }
            tmp = dataOfNodes.get(dui.branchOfKthLongestPath[0]);
            if ((tmp.L < dui.L && tmp.R >= dui.L) || tmp.L > dvo.R || tmp.R < dvo.L) {
                valB = Math.max(valB, dui.kthLongestPath[0]);
            } else {
                valB = Math.max(valB, dui.kthLongestPath[1]);
            }
        } else {
            tmp = dataOfNodes.get(dui.branchOfKthLongestPath[0]);
            if (tmp.L < dui.L) {
                valB = dui.kthLongestPath[1];
            } else {
                valB = dui.kthLongestPath[0];
            }

            Data dp = getLCA(dui, duo);
            valB = Math.max(valB, dui.getLongestUpLCA(dui.h - dp.h - 1));
            if (duo.h - dp.h > 0) {
                valB = Math.max(valB, dui.depth + dvo.getLongestDownLCA(duo.h - dp.h) - 2 * dp.depth);
            }
            valB = Math.max(valB, dui.depth + duo.depth - 2 * dp.depth);
            //valB = Math.max(valB, dui.depth + dvo.getLongestDownLCA(duo.h - dp.h) - 2 * dp.depth);

            //Data droot = dataOfNodes.get(root);
            for (int i = 0; i < 4 && dp.branchOfKthLongestPath[i] != null; i++) {
                tmp = dataOfNodes.get(dp.branchOfKthLongestPath[i]);
                if ((tmp.L < dp.L && tmp.R >= dp.L) || (!((tmp.L <= dui.L && tmp.R >= dui.L) || ((tmp.L <= dvo.L && tmp.R >= dvo.L))))) {
                    valB = Math.max(valB, dui.depth + dp.kthLongestPath[i] - dp.depth);
                    break;
                }
            }
        }

        newVal = Math.max(newVal, valA + valB + weightOfEdge(ei));
        return newVal - value;
	}

	@Override
	public double getNodeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		// TODO Auto-generated method stub
        if (this.vt != vt) {
            return 0;
        }
        if (vt.getFatherNode(u) == v) {
            return 0;
        }

        Node p = vt.nca(u, v);
        Node fu = vt.getFatherNode(u);
        Node fv = vt.getFatherNode(v);
        Data d_u = dataOfNodes.get(u);
        Data d_v = dataOfNodes.get(v);
        Data d_p = dataOfNodes.get(p);
        Data d_fu = dataOfNodes.get(fu);
        Data d_fv = dataOfNodes.get(fv);
        if (idxWeight < 0) {
            if (p == u) {
                double valX = d_fu.getLongestOfNotBranch(u);
                double valY = valX + d_v.getLongestDownLCA(d_v.h - d_u.h) - d_u.depth + 1;
                if (valY == value) {
                    return 1;
                }
                valX += d_v.getLongestOfNotBranch(vt.getFatherNode(v)) + d_v.depth - d_fu.depth;
                if (valY == value - 1 || valX == value || d_v.diameterA == value || d_v.diameterB == value) {
                    return 0;
                }
                return -1;
            } else if (p == v) {
                double valX = d_u.getLongestOfNotBranch(vt.getFatherNode(u));
                double valY = valX + d_u.getLongestUpLCA(d_u.h - d_v.h - 1);
                if (valY == value) {
                    return 1;
                }
                valX += d_v.getLongestOfNotBranch(d_u.getParent(d_u.h - d_v.h - 1));
                valX += d_u.depth - d_v.depth;
                if (valY == value - 1 || valX == value || d_v.diameterA == value || d_v.diameterB == value) {
                    return 0;
                }
                return -1;
            } else {
                double valX = d_u.getLongestOfNotBranch(vt.getFatherNode(u));
                double valY = d_u.getLongestUpLCA(d_u.h - d_p.h - 1);
                valY = Math.max(valY, d_v.getLongestDownLCA(d_v.h - d_p.h - 1) - 2 * d_p.depth + d_u.depth);
                valY = Math.max(valY, d_u.depth - d_p.depth + d_p.getLongestOfNotBranchs(d_u.getParent(d_u.h - d_p.h - 1), d_v.getParent(d_v.h - d_p.h - 1)));
                valY += valX;
                if (valY == value) {
                    return 1;
                }
                valX += d_v.getLongestOfNotBranch(vt.getFatherNode(v)) + d_u.depth + d_v.depth - 2 * d_p.depth;
                if (valY == value - 1 || valX == value || d_v.diameterA == value || d_v.diameterB == value) {
                    return 0;
                }
                return -1;
            }
        } else {
            if (p == u) {
                System.out.println("Case p == u");

                double newVal = d_u.diameterB;

                System.out.println("diameterB = " + newVal);

                newVal = Math.max(newVal, d_v.getLongestDiameterLCA(d_v.h - d_u.h));

                System.out.println("getLongestDiameterLCA = " + newVal);

                double maxUp = d_v.getLongestUpLCA(d_v.h - d_u.h) - d_v.depth + d_fv.depth;

                System.out.println("maxUp = " + maxUp);

                double maxV1 = 0;
                double maxV2 = 0;
                for (Edge e : vt.getAdj(v)) {
                    Node tmp = e.otherNode(v);
                    if (tmp != fv) {
                        double w = weightOfEdge(vt.getLUB().getEdge(fv, tmp));
                        Data d_tmp = dataOfNodes.get(tmp);
                        newVal = Math.max(newVal, d_tmp.diameterA);
                        double len = d_tmp.getLongestOfNotBranch(v);
                        if (w + len > maxV1) {
                            maxV2 = maxV1;
                            maxV1 = w + len;
                        } else if (w + len > maxV2) {
                            maxV2 = w + len;
                        }
                    }
                }

                System.out.println("maxV1 = " + maxV1 + ", maxV2 = " + maxV2);
                newVal = Math.max(newVal, maxV1 + maxUp);
                System.out.println("newVal = " + newVal);
                for (int i = 0; i < 4; i++) {
                    Node branch = d_fv.branchOfKthLongestPath[i];
                    if (branch != v && branch != vt.getFatherNode(d_fv.v)) {
                        if (d_fv.kthLongestPath[i] > maxV1) {
                            maxV2 = maxV1;
                            maxV1 = d_fv.kthLongestPath[i];
                        } else if (d_fv.kthLongestPath[i] > maxV2) {
                            maxV2 = d_fv.kthLongestPath[i];
                        }
                    }
                }

                System.out.println("maxV1 = " + maxV1 + ", maxV2 = " + maxV2);

                newVal = Math.max(newVal, maxV1 + maxV2);


                double valA = Math.max(maxV1 + d_fv.depth, d_v.getLongestDownLCA(d_v.h - d_u.h)) - d_u.depth;
                double valB = d_fu.getLongestOfNotBranch(u);

                System.out.println("valA = " + valA + ", valB = " + valB);

                Graph g = vt.getLUB();
                double wuv = weightOfEdge(g.getEdge(u, v));
                double wfuv = weightOfEdge(g.getEdge(fu, v));

                System.out.println("wuv = " + wuv + ", wfuv = " + wfuv);

                newVal = Math.max(newVal, valA + valB + wuv + wfuv);

                return newVal - value;
            } else if (p == v) {
                System.out.println("Case p == v");

                double newVal = d_v.diameterB;

                System.out.println("d_v.diameterB = " + newVal);

                newVal = Math.max(newVal, d_u.diameterA);

                System.out.println("d_u.diameterA = " + newVal);

                newVal = Math.max(newVal, d_u.getLongestDiameterLCA(d_u.h - d_v.h - 1));

                double maxV1 = 0;
                double maxV2 = 0;
                Node cv = d_u.getParent(d_u.h - d_v.h - 1);
                for (Edge e : vt.getAdj(v)) {
                    Node tmp = e.otherNode(v);
                    if (tmp != fv && tmp != cv) {
                        double w = weightOfEdge(vt.getLUB().getEdge(fv, tmp));
                        Data d_tmp = dataOfNodes.get(tmp);
                        newVal = Math.max(newVal, d_tmp.diameterA);
                        double len = d_tmp.getLongestOfNotBranch(v);
                        if (w + len > maxV1) {
                            maxV2 = maxV1;
                            maxV1 = w + len;
                        } else if (w + len > maxV2) {
                            maxV2 = w + len;
                        }
                    }
                }

                double len = d_fv.getLongestOfNotBranch(v);
                if (len > maxV1) {
                    maxV2 = maxV1;
                    maxV1 = len;
                } else if (len > maxV2) {
                    maxV2 = len;
                }
                System.out.println("maxV1 = " + maxV1 + ", maxV2 = " + maxV2);

                newVal = Math.max(newVal, maxV1 + maxV2);

                double maxDown = d_u.getLongestDownLCA(d_u.h - d_v.h - 1);
                System.out.print("maxDown = " + maxDown);

                Data d_cv = dataOfNodes.get(cv);
                maxDown -= d_cv.depth;

                Graph g = vt.getLUB();
                double wfvcv = weightOfEdge(g.getEdge(fv, cv));
                newVal = Math.max(newVal, maxV1 + maxDown + wfvcv);

                System.out.println("newVal = " + newVal);

                double valA = d_u.getLongestOfNotBranch(fu);
                double valB = maxV1 + wfvcv + d_fu.depth - d_cv.depth;
                valB = Math.max(valB, d_u.getLongestUpLCA(d_u.h - d_cv.h) - d_u.depth + d_fu.depth);

                System.out.println("valA = " + valA + ", valB = " + valB);

                double wuv = weightOfEdge(g.getEdge(u, v));
                double wfuv = weightOfEdge(g.getEdge(fu, v));

                System.out.println("wuv = " + wuv + ", wfuv = " + wfuv);

                newVal = Math.max(newVal, valA + valB + wuv + wfuv);

                return newVal - value;
            } else {
                System.out.println("Case u != p && v != p");

                double newVal = d_u.diameterA;

                System.out.println("d_u.diameterA = " + newVal);

                newVal = Math.max(newVal, d_p.diameterB);

                System.out.println("d_p.diameterB = " + d_p.diameterB);

                Node kthParentOfU = d_u.getParent(d_u.h - d_p.h - 1);
                Node kthParentOfV = d_v.getParent(d_v.h - d_p.h - 1);

                double valX = d_u.getLongestDownLCA(d_u.h - d_p.h - 1) - d_p.depth;
                double valY = d_v.getLongestDownLCA(d_v.h - d_p.h - 1) - d_p.depth;
                System.out.println("valX = " + valX + ", valY = " + valY );

                newVal = Math.max(newVal, valX + valY);
                double maxXY = Math.max(valX, valY);

                for (int i = 0; i < 4; i++) {
                    if (d_p.branchOfKthLongestPath[i] != kthParentOfU && d_p.branchOfKthLongestPath[i] != kthParentOfV) {
                        for (int j = i + 1; j < 4; j++) {
                            if (d_p.branchOfKthLongestPath[j] != kthParentOfU && d_p.branchOfKthLongestPath[j] != kthParentOfV) {
                                newVal = Math.max(newVal, d_p.kthLongestPath[i] + d_p.kthLongestPath[j]);
                            }
                        }
                        newVal = Math.max(newVal, d_p.kthLongestPath[i] + maxXY);
                    }
                }

                for (int i = 0; i < 2; i++) {
                    if (d_p.branchOfKthLongestDiameterA[i] != kthParentOfU && d_p.branchOfKthLongestDiameterA[i] != kthParentOfV) {
                        newVal = Math.max(newVal, d_p.kthLongestDiameterA[i]);
                    }
                }

                System.out.println("newVal = " + newVal);

                double maxV1 = 0;
                double maxV2 = 0;
                for (Edge e : vt.getAdj(v)) {
                    Node tmp = e.otherNode(v);
                    if (tmp != fv) {
                        double w = weightOfEdge(vt.getLUB().getEdge(fv, tmp));
                        Data d_tmp = dataOfNodes.get(tmp);
                        newVal = Math.max(newVal, d_tmp.diameterA);
                        double len = d_tmp.getLongestOfNotBranch(v);
                        if (w + len > maxV1) {
                            maxV2 = maxV1;
                            maxV1 = w + len;
                        } else if (w + len > maxV2) {
                            maxV2 = w + len;
                        }
                    }
                }

                if (fv != p) {
                    newVal = Math.max(newVal, d_fv.getLongestUpLCA(d_fv.h - d_p.h - 1) + maxV1);
                    for (int i = 0; i < 4; i++) {
                        Node branch = d_fv.branchOfKthLongestPath[i];
                        if (branch != v && branch != vt.getFatherNode(d_fv.v)) {
                            if (d_fv.kthLongestPath[i] > maxV1) {
                                maxV2 = maxV1;
                                maxV1 = d_fv.kthLongestPath[i];
                            } else if (d_fv.kthLongestPath[i] > maxV2) {
                                maxV2 = d_fv.kthLongestPath[i];
                            }
                        }
                    }
                }

                System.out.println("maxV1 = " + maxV1 + ", maxV2 = " + maxV2);

                newVal = Math.max(newVal, maxV1 + maxV2);
                System.out.println("newVal = " + newVal);

                double maxLenOfDuNotBranch = d_p.getLongestOfNotBranchs(kthParentOfU, kthParentOfV);
                newVal = Math.max(newVal, maxV1 + maxLenOfDuNotBranch + d_fv.depth - d_p.depth);
                System.out.println("newVal = " + newVal);

                newVal = Math.max(newVal, d_v.getLongestDiameterLCA(d_v.h - d_p.h - 1));
                System.out.println("newVal = " + newVal);

                newVal = Math.max(newVal, d_u.getLongestDiameterLCA(d_u.h - d_p.h - 1));
                System.out.println("newVal = " + newVal);

                double valA = Math.max(maxLenOfDuNotBranch, maxV1 + d_fv.depth - d_p.depth);
                System.out.println("valA = " + valA);

                valA = Math.max(valA, d_v.getLongestDownLCA(d_v.h - d_p.h - 1) - d_p.depth);
                System.out.println("valA = " + valA);

                if (fu != p) {
                    newVal = Math.max(newVal, valA + d_u.getLongestDownLCA(d_u.h - d_p.h - 1) - d_p.depth);
                }

                valA += d_fu.depth - d_p.depth;
                System.out.println("valA = " + valA);

                valA = Math.max(valA, d_u.getLongestUpLCA(d_u.h - d_p.h - 1) - d_u.depth + d_fu.depth);
                System.out.println("valA = " + valA);

                double valB = d_u.getLongestOfNotBranch(fu);
                System.out.println("valA = " + valA + ", valB = " + valB);

                Graph g = vt.getLUB();
                double wuv = weightOfEdge(g.getEdge(u, v));
                double wfuv = weightOfEdge(g.getEdge(fu, v));

                System.out.println("wuv = " + wuv + ", wfuv = " + wfuv);

                newVal = Math.max(newVal, valA + valB + wuv + wfuv);

                return newVal - value;
            }
        }


	}

	@Override
	public double getSubTreeOptVarRootedTree(VarRootedTree vt, Node v, Node u) {
		// TODO Auto-generated method stub
        if (this.vt != vt) {
            return 0;
        }
        Node p = vt.nca(u, v);
        Node fu = vt.getFatherNode(u);
        Node fv = vt.getFatherNode(v);
        Data d_u = dataOfNodes.get(u);
        Data d_v = dataOfNodes.get(v);
        Data d_p = dataOfNodes.get(p);
        Data d_fu = dataOfNodes.get(fu);
        if (p == u) {
            double newVal = Math.max(d_u.diameterB, d_v.diameterA);
            newVal = Math.max(newVal, d_v.getLongestDiameterLCA(d_v.h - d_u.h));

            double valA = d_v.getLongestDownLCA(d_v.h - d_u.h) - d_u.depth;
            double valB = d_v.getLongestOfNotBranch(fv);
            double valC = d_fu.getLongestOfNotBranch(u);

            Graph g = vt.getLUB();
            double wuv = weightOfEdge(g.getEdge(u, v));
            double wfuv = weightOfEdge(g.getEdge(fu, v));
            newVal = Math.max(newVal, valA + valB + wuv);
            newVal = Math.max(newVal, valC + valB + wfuv);
            newVal = Math.max(newVal, valA + valC + wuv + wfuv);

            return newVal - value;
        } else {
            double newVal = Math.max(d_u.diameterA, d_v.diameterA);
            newVal = Math.max(newVal, d_p.diameterB);

            //System.out.println("newVal = " + newVal);

            double valA = Math.max(0, d_u.getLongestDownLCA(d_u.h - d_p.h - 1) - d_p.depth);
            double valB = Math.max(0, d_v.getLongestDownLCA(d_v.h - d_p.h - 1) - d_p.depth);



            Node kthParentOfU = d_u.getParent(d_u.h - d_p.h - 1);
            Node kthParentOfV = d_v.getParent(d_v.h - d_p.h - 1);
            double valC = d_p.getLongestOfNotBranchs(kthParentOfU, kthParentOfV);

            //System.out.println("1. valA = " + valA + ", valB = " + valB + ", valC = " + valC);

            newVal = Math.max(newVal, valA + valB);
            newVal = Math.max(newVal, valA + valC);
            newVal = Math.max(newVal, valC + valB);

            for (int i = 0; i < 4; i++) {
                if (d_p.branchOfKthLongestPath[i] != kthParentOfU && d_p.branchOfKthLongestPath[i] != kthParentOfV) {
                    for (int j = i + 1; j < 4; j++) {
                        if (d_p.branchOfKthLongestPath[j] != kthParentOfU && d_p.branchOfKthLongestPath[j] != kthParentOfV) {
                            newVal = Math.max(newVal, d_p.kthLongestPath[i] + d_p.kthLongestPath[j]);
                        }
                    }
                }
            }

            for (int i = 0; i < 2; i++) {
                if (d_p.branchOfKthLongestDiameterA[i] != kthParentOfU && d_p.branchOfKthLongestDiameterA[i] != kthParentOfV) {
                    newVal = Math.max(newVal, d_p.kthLongestDiameterA[i]);
                }
            }

            newVal = Math.max(newVal, d_v.getLongestDiameterLCA(d_v.h - d_p.h - 1));
            newVal = Math.max(newVal, d_u.getLongestDiameterLCA(d_u.h - d_p.h - 1));

            valC = Math.max(valC, valB) + d_fu.depth - d_p.depth;
            valC = Math.max(valC, d_u.getLongestUpLCA(d_u.h - d_p.h - 1) - d_u.depth + d_fu.depth);

            valA = d_u.getLongestOfNotBranch(fu);
            valB = d_v.getLongestOfNotBranch(fv);

           // System.out.println("1. valA = " + valA + ", valB = " + valB + ", valC = " + valC);

            Graph g = vt.getLUB();
            double wuv = weightOfEdge(g.getEdge(u, v));
            double wfuv = weightOfEdge(g.getEdge(fu, v));
            newVal = Math.max(newVal, valA + valB + wuv);
            newVal = Math.max(newVal, valC + valB + wfuv);
            newVal = Math.max(newVal, valA + valC + wuv + wfuv);

            return newVal - value;
        }

	}

//    public void debugGetReplaceEdgeDelta(VarGraph vt, Edge ei, Edge eo) {
//        // TODO Auto-generated method stub
//        Data tmp;
//        Node uo = eo.getBegin();
//        Node vo = eo.getEnd();
//        Data duo = dataOfNodes.get(uo);
//        Data dvo = dataOfNodes.get(vo);
//        if (duo.L > dvo.L) {
//            uo = eo.getEnd();
//            vo = eo.getBegin();
//            tmp = duo;
//            duo = dvo;
//            dvo = tmp;
//        }
//        System.out.println("Debug");
//        System.out.println("data uo = " + uo);
//        duo.print();
//        System.out.println("data vo = " + vo);
//        dvo.print();
//        double newVal = Math.max(dvo.diameterA, dvo.diameterB);
//        System.out.println("newVal : " + newVal);
//
//        Node ui = ei.getBegin();
//        Node vi = ei.getEnd();
//        Data dui = dataOfNodes.get(ui);
//        Data dvi = dataOfNodes.get(vi);
//        if (dvo.L <= dui.L && dvo.R >= dui.L) {
//            ui = ei.getEnd();
//            vi = ei.getBegin();
//            tmp = dui;
//            dui = dvi;
//            dvi = tmp;
//        }
//        System.out.println("data ui = " + ui);
//        dui.print();
//        System.out.println("data vi = " + vi);
//        dvi.print();
//
//        double valA = 0;
//        tmp = dataOfNodes.get(dvi.branchOfKthLongestPath[0]);
//        if (tmp.L < dvi.L) {
//            valA = dvi.kthLongestPath[1];
//        } else {
//            valA = dvi.kthLongestPath[0];
//        }
//        System.out.println("valA = " + valA);
//        valA = Math.max(valA, dvi.getLongestUpLCA(dvi.h - dvo.h));
//        System.out.println("valA = " + valA);
//
//        double valB = 0;
//        if (dui.L <= duo.L && dui.R >= duo.L) {
//            System.out.println("----");
//            valB = dvo.getLongestDownLCA(duo.h - dui.h) - dui.depth;
//            System.out.println("valB = " + valB);
//            tmp = dataOfNodes.get(dui.branchOfKthLongestPath[0]);
//            if ((tmp.L < dui.L && tmp.R >= dui.L) || tmp.L > dvo.R || tmp.R < dvo.L) {//if (tmp.L <= dvo.L && tmp.R >= dvo.L) {
//                System.out.println("....");
//                valB = Math.max(valB, dui.kthLongestPath[0]);
//            } else {
//                System.out.println(",,,,");
//                valB = Math.max(valB, dui.kthLongestPath[1]);
//            }
//            System.out.println("valB = " + valB);
//        } else {
//            System.out.println("++++");
//            tmp = dataOfNodes.get(dui.branchOfKthLongestPath[0]);
//            if (tmp.L < dui.L) {
//                System.out.println("....");
//                valB = dui.kthLongestPath[1];
//            } else {
//                System.out.println(",,,,");
//                valB = dui.kthLongestPath[0];
//            }
//            System.out.println("valB = " + valB);
//            Data dp = getLCA(dui, duo);
//            System.out.println("dp = " + dp.v);
//            dp.print();
//            System.out.println("getLongestUpLca " + dui.getLongestUpLCA(dui.h - dp.h - 1));
//
//            valB = Math.max(valB, dui.getLongestUpLCA(dui.h - dp.h - 1));
//
//            //valB = Math.max(valB, dui.getLongestUpLCA(dui.h - 1));
//            System.out.println("getLongestDownLca " + dvo.getLongestDownLCA(duo.h - dp.h));
//            if (duo.h - dp.h > 0) {
//                valB = Math.max(valB, dui.depth + dvo.getLongestDownLCA(duo.h - dp.h) - 2 * dp.depth);
//            }
//            System.out.println("valB = " + valB);
//            valB = Math.max(valB, dui.depth + duo.depth - 2 * dp.depth);
//            //valB = Math.max(valB, dui.depth + dvo.getLongestDownLCA(duo.h));
//            System.out.println("valB = " + valB);
//
//
//
//
//            //Data droot = dataOfNodes.get(root);
//            for (int i = 0; i < 3 && dp.branchOfKthLongestPath[i] != null; i++) {
//                tmp = dataOfNodes.get(dp.branchOfKthLongestPath[i]);
//                System.out.println("????");
//                tmp.print();
//                System.out.println(tmp.L + " " + tmp.R);
//                System.out.println(dui.L + " " + dui.R);
//                System.out.println(duo.L + " " + duo.R);
//                if ((tmp.L < dp.L && tmp.R >= dp.L) || (!((tmp.L <= dui.L && tmp.R >= dui.L) || ((tmp.L <= dvo.L && tmp.R >= dvo.L))))) {
//                    valB = Math.max(valB, dui.depth + dp.kthLongestPath[i] - dp.depth);
//                    System.out.println("abcxyz");
//                    break;
//                }
//            }
//
//
//            System.out.println("valB = " + valB);
//        }
//        System.out.println("valA + valB + weightOfEdge(ei) = " + (valA + valB + weightOfEdge(ei)));
//        //newVal = Math.max(newVal, valA + valB + weightOfEdge(ei));
////        return newVal - value;
//    }

}
