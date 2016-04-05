package io.github.mimerme.dobotsPort;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;


public abstract class RoverBaseController{
    protected static final int CONNECT_TIMEOUT = 10000;
    protected static final String TAG = "RoverBaseController";
    protected boolean m_bConnected;
    protected boolean m_bStreaming;
    protected int m_nTargetPort;
    protected String m_strTargetHost;
/*    protected IRawVideoListener oVideoListener;
*/    
    protected RoverParameters parameters;
    protected String targetId;
    protected String targetPassword;

    private class GetParametersRunnable implements Runnable {
        private GetParametersRunnable() {
        }

        public void run() {
            try {
                ArrayList<String> params = new ArrayList();
                HttpClient mClient = new DefaultHttpClient();
                HttpGet get = new HttpGet(String.format("http://%s:%d/get_params.cgi", new Object[]{RoverBaseController.this.m_strTargetHost, Integer.valueOf(RoverBaseController.this.m_nTargetPort)}));
                get.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(RoverBaseController.this.targetId, RoverBaseController.this.targetPassword), "UTF-8", false));
                mClient.execute(get);
                BufferedReader rd = new BufferedReader(new InputStreamReader(mClient.execute(get).getEntity().getContent()));
                String str = Rover2Types.SSID_FILTER;
                while (true) {
                    str = rd.readLine();
                    if (str == null) {
                        RoverBaseController.this.parameters.fillParameters(params);
                        return;
                    }
                    params.add(str.substring(str.indexOf("=") + 1, str.indexOf(";")).replace("'", Rover2Types.SSID_FILTER));
                }
            } catch (Exception e) {
                e.printStackTrace();
                RoverBaseController.this.error(RoverBaseController.TAG, "GET PARAMETERS ERROR: " + e.toString());
            }
        }
    }

    private class ResolutionCommandRunnable implements Runnable {
        int command;
        public boolean success;

        public ResolutionCommandRunnable(int command) {
            this.success = false;
            this.command = command;
        }

        public void run() {
            try {
                HttpClient mClient = new DefaultHttpClient();
                HttpGet get = new HttpGet(String.format("http://%s:%d/set_params.cgi?resolution=%s", new Object[]{RoverBaseController.this.m_strTargetHost, Integer.valueOf(RoverBaseController.this.m_nTargetPort), Integer.valueOf(this.command)}));
                get.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(RoverBaseController.this.targetId, RoverBaseController.this.targetPassword), "UTF-8", false));
                mClient.execute(get);
                BufferedReader rd = new BufferedReader(new InputStreamReader(mClient.execute(get).getEntity().getContent()));
                String str = Rover2Types.SSID_FILTER;
                while (true) {
                    str = rd.readLine();
                    if (str != null) {
                        RoverBaseController.this.info(RoverBaseController.TAG, "RESOLUTION COMMAND: " + str + " " + this.command);
                        if (str.startsWith("ok")) {
                            this.success = true;
                        }
                    } else {
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                RoverBaseController.this.error(RoverBaseController.TAG, "Resolution Command Error " + e.toString());
            }
        }
    }

    public abstract void close();

    public abstract boolean connect();

    public abstract boolean disconnect();

    public abstract boolean isConnected();

    public abstract void keepAlive();

    protected abstract void moveLeftBackward(int i);

    protected abstract void moveLeftForward(int i);

    protected abstract void moveLeftStop();

    protected abstract void moveRightBackward(int i);

    protected abstract void moveRightForward(int i);

    protected abstract void moveRightStop();

    public RoverBaseController() {
        this.m_bStreaming = false;
/*        this.oVideoListener = null;
*/        this.m_strTargetHost = null;
    }

    public String getAddress() {
        return this.m_strTargetHost;
    }
/*
    public void setVideoListener(IRawVideoListener listener) {
        this.oVideoListener = listener;
    }

    public void removeVideoListener(IRawVideoListener listener) {
        if (this.oVideoListener == listener) {
            this.oVideoListener = null;
        }
    }*/

    public void setConnection(String address, int port) {
        this.m_strTargetHost = address;
        this.m_nTargetPort = port;
    }

    public void moveForward(int i_nVelocity) {
        moveLeftForward(i_nVelocity);
        moveRightForward(i_nVelocity);
    }

    public void moveForward(int i_nLeftVelocity, int i_nRightVelocity) {
        moveLeftForward(i_nLeftVelocity);
        moveRightForward(i_nRightVelocity);
    }

    public void moveBackward(int i_nVelocity) {
        moveLeftBackward(i_nVelocity);
        moveRightBackward(i_nVelocity);
    }

    public void moveBackward(int i_nLeftVelocity, int i_nRightVelocity) {
        moveLeftBackward(i_nLeftVelocity);
        moveRightBackward(i_nRightVelocity);
    }

    public void rotateLeft(int i_nVelocity) {
        moveLeftBackward(i_nVelocity);
        moveRightForward(i_nVelocity);
    }

    public void rotateRight(int i_nVelocity) {
        moveRightBackward(i_nVelocity);
        moveLeftForward(i_nVelocity);
    }

    public void moveStop() {
        moveLeftStop();
        moveRightStop();
    }

    public void switchTo640X480Resolution() {
        new Thread(new RoverBaseController.ResolutionCommandRunnable(32)).start();
    }

    public boolean setResolution640x480() {
    	RoverBaseController.ResolutionCommandRunnable oRunner = new RoverBaseController.ResolutionCommandRunnable(32);
        oRunner.run();
        return oRunner.success;
    }

    public void switchTo320X240Resolution() {
        new Thread(new RoverBaseController.ResolutionCommandRunnable(8)).start();
    }

    public boolean setResolution320x240() {
    	RoverBaseController.ResolutionCommandRunnable oRunner = new RoverBaseController.ResolutionCommandRunnable(8);
        oRunner.run();
        return oRunner.success;
    }
/*
    public RoverParameters getParameters() {
        return this.parameters;
    }*/

    public void requestAllParameters() {
        new Thread(new RoverBaseController.GetParametersRunnable()).start();
    }
    
    public void info(String tag, String msg){
    	
    }
    
   public void error(String tag, String msg){
    	
    }
   public void debug(String tag, String msg){
	   
   }
}
