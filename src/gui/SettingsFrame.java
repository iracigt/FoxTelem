package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import common.Config;
import common.Location;
import common.Log;
import common.TlmServer;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

/**
 * 
 * FOX 1 Telemetry Decoder
 * @author chris.e.thompson g0kla/ac2cz
 *
 * Copyright (C) 2015 amsat.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
@SuppressWarnings("serial")
public class SettingsFrame extends JDialog implements ActionListener, ItemListener, FocusListener, WindowListener {

	public static final int MAX_CALLSIGN_LEN = 32;
	public static final int MAX_STATION_LEN = 50;
	
	private JPanel contentPane;
	private JTextField txtDisplayModuleFontSize;
	private JTextField txtGraphAxisFontSize;
	//private JCheckBox cbHtmlFormatting;

	private JTextField txtLogFileDirectory;
	private JTextField txtServerUrl;
	private JTextField txtCallsign;
	private JTextField txtLatitude;
	private JTextField txtLongitude;
	private JTextField txtMaidenhead;
	private JTextField txtStation;
	private JTextField txtAltitude;
	private JTextField txtPrimaryServer;
	private JTextField txtSecondaryServer;
	
	private JCheckBox cbUploadToServer;
	private JCheckBox rdbtnTrackSignal;
//	private JCheckBox cbUseUDP;
	private JCheckBox storePayloads;
	private JCheckBox saveFcdParams;
	private JCheckBox useLeftStereoChannel;
	private JCheckBox swapIQ;
	private JCheckBox insertMissingBits;
	private JCheckBox useLongPRN;
	private JCheckBox cbUseDDEAzEl;
	private JCheckBox cbUseDDEFreq;
	private JCheckBox cbFoxTelemCalcsPosition;
	private JCheckBox cbWhenAboveHorizon;
	
	boolean useUDP;
	
	private JPanel serverPanel;
	
	OptionsPanel optionsPanel;
	
	JButton btnSave;
	JButton btnCancel;
	JButton btnBrowse;
	
	/**
	 * Create the Dialog
	 */
	public SettingsFrame(JFrame owner, boolean modal) {
		super(owner, modal);
		setTitle("Settings");
		addWindowListener(this);
//		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadProperties();
		//this.setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// South panel for the buttons
		JPanel southpanel = new JPanel();
		contentPane.add(southpanel, BorderLayout.SOUTH);
		southpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(this);
		southpanel.add(btnSave);
		getRootPane().setDefaultButton(btnSave);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		southpanel.add(btnCancel);
		
		// North panel for settings that span across the window
		JPanel northpanel = new JPanel();
		contentPane.add(northpanel, BorderLayout.NORTH);
		//JPanel northcolumnpanel = addColumn(northpanel);
		//txtLogFileDirectory = addSettingsRow(northpanel, 20, "Log files directory", Config.logFileDirectory);
		//JPanel panel = new JPanel();
		//northpanel.add(panel);
		northpanel.setLayout(new BorderLayout());

		JPanel northpanel1 = new JPanel();
		JPanel northpanel2 = new JPanel();
		JPanel northpanelA = new JPanel();
		JPanel northpanelB = new JPanel();
		northpanel.add(northpanel1, BorderLayout.NORTH);
		northpanel1.setLayout(new BorderLayout());
		northpanel1.add(northpanelA, BorderLayout.NORTH);
		northpanel1.add(northpanelB, BorderLayout.SOUTH);
		northpanel.add(northpanel2, BorderLayout.SOUTH);
		
		northpanel2.setLayout(new BorderLayout());
		northpanelA.setLayout(new BorderLayout());
		northpanelB.setLayout(new BorderLayout());
		
		JLabel lblHomeDir = new JLabel("Home directory     ");
		lblHomeDir.setToolTipText("This is the directory that contains the settings file");
		lblHomeDir.setBorder(new EmptyBorder(5, 2, 5, 5) );
		northpanelA.add(lblHomeDir, BorderLayout.WEST);

		JLabel lblHomeDir2 = new JLabel(Config.homeDirectory);
		northpanelA.add(lblHomeDir2, BorderLayout.CENTER);

		JLabel lblServerUrl = new JLabel("Server Data URL  ");
		lblServerUrl.setToolTipText("This sets the URL we use to fetch and download server data");
		lblServerUrl.setBorder(new EmptyBorder(5, 2, 5, 5) );
		northpanelB.add(lblServerUrl, BorderLayout.WEST);
		
		txtServerUrl = new JTextField(Config.webSiteUrl);
		northpanelB.add(txtServerUrl, BorderLayout.CENTER);
		txtServerUrl.setColumns(30);
		
		txtServerUrl.addActionListener(this);

		
		JLabel lblLogFilesDir = new JLabel("Log files directory");
		lblLogFilesDir.setToolTipText("This sets the directory that the downloaded telemetry data is stored in");
		lblLogFilesDir.setBorder(new EmptyBorder(5, 2, 5, 5) );
		northpanel2.add(lblLogFilesDir, BorderLayout.WEST);
		
		txtLogFileDirectory = new JTextField(Config.logFileDirectory);
		northpanel2.add(txtLogFileDirectory, BorderLayout.CENTER);
		txtLogFileDirectory.setColumns(30);
		
		txtLogFileDirectory.addActionListener(this);

		btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(this);
		northpanel2.add(btnBrowse, BorderLayout.EAST);
		
		if (Config.logDirFromPassedParam) {
			txtLogFileDirectory.setEnabled(false);
			btnBrowse.setVisible(false);
			JLabel lblPassedParam = new JLabel("  (Fixed at Startup)");
			northpanel2.add(lblPassedParam, BorderLayout.EAST);
		}
//		TitledBorder eastTitle1 = new TitledBorder(null, "<html><body> <h3>Files and Directories</h3></body></html>", TitledBorder.LEADING, TitledBorder.TOP, null, null); 
		TitledBorder eastTitle1 = title("Files and Directories");
		northpanel.setBorder(eastTitle1);
		
		// Center panel for 2 columns of settings
		JPanel centerpanel = new JPanel();
		contentPane.add(centerpanel, BorderLayout.CENTER);
		//centerpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		centerpanel.setLayout(new BoxLayout(centerpanel, BoxLayout.X_AXIS));
		
		// Add left column with 2 panels in it
		JPanel leftcolumnpanel = new JPanel();
		centerpanel.add(leftcolumnpanel);
		//leftcolumnpanel.setLayout(new GridLayout(2,1,10,10));
		leftcolumnpanel.setLayout(new BoxLayout(leftcolumnpanel, BoxLayout.Y_AXIS));
		
		serverPanel = addColumn(leftcolumnpanel,6);
		TitledBorder eastTitle2 = title("Ground Station Params");
		serverPanel.setBorder(eastTitle2);

		txtCallsign = addSettingsRow(serverPanel, 15, "Groundstation Name", 
				"Ground station name is the unique identifier that you will use to store data on the AMSAT telemetry server", Config.callsign);
		
		txtPrimaryServer = addSettingsRow(serverPanel, 15, "Primary Server", "The address of the Amsat Telemetry server. "
				+ "Should not need to be changed", Config.primaryServer);
		txtSecondaryServer = addSettingsRow(serverPanel, 15, "Secondary Server", "The backup address of the Amsat Telemetry server. "
				+ "Should not need to be changed",Config.secondaryServer);
		txtLatitude = addSettingsRow(serverPanel, 10, "Lat (S is -ve)", "Latitude / Longitude or Locator need to be specified if you supply decoded data to AMSAT", Config.latitude); // South is negative
		txtLongitude = addSettingsRow(serverPanel, 10, "Long (W is -ve)", "Latitude / Longitude or Locator need to be specified if you supply decoded data to AMSAT", Config.longitude); // West is negative
		JPanel locatorPanel = new JPanel();
		JLabel lblLoc = new JLabel("Lat Long gives Locator: ");
		txtMaidenhead = new JTextField(Config.maidenhead);
		txtMaidenhead.addActionListener(this);
		txtMaidenhead.addFocusListener(this);

		txtMaidenhead.setColumns(10);
		serverPanel.add(locatorPanel);
		locatorPanel.add(lblLoc);
		locatorPanel.add(txtMaidenhead);

		txtAltitude = addSettingsRow(serverPanel, 15, "Altitude (m)", "Altitude will be supplied to AMSAT along with your data if you specify it", Config.altitude);
		txtStation = addSettingsRow(serverPanel, 15, "RF-Receiver Description", "RF-Receiver can be specified to give us an idea of the types of stations that are in operation", Config.stationDetails);
		
		serverPanel.add(new Box.Filler(new Dimension(10,10), new Dimension(100,400), new Dimension(100,500)));
		

		
		JPanel leftcolumnpanel2 = addColumn(leftcolumnpanel,6);
		TitledBorder eastTitle3 = title("Formatting");
		leftcolumnpanel2.setBorder(eastTitle3);

		txtDisplayModuleFontSize = addSettingsRow(leftcolumnpanel2, 5, "Health Module Font Size", 
				"Change the size of the font on the Satellite tabs so that it is more readable or so that it fits in the space available", Integer.toString(Config.displayModuleFontSize));
		txtGraphAxisFontSize = addSettingsRow(leftcolumnpanel2, 5, "Graph Font Size", "Change the size of the font on the graph axis", Integer.toString(Config.graphAxisFontSize));
		//JPanel cb = new JPanel();
		//leftcolumnpanel2.add(cb);
		//cbHtmlFormatting = addCheckBoxRow("Use HTML Formatting", Config.htmlFormatting, cb );
		
		//leftcolumnpanel2.add(new Box.Filler(new Dimension(10,10), new Dimension(100,400), new Dimension(100,500)));
		
		//leftcolumnpanel.add(new Box.Filler(new Dimension(10,10), new Dimension(100,400), new Dimension(100,500)));
		
		JPanel leftcolumnpanel3 = addColumn(leftcolumnpanel,6);
		TitledBorder measureTitle = title("Measurements");
		leftcolumnpanel3.setBorder(measureTitle);
		if (Config.useDDEforAzEl) Config.foxTelemCalcsPosition = false;
		cbUseDDEFreq = addCheckBoxRow("Log Freq from SatPC32 in AF mode", "In AF mode FoxTelem can read the CAT frequency from SatPC32.  It is stored alongside other measurements",
				Config.useDDEforFreq, leftcolumnpanel3 );
		cbUseDDEAzEl = addCheckBoxRow("Read position from SatPC32", "FoxTelem can read the position of the satellite from SatPC32.  It is stored alongside other measurements",
				Config.useDDEforAzEl, leftcolumnpanel3 );
		if (Config.isWindowsOs()) {
			cbUseDDEFreq.setVisible(true);
			cbUseDDEAzEl.setVisible(true);
		} else {
			cbUseDDEFreq.setVisible(false);
			cbUseDDEAzEl.setVisible(false);
		}
		cbFoxTelemCalcsPosition = addCheckBoxRow("FoxTelem Calculates Position", "FoxTelem can calculate the position of the spacecraft and store it for analysis",
				Config.foxTelemCalcsPosition, leftcolumnpanel3 );
		cbWhenAboveHorizon = addCheckBoxRow("Auto Start Decoder when above horizon", "FoxTelem can start/stop the decoder when the spacecraft is above/below the horizon",
				Config.whenAboveHorizon, leftcolumnpanel3 );
			
		leftcolumnpanel3.add(new Box.Filler(new Dimension(200,10), new Dimension(150,400), new Dimension(500,500)));
		
		// Add a right column with two panels in it
		JPanel rightcolumnpanel = new JPanel();
		centerpanel.add(rightcolumnpanel);
		//rightcolumnpanel.setLayout(new GridLayout(2,1,10,10));
		rightcolumnpanel.setLayout(new BoxLayout(rightcolumnpanel, BoxLayout.Y_AXIS));
		
		JPanel rightcolumnpanel0 = addColumn(rightcolumnpanel,3);
		rightcolumnpanel0.setLayout(new BoxLayout(rightcolumnpanel0, BoxLayout.Y_AXIS));
		TitledBorder eastTitle4 = title("Decoder Options");
		rightcolumnpanel0.setBorder(eastTitle4);
		cbUploadToServer = addCheckBoxRow("Upload to Server", "Select this if you want to send your collected data to the AMSAT telemetry server",
				Config.uploadToServer, rightcolumnpanel0 );
		rdbtnTrackSignal = addCheckBoxRow("Track Doppler","Leave this on except in a test situation.  It allows FoxTelem to follow the spacecraft downllink signal",
				Config.trackSignal, rightcolumnpanel0);
		useUDP = true;
		if (Config.serverProtocol == TlmServer.TCP)
			useUDP = false;
//		cbUseUDP = addCheckBoxRow("Use UDP", "Use UDP (vs TCP) to send data to the AMSAT telemetry server",
//				useUDP, rightcolumnpanel0 );
		storePayloads = addCheckBoxRow("Store Payloads", "Uncheck this if you do not want to store the decoded payloads on disk", Config.storePayloads, rightcolumnpanel0 );
		saveFcdParams = addCheckBoxRow("Store FCD Params", "Save the FCD settings to disk and restore at start up.  May conflict with other programs or other copies of FoxTelem.", Config.saveFcdParams, rightcolumnpanel0 );
		useLeftStereoChannel = addCheckBoxRow("Use Left Stereo Channel", "The default is for FoxTelem to read audio from the left stereo channel of your soundcard.  "
				+ "If you uncheck this it will read from the right",
				Config.useLeftStereoChannel, rightcolumnpanel0 );
		swapIQ = addCheckBoxRow("Swap IQ", "Swap the I and Q channels in IQ deocder mode",
				Config.swapIQ, rightcolumnpanel0 );
		insertMissingBits = addCheckBoxRow("Fix Dropped Bits", "Fix bits dropped in the audio channel (may fix frames but use more CPU)",
				Config.insertMissingBits, rightcolumnpanel0 );
		useLongPRN = addCheckBoxRow("Use Long PRN", "Use a 31 bit SYNC word between frames",
				Config.useLongPRN, rightcolumnpanel0 );
		//insertMissingBits.setForeground(Config.AMSAT_RED);
		//useLongPRN.setForeground(Config.AMSAT_RED);
		rightcolumnpanel0.add(new Box.Filler(new Dimension(10,10), new Dimension(150,400), new Dimension(500,500)));
		
		//JPanel rightcolumnpanel1 = addColumn(rightcolumnpanel,3);
		//txtGraphAxisFontSize = addSettingsRow(rightcolumnpanel, 5, "Graph Font Size", Integer.toString(Config.graphAxisFontSize));
		optionsPanel = new OptionsPanel();
		rightcolumnpanel.add(optionsPanel);
		rightcolumnpanel.add(new Box.Filler(new Dimension(10,10), new Dimension(100,400), new Dimension(100,500)));

		setServerPanelEnabled(Config.uploadToServer);
//		cbUseUDP.setEnabled(false);
		txtPrimaryServer.setEnabled(false);
		txtSecondaryServer.setEnabled(false);
		
		enableDependentParams();
	}

	public void saveProperties() {
		Config.saveGraphIntParam("Global", 0, 0, "settingsWindow", "windowHeight", this.getHeight());
		Config.saveGraphIntParam("Global", 0, 0, "settingsWindow", "windowWidth", this.getWidth());
		Config.saveGraphIntParam("Global", 0, 0, "settingsWindow", "windowX", this.getX());
		Config.saveGraphIntParam("Global", 0, 0, "settingsWindow",  "windowY", this.getY());
	}
	
	public void loadProperties() {
		int windowX = Config.loadGraphIntValue("Global", 0, 0, "settingsWindow", "windowX");
		int windowY = Config.loadGraphIntValue("Global", 0, 0, "settingsWindow", "windowY");
		int windowWidth = Config.loadGraphIntValue("Global", 0, 0, "settingsWindow", "windowWidth");
		int windowHeight = Config.loadGraphIntValue("Global", 0, 0, "settingsWindow", "windowHeight");
		if (windowX == 0 ||windowY == 0 ||windowWidth == 0 ||windowHeight == 0) {
			setBounds(100, 100, 650, 630);
		} else {
			setBounds(windowX, windowY, windowWidth, windowHeight);
		}
	}
	
	private TitledBorder title(String s) {
		TitledBorder title = new TitledBorder(null, s, TitledBorder.LEADING, TitledBorder.TOP, null, null);
		title.setTitleFont(new Font("SansSerif", Font.BOLD, 14));
		return title;
	}
	
	private void setServerPanelEnabled(boolean en) {
		//serverPanel.setEnabled(en);
		//txtCallsign.setEnabled(en);
	//	txtPrimaryServer.setEnabled(en);
	//	txtSecondaryServer.setEnabled(en);
	//	cbUseUDP.setEnabled(en);
		//txtLatitude.setEnabled(en);
		//txtLongitude.setEnabled(en);
	}
	
	private boolean validLatLong() {
		float lat = 0, lon = 0;
		try {
			lat = Float.parseFloat(txtLatitude.getText());
			lon = Float.parseFloat(txtLongitude.getText());
			if (lat == Float.parseFloat(Config.DEFAULT_LATITUDE) || 
					txtLatitude.getText().equals("")) return false;
			if (lon == Float.parseFloat(Config.DEFAULT_LONGITUDE) || 
					txtLongitude.getText().equals("")) return false;
		} catch (NumberFormatException n) {
			JOptionPane.showMessageDialog(this,
					"Only numerical values are valid for the latitude and longitude.",
					"Format Error\n",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if ((Float.isNaN(lon)) ||
		(Math.abs(lon) > 180) ||
		(Float.isNaN(lat)) ||
		(Math.abs(lat) == 90.0) ||
		(Math.abs(lat) > 90)) {
			JOptionPane.showMessageDialog(this,
					"Invalid latitude or longitude.",
					"Error\n",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}
	
	private boolean validLocator() {
		if (txtMaidenhead.getText().equalsIgnoreCase(Config.DEFAULT_LOCATOR) || 
				txtMaidenhead.getText().equals("")) {
			JOptionPane.showMessageDialog(this,
					"Enter a latitude/longitude or set the locator to a valid value",
					"Format Error\n",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	private boolean validCallsign() {
		if (txtCallsign.getText().equalsIgnoreCase(Config.DEFAULT_CALLSIGN) || 
				txtCallsign.getText().equals("")) return false;
		return true;
	}
	
	private boolean validServerParams() {
		if (!validCallsign()) return false;
		if (!validLocator()) return false;
		if (!validLatLong()) return false;
		if (!validAltitude()) return false;
		return true;
	}

	private JPanel addColumn(JPanel parent, int rows) {
		JPanel columnpanel = new JPanel();
		parent.add(columnpanel);
		//columnpanel.setLayout(new GridLayout(rows,1,10,10));
		columnpanel.setLayout(new BoxLayout(columnpanel, BoxLayout.Y_AXIS));

		return columnpanel;
	}


	private JCheckBox addCheckBoxRow(String name, String tip, boolean value, JPanel parent) {
		JCheckBox checkBox = new JCheckBox(name);
		checkBox.setEnabled(true);
		checkBox.addItemListener(this);
		checkBox.setToolTipText(tip);
		parent.add(checkBox);
		if (value) checkBox.setSelected(true); else checkBox.setSelected(false);
		return checkBox;
	}


	private JTextField addSettingsRow(JPanel column, int length, String name, String tip, String value) {
		JPanel panel = new JPanel();
		column.add(panel);
		panel.setLayout(new GridLayout(1,2,5,5));
		JLabel lblDisplayModuleFont = new JLabel(name);
		lblDisplayModuleFont.setToolTipText(tip);
		panel.add(lblDisplayModuleFont);
		JTextField textField = new JTextField(value);
		panel.add(textField);
		textField.setColumns(length);
		textField.addActionListener(this);
		textField.addFocusListener(this);

		column.add(new Box.Filler(new Dimension(10,5), new Dimension(10,5), new Dimension(10,5)));

		return textField;
	
	}

	private boolean validAltitude() {
		int alt = 0;
			try {
				alt = Integer.parseInt(txtAltitude.getText());
			} catch (NumberFormatException n) {
				JOptionPane.showMessageDialog(this,
						"Only integer values are valid for the altitude. Specify it to the nearest meter, but with no units.",
						"Format Error\n",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		if ((alt < 0) ||(alt > 8484)) {
			JOptionPane.showMessageDialog(this,
					"Invalid altitude.  Must be between 0 and 8484m.",
					"Format Error\n",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
		
	private void updateLocator() {
		if (validLatLong()) {
			Location l = new Location(txtLatitude.getText(), txtLongitude.getText());
			txtMaidenhead.setText(l.maidenhead);
		}
	}
	private void updateLatLong() {
		if (validLocator()) {
			Location l = new Location(txtMaidenhead.getText());
			txtLatitude.setText(Float.toString(l.latitude)); 
			txtLongitude.setText(Float.toString(l.longitude));
		}
	}

	private void enableDependentParams() {
		if (validLatLong() && validAltitude()) {
			if (validCallsign())
				cbUploadToServer.setEnabled(true);
			else
				cbUploadToServer.setEnabled(false);
			cbFoxTelemCalcsPosition.setEnabled(true);
			cbWhenAboveHorizon.setEnabled(true);
			
		} else {
			cbUploadToServer.setEnabled(false);
			cbFoxTelemCalcsPosition.setEnabled(false);
			cbWhenAboveHorizon.setEnabled(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCancel) {
			this.dispose();
		}
		if (e.getSource() == txtLatitude || e.getSource() == txtLongitude ) {
			updateLocator();
			enableDependentParams();
		}
		if (e.getSource() == txtMaidenhead) {
			updateLatLong();
			enableDependentParams();
			
		}
		if (e.getSource() == txtAltitude) {
			validAltitude();
			enableDependentParams();
		}
		if (e.getSource() == btnSave) {
			boolean dispose = true;
			boolean refreshTabs = false;
			boolean refreshGraphs = false;
			
			Config.uploadToServer = cbUploadToServer.isSelected();
			if (Config.uploadToServer && !validServerParams()) {
				JOptionPane.showMessageDialog(this,
						"You need to specify a Groundstation name to upload to the server.\n"
						+ "Use an amateur radio callsign or something like 'Sunnytown Primary School'.\n"
						+ "You also need to specify a valid latitude and longitude or a Maidenhead locator.\n"
						+ "If you specify an altitude, then it needs to be a valid value.",
						"Missing Server Upload Settings",
						JOptionPane.ERROR_MESSAGE);	
				this.cbUploadToServer.setSelected(false);
				Config.uploadToServer = cbUploadToServer.isSelected();
			} else {
				// grab all the latest settings
				Config.callsign = txtCallsign.getText();
				Log.println("Setting callsign: " + Config.callsign);
				if (validLatLong()) {
					Config.latitude = txtLatitude.getText();
					Config.longitude = txtLongitude.getText();
				} else {
					if (txtLatitude.getText().equalsIgnoreCase(Config.DEFAULT_LATITUDE) && txtLongitude.getText().equalsIgnoreCase(Config.DEFAULT_LONGITUDE))
						dispose = true;
					else 
						dispose = false;
				}
				if (validLocator()) {
					Config.maidenhead = txtMaidenhead.getText();
				} else dispose = false;
				if (validAltitude()) {
					Config.altitude = txtAltitude.getText();
				} else dispose = false;
				Config.stationDetails = txtStation.getText();
				Config.primaryServer = txtPrimaryServer.getText();
				Config.secondaryServer = txtSecondaryServer.getText();
				
				Config.webSiteUrl = txtServerUrl.getText();
				
				Config.storePayloads = storePayloads.isSelected();
				Config.saveFcdParams = saveFcdParams.isSelected();
				Config.useLeftStereoChannel = useLeftStereoChannel.isSelected();
				Config.swapIQ = swapIQ.isSelected();
				Config.insertMissingBits = insertMissingBits.isSelected();
				if (Config.useLongPRN != useLongPRN.isSelected())
					Log.errorDialog("CHANGED PRN LENGTH", "If you changed the length of the PRN then the decoder needs to be stopped and restarted.  "
							+ "You do not\n need to exit FoxTelem.");
				Config.useLongPRN = useLongPRN.isSelected();
				
				if (Config.isWindowsOs()) {
					Config.useDDEforFreq = cbUseDDEFreq.isSelected();
					Config.useDDEforAzEl = cbUseDDEAzEl.isSelected();
				}
				Config.foxTelemCalcsPosition = cbFoxTelemCalcsPosition.isSelected();
				Config.whenAboveHorizon = cbWhenAboveHorizon.isSelected();
				Config.trackSignal = rdbtnTrackSignal.isSelected();
				/*
				if (cbUseUDP.isSelected()) {
					Config.serverPort = Config.udpPort;
					Config.serverProtocol = TlmServer.UDP;
				} else {
					Config.serverPort = Config.tcpPort;
					Config.serverProtocol = TlmServer.TCP;
				}
				*/
				if (Config.displayModuleFontSize != parseIntTextField(txtDisplayModuleFontSize)) {
					Config.displayModuleFontSize = parseIntTextField(txtDisplayModuleFontSize);
					Log.println("Setting Health Tab font to: " + Config.displayModuleFontSize);
					//MainWindow.refreshTabs(false);
					refreshTabs = true;
				}
				if (Config.graphAxisFontSize != parseIntTextField(txtGraphAxisFontSize)) {
					Object[] options = {"Yes",
					"No"};
					int n = JOptionPane.showOptionDialog(
							MainWindow.frame,
							"Are you sure you want to change the font size? It will close any open graphs.",
							"Do you want to continue?",
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.QUESTION_MESSAGE,
							null,
							options,
							options[1]);

					if (n == JOptionPane.YES_OPTION) {
						Config.graphAxisFontSize = parseIntTextField(txtGraphAxisFontSize);
						Log.println("Setting Graph font to: " + Config.displayModuleFontSize);
						//MainWindow.refreshTabs(true);
						refreshTabs = true;
						refreshGraphs = true;
					}
				}

				if (!Config.logFileDirectory.equalsIgnoreCase(txtLogFileDirectory.getText())) {
					boolean currentDir = false;
					if (txtLogFileDirectory.getText().equalsIgnoreCase(""))
						currentDir = true;
					
					File file = new File(txtLogFileDirectory.getText());
					//if (!file.isDirectory())
					//	file = file.getParentFile();
					if (!currentDir && (!file.isDirectory() || file == null || !file.exists())){
						Log.errorDialog("Invalid directory", "Can not find the specified directory: " + txtLogFileDirectory.getText());
						dispose = false;
						refreshGraphs=false;
					} else {

						Object[] options = {"Yes",
						"No"};
						int n = JOptionPane.showOptionDialog(
								MainWindow.frame,
								"Do you want to switch log file directories? It will close any open graphs.",
								"Do you want to continue?",
								JOptionPane.YES_NO_OPTION, 
								JOptionPane.QUESTION_MESSAGE,
								null,
								options,
								options[1]);

						if (n == JOptionPane.YES_OPTION) {
							Config.logFileDirectory = txtLogFileDirectory.getText();
							Log.println("Setting log file directory to: " + Config.logFileDirectory);
							Config.totalFrames = 0;
							Config.initSatelliteManager();
							Config.initPayloadStore();
							Config.initPassManager();
							Config.initSequence();
							Config.initServerQueue();
							Config.mainWindow.initSatMenu();
							
							refreshTabs = true;
							refreshGraphs = true;

						}
					}		
				} 
			}
			
			if (dispose) {
				Config.save();
				Config.storeGroundStation();
				this.dispose();
			}
			if (refreshTabs)
				MainWindow.refreshTabs(refreshGraphs);
			// We are fully up, remove the database loading message
			Config.fileProgress.updateProgress(100);
		}

		if (e.getSource() == btnBrowse) {
			File dir = null;
			if (!Config.logFileDirectory.equalsIgnoreCase("")) {
				dir = new File(Config.logFileDirectory);
			}
			if(Config.useNativeFileChooser && !Config.isWindowsOs()) { // not on windows because native dir chooser does not work) {
				// use the native file dialog on the mac
				System.setProperty("apple.awt.fileDialogForDirectories", "true");
				FileDialog fd =
						new FileDialog(this, "Choose Directory for Log Files",FileDialog.LOAD);
				if (dir != null) {
					fd.setDirectory(dir.getAbsolutePath());
				}
				fd.setVisible(true);
				System.setProperty("apple.awt.fileDialogForDirectories", "false");
				String filename = fd.getFile();
				String dirname = fd.getDirectory();
				if (filename == null)
					Log.println("You cancelled the choice");
				else {
					Log.println("File: " + filename);
					Log.println("DIR: " + dirname);
					File selectedFile = new File(dirname + filename);
					txtLogFileDirectory.setText(selectedFile.getAbsolutePath());
				}
				
			} else {

				JFileChooser fc = new JFileChooser();
				fc.setApproveButtonText("Choose");
				if (dir != null) {
					fc.setCurrentDirectory(dir);	
				}			
				fc.setDialogTitle("Choose Directory for Log Files");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setPreferredSize(new Dimension(Config.windowFcWidth, Config.windowFcHeight));
				int returnVal = fc.showOpenDialog(this);
				Config.windowFcHeight = fc.getHeight();
				Config.windowFcWidth = fc.getWidth();	

				if (returnVal == JFileChooser.APPROVE_OPTION) { 

					txtLogFileDirectory.setText(fc.getSelectedFile().getAbsolutePath());
				} else {
					System.out.println("No Selection ");
				}
			}
		}

	}
	
	
	private int parseIntTextField(JTextField text) {
		int value = 0;
	
		try {
			value = Integer.parseInt(text.getText());
			if (value > 30) {
				value = 30;
				text.setText(Integer.toString(30));
			}
		} catch (NumberFormatException ex) {
			
		}
		return value;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object source = e.getItemSelectable();

		if (source == cbUploadToServer) { 
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				setServerPanelEnabled(false);
			} else {
				setServerPanelEnabled(true);
			}
		}
		
		if (source == cbUseDDEAzEl) { 
			if (e.getStateChange() == ItemEvent.SELECTED) {
				cbFoxTelemCalcsPosition.setSelected(false);
			}
		}
		
		if (source == cbFoxTelemCalcsPosition) { 
			if (e.getStateChange() == ItemEvent.SELECTED) {
				cbUseDDEAzEl.setSelected(false);
			} else {
				if (cbWhenAboveHorizon.isSelected())
					cbWhenAboveHorizon.setSelected(false);
			}
		}
		if (source == cbWhenAboveHorizon) { 
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (!cbFoxTelemCalcsPosition.isSelected() && !cbUseDDEAzEl.isSelected())
					cbFoxTelemCalcsPosition.setSelected(true);
				MainWindow.inputTab.rdbtnFindSignal.setSelected(true);
			}
			
		}
		if (e.getSource() == rdbtnTrackSignal) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				
	            Config.trackSignal=false;
	            //Config.save();
	        } else {
	        	Config.trackSignal=true;
	        	
	        	//Config.save();
	        }
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource() == txtCallsign) {
			if (txtCallsign.getText().length() > MAX_CALLSIGN_LEN) 
				txtCallsign.setText(txtCallsign.getText().substring(0, MAX_CALLSIGN_LEN));
			enableDependentParams();
		}
		if (e.getSource() == txtStation) {
			if (txtStation.getText().length() > MAX_STATION_LEN) 
				txtStation.setText(txtStation.getText().substring(0, MAX_STATION_LEN));
		}
		if (e.getSource() == txtLatitude || e.getSource() == txtLongitude ) {
			updateLocator();
			enableDependentParams();
		}
		if (e.getSource() == txtMaidenhead) {
			updateLatLong();
			enableDependentParams();
		}
		if (e.getSource() == txtAltitude) {
			validAltitude();
			enableDependentParams();
		}
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		saveProperties();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}		
		
}
