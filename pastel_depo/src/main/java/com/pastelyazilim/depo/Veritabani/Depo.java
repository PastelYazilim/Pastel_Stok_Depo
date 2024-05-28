package com.pastelyazilim.depo.Veritabani;

import com.pastelyazilim.depo.Global.GlobalClass;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * guven_bulut tarafından 5.11.2018 tarihinde oluşturulmuştur.
 * pastel_yazilim projesi için tasarlanmaştır.
 */
public class Depo {
    private String ID;
    private String adi;
    private GlobalClass glob;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public List<Depo> depolariYukle(){


        if(!(glob.baglanti.Baglan())) return null;
        ArrayList<Depo> dps=new ArrayList<Depo>();
        Depo dp=new Depo();
        String SQL_cumlesi="Select DEPOID, DEPOADI  from Depolar Where DepoID>0 ORDER BY DEPOID";
        ResultSet sonuc= glob.baglanti.Select(SQL_cumlesi);

        try {

            while (sonuc.next()) {
                dp = new Depo();
                try {dp.setID(String.valueOf(sonuc.getInt("DEPOID")));}catch (Exception e) {}
                try {dp.setAdi(TurkceKodla(sonuc.getBytes("DEPOADI")));}catch (Exception e) {}
                dps.add(dp);
            }





        } catch (Exception e) {e.printStackTrace();}

        try {glob.baglanti.Kapat();} catch (Exception ex) {ex.printStackTrace();}

        return dps;
    }
    public String TurkceKodla(byte[] gelen){
        try {return new String(gelen, glob.KARAKTER_KODLAMASI);} catch (Exception e) {return "";}
    }
}
