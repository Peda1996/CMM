package at.jku.ssw.cmm.launcher;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import at.jku.ssw.cmm.gui.GUImain;
import at.jku.ssw.cmm.gui.GUImainSettings;
import at.jku.ssw.cmm.profile.Profile;


public class LauncherListener implements MouseListener{
	
	private Profile profile;
	private JFrame jFrame;
	
	public LauncherListener(Profile profile, JFrame jFrame) {
		this.profile = profile;
		this.jFrame = jFrame;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(profile != null){
			Profile.setActiveProfile(profile);
			
			
			jFrame.dispose();
			System.out.println("Profile set");
			
			GUImain app = new GUImain(new GUImainSettings(profile));
			app.start(false);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

}
 