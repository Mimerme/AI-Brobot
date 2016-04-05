package io.github.mimerme.rover2;

import io.github.mimerme.dobotsPort.Rover2Controller;

public class Main {
	//public static Rover2Controller controller;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Rover2Controller controller = new Rover2Controller();
		controller.setConnection("192.168.1.100", 80);
		controller.connect();
		//controller.moveRightForward(5);
		//controller.moveRightForward(5);
	}

}
