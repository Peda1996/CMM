package at.jku.ssw.cmm.gui.event;

import static at.jku.ssw.cmm.gettext.Language._;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import at.jku.ssw.cmm.gui.GUImain;
import at.jku.ssw.cmm.gui.GUImainSettings;
import at.jku.ssw.cmm.gui.debug.GUIdebugPanel;
import at.jku.ssw.cmm.gui.file.FileManagerCode;
import at.jku.ssw.cmm.gui.file.SaveDialog;
import at.jku.ssw.cmm.launcher.GUILauncherMain;
import at.jku.ssw.cmm.profile.Profile;
import at.jku.ssw.cmm.profile.settings.GUIprofileSettings;

/**
 * Contains event listeners for the menu bar in the main GUI.
 * 
 * @author fabian
 *
 */
public class MenuBarEventListener {

	/**
	 * Contains event listeners for the menu bar in the main GUI.
	 * 
	 * @param jFrame
	 *            The main window frame
	 * @param jSourcePane
	 *            The text area for the source code
	 * @param main
	 *            Reference to the main GUI
	 * @param settings
	 *            A reference to the configuration object of the main GUI
	 * @param modifier
	 *            Reference to the debug panel of the GUI which is responsible
	 *            for debug control elements and the variable tree table
	 * @param saveDialog
	 *            A reference to the save dialog manager class
	 */
	public MenuBarEventListener(JFrame jFrame, RSyntaxTextArea jSourcePane,
			JTextPane jInputPane, GUImain main, GUImainSettings settings,
			GUIdebugPanel debug, SaveDialog saveDialog) {
		this.jFrame = jFrame;
		this.jSourcePane = jSourcePane;
		this.jInputPane = jInputPane;
		this.main = main;
		this.settings = settings;
		this.debug = debug;
		this.saveDialog = saveDialog;
	}
	
	public MenuBarEventListener(JFrame jFrame, RSyntaxTextArea jSourcePane,
			JTextPane jInputPane, GUImain main, GUImainSettings settings,
			GUIdebugPanel debug, SaveDialog saveDialog, Profile profile) {
		this(jFrame, jSourcePane, jInputPane, main, settings, debug, saveDialog);
		this.profile = profile;
	}

	/**
	 * The main window frame
	 */
	private final JFrame jFrame;

	/**
	 * The text area for the source code
	 */
	private final RSyntaxTextArea jSourcePane;

	/**
	 * The input text pane
	 */
	private final JTextPane jInputPane;

	/**
	 * Reference to the main GUI
	 */
	private final GUImain main;

	/**
	 * A reference to the configuration object of the main GUI
	 */
	private final GUImainSettings settings;

	/**
	 * Reference to the debug panel of the GUI which is responsible for debug
	 * control elements and the variable tree table
	 */
	private final GUIdebugPanel debug;

	/**
	 * A reference to the save dialog manager class
	 */
	private final SaveDialog saveDialog;
	
	/**
	 * A profile reference
	 */
	private Profile profile;

	/**
	 * Event listener for the "new file" entry in the "file" drop-down menu
	 */
	public ActionListener newFileHandler = new ActionListener() {
		

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (settings.getCMMFilePath() == null) {
				// Custom button text
				Object[] options = { _("Save now"), _("Proceed without saving") };

				// Init warning dialog with two buttons
				int n = JOptionPane.showOptionDialog(jFrame,
						_("The current file has not yet been saved!"),
						_("Opening new file"), JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE, null, // do not use a
															// custom Icon
						options, // the titles of buttons
						options[0]); // default button title

				if (n == JOptionPane.YES_OPTION)
					// Save the last changes to current file path
					saveDialog.directSave();
			}

			jSourcePane.setText("");
			settings.setCMMFilePath(null);
			main.updateWinFileName();
			debug.updateFileName();
		}

	};

	/**
	 * Event listener for the "open" entry in the "file" drop-down menu
	 */
	public ActionListener openHandler = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			main.getSaveManager().safeCheck(_("Opening new file"));

			// Create file chooser (opens a window to select a file)
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("CMM "
					+ _("file"), "cmm"));

			// User selected a file
			if (chooser.showOpenDialog(jFrame) == JFileChooser.APPROVE_OPTION) {

				openFile(chooser.getSelectedFile());
			}
		}

	};
	
	public void openFile( File file ){
		// Open file and load text t source code panel
		jSourcePane.setText(FileManagerCode.readSourceCode(file));

		// Set input data
		jInputPane.setText(FileManagerCode.readInputData(file));

		// Set the new C-- file directory
		settings.setCMMFilePath(file.getPath());

		main.updateWinFileName();
		debug.updateFileName();
	}
	
	public RecentFileHandler getRecentFileHandler( String path ){
		return new RecentFileHandler(path);
	}
	
	public class RecentFileHandler implements ActionListener{
		
		public RecentFileHandler( String path ){
			this.path = path;
		}
		
		private final String path;

		@Override
		public void actionPerformed(ActionEvent e) {
			File file = new File(path);
			
			main.getSaveManager().safeCheck(_("Opening new file"));
			
			if (!file.exists()){
				//Show error message with information about the error
				JOptionPane.showMessageDialog(new JFrame(),
						_("The following file could not be found: ") + "\n" + path,
						_("File does not exist"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			openFile(new File(path));
		}
	}

	/**
	 * Event listener for the "save as" entry in the "file" drop-down menu
	 */
	public ActionListener saveAsHandler = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			// Call the main GUI's save dialog (contains file chooser and save
			// routines
			saveDialog.doSaveAs();
			main.setFileSaved();
			main.updateWinFileName();
			debug.updateFileName();
		}

	};

	/**
	 * Event listener for the "save" entry in the "file" drop-down menu
	 */
	public ActionListener saveHandler = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (settings.getCMMFilePath() != null)
				// Save to working directory
				saveDialog.directSave();
			else
				// Open "save as" dialog if there is no working directory
				saveDialog.doSaveAs();

			main.setFileSaved();
			main.updateWinFileName();
			debug.updateFileName();

		}
	};

	/**
	 * Event listener for the "exit" entry in the "file" drop-down menu - closes
	 * the program
	 */
	public ActionListener exitHandler = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if(main.getSaveManager().safeCheck(_("Closing C Compact")))
				WindowEventListener.updateAndExit(jFrame, settings);
		}
	};

	/**
	 * Event listener for the "select profile" option in the "progress"
	 * drop-down menu of menu bar
	 */
	public ActionListener profileHandler = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			main.dispose();
			GUILauncherMain.init();
		}
	};

	/**
	 * Event listener for the "select quest" option in the "progress" drop-down
	 * menu of the menu bar
	 */
	public ActionListener questHandler = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			main.startQuestGUI();
		}
	};
	
	/**
	 * Event listener for the "edit profile" option in the "progress"
	 * drop-down menu of menu bar
	 */
	public ActionListener editProfileHandler = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			main.dispose();
			GUIprofileSettings.init(profile);
		}
	};
	
}
