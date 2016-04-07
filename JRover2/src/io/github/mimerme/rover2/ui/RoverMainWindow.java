package io.github.mimerme.rover2.ui;

import io.github.mimerme.dobotsPort.Rover2Controller;
import io.github.mimerme.rover2.Main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RoverMainWindow {

	private JFrame frmBrookstoneRover;
	private Rover2Controller controller;
	private UICommands commander;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RoverMainWindow window = new RoverMainWindow();
					window.frmBrookstoneRover.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RoverMainWindow() {
		controller = new Rover2Controller();
		controller.setConnection("192.168.1.100", 80);
		//Make the connection
		controller.connect();
		commander = new UICommands(controller);		
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBrookstoneRover = new JFrame();
		frmBrookstoneRover.setTitle("Brookstone Rover 2.0");
		frmBrookstoneRover.setBounds(100, 100, 450, 300);
		frmBrookstoneRover.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBrookstoneRover.getContentPane().setLayout(new MigLayout("", "[][][]", "[][][]"));
		frmBrookstoneRover.addKeyListener(new Rover2KeyListener(commander));
		JButton btnUp = new JButton(" FWD ");
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				commander.moveFwd();
			}
		});
		frmBrookstoneRover.getContentPane().add(btnUp, "cell 1 0");
		
		JButton btnLeft = new JButton("LEFT");
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commander.rotateLeft();
			}
		});
		frmBrookstoneRover.getContentPane().add(btnLeft, "cell 0 1");
		
		JButton btnRight = new JButton("RIGHT");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commander.rotateRight();
			}
		});
		frmBrookstoneRover.getContentPane().add(btnRight, "cell 2 1");
		
		JButton btnDown = new JButton("BKWD");
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commander.moveBkwd();
			}
		});
		frmBrookstoneRover.getContentPane().add(btnDown, "cell 1 2");
	}

}
