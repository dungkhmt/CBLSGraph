package localsearch.domainspecific.graphs.search.moves;

import localsearch.domainspecific.graphs.core.LexMultiValues;

public interface GMove {
	public void move();
	public LexMultiValues evaluation();
}
