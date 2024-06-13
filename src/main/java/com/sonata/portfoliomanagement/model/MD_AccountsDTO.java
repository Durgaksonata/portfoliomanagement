package com.sonata.portfoliomanagement.model;

import java.util.List;

public class MD_AccountsDTO {

    private List<String> dmList;
    private List<String> accountList;

    public MD_AccountsDTO(List<String> dmList, List<String> accountList) {
        super();
        this.dmList = dmList;
        this.accountList = accountList;
    }
    public MD_AccountsDTO() {}
    public List<String> getDmList() {
        return dmList;
    }
    public void setDmList(List<String> dmList) {
        this.dmList = dmList;
    }
    public List<String> getAccountList() {
        return accountList;
    }
    public void setAccountList(List<String> accountList) {
        this.accountList = accountList;
    }




}