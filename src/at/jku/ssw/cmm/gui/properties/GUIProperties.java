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

package at.jku.ssw.cmm.gui.properties;

import static at.jku.ssw.cmm.gettext.Language._;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import at.jku.ssw.cmm.gui.GUImain;

public class GUIProperties {

	private static final int MAX_FONTSIZE_STEPS = 13;
	private static final int MAX_DESCSIZE_STEPS = 4;

	public GUIProperties(GUImain main) {
		this.main = main;

		this.listener = new PropertiesSliderListener(main, this);
	}

	private final GUImain main;
	private final PropertiesSliderListener listener;

	private JFrame jFrame;

	private JLabel jLabelCode;
	private JLabel jLabelText;
	private JLabel jLabelVar;
	private JLabel jLabelDesc;

	public void start() {

		// Thread analysis
		if (SwingUtilities.isEventDispatchThread())
			System.out.println("[EDT Analyse] Properties GUI runnung on EDT.");

		// Initialize window
		this.jFrame = new JFrame(_("Properties"));
		this.jFrame.setLocationRelativeTo(null);
		this.jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		this.jFrame.add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		mainPanel.add(this.initLayoutPanel());
		mainPanel.add(this.initCodeSizePanel());
		mainPanel.add(this.initTextSizePanel());
		mainPanel.add(this.initDescSizePanel());
		mainPanel.add(this.initDebugOptionPanel());
		// mainPanel.add(this.initVarSizePanel());
		this.updateTextSize();

		// Causes this Window to be sized to fit the preferred size and layouts
		// of its subcomponents.
		this.jFrame.setResizable(false);
		this.jFrame.pack();
		this.jFrame.setVisible(true);
	}

	private JPanel initCodeSizePanel() {

		// Initialize panel
		JPanel panel = new JPanel();

		// Panel properties
		panel.setBorder(new TitledBorder(_("Source code font size")));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JSlider jSliderCode = new JSlider(JSlider.HORIZONTAL, 0,
				MAX_FONTSIZE_STEPS, 1);
		jSliderCode.setMajorTickSpacing(1);
		jSliderCode.setMinorTickSpacing(1);
		jSliderCode.setPaintTicks(true);
		jSliderCode.setValue(fontToSliderPos(this.main.getSettings()
				.getCodeSize()));
		jSliderCode.addChangeListener(this.listener.sliderCodeListener);
		panel.add(jSliderCode);

		this.jLabelCode = new JLabel("" + this.main.getSettings().getCodeSize()
				+ " " + _("pixels"));
		panel.add(this.jLabelCode);

		// Return ready panel
		return panel;
	}

	private JPanel initTextSizePanel() {

		// Initialize panel
		JPanel panel = new JPanel();

		// Panel properties
		panel.setBorder(new TitledBorder(_("Text field font size")));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JSlider jSliderText = new JSlider(JSlider.HORIZONTAL, 0,
				MAX_FONTSIZE_STEPS, 1);
		jSliderText.setMajorTickSpacing(1);
		jSliderText.setMinorTickSpacing(1);
		jSliderText.setPaintTicks(true);
		jSliderText.setValue(fontToSliderPos(this.main.getSettings()
				.getTextSize()));
		jSliderText.addChangeListener(this.listener.sliderTextListener);
		panel.add(jSliderText);

		this.jLabelText = new JLabel("" + this.main.getSettings().getTextSize()
				+ " " + _("pixels"));
		panel.add(this.jLabelText);

		// Return ready panel
		return panel;
	}

	private JPanel initDescSizePanel() {
		// Initialize panel
		JPanel panel = new JPanel();

		// Panel properties
		panel.setBorder(new TitledBorder(_("Document font size")));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JSlider jSliderText = new JSlider(JSlider.HORIZONTAL, 0,
				MAX_DESCSIZE_STEPS, 1);
		jSliderText.setMajorTickSpacing(1);
		jSliderText.setMinorTickSpacing(1);
		jSliderText.setPaintTicks(true);
		jSliderText.setValue(this.main.getSettings().getDescSize() + 1);
		jSliderText.addChangeListener(this.listener.sliderDescListener);
		panel.add(jSliderText);

		this.jLabelDesc = new JLabel(this.getDescSizeName(this.main
				.getSettings().getDescSize()));
		panel.add(this.jLabelDesc);

		// Return ready panel
		return panel;
	}

	private JPanel initLayoutPanel() {

		// Initialize panel
		JPanel panel = new JPanel();

		// Panel properties
		panel.setBorder(new TitledBorder(_("Interface Layout")));
		panel.setLayout(new BorderLayout());

		// Combo box
		String[] layouts = { _("Classic Vertical"), _("Classic Horizontal"),
				_("Widescreen Central"), _("Widescreen Left") };
		JComboBox<String> combo = new JComboBox<>(layouts);
		combo.addActionListener(new PropertiesComboListener(this.main));

		panel.add(combo, BorderLayout.CENTER);

		// Return ready panel
		return panel;
	}

	private JPanel initDebugOptionPanel() {

		// Initialize panel
		JPanel panel = new JPanel();

		// Panel properties
		panel.setBorder(new TitledBorder(_("Debugger options")));
		panel.setLayout(new BorderLayout());

		// Combo box
		JCheckBox popupCheck = new JCheckBox(_("Show return values"));
		popupCheck.setSelected(this.main.getSettings().getShowReturn());
		popupCheck.addActionListener(new PropertiesActionListener(this.main));

		panel.add(popupCheck, BorderLayout.CENTER);

		// Return ready panel
		return panel;
	}

	@SuppressWarnings("unused")
	// TODO reimplement this feature and fix treeTable bugs concerning rowHeight
	private JPanel initVarSizePanel() {

		// Initialize panel
		JPanel panel = new JPanel();

		// Panel properties
		panel.setBorder(new TitledBorder(_("Variable table font size")));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JSlider jSliderVar = new JSlider(JSlider.HORIZONTAL, 0,
				MAX_FONTSIZE_STEPS, 1);
		jSliderVar.setMajorTickSpacing(1);
		jSliderVar.setMinorTickSpacing(1);
		jSliderVar.setPaintTicks(true);
		jSliderVar.setValue(fontToSliderPos(this.main.getSettings()
				.getVarSize()));
		jSliderVar.addChangeListener(this.listener.sliderVarListener);
		panel.add(jSliderVar);

		this.jLabelVar = new JLabel("" + this.main.getSettings().getTextSize()
				+ " " + _("pixels"));
		panel.add(this.jLabelVar);

		// Return ready panel
		return panel;
	}

	public void updateTextSize() {

		this.main.getLeftPanel().updateFontSize();
		this.main.getRightPanel().getDebugPanel().updateFontSize();

		this.jLabelCode.setText("" + this.main.getSettings().getCodeSize()
				+ " " + _("pixels"));
		this.jLabelText.setText("" + this.main.getSettings().getTextSize()
				+ " " + _("pixels"));
		this.jLabelDesc.setText(this.getDescSizeName(this.main.getSettings()
				.getDescSize()));
		// this.jLabelVar.setText("" + this.main.getSettings().getVarSize() +
		// " " + _("pixels"));
	}

	private String getDescSizeName(int size) {

		String name = _("medium");

		switch (size) {
		case -1:
			name = _("small");
			break;
		case 1:
			name = _("large");
			break;
		case 2:
			name = _("extra large");
			break;
		case 3:
			name = _("giant");
			break;
		}

		return name;
	}

	public static int sliderPosToFont(int slider) {
		return 2 * slider + 6;
	}

	public static int fontToSliderPos(int fontSize) {
		return (fontSize - 6) / 2;
	}
}
