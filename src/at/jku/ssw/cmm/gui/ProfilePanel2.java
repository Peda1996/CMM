package at.jku.ssw.cmm.gui;

import static at.jku.ssw.cmm.gettext.Language._;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import at.jku.ssw.cmm.gui.file.LoadStatics;
import at.jku.ssw.cmm.profile.Profile;
import at.jku.ssw.cmm.profile.settings.CentralPanel;

public class ProfilePanel2 {
	
	public ProfilePanel2( GUImain main ) {
		this.main = main;
		init();
	}
	
	private final GUImain main;
	
	private JLabel jProfilePicture;
	private JTextField jProfileName;
	
	private String profilePicPath;
	
	private ProfileListener listener = new ProfileListener(this);
	
	//private ProfileSettingsListener listener = new ProfileSettingsListener(main.getSettings());
	private CentralPanel centralPanel;
	
	private JPanel masterPanel;
	
	public void init() {
		
		masterPanel = new JPanel();
		masterPanel.setLayout(new BorderLayout());
		
		masterPanel.add(this.initProfileInfo(), BorderLayout.PAGE_START);
		centralPanel = new CentralPanel(this.main.getSettings().getProfile()) ;
		masterPanel.add(centralPanel, BorderLayout.CENTER);
	}
	
	public JComponent getProfilePanel(){
		return masterPanel;
	}
	
	public CentralPanel getCentralPanel(){
		return centralPanel;
	}
	
	public void refreshCentralPanel(){
		masterPanel.remove(centralPanel);
		this.centralPanel = new CentralPanel(this.main.getSettings().getProfile());
		masterPanel.add(centralPanel, BorderLayout.CENTER);
		masterPanel.repaint();
	}
	
	private JPanel initProfileInfo() {
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(_("Profile Information")));
		Box box = new Box(BoxLayout.Y_AXIS);
	    box.setAlignmentX(JComponent.CENTER_ALIGNMENT);
	    box.add(Box.createVerticalGlue());
		
		Profile profile = this.main.getSettings().getProfile();
		
		//Init Profile text field
		this.jProfileName = new JTextField(profile.getName());
		jProfileName.addFocusListener(listener.nameFocusListener);
		jProfileName.setToolTipText("<html><b>" + _("click to change name")
				+ "</b><br>" + _("click here to change your<br>profile name")
				+ "</html>");
		box.add(this.jProfileName);
		
		//Load Profile Image
		this.jProfilePicture = new JLabel();
		
		loadProfilePic();
		jProfilePicture.setToolTipText("<html><b>" + _("click to change image")
				+ "</b><br>" + _("click here and select your<br>profile image")
				+ "</html>");
		jProfilePicture.addMouseListener(listener.profileImageListener);
		
        box.add(jProfilePicture);
        box.add(Box.createVerticalGlue());
        
		panel.add(box);
		
		return panel;
	}
	
	
	/**
	 * The choosen Profile Pic Path, in the image selection window
	 * @return profilePicPath
	 */
	public String getProfilePic(){
		
		return profilePicPath;
	}
	
	
	/**
	 * load the current profile Image
	 */
	public void loadProfilePic(){
		Profile profile = main.getSettings().getProfile();
		
		if (profile == null || profile.getProfileimage() == null)
			jProfilePicture.setIcon(LoadStatics.loadIcon(Profile.IMAGE_DEFAULT,
					128, 128));
		else
			//Loading fixed profile image
			if(profile.getInitPath() != null)
				jProfilePicture.setIcon(LoadStatics.loadIcon(profile.getInitPath() + Profile.sep + profile.getProfileimage(), 128, 128));
		
			//Loading temporary chosen Image files
			else
				jProfilePicture.setIcon(LoadStatics.loadIcon(profile.getProfileimage(), 128, 128));
		
		if(jProfilePicture.getIcon() == null || (jProfilePicture.getIcon().getIconHeight() == -1 && jProfilePicture.getIcon().getIconWidth() == -1)){
			jProfilePicture.setText(_("Corrupted Avatar"));
			jProfilePicture.setIcon(LoadStatics.loadIcon(Profile.IMAGE_DEFAULT,	128, 128));
		}
		else
			jProfilePicture.setText("");
				

		jProfilePicture.revalidate();
		jProfilePicture.repaint();
	}
	
	/**
	 * Refreshes the Profile pic, TODO
	 */
	public void refreshProfilePic(String profilePicPath){
		Profile profile = main.getSettings().getProfile();
		
		if( profile != null )
			System.out.println("Current ProfilePic: " + profile.getProfileimage());
		jProfilePicture.setIcon(LoadStatics.loadIcon(profilePicPath,  128, 128));
		
		//Setting for later usage
		if(jProfilePicture.getIcon().getIconHeight() == -1 && jProfilePicture.getIcon().getIconWidth() == -1)
			jProfilePicture.setText(_("Corrupted Image"));
		else{
			//Only saving if the file is useable, TODO Error field
			this.profilePicPath = profilePicPath;
			jProfilePicture.setText("");
		}
		
	}
	
	public GUImain getGUImain(){
		return main;
	}
	
	public JTextField getJProfileName(){
		return jProfileName;
	}
	
	

}
