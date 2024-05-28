package com.pastelyazilim.depo.Veritabani;

import android.app.Activity;
import android.os.StrictMode;

import com.pastelyazilim.depo.Global.GlobalClass;

import java.sql.ResultSet;

/**
 * guven_bulut tarafından 28/02/2017 tarihinde oluşturulmuştur.
 * pastel_yazilim projesi için tasarlanmaştır.
 */

public class Login {

    private int GARSON_AND_LISANS;
    private int ADISYON_GARSON_SIFRE;

    private int GARSON_ADISYON_IPTAL_ETSIN;
    private int PASTEL_VERSIYON;

    Activity act;

    private GlobalClass glob;

    public Login(Activity act) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy);

        this.act=act;
        if(!(glob.baglanti.Baglan(this.act))) return ;

        String SQL_cumlesi="SELECT * FROM PROGAYARLAR";
        ResultSet sonuc= glob.baglanti.Select(SQL_cumlesi);
        try {
            while (sonuc.next()) {
                GARSON_AND_LISANS=sonuc.getInt("GARSON_AND_LISANS");
                ADISYON_GARSON_SIFRE=sonuc.getInt("ADISYON_GARSON_SIFRE");
                PASTEL_VERSIYON=sonuc.getInt("VERSIYON");
                try {if ((PASTEL_VERSIYON>=5500)) GARSON_ADISYON_IPTAL_ETSIN=sonuc.getInt("GARSON_ADISYON_IPTAL_ETSIN"); else GARSON_ADISYON_IPTAL_ETSIN=1;} catch (Exception ex) {ex.printStackTrace();}

            }
        } catch (Exception e) {e.printStackTrace();}

        try {glob.baglanti.Kapat();} catch (Exception ex) {ex.printStackTrace();}

        glob.GARSON_AND_LISANS=GARSON_AND_LISANS;
        glob.ADISYON_GARSON_SIFRE=ADISYON_GARSON_SIFRE;
        glob.PASTEL_VERSIYON=PASTEL_VERSIYON;
        glob. GARSON_ADISYON_IPTAL_ETSIN= GARSON_ADISYON_IPTAL_ETSIN;
    }

    public int getGARSON_AND_LISANS() {
        return GARSON_AND_LISANS;
    }
}
