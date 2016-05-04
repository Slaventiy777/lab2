package com;

public class User {
    private String nickname;
    private String password;
    private int rank;

    public User(String password, String nickname) {
        this.password = password;
        this.nickname = nickname;
        this.rank = 1000;
    }

    public User(String nickname, String password, int rank) {
        this.nickname = nickname;
        this.password = password;
        this.rank = rank;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}

