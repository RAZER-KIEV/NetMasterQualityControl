package ua.kiev.netmaster.netmasterqualitycontrol.domain;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by ПК on 11.12.2015.
 */



public class Employee {

    private Long id;
    private String position;            // "admin", "tech", "superAdmin"
    private String login;
    private String password;
    private String inn;
    private String phone;
    private String home;
    private Date regdate;
    private Integer wrongPass;
    private Integer bonusSumm;
    private Boolean isBlocked;
    private Boolean isBusy;
    private Double[] lastlongLat;
    private Date lastOnline;

    public Employee() {
    }

    public Employee(String login, String password) {
        this.login = login;
        this.password = password;
        isBlocked = false;
        isBusy = false;
    }

    public Employee(Long id,String position, String login, String password, String inn, String phone, String home, Date regdate, Integer wrongPass, Integer bonusSumm, Boolean isBlocked, Boolean isBusy, Double[] lastlongLat, Date lastOnline) {
        this.id = id;
        this.position = position;
        this.login = login;
        this.password = password;
        this.inn = inn;
        this.phone = phone;
        this.home = home;
        this.regdate = regdate;
        this.wrongPass = wrongPass;
        this.bonusSumm = bonusSumm;
        this.isBlocked = isBlocked;
        this.isBusy = isBusy;
        this.lastlongLat = lastlongLat;
        this.lastOnline = lastOnline;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public Integer getWrongPass() {
        return wrongPass;
    }

    public void setWrongPass(Integer wrongPass) {
        this.wrongPass = wrongPass;
    }

    public Integer getBonusSumm() {
        return bonusSumm;
    }

    public void setBonusSumm(Integer bonusSumm) {
        this.bonusSumm = bonusSumm;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Boolean getIsBusy() {
        return isBusy;
    }

    public void setIsBusy(Boolean isBusy) {
        this.isBusy = isBusy;
    }

    public Double[] getLastlongLat() {
        return lastlongLat;
    }

    public void setLastlongLat(Double[] lastlongLat) {
        this.lastlongLat = lastlongLat;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public Date getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", position='" + position + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", inn='" + inn + '\'' +
                ", phone='" + phone + '\'' +
                ", home='" + home + '\'' +
                ", regdate=" + regdate +
                ", wrongPass=" + wrongPass +
                ", bonusSumm=" + bonusSumm +
                ", isBlocked=" + isBlocked +
                ", isBusy=" + isBusy +
                ", lastlongLat=" + Arrays.toString(lastlongLat) +
                ", lastOnline=" + lastOnline +
                '}';
    }
}
