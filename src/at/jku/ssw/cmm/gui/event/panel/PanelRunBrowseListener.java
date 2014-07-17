package at.jku.ssw.cmm.gui.event.panel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import at.jku.ssw.cmm.gui.GUIrightPanel;

public class PanelRunBrowseListener implements MouseListener {
	
	public PanelRunBrowseListener( GUIrightPanel master, boolean global ){
		this.master = master;
		this.global = global;
	}
	
	private final GUIrightPanel master;
	private final boolean global;

	@Override
	public void mouseClicked(MouseEvent e) {
		
		this.master.browseBack(this.global);
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}
