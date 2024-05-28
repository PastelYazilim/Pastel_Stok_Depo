package com.pastelyazilim.depo.Veritabani;

import java.io.Serializable;

/**
 * Created on 05/12/16.
 */
public class Kullanici implements Serializable
{
    private static final long serialVersionUID = -2770853334115630326L;

    private String sunucu;
    private Integer port;
    private String kullaniciAdi;
    private String parola;
    private String veriTabaniYolu;

    public String getSunucu() {return sunucu;}
    public void setSunucu(String sunucu) {this.sunucu = sunucu;}
    public Integer getPort() {return port;}
    public void setPort(Integer port) {this.port = port;}
    public String getKullaniciAdi() {return kullaniciAdi;}
    public void setKullaniciAdi(String kullaniciAdi) {this.kullaniciAdi = kullaniciAdi;}
    public String getParola() {return parola;}
    public void setParola(String parola) {this.parola = parola;}
    public String getVeriTabaniYolu() {return veriTabaniYolu;}
    public void setVeriTabaniYolu(String veriTabaniYolu) {this.veriTabaniYolu = veriTabaniYolu;}

}