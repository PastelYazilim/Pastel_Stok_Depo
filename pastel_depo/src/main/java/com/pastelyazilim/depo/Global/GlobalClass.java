package com.pastelyazilim.depo.Global;

import android.app.Application;
import android.os.StrictMode;

import com.pastelyazilim.depo.Veritabani.Baglanti;
import com.pastelyazilim.depo.Veritabani.Kullanici;

import java.util.Calendar;
import java.util.List;

/**
 * Created on 05/12/16.
 */
public class GlobalClass extends Application {
    public static String KARAKTER_KODLAMASI = "windows-1254";
    public static String WIN1254 = "WIN1254";

 /*

        //MAC OS
        private static String           sunucu="192.168.220.39";//"192.168.0.12";//192.168.0.11 local ip
        private static String           veritabaniYolu="/Users/guven_bulut/proje_program_deneme/Android_Studio/pastel/veritabanı/S01/2018.FDB"; //  /Volumes/Other_OS/proje_program_deneme/Android_Studio/pastel/veritabanı/S01/2018.FDB";
        private static String           sprator="/";

*/
    //Windows
    private static String sunucu = "192.168.1.100";
    private static String veritabaniYolu = "C:\\Pastel\\S01\\" + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) + ".FDB";
    private static String sprator = "\\\\";


    private static String kullaniciAdi = "SYSDBA";
    private static String parola = "masterkey";
    private static String port = "3050";
    private static int id;

    private static int id_yazici;
    private static String yaziciID = "-1";
    private static String yaziciTanim = "VARSAYILAN FİŞ YAZICISINI SEÇİNİZ";
    private static String yaziciAdi = "\nFİŞ YAZICISI SEÇİLMEDİ";

    private static Kullanici kullanici = null;
    public static boolean otomatikKontrol = true;
    public static boolean baglanti_var_mi2 = true;
    public static int VERI_CEKME_ARALIGI = 2;
    public static int masa_ADET_yeni;
    public static int ZAMAN_ASIMI = 10;
    public static int EN_KUCUK_MASA_KODU = 0;

    public static int GARSON_AND_LISANS;
    public static int ADISYON_GARSON_SIFRE;
    public static int PASTEL_VERSIYON;
    public static int GARSON_ADISYON_IPTAL_ETSIN;

    public static Baglanti baglanti = new Baglanti();

    public static int KURUS_HESABI = 2;
    public static double GUNLUKKUR;
    public static double ISKONTO;
    public static String PARASEMBOL;


    public GlobalClass() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.kullaniciOlustur();
    }

    public static Baglanti getBaglanti() {
        return baglanti;
    }

    public static void setBaglanti(Baglanti baglanti) {
        GlobalClass.baglanti = baglanti;
    }

    public Kullanici kullaniciOlustur() {
        try {
            kullanici = new Kullanici();
            kullanici.setSunucu(getSunucu());
            kullanici.setKullaniciAdi(getKullaniciAdi());
            kullanici.setParola(getParola());
            kullanici.setVeriTabaniYolu(getVeritabaniYolu());
            kullanici.setPort(Integer.parseInt(getPort()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kullanici;
    }

    public static Kullanici getKullanici() {
        return kullanici;
    }

    public static void setKullanici(Kullanici kullanici) {
        GlobalClass.kullanici = kullanici;
    }

    public static String getSunucu() {
        return sunucu;
    }

    public static void setSunucu(String sunucu) {
        GlobalClass.sunucu = sunucu;
    }

    public static String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public static void setKullaniciAdi(String kullaniciAdi) {
        GlobalClass.kullaniciAdi = kullaniciAdi;
    }

    public static String getParola() {
        return parola;
    }

    public static void setParola(String parola) {
        GlobalClass.parola = parola;
    }

    public static String getVeritabaniYolu() {
        return veritabaniYolu;
    }

    public static void setVeritabaniYolu(String veritabaniYolu) {
        GlobalClass.veritabaniYolu = veritabaniYolu;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        GlobalClass.id = id;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        GlobalClass.port = port;
    }

    public static String getYaziciID() {
        return yaziciID;
    }

    public static void setYaziciID(String yaziciID) {
        GlobalClass.yaziciID = yaziciID;
    }

    public static String getYaziciTanim() {
        return yaziciTanim;
    }

    public static void setYaziciTanim(String yaziciTanim) {
        GlobalClass.yaziciTanim = yaziciTanim;
    }

    public static String getYaziciAdi() {
        return yaziciAdi;
    }

    public static void setYaziciAdi(String yaziciAdi) {
        GlobalClass.yaziciAdi = yaziciAdi;
    }

    public static int getId_yazici() {
        return id_yazici;
    }

    public static void setId_yazici(int id_yazici) {
        GlobalClass.id_yazici = id_yazici;
    }

    public static String getLisansVeritabaniYolu() {
        //Log.e("veritabaniYolu:",veritabaniYolu);
        String[] yollar = veritabaniYolu.split(sprator);
        String lisansYolu = "";
        for (int i = 0; i < yollar.length - 2; i++)
            lisansYolu = lisansYolu + yollar[i] + sprator;

        return lisansYolu + "VT" + sprator + "SIRKETLER.FDB";
    }
}
