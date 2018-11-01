package com.chinapalms.kwobox.dao;

import com.chinapalms.kwobox.javabean.Token;

public interface TokenDAO {

    public boolean addToken(Token token);

    public boolean updateToken(Token token);

    public Token findTokenByTokenType(String tokenType);

}
