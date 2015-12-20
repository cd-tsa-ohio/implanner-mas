package edu.ohiou.mfgresearch.impmas.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class AgentLauncherPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = -5916463549697159590L;
	private JTextArea console = null;
	private static final String welcomeMessage = 
			"Welcome to IMPlanner-MAS agnet launcher console"+
					"\n choose type of agent from dropdown menu.";
	

	public AgentLauncherPanel() {
		super(new BorderLayout());
		add(getAgentPanel(), BorderLayout.EAST);
		//add console in the center
		console = new JTextArea(){{
			setEditable(false);
			setText(welcomeMessage);
			setBackground(Color.WHITE);
		}};
		add(new JScrollPane(console), BorderLayout.CENTER);
	}

	protected JComponent getAgentPanel() {
		// TODO Auto-generated method stub
		JSplitPane splitPanel = new JSplitPane();
		return null;
	}

}
