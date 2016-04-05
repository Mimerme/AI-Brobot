package io.github.mimerme.dobotsPort;
import java.util.ArrayList;


    public class RoverParameters {
        public String adhoc_ssid;
        public String alias;
        public String app_ver;
        public String gateway;
        public String id;
        public String ip;
        public String mask;
        public String port;
        public String resolution;
        public String sys_ver;
        public String username;
        public String userpwd;
        public String wifi_authtype;
        public String wifi_defkey;
        public String wifi_encrypt;
        public String wifi_key1;
        public String wifi_key1_bits;
        public String wifi_key2;
        public String wifi_key2_bits;
        public String wifi_key3;
        public String wifi_key3_bits;
        public String wifi_key4;
        public String wifi_key4_bits;
        public String wifi_keyformat;
        public String wifi_ssid;
        public String wifi_wpa_psk;

        public void fillParameters(ArrayList<String> parameters) {
            this.id = (String) parameters.get(RoverBaseTypes.MIN_SPEED);
            this.sys_ver = (String) parameters.get(RoverBaseTypes.MIN_RADIUS);
            this.app_ver = (String) parameters.get(2);
            this.alias = (String) parameters.get(3);
            this.adhoc_ssid = (String) parameters.get(4);
            this.username = (String) parameters.get(5);
            this.userpwd = (String) parameters.get(6);
            this.resolution = (String) parameters.get(7);
            this.ip = (String) parameters.get(8);
            this.mask = (String) parameters.get(RoverBaseTypes.MAX_SPEED);
            this.gateway = (String) parameters.get(10);
            this.port = (String) parameters.get(11);
            this.wifi_ssid = (String) parameters.get(12);
            this.wifi_encrypt = (String) parameters.get(13);
            this.wifi_defkey = (String) parameters.get(14);
            this.wifi_key1 = (String) parameters.get(15);
            this.wifi_key2 = (String) parameters.get(16);
            this.wifi_key3 = (String) parameters.get(17);
            this.wifi_key4 = (String) parameters.get(18);
            this.wifi_authtype = (String) parameters.get(19);
            this.wifi_keyformat = (String) parameters.get(20);
            this.wifi_key1_bits = (String) parameters.get(21);
            this.wifi_key2_bits = (String) parameters.get(22);
            this.wifi_key3_bits = (String) parameters.get(23);
            this.wifi_key4_bits = (String) parameters.get(24);
            this.wifi_wpa_psk = (String) parameters.get(25);
        }
    }