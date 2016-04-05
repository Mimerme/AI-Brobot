package io.github.mimerme.rover2.ui;

import io.github.mimerme.dobotsPort.Rover2Controller;
import io.github.mimerme.rover2.Main;

//Commands organized into other commands
public class UICommands {
	private Rover2Controller controller;
	
	public UICommands(Rover2Controller controller){
		this.controller = controller;
	}
	
	public void rotateRight(){
		System.out.println("right");
		//Rotate on the same axis
		controller.moveLeftForward(5);
		controller.moveRightBackward(5);
	}
	public void rotateLeft(){
		//Rotate on the same axis
		controller.moveLeftBackward(5);
		controller.moveRightForward(5);
	}
	public void moveFwd(){
		controller.moveForward(5);
	}
	public void moveBkwd(){
		controller.moveBackward(5);
	}
}
