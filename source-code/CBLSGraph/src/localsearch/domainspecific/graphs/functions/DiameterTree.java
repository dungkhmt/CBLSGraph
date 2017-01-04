package localsearch.domainspecific.graphs.functions;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.core.UndirectedGraph;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;
import localsearch.domainspecific.graphs.model.VarTree;
import localsearch.domainspecific.graphs.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
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
        idxWeight = -1;
        this.vt = vt;
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
            //System.out.println("????");
            count = 0;
            root = vt.getNodes().iterator().next();
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
        double[] longestUpLCA;
        double[] longestDownLCA;
        Data[] lca;

        int L;
        int R;

        public Data(Node v) {
            this.v = v;
            diameterA = diameterB = 0;
            kthLongestPath = new double[3];
            kthLongestDiameterA = new double[2];
            branchOfKthLongestPath = new Node[3];
            branchOfKthLongestDiameterA = new Node[2];
            h = log2 = 0;
            depth = 0;
        }

        void updateLongestPath(double length, Node node) {
            for (int i = 0; i < 3; i++) {
                if (kthLongestPath[i] < length) {
                    for (int j = 2; j > i; j--) {
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
            longestUpLCA = new double[log2];
            longestDownLCA = new double[log2];
            lca = new Data[log2];
            depth = du.depth + w;

            lca[0] = du;
            if (v != du.branchOfKthLongestPath[0]) {
                longestUpLCA[0] = du.kthLongestPath[0] - du.depth;
                longestDownLCA[0] = du.kthLongestPath[0] + du.depth;
            } else {
                longestUpLCA[0] = du.kthLongestPath[1] - du.depth;
                longestDownLCA[0] = du.kthLongestPath[1] + du.depth;
            }

            for (int i = 1; i < log2; i++) {
                lca[i] = lca[i - 1].lca[i - 1];
                longestUpLCA[i] = Math.max(longestUpLCA[i - 1], lca[i - 1].longestUpLCA[i - 1]);
                longestDownLCA[i] = Math.max(longestDownLCA[i - 1], lca[i - 1].longestDownLCA[i - 1]);
            }
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
        UndirectedGraph lub = new UndirectedGraph();
        int n = 20;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i != j) {
                    lub.addEdgeByID((i - 1) * n + j, i, j);
                }
            }
        }
        lub.print();
        LSGraphManager mgr = new LSGraphManager();
        VarRootedTree vt = new VarRootedTree(mgr, lub, lub.getNodeByID(1));
        DiameterTree diameterTree = new DiameterTree(vt);

        // init tree
        for (int i = 2; i <= n; i++) {
            while (true) {
                Edge e = Utility.randomSelect(lub.getEdges());
                Node u = e.getBegin();
                Node v = e.getEnd();
                if ((vt.contains(u) && !vt.contains(v)) || (!vt.contains(u) && vt.contains(v))) {
                    vt.addEdge(e);
                    break;
                }
            }
        }
        mgr.close();
        //vt.print();

        Random rand = new Random();
        for (int step = 1; step <= 1000; step++) {
            System.out.println("Steps = " + step);

            ArrayList<Edge> EI = new ArrayList<Edge>();
            ArrayList<Edge> EO = new ArrayList<Edge>();
            for (Edge ei : lub.getEdges()) {
                if (!vt.contains(ei)) {
                    for (Edge eo : Utility.collectReplacedEdges(vt, ei)) {
                        EI.add(ei);
                        EO.add(eo);
                        double d = diameterTree.getReplaceEdgeDelta(vt, ei, eo);
                        double oldV = diameterTree.getValue();
                        vt.replaceEdgePropagate(eo, ei);
                        double newV = diameterTree.getValue();
                        vt.replaceEdgePropagate(ei, eo);
                        if (Math.abs(newV - (oldV + d)) > 1e-6) {
                            vt.print();
                            diameterTree.print();
                            System.out.println("ei = " + ei + ", eo = " + eo);
                            System.out.println("oldV = " + oldV + ", delta = " + d + ", newV = " + newV);
                            diameterTree.debugGetReplaceEdgeDelta(vt, ei, eo);
                            System.exit(-1);
                        }
                    }
                }
            }
            System.out.println("Ok");
            int p = rand.nextInt(EI.size());
            vt.replaceEdgePropagate(EO.get(p), EI.get(p));
        }
    }

    public void debugGetReplaceEdgeDelta(VarGraph vt, Edge ei, Edge eo) {
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
        System.out.println("Debug");
        System.out.println("data uo = " + uo);
        duo.print();
        System.out.println("data vo = " + vo);
        dvo.print();
        double newVal = Math.max(dvo.diameterA, dvo.diameterB);
        System.out.println("newVal : " + newVal);

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
        System.out.println("data ui = " + ui);
        dui.print();
        System.out.println("data vi = " + vi);
        dvi.print();

        double valA = 0;
        tmp = dataOfNodes.get(dvi.branchOfKthLongestPath[0]);
        if (tmp.L < dvi.L) {
            valA = dvi.kthLongestPath[1];
        } else {
            valA = dvi.kthLongestPath[0];
        }
        System.out.println("valA = " + valA);
        valA = Math.max(valA, dvi.getLongestUpLCA(dvi.h - dvo.h));
        System.out.println("valA = " + valA);

        double valB = 0;
        if (dui.L <= duo.L && dui.R >= duo.L) {
            System.out.println("----");
            valB = dvo.getLongestDownLCA(duo.h - dui.h) - dui.depth;
            System.out.println("valB = " + valB);
            tmp = dataOfNodes.get(dui.branchOfKthLongestPath[0]);
            if ((tmp.L < dui.L && tmp.R >= dui.L) || tmp.L > dvo.R || tmp.R < dvo.L) {//if (tmp.L <= dvo.L && tmp.R >= dvo.L) {
                System.out.println("....");
                valB = Math.max(valB, dui.kthLongestPath[0]);
            } else {
                System.out.println(",,,,");
                valB = Math.max(valB, dui.kthLongestPath[1]);
            }
            System.out.println("valB = " + valB);
        } else {
            System.out.println("++++");
            tmp = dataOfNodes.get(dui.branchOfKthLongestPath[0]);
            if (tmp.L < dui.L) {
                System.out.println("....");
                valB = dui.kthLongestPath[1];
            } else {
                System.out.println(",,,,");
                valB = dui.kthLongestPath[0];
            }
            System.out.println("valB = " + valB);
            Data dp = getLCA(dui, duo);
            System.out.println("dp = " + dp.v);
            dp.print();
            System.out.println("getLongestUpLca " + dui.getLongestUpLCA(dui.h - dp.h - 1));

            valB = Math.max(valB, dui.getLongestUpLCA(dui.h - dp.h - 1));

            //valB = Math.max(valB, dui.getLongestUpLCA(dui.h - 1));
            System.out.println("getLongestDownLca " + dvo.getLongestDownLCA(duo.h - dp.h));
            if (duo.h - dp.h > 0) {
                valB = Math.max(valB, dui.depth + dvo.getLongestDownLCA(duo.h - dp.h) - 2 * dp.depth);
            }
            System.out.println("valB = " + valB);
            valB = Math.max(valB, dui.depth + duo.depth - 2 * dp.depth);
            //valB = Math.max(valB, dui.depth + dvo.getLongestDownLCA(duo.h));
            System.out.println("valB = " + valB);




            //Data droot = dataOfNodes.get(root);
            for (int i = 0; i < 3 && dp.branchOfKthLongestPath[i] != null; i++) {
                tmp = dataOfNodes.get(dp.branchOfKthLongestPath[i]);
                System.out.println("????");
                tmp.print();
                System.out.println(tmp.L + " " + tmp.R);
                System.out.println(dui.L + " " + dui.R);
                System.out.println(duo.L + " " + duo.R);
                if ((tmp.L < dp.L && tmp.R >= dp.L) || (!((tmp.L <= dui.L && tmp.R >= dui.L) || ((tmp.L <= dvo.L && tmp.R >= dvo.L))))) {
                    valB = Math.max(valB, dui.depth + dp.kthLongestPath[i] - dp.depth);
                    System.out.println("abcxyz");
                    break;
                }
            }


            System.out.println("valB = " + valB);
        }
        System.out.println("valA + valB + weightOfEdge(ei) = " + (valA + valB + weightOfEdge(ei)));
        //newVal = Math.max(newVal, valA + valB + weightOfEdge(ei));
//        return newVal - value;
    }

}
