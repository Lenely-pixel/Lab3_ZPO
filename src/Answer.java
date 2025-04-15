package com.company;

public class Answer {
    private final String nickname;
    private final String response;

    public Answer(String nickname, String response) {
        this.nickname = nickname;
        this.response = response;
    }

    public String getNickname() {
        return nickname;
    }

    public String getResponse() {
        return response;
    }
}

