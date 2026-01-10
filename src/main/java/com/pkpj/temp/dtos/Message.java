package com.pkpj.temp.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Message {
    @JsonAlias("body")
    private String content;

    public Message(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

}
