package com.sonata.portfoliomanagement.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeliveryManagerDataDTO {

    public DeliveryManagerDataDTO() { }

    public DeliveryManagerDataDTO(String dd, String dm, String account, List<DataDTO> previous, List<DataDTO> current) {
        this.dd = dd;
        this.dm = dm;
        this.account = account;
        this.previous = previous;
        this.current = current;
    }

    private String dd;
    private String dm;
    private String account;
    private List<DataDTO> previous;
    private List<DataDTO> current;
    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<DataDTO> getPrevious() {
        return previous;
    }

    public void setPrevious(List<DataDTO> previous) {
        this.previous = previous;
    }

    public List<DataDTO> getCurrent() {
        return current;
    }

    public void setCurrent(List<DataDTO> current) {
        this.current = current;
    }



}