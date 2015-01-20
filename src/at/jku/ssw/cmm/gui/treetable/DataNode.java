package at.jku.ssw.cmm.gui.treetable;

import java.util.List;

import at.jku.ssw.cmm.gui.treetable.example.DataNodeExample;

public abstract class DataNode {
	public abstract List<DataNode> getChildren();
	public abstract int getChildCount();
	public abstract Object getValueByColumn(int col);
	public abstract void addChild(DataNode n);
	
	@Override
	public abstract String toString();
	public void add(DataNode subNode) {
		// TODO Auto-generated method stub
		
	}
	
	
}
