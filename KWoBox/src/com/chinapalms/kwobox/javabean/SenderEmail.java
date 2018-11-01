package com.chinapalms.kwobox.javabean;

public class SenderEmail {

    private String emailAccount;// 发件人邮箱账号
    private String emailPassword;// 发件人邮箱密码

    public String getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    @Override
    public String toString() {
        return "SenderEmail [emailAccount=" + emailAccount + ", emailPassword="
                + emailPassword + "]";
    }

}
