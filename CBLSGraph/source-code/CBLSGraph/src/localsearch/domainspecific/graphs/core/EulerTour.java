/*
 * authors: DungPQ
 * date 12/7/2015
 */
package localsearch.domainspecific.graphs.core;

public class EulerTour {
	Node[]					_ET;
	Node[]					_ETT;
	int[]							_level;
	int[]							_tlevel;
	int[]							_ind;
	//range							_R;
	int								_low;
	int								_up;
	int								_n;
	int								_l;
	int								_u;
	public EulerTour(Node[] ET, int[] level, int[] ind){
		_ET = ET;
		_level = level;
		//_R = _ET.rng();
		_low = 1;//0;
		_up = _ET.length-1;
		_n = _up;
		_ETT = new Node[_up+1];
		_tlevel = new int[_up+1];
		for(int i = 0; i <= _up; i++)
			_tlevel[i] = 0;
		_ind = ind;
	}
	public void initPropagation(){
	}
	int firstOccurence(Node v, Node[] ET){
		int i = 0;//ET.rng().getLow();
		while(i <= _n && v != ET[i])
			i++;
		if(i > _n)
			return  -1;	
		return i;
	}
	int lastOccurence(Node v, Node[] ET){
		int i = _n;
		while(i >= _low && v != ET[i])
			i--;
		if(i < _low)
			return  -1;	
		return i;
	}
	public int getLowIndexChanged(){
		return _l;
	}
	public int getUpIndexChanged(){
		return _u;
	}
	public void setSize(int n){
		_n = n;
	}
	int getSize(){
		return _n;
	}
	public int index(Node v){
		return _ind[v.getID()];
	}
	public Node node(int i){
		return _ET[i];
	}
	public Node[] getET(){
		return _ET;
	}
	public int[] getLevel(){
		return _level;
	}
	public int[] getIndex(){
		return _ind;
	}
	public void updateRMQWhenAdd(Node leaf, Node other){
		int b = lastOccurence(other,_ET);
		for(int i = b+1; i <= _n; i++){
			if(_ind[_ET[i].getID()] == i && 
				_ind[_ET[i].getID()] != 0) 
				_ind[_ET[i].getID()] = 0;
		}
		int i = _n;
		while(i > b){
			_ET[i+2] = _ET[i];
			_level[i+2] = _level[i];
			i--;
		}
		_ET[b+2] = other;
		_level[b+2] = _level[b];
		_ET[b+1] = leaf;
		_level[b+1] = _level[b]+1;
		_n = _n + 2;
		for(int j = b+1; j <= _n; j++){
			if(_ind[_ET[j].getID()] == 0)
				_ind[_ET[j].getID()] = j;
		}
		_l = b+1;
		_u = _n;
	}
	public String getName(){
		return "EulerTour";
	}
	public void updateRMQWhenRemove(Node cv){
		int b = lastOccurence(cv,_ET);
		b--;
		for(int i = b+1; i <= _n; i++){
			if(_ind[_ET[i].getID()] == i && 
				_ind[_ET[i].getID()] != 0) 
				_ind[_ET[i].getID()] = 0;
		}
		int i = b+1;
		while(i <= _n - 2){
			_ET[i] = _ET[i+2];
			_level[i] = _level[i+2];
			i++;
		}
		_n = _n - 2;
		for(int ii = b; ii <= _n; ii++){
			if(_ind[_ET[ii].getID()] == 0)
				_ind[_ET[ii].getID()] = ii;
		}
		_l = b;
		_u = _n;
	}
	public void updateRMQWhenReplace(Node u1, Node v1, Node u2, Node v2){
		int ov21 = firstOccurence(v2,_ET);	
		int ov22 = lastOccurence(v2,_ET);
		int ou12 = lastOccurence(u1,_ET);
		int len = ov22 - ov21 + 1;
		int new_ov21;
		int new_ov22;
		int new_ou12;
		int l;
		int u;
		if(ou12 < ov22){
			l = ou12+1;
			u = ov22+1;
			new_ou12 = ou12;
		}else{
			l = ov21;
			u = ou12;
			new_ou12 = ou12 - len - 1;
		}
		new_ov22 = _n - 1;
		new_ov21 = new_ov22 - len + 1;
		for(int i = l; i <= u; i++){
			if(_ind[_ET[i].getID()] == i && 
				_ind[_ET[i].getID()] != 0) 
				_ind[_ET[i].getID()] = 0;
		}
		for(int i = l; i <= u; i++){
			if(i >= ov21 && i <= ov22)
				_tlevel[_ET[i].getID()] = -1;
			else
				_tlevel[_ET[i].getID()] = _level[i];
		}
		int i = ov21;
		int j = ov22 + 2;
		while(j <= _n){
			_ETT[i] = _ET[j];
			i++; j++;
		}
		j = ov21;
		int old_ov21 = ov21;
		while(j <= ov22){
			_ETT[i] = _ET[j];
			i++;j++;
		}
		_ETT[_n] = u1; 
		for(int ii = old_ov21; ii <=_n; ii++){
			_ET[ii] = _ETT[ii];
		}
		int ov11 = firstOccurence(v1,_ET);
		i = ov11;
		j = new_ov21;
		while(i <= new_ov22){
			_ETT[j] = _ET[i];
			i++;j++;
		}
		i = new_ov21+1;
		while(j <= new_ov22-1){
			_ETT[j] = _ET[i];
			i++;j++;
		}
		_ETT[j] = v1;
		for(int ii = new_ov21; ii <= new_ov22; ii++){
			_ET[ii] = _ETT[ii];
		}
		i = new_ou12 + 1;
		j = new_ov21;
		while(j <= new_ov22+1){
			_ETT[i] = _ET[j];
			i++;j++;
		}	
		j = new_ou12+1;
		while(i <= _n){
			_ETT[i] = _ET[j];
			i++;j++;
		}
		for(int ii = new_ou12+1; ii <= _n; ii++){
			_ET[ii] = _ETT[ii];
		}
		for(int ii = l; ii <= u; ii++){
			if(_ind[_ET[ii].getID()] == 0)
				_ind[_ET[ii].getID()] = i;
		}
		_level[1] = 1;
		for(int ii = l; ii <= u; ii++)if(ii > 1){
			Node v = _ET[ii];
			if(_ind[v.getID()] == i){
				_level[ii] = _level[ii-1] + 1;
			}else{
				_level[ii] = _level[ii-1] - 1;
			}
		}	
		_l = l;
		_u = u;
	}
	public void print(int[] level){
		for(int i = 1; i <= _n; i++)
			System.out.print(level[i] + "\t");
		System.out.println();
	}
	void print(Node[] _ET){
		for(int i = 1; i <= _n; i++){
			if(_ET[i] != null)
				System.out.print(_ET[i].getID() + "\t");
			else 
				System.out.print(0 + "\t");
		}
		System.out.println();
	}
	public void printInd(int[] ind){
		int[] ii = new int[ind.length];
		for(int j = 0; j < ind.length; j++) ii[j] = 0;
		for(int i = 1; i <= _n; i++)
			ii[_ET[i].getID()] = 1;
		for(int i=0; i < ind.length; i++)if(ii[i] == 1)
			System.out.print(i + "(" + ind[i] + ")\t");
		System.out.println();
	}
	void print(){
		print(_ET);
		print(_level);
		printInd(_ind);
	}
	Node[] getTour(){
		return _ET;
	}	
	public boolean verify(){
		System.out.println(getName() + "::verify()....");
		int[] level = new int[_level.length];
		for(int i = 0; i < _level.length; i++) level[i] = 0;
		int[] index = new int[_ind.length];
		for(int i = 0; i < _ind.length; i++) index[i] = 0;
		level[1] = 1;
		for(int i = 1; i <= _n; i++){
			Node v = _ET[i];
			if(index[v.getID()] == 0){
				index[v.getID()] = i;
				if(index[v.getID()] != _ind[v.getID()]){
					System.out.print(getName() + "::verify->failed, index[" + v.getID() + "] = " + 
				index[v.getID()] + " but _ind[" + v.getID() + "] = ");
					System.out.println(_ind[v.getID()]);
					return false;
				}
				if(i > 1){
					level[i] = level[i-1] + 1;
				}else{
				}	
			}else{
				level[i] = level[i-1]-1;
				if(level[i] != level[index[v.getID()]]){
					System.out.print(getName() + "::verify -> failed inconsistent at position " + i + " : ");
					System.out.println("level[" + i + "] = " + level[i] + " differs from level[" +
					index[v.getID()] + "] = " + level[index[v.getID()]] );
					return false;
				}
			}	
		}
		for(int i = 1; i <= _n; i++)
			if(level[i] != _level[i]){
				System.out.print(getName() + "::verify->failed level != _level at position " + i);
				System.out.println(" level[" + i + "] = " + level[i] + " but _level[" + i + "] = " +
				_level[i]);
				return false;
			}
		return true;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
