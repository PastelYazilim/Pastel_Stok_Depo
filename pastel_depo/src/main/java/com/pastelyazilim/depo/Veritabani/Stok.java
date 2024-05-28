package com.pastelyazilim.depo.Veritabani;


import android.util.Log;

import com.pastelyazilim.depo.Global.GlobalClass;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * guven_bulut tarafından 6.11.2018 tarihinde oluşturulmuştur.
 * pastel_yazilim projesi için tasarlanmaştır.
 */
public class Stok {

    private String barkod;
    private String stokKodu;
    private int stokID;
    private String stokAdi;
    private Double prgBakiye;
    private Double sayimBakiye;
    private String paraSembol;
    private Double fiyatS1;
    private Double fiyatS2;
    private Double fiyatS3;
    private Double fiyatS4;
    private Double fiyatS5;
    private Double alisKDV;
    private Double alisKDVsiz;

    private GlobalClass glob;

    public Stok(){

    }
    public String TurkceKodla(byte[] gelen){
        try {return new String(gelen, glob.KARAKTER_KODLAMASI);} catch (Exception e) {return "";}
    }
    public Stok stokBul(String depoID, String barkod){

        Stok stk=new Stok();
        stk.setBarkod("-2");

        if(!(glob.baglanti.Baglan())) {
            stk.setBarkod("-1");
            return stk;
        }

        String SQL_cumlesi="SELECT * FROM TERMINAL_STOK_ARAMA(" + depoID + ", '" + barkod + "' , NULL);";
        ResultSet sonuc= glob.baglanti.Select(SQL_cumlesi);

       Log.e("SQL_cumlesi:", SQL_cumlesi);
        try {

            while (sonuc.next()) {
                try {stk.setBarkod(barkod);}catch (Exception e) {}
                try {stk.setStokKodu(TurkceKodla(sonuc.getBytes("STOKKODU")));}catch (Exception e) {}
                try {stk.setStokAdi(TurkceKodla(sonuc.getBytes("STOKADI")));}catch (Exception e) {}
                try {stk.setPrgBakiye((sonuc.getDouble("BAKIYE1")));}catch (Exception e) {}
                try {stk.setSayimBakiye((sonuc.getDouble("BAKIYE2")));}catch (Exception e) {}
                try {stk.setFiyatS1(sonuc.getDouble("SATISFIYATI"));}catch (Exception e) {}
                try {stk.setFiyatS2(sonuc.getDouble("SATISFIYATI2"));}catch (Exception e) {}
                try {stk.setFiyatS3(sonuc.getDouble("SATISFIYATI3"));}catch (Exception e) {}
                try {stk.setFiyatS4(sonuc.getDouble("SATISFIYATI4"));}catch (Exception e) {}
                try {stk.setFiyatS5(sonuc.getDouble("SATISFIYATI5"));}catch (Exception e) {}
                try {stk.setAlisKDVsiz(sonuc.getDouble("ALISFIYATI"));}catch (Exception e) {}
                try {stk.setAlisKDV(sonuc.getDouble("ALISFIYATIKDVLI"));}catch (Exception e) {}
                try {stk.setParaSembol(sonuc.getString("PARASEMBOL"));}catch (Exception e) {}

                try {stk.setStokID(sonuc.getInt("STOK_ID"));}catch (Exception e) {}
            }

        } catch (Exception e) {stk.setBarkod("-2");e.printStackTrace();}

        try {
            glob.baglanti.Kapat();

            Baglanti bag = new Baglanti();
            bag.Baglan();
            ResultSet kurus= bag.Select("Select stokkartkurushanesi from progayarlar");
            while (kurus.next())
                glob.KURUS_HESABI=kurus.getInt("stokkartkurushanesi");
        } catch (Exception ex) {ex.printStackTrace();}



        return stk;

    }

    public int getStokID() {
        return stokID;
    }

    public void setStokID(int stokID) {
        this.stokID = stokID;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getStokKodu() {
        return stokKodu;
    }

    public void setStokKodu(String stokKodu) {
        this.stokKodu = stokKodu;
    }

    public String getStokAdi() {
        return stokAdi;
    }

    public void setStokAdi(String stokAdi) {
        this.stokAdi = stokAdi;
    }

    public Double getPrgBakiye() {
        return prgBakiye;
    }

    public void setPrgBakiye(Double prgBakiye) {
        this.prgBakiye = prgBakiye;
    }

    public Double getSayimBakiye() {
        return sayimBakiye;
    }

    public void setSayimBakiye(Double sayimBakiye) {
        this.sayimBakiye = sayimBakiye;
    }

    public Double getFiyatS1() {
        return fiyatS1;
    }

    public void setFiyatS1(Double fiyatS1) {
        this.fiyatS1 = fiyatS1;
    }

    public Double getFiyatS2() {
        return fiyatS2;
    }

    public void setFiyatS2(Double fiyatS2) {
        this.fiyatS2 = fiyatS2;
    }

    public Double getFiyatS3() {
        return fiyatS3;
    }

    public void setFiyatS3(Double fiyatS3) {
        this.fiyatS3 = fiyatS3;
    }

    public Double getFiyatS4() {
        return fiyatS4;
    }

    public void setFiyatS4(Double fiyatS4) {
        this.fiyatS4 = fiyatS4;
    }

    public Double getFiyatS5() {
        return fiyatS5;
    }

    public void setFiyatS5(Double fiyatS5) {
        this.fiyatS5 = fiyatS5;
    }

    public Double getAlisKDV() {
        return alisKDV;
    }

    public void setAlisKDV(Double alisKDV) {
        this.alisKDV = alisKDV;
    }

    public Double getAlisKDVsiz() {
        return alisKDVsiz;
    }

    public void setAlisKDVsiz(Double alisKDVsiz) {
        this.alisKDVsiz = alisKDVsiz;
    }

    public String getParaSembol() {
        return paraSembol;
    }

    public void setParaSembol(String paraSembol) {
        this.paraSembol = paraSembol;
    }

    public GlobalClass getGlob() {
        return glob;
    }

    public void setGlob(GlobalClass glob) {
        this.glob = glob;
    }
}
