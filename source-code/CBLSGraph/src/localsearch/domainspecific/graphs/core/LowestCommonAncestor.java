package localsearch.domainspecific.graphs.core;

import localsearch.domainspecific.graphs.model.VarRootedTree;

public class LowestCommonAncestor {
	
private VarRootedTree vt;
	
	private int n;
	private int count;
	private int[] level;
	private int[] pos;
	private int[][] rmq;
	private int[] log2;
	private Node[] eulerTour;
	
	public LowestCommonAncestor(VarRootedTree vt) {
		this.vt = vt;
		init();
	}
	
	private void init() {
		n = vt.getLUB().getNbrNodes();
		eulerTour = new Node[2 * n - 1];
		level = new int[2 * n - 1];
		
		int maxId = 0;
		for (Node u : vt.getLUB().getNodes()) {
			maxId = Math.max(maxId, u.getID());
		}
		pos = new int[maxId + 1];
		
		log2 = new int[2 * n];
		for (int i = 0, j = 0; i < log2.length; i++) {
			if (i == (1 << (j + 1))) {
				j++;
			}
			log2[i] = j;
		}
		
		rmq = new int[2 * n - 1][log2[2 * n - 1] + 1];
	}
	
	private void DFS(Node u, int l) {
		level[count] = l;
		pos[u.getID()] = count;
		eulerTour[count++] = u;
		Node fu = vt.getFatherNode(u);
		for (Edge e : vt.getAdj().get(u)) {
			Node v = e.otherNode(u);
			if (v != fu) {
				DFS(v, l + 1);
				level[count] = l;
				eulerTour[count++] = u;
			}
		}
	}
	
	public void makeNCA() {
		count = 0;
		DFS(vt.root(), 0);
		
		for (int i = 0; i < count; i++) {
			rmq[i][0] = i;
		}
		for (int j = 1, step = 1; j <= log2[count]; j++, step <<= 1) {
			for (int i = 0; i + 2 * step < count; i++) {
				if (level[rmq[i][j - 1]] > level[rmq[i + step][j - 1]]) {
					rmq[i][j] = rmq[i + step][j - 1];
				} else {
					rmq[i][j] = rmq[i][j - 1];
				}
			}
		}
	}
	
	public Node nca(Node u, Node v) {
		int i = pos[u.getID()];
		int j = pos[v.getID()];
		if (i > j) {
			j = pos[u.getID()];
			i = pos[v.getID()];
		}
		int b = log2[j - i + 1];
		if (level[rmq[i][b]] > level[rmq[j - (1 << b) + 1][b]]) {
			return eulerTour[rmq[j - (1 << b) + 1][b]];
		} else {
			return eulerTour[rmq[i][b]];
		}
	}
	
	public void updateWhenRemove(Node u) {
		makeNCA();
	}
	
	public void updateWhenAdd(Node leaf, Node other) {
		makeNCA();
	}
	
	public void updateWhenReplace(Node u1, Node v1, Node u2, Node v2) {
		makeNCA();
	}
}
