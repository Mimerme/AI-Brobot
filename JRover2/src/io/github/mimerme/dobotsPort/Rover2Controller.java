package io.github.mimerme.dobotsPort;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.net.SocketFactory;
import org.apache.http.util.ByteArrayBuffer;

public class Rover2Controller extends RoverBaseController {
	private static final int BUFFER_SIZE = 1048576;
	private static final String TAG = "Rover2Ctrl";
	private int[] challenge;
	private boolean m_bConnecting;
	private boolean m_bRun;
	private double m_dblBatteryPower;
	private TimerTask m_oBatteryTask;
	private Timer m_oBatteryTimer;
	private Socket m_oCommandSocket;
	private DataInputStream m_oDataIn;
	private DataOutputStream m_oDataOut;
	private DataInputStream m_oMediaIn;
	private DataOutputStream m_oMediaOut;
	private Socket m_oMediaSocket;
	private String m_strCameraId;
	private String m_strDeviceId;
	private int[] reverse_challenge;
	private int CONNECT_TIMEOUT = 10000;

	/* renamed from: robots.rover.rover2.ctrl.Rover2Controller.1 */
	class C01711 extends TimerTask {
		C01711() {
		}

		public void run() {
			try {
				if (Rover2Controller.this.m_bConnected
						&& !Rover2Controller.this.m_bConnecting) {
					Rover2Controller.this.send(CommandEncoder
							.cmdBatteryPowerReq());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/* renamed from: robots.rover.rover2.ctrl.Rover2Controller.2 */
	class C01722 implements Runnable {
		C01722() {
		}

		public void run() {
			ByteArrayBuffer arrayBuffer = new ByteArrayBuffer(
					Rover2Controller.BUFFER_SIZE);
			arrayBuffer.clear();
			while (Rover2Controller.this.m_bRun) {
				System.out.println("connecting memes");
				try {
					if (Rover2Controller.this.m_bConnected) {
						int nCount = Rover2Controller.this.m_oDataIn
								.available();
						if (nCount > 0) {
							byte[] buffer = new byte[nCount];
							arrayBuffer.append(buffer, 0,
									Rover2Controller.this.m_oDataIn.read(
											buffer, 0, nCount));
							arrayBuffer = CommandEncoder.parseCommand(
									Rover2Controller.this, arrayBuffer);
						}
					} else {
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (Rover2Controller.this.m_bConnected) {
						try {
							Rover2Controller.this.connectCommand();
							return;
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					} else {
						return;
					}
				}
			}
		}
	}

	/* renamed from: robots.rover.rover2.ctrl.Rover2Controller.3 */
	class C01733 implements Runnable {
		C01733() {
		}

		public void run() {
			ByteArrayBuffer arrayBuffer = new ByteArrayBuffer(
					Rover2Controller.BUFFER_SIZE);
			arrayBuffer.clear();
			while (Rover2Controller.this.m_bRun) {
				try {
					if (Rover2Controller.this.m_bStreaming) {
						byte[] buffer = new byte[8192];
						int nCount = Rover2Controller.this.m_oMediaIn.read(
								buffer, 0, 8192);
						if (nCount > 0) {
							arrayBuffer.append(buffer, 0, nCount);
							arrayBuffer = CommandEncoder.parseMediaCommand(
									Rover2Controller.this, arrayBuffer);
						}
						Utils.waitSomeTime(5);
					} else {
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Rover2Controller() {
		this.m_bRun = true;
		this.challenge = new int[4];
		this.reverse_challenge = new int[4];
		this.targetId = RoverBaseTypes.PWD;
		this.targetPassword = RoverBaseTypes.PWD;
		RoverBaseTypes aC13RoverTypes = new AC13RoverTypes();
		aC13RoverTypes.getClass();
		this.parameters = new RoverParameters();
		this.m_oBatteryTimer = new Timer("battery timer");
		this.m_oBatteryTask = new Rover2Controller.C01711();
	}

	public void close() {
		this.m_oBatteryTimer.cancel();
	}

	public void keepAlive() {
		try {
			if (this.m_bConnected && !this.m_bConnecting) {
				send(CommandEncoder.cmdKeepAlive());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void send(byte[] buffer) throws IOException {
		if (this.m_bConnected) {
			this.m_oDataOut.write(buffer);
			this.m_oDataOut.flush();
		}
	}

	public void startBatteryTask() {
		this.m_oBatteryTimer.schedule(this.m_oBatteryTask, 1000, 1000);
	}

	private Socket createSocket(String host, int port) throws IOException {
		Socket localSocket = SocketFactory.getDefault().createSocket();
		localSocket.connect(new InetSocketAddress(host, port), CONNECT_TIMEOUT);
		return localSocket;
	}

	public boolean cameraDown() throws IOException {
		if (!this.m_bConnected) {
			return false;
		}
		send(CommandEncoder.cmdDecoderControlReq(2));
		return true;
	}

	public boolean cameraStop() throws IOException {
		if (!this.m_bConnected) {
			return false;
		}
		send(CommandEncoder.cmdDecoderControlReq(1));
		return true;
	}

	public boolean cameraUp() throws IOException {
		if (!this.m_bConnected) {
			return false;
		}
		send(CommandEncoder.cmdDecoderControlReq(0));
		return true;
	}

	private int[] createChallenge() {
		int[] newChallenge = new int[4];
		Random r = new Random(System.currentTimeMillis());
		newChallenge[0] = r.nextInt();
		newChallenge[1] = r.nextInt();
		newChallenge[2] = r.nextInt();
		newChallenge[3] = r.nextInt();
		String challenge_1 = Utils.intToHex(newChallenge[0]);
		String challenge_2 = Utils.intToHex(newChallenge[1]);
		String challenge_3 = Utils.intToHex(newChallenge[2]);
		String challenge_4 = Utils.intToHex(newChallenge[3]);
		return newChallenge;
	}

	private void connectCommand() throws IOException {
		if (this.m_strTargetHost == null) {
			throw new IOException("Address not defined!");
		}
		this.challenge = createChallenge();
		this.m_oCommandSocket = createSocket(this.m_strTargetHost,
				this.m_nTargetPort);
		this.m_oDataOut = new DataOutputStream(
				this.m_oCommandSocket.getOutputStream());
		this.m_oDataIn = new DataInputStream(
				this.m_oCommandSocket.getInputStream());
		send(CommandEncoder.cmdLoginReq(this.challenge));
		Thread commandReceiver = new Thread(new C01722());
		commandReceiver.setName("Rover 2 Command Thread");
		commandReceiver.start();
	}

	public void connectMediaReceiver(int nID) throws IOException {
		if (this.m_strTargetHost == null) {
			throw new IOException("Address not defined!");
		}
		this.m_oMediaSocket = createSocket(this.m_strTargetHost,
				this.m_nTargetPort);
		this.m_oMediaOut = new DataOutputStream(
				this.m_oMediaSocket.getOutputStream());
		this.m_oMediaIn = new DataInputStream(
				this.m_oMediaSocket.getInputStream());
		this.m_oMediaOut.write(CommandEncoder.cmdMediaLoginReq(nID));
		this.m_oMediaOut.flush();
		Thread mediaReceiver = new Thread(new Rover2Controller.C01733());
		mediaReceiver.setName("Rover 2 Media Thread");
		mediaReceiver.start();
	}

	public void verifyCommand() throws IOException {
		send(CommandEncoder
				.cmdVerifyReq(this, getKey(), this.reverse_challenge));
		startBatteryTask();
		requestAllParameters();
	}

	public void onVideoReceived(byte[] data) {
		/*
		 * if (this.oVideoListener != null) { this.oVideoListener.onFrame(data,
		 * 0); }
		 */
		System.out.println("Video Recieved - TODO Handle");
	}

	public boolean enableInfrared() throws IOException {
		if (!this.m_bConnected) {
			return false;
		}
		send(CommandEncoder.cmdDecoderControlReq(94));
		return true;
	}

	public boolean disableInfrared() throws IOException {
		if (!this.m_bConnected) {
			return false;
		}
		send(CommandEncoder.cmdDecoderControlReq(95));
		return true;
	}

	public boolean enableVideo() throws IOException {
		if (!this.m_bConnected) {
			return false;
		}
		this.m_bStreaming = true;
		send(CommandEncoder.cmdVideoStartReq());
		return true;
	}

	public boolean disableVideo() throws IOException {
		if (!this.m_bConnected) {
			return false;
		}
		this.m_bStreaming = false;
		send(CommandEncoder.cmdVideoEnd());
		return true;
	}

	public boolean isStreaming() {
		return this.m_bStreaming;
	}

	public boolean isConnected() {
		try {
			return this.m_bConnected;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean move(int id, int speed) {
		try {
			send(CommandEncoder.cmdDeviceControlReq(id, speed));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void ledON() throws IOException {
		send(CommandEncoder.cmdDeviceControlReq(8, 0));
	}

	public void ledOFF() throws IOException {
		send(CommandEncoder.cmdDeviceControlReq(9, 0));
	}

	public boolean connect() {
		try {
			System.out.println("connecting ...");
			this.m_bConnected = true;
			this.m_bConnecting = true;
			connectCommand();
			System.out.println("... ok");
			this.m_bConnecting = false;
			return true;
		} catch (IOException e) {
			System.out.println("... failed");
			e.printStackTrace();
			this.m_bConnecting = false;
			this.m_bConnected = false;
			return false;
		}
	}

	public boolean disconnect() {
		try {
			System.out.println("disconnecting ...");
			if (this.m_bConnected) {
				this.m_bConnected = false;
				this.m_bStreaming = false;
				System.out.println("disconnect");
				if (this.m_oCommandSocket != null) {
					this.m_oCommandSocket.close();
					this.m_oCommandSocket = null;
				}
				if (this.m_oMediaSocket != null) {
					this.m_oMediaSocket.close();
					this.m_oCommandSocket = null;
				}
				this.m_oDataOut = null;
				this.m_oDataIn = null;
				this.m_oMediaIn = null;
				this.m_oMediaOut = null;
				return true;
			}
			System.out.println("... ok");
			return false;
		} catch (IOException e) {
			System.out.println("... failed");
			e.printStackTrace();
			return false;
		}
	}

	public void setBatteryPower(double value) {
		this.m_dblBatteryPower = value;
	}

	public double getBatteryPower() {
		return this.m_dblBatteryPower;
	}

	public void setDeviceId(String id) {
		this.m_strDeviceId = id;
	}

	public void setCameraId(String id) {
		this.m_strCameraId = id;
	}

	public String getKey() {
		return this.targetId + ":" + this.m_strCameraId + "-save-private:"
				+ this.targetPassword;
	}

	public int[] getChallenge() {
		return this.challenge;
	}

	public void setReverseChallenge(int[] challenge) {
		this.reverse_challenge = challenge;
	}

	public void moveRightForward(int i_nVelocity) {
		move(1, i_nVelocity);
	}

	public void moveLeftForward(int i_nVelocity) {
		move(4, i_nVelocity);
	}

	public void moveRightBackward(int i_nVelocity) {
		move(2, i_nVelocity);
	}

	public void moveLeftBackward(int i_nVelocity) {
		move(5, i_nVelocity);
	}

	public void moveRightStop() {
		move(0, 0);
	}

	public void moveLeftStop() {
		move(3, 0);
	}
}
