/*
 *  This file is part of C-Compact.
 *
 *  C-Compact is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  C-Compact is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with C-Compact. If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright (c) 2014-2015 Fabian Hummer
 *  Copyright (c) 2014-2015 Thomas Pointhuber
 *  Copyright (c) 2014-2015 Peter Wassermair
 */
 
package at.jku.ssw.cmm.gui.treetable;

import javax.swing.JButton;

/**
 * Defines the complete data model as well as column names and column data types
 * <br>
 * <i>NOTE: </i> This code has been adapted from a tutorial, see {@link http://www.hameister.org/JavaSwingTreeTable.html}
 * 
 * @author fabian
 *
 */
public class TreeTableDataModel<TreeNode extends DataNode> extends AbstractTreeTableModel<TreeNode> {
    // Column names
    protected final String[] columnNames;// = { _("Name"), _("Type"), _("Value") };
 
    // Column types
    protected final Class<?>[] columnTypes;// = { TreeTableModel.class, String.class, Object.class };
    
    private int currFunc;

    public TreeTableDataModel(TreeNode rootNode, String[] columnNames, Class<?>[] columnTypes, int currFunc) {
        super(rootNode);
        this.currFunc = currFunc;
        root = rootNode;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
    }
    
    public TreeTableDataModel(TreeNode rootNode, String[] columnNames, Class<?>[] columnTypes) {
        super(rootNode);
        this.currFunc = -1;
        root = rootNode;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
    }
    
    public TreeNode getRoot(){
    	return root;
    }

	@SuppressWarnings("unchecked")
	public Object getChild(Object parent, int index) {
        return ((TreeNode)parent).getChildren().get(index);
    }

    @SuppressWarnings("unchecked")
	public int getChildCount(Object parent) {
        return ((TreeNode)parent).getChildren().size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }
 
 
    public Class<?> getColumnClass(int column) {
        return columnTypes[column];
    }
 
    @SuppressWarnings("unchecked")
	public Object getValueAt(Object node, int column) {
        return ((TreeNode)node).getValueByColumn(column);
    }
 
    public boolean isCellEditable(Object node, int column) {
        // First column must be editable, so that the user can open a jTree node
    	// Cell with button must be editable too so that a click event is passed to the button
    	if( column == 0 || /*(column == 2 && */node instanceof JButton ) //)
        	return true;
    	// Other cells are not editable
        else
        	return false;
    }
 
    public void setValueAt(Object aValue, Object node, int column) {
    }
    
    public int getCurrentFunction(){
    	return this.currFunc;
    }

	public void setCurrentFunction(int currFunc) {
		this.currFunc = currFunc;
	}
 
}
