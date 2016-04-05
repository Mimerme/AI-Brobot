package io.github.mimerme.dobotsPort;
public class Command extends Message {
	public Command(int op_code, int content_length) {
		super(CommandEncoder.OP_HEADER, op_code, content_length);
	}
}