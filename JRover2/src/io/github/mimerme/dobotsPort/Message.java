package io.github.mimerme.dobotsPort;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Message {
	ByteBuffer content;
	int contentLength;
	byte dummy1;
	byte[] dummy2;
	int dummy3;
	byte[] header;
	int opCode;

	public Message(CommandEncoder commandEncoder, byte[] buffer) {
		this(buffer, CommandEncoder.VIDEO_END_LEN);
	}

	public Message(byte[] buffer, int offset) {
		this.content = null;
		this.contentLength = CommandEncoder.VIDEO_END_LEN;
		this.header = new byte[CommandEncoder.VIDEO_START_REQ_LEN];
		this.opCode = CommandEncoder.VIDEO_END_LEN;
		this.dummy1 = (byte) 0;
		this.dummy2 = new byte[CommandEncoder.AUDIO_START_REQ];
		this.dummy3 = CommandEncoder.VIDEO_END_LEN;
		ByteBuffer bb = ByteBuffer.wrap(buffer);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		if (bb.capacity() > CommandEncoder.HEAD_LEN) {
			this.contentLength = bb.getInt(offset + CommandEncoder.CONTENT_LENGTH_IDX);
			if (bb.capacity() < this.contentLength + CommandEncoder.HEAD_LEN) {
				throw new BufferUnderflowException();
			}
			bb.position(offset + CommandEncoder.VIDEO_END_LEN);
			bb.get(this.header);
			this.opCode = bb.getShort(offset + CommandEncoder.VIDEO_START_REQ_LEN);
			this.content = ByteBuffer.allocate(this.contentLength);
			this.content.order(ByteOrder.LITTLE_ENDIAN);
			bb.position(offset + CommandEncoder.HEAD_LEN);
			bb.get(this.content.array());
			return;
		}
		throw new BufferUnderflowException();
	}

	public Message(String header, int op_code, int content_length) {
		this.content = null;
		this.contentLength = CommandEncoder.VIDEO_END_LEN;
		this.header = new byte[CommandEncoder.VIDEO_START_REQ_LEN];
		this.opCode = CommandEncoder.VIDEO_END_LEN;
		this.dummy1 = (byte) 0;
		this.dummy2 = new byte[CommandEncoder.AUDIO_START_REQ];
		this.dummy3 = CommandEncoder.VIDEO_END_LEN;
		this.header = header.getBytes();
		this.opCode = op_code;
		this.contentLength = content_length;
		this.content = ByteBuffer.allocate(content_length);
		this.content.order(ByteOrder.LITTLE_ENDIAN);
	}

	public ByteBuffer getContent() {
		return this.content;
	}

	public int getOpCode() {
		return this.opCode;
	}

	public int size() {
		return this.contentLength + CommandEncoder.HEAD_LEN;
	}

	public byte[] output() throws IOException {
		ByteBuffer bb = ByteBuffer.allocate(this.contentLength + CommandEncoder.HEAD_LEN);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(this.header);
		bb.putShort((short) this.opCode);
		bb.put(this.dummy1);
		bb.put(this.dummy2);
		bb.putInt(this.contentLength);
		bb.putInt(this.dummy3);
		bb.put(this.content.array());
		return bb.array();
	}
}