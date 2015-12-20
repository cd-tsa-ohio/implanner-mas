package edu.ohiou.mfgresearch.impmas.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import edu.ohiou.mfgresearch.implanner.parts.IntegrationPanel;
import jess.Rete;

public class IMPlannerMAS_launcher extends IntegrationPanel{

	private static final long serialVersionUID = 8885474165922762252L;

	private JButton launchAgentButton = null;
	
	public IMPlannerMAS_launcher() {
		super();
	}

	/**
	 * Override getButtonToolBar to add IMPLanner MAS specific buttons in the integration panel
	 */
	@Override	
	protected JToolBar getButtonToolBar() {
		// TODO Auto-generated method stub
		JToolBar buttonToolBar = super.getButtonToolBar();
		buttonToolBar.add(getLaunchAgentButton());
		return buttonToolBar;
	}
	
	private JButton getLaunchAgentButton() {
		if (launchAgentButton == null) {
			launchAgentButton = new JButton();
			launchAgentButton.setText("Launch Agents");
			launchAgentButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					launchAgent_actionPerformed(e);
				}
			});
		}
		return launchAgentButton;
	}

	void launchAgent_actionPerformed(ActionEvent e) {
		System.out.println("launch agent dialog");
	}

	public static void main(String[] args) {
		IMPlannerMAS_launcher launcher =  new IMPlannerMAS_launcher();
		launcher.display("Integration Panel",new Dimension(1000,600),JFrame.EXIT_ON_CLOSE);
	}
}
