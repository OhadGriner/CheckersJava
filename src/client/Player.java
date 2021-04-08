package client;

import java.io.Serializable;

public class Player implements Serializable {
    private String ip;
    private String user_name;

    public Player(String ip, String user_name) {
        this.ip = ip;
        this.user_name = user_name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String toString() {
        return "Player{" +
                "ip='" + ip + '\'' +
                ", user_name='" + user_name + '\'' +
                '}';
    }
}
