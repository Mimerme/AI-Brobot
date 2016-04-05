package io.github.mimerme.rover2.ui;

import io.github.mimerme.rover2.Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Rover2KeyListener implements KeyListener {
	UICommands commander;
	
	public Rover2KeyListener(UICommands commander) {
		// TODO Auto-generated constructor stub
		this.commander = commander;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	    int keyCode = e.getKeyCode();
	    switch( keyCode ) { 
	        case KeyEvent.VK_UP:
	            // handle up 
	        	commander.moveFwd();
	            break;
	        case KeyEvent.VK_DOWN:
	            // handle down 
	        	commander.moveBkwd();
	            break;
	        case KeyEvent.VK_LEFT:
	            // handle left
	        	commander.moveFwd();
	            break;
	        case KeyEvent.VK_RIGHT :
	            // handle right
	        	commander.rotateRight();
	            break;
	     }
	} 

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		//Otherwise the robot would keep moving
		//Main.controller.moveStop();
		
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {
		// TODO Auto-generated method stub
		
	}
	
}
