package com.chinapalms.kwobox.dao;

import java.util.List;

import com.chinapalms.kwobox.javabean.SenderEmail;

public interface SenderEmailDAO {

    public List<SenderEmail> findSenderEmails();

}
