package io.github.mimerme.dobotsPort;

import java.util.ArrayList;

public class RoverBaseTypes {
    public static final String ADDRESS = "192.168.1.100";
    public static final double AXLE_WIDTH = 230.0d;
    public static final String ID = "AC13";
    public static final int MAX_RADIUS = 1000;
    public static final int MAX_SPEED = 9;
    public static final int MIN_RADIUS = 1;
    public static final int MIN_SPEED = 0;
    public static final int PORT = 80;
    public static final String PWD = "AC13";

    public enum VideoResolution {
        res_unknown,
        res_320x240,
        res_640x480
    }
}
