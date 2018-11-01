package com.chinapalms.kwobox.javabean;

import java.util.Date;

public class Token {

    private int id;
    private String requestToken;
    private String requestTicket;
    private String tokenType;
    private Date tokenDateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public String getRequestTicket() {
        return requestTicket;
    }

    public void setRequestTicket(String requestTicket) {
        this.requestTicket = requestTicket;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Date getTokenDateTime() {
        return tokenDateTime;
    }

    public void setTokenDateTime(Date tokenDateTime) {
        this.tokenDateTime = tokenDateTime;
    }

    @Override
    public String toString() {
        return "Token [id=" + id + ", requestToken=" + requestToken
                + ", requestTicket=" + requestTicket + ", tokenType="
                + tokenType + ", tokenDateTime=" + tokenDateTime + "]";
    }

}
