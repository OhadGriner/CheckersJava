package sample;

import java.io.Serializable;

public class Player implements Serializable {
    private String ip;
    private String user_name;
    private int games_won;
    private int games_played;

    public Player(String ip, String user_name,int games_won,int games_played) {
        this.ip = ip;
        this.user_name = user_name;
        this.games_won=games_won;
        this.games_played=games_played;
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

    public int getGames_won() {
        return games_won;
    }

    public void setGames_won(int games_won) {
        this.games_won = games_won;
    }

    public int getGames_played() {
        return games_played;
    }

    public void setGames_played(int games_played) {
        this.games_played = games_played;
    }
}
