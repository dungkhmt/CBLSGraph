package localsearch.domainspecific.graphs.functions;

import localsearch.domainspecific.graphs.core.Edge;
import localsearch.domainspecific.graphs.core.Node;
import localsearch.domainspecific.graphs.model.LSGraphManager;
import localsearch.domainspecific.graphs.model.VarGraph;
import localsearch.domainspecific.graphs.model.VarRootedTree;
import localsearch.domainspecific.graphs.model.VarTree;

public class DiameterTree implements GFunction {

    private Node root;
    private VarTree vt;
    private int idxWeight;
    private double[] diameterA;
    private double[] diameterB;
    private double[][] maxLength;

    public DiameterTree(VarRootedTree vt) {
        idxWeight = -1;
        this.vt = vt;
    }

    private double weightOfEdge(Edge e) {
        return idxWeight < 0 ? 1 : e.getWeight(idxWeight);
    }

    @Override
    public VarGraph[] getVarGraphs() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LSGraphManager getLSGraphManager() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setID(int id) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getID() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void initPropagate() {
        // TODO Auto-generated method stub

    }

    @Override
    public void propagateAddEdge(VarRootedTree vt, Edge e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void propagateRemoveEdge(VarRootedTree vt, Edge e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void propagateReplaceEdge(VarRootedTree vt, Edge eo, Edge ei) {
        // TODO Auto-generated method stub

    }

    @Override
    public double getAddEdgeDelta(VarRootedTree vt, Edge e) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getRemoveEdgeDelta(VarRootedTree vt, Edge e) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getReplaceEdgeDelta(VarRootedTree vt, Edge ei, Edge eo) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getValue() {
        // TODO Auto-generated method stub
        return 0;
    }

}
