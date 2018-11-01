package com.chinapalms.kwobox.service;

import java.util.List;

import com.chinapalms.kwobox.dao.impl.SenderEmailDAOImpl;
import com.chinapalms.kwobox.javabean.SenderEmail;

public class SenderEmailService extends SenderEmailDAOImpl {

    @Override
    public List<SenderEmail> findSenderEmails() {
        return super.findSenderEmails();
    }

}
