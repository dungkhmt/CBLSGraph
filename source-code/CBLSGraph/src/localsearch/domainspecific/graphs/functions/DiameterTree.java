package localsearch.domainspecific.graphs.functions;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;
import localsearch.domainspecific.graphs.model.VarTree;

import java.util.HashMap;

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
    }

    private double weightOfEdge(Edge e) {
        return idxWeight < 0 ? 1 : e.getWeight(idxWeight);
    }

    private Data dfs1(Node u, Node f) {
        Data du = new Data(u);
        du.L = count++;
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
            valB = dvo.getLongestDownLCA(duo.h - dui.h) - dui.depth;
            tmp = dataOfNodes.get(dui.branchOfKthLongestPath[0]);
            if (tmp.L <= dvo.L && tmp.R >= dvo.L) {
                valB = Math.max(valB, dui.kthLongestPath[1]);
            } else {
                valB = Math.max(valB, dui.kthLongestPath[0]);
            }
        } else {
            tmp = dataOfNodes.get(dui.branchOfKthLongestPath[0]);
            if (tmp.L < dui.L) {
                valB = dui.kthLongestPath[1];
            } else {
                valB = dui.kthLongestPath[0];
            }
            valB = Math.max(valB, dui.getLongestUpLCA(dui.h - 1));
            valB = Math.max(valB, dui.depth + dvo.getLongestDownLCA(duo.h));

            Data droot = dataOfNodes.get(root);
            for (int i = 0; i < 3 && droot.branchOfKthLongestPath[i] != null; i++) {
                tmp = dataOfNodes.get(droot.branchOfKthLongestPath[i]);
                if (!((tmp.L <= dui.L && tmp.R >= dui.L) || (tmp.L <= dvo.L && tmp.R >= dvo.L))) {
                    valB = Math.max(valB, dui.depth + droot.kthLongestPath[i]);
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
        return 0;
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
                diameterB = Math.max(du.diameterB, du.kthLongestDiameterA[0]);
            } else {
                diameterB = Math.max(du.diameterB, du.kthLongestDiameterA[1]);
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
    }

    public static void main(String[] args) {

    }
}
