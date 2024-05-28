package com.pastelyazilim.depo.Veritabani;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.pastelyazilim.depo.Global.GlobalClass;
import com.pastelyazilim.depo.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;


/**
 * Created on 05/12/16.
 */
public class Baglanti
{
    private static Kullanici kullanici;
    public static Connection conn;

    private GlobalClass glob=new GlobalClass();

    private static final String URL_DEFAULT = "jdbc:firebirdsql://192.168.0.11:3050/Volumes/Other_OS/veritabani.FDB";
    private static final String USER_DEFAULT = "sysdba";
    private static final String PASSWORD_DEFAULT = "masterkey";

    private static final int REGISTER_CLASS_FOR_NAME = 1;
    private static final int REGISTER_PROPERTIES = 2;
    private static final int REGISTER_JDBC4 = 3;

    private static final int CONNECT_DRIVERMANAGER = 1;
    private static final int CONNECT_DRIVER = 2;





    public Baglanti(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy);
        try {
            //Class.forName("org.firebirdsql.jdbc.FBDriver");
            java.sql.DriverManager.registerDriver ((java.sql.Driver) Class.forName ("org.firebirdsql.jdbc.FBDriver").newInstance ());
        } catch (Exception e) {e.printStackTrace();}
    }






    public boolean Baglan() {

        kullanici=glob.getKullanici();
        String connnectionString=null;
        try {
            String connectionStringFormat="jdbc:firebirdsql:%s/%s:%s";
            connnectionString=String.format(connectionStringFormat, this.kullanici.getSunucu(),this.kullanici.getPort().toString(),this.kullanici.getVeriTabaniYolu());

 //Log.e("Baglanti.java", connnectionString + " , " + this.kullanici.getKullaniciAdi()+ " , " + this.kullanici.getParola());

            DriverManager.setLoginTimeout(glob.ZAMAN_ASIMI);

            Properties props = new Properties();
            props.setProperty("user",  this.kullanici.getKullaniciAdi());
            props.setProperty("password", this.kullanici.getParola());
            props.setProperty("encoding", glob.WIN1254);
            this.conn = DriverManager.getConnection(connnectionString, props);

            //this.conn = DriverManager.getConnection(connnectionString, this.kullanici.getKullaniciAdi(), this.kullanici.getParola());

           return true;
        } catch (Exception e) {
            glob.baglanti_var_mi2=false;
            e.printStackTrace();
            return false;
        }
    }

    public boolean Baglan( String vtYolu) {

        kullanici=glob.getKullanici();
        String connnectionString=null;
        try {
            String connectionStringFormat="jdbc:firebirdsql:%s/%s:%s";
            connnectionString=String.format(connectionStringFormat, this.kullanici.getSunucu(),this.kullanici.getPort().toString(),vtYolu);

            //Log.e("Baglanti.java-Baglan( String vtYolu)-", connnectionString + " , " + this.kullanici.getKullaniciAdi()+ " , " + this.kullanici.getParola());

            DriverManager.setLoginTimeout(glob.ZAMAN_ASIMI);

            Properties props = new Properties();
            props.setProperty("user",  this.kullanici.getKullaniciAdi());
            props.setProperty("password", this.kullanici.getParola());
            props.setProperty("encoding", glob.WIN1254);
            this.conn = DriverManager.getConnection(connnectionString, props);

            //this.conn = DriverManager.getConnection(connnectionString, this.kullanici.getKullaniciAdi(), this.kullanici.getParola());

            return true;
        } catch (Exception e) {
            glob.baglanti_var_mi2=false;
            e.printStackTrace();
            return false;
        }
    }

    public boolean Baglan(Context ctx) {


        kullanici=glob.getKullanici();
        String connnectionString=null;
        try {
            String connectionStringFormat="jdbc:firebirdsql:%s/%s:%s";
            connnectionString=String.format(connectionStringFormat, this.kullanici.getSunucu(),this.kullanici.getPort().toString(),this.kullanici.getVeriTabaniYolu());

            DriverManager.setLoginTimeout(glob.ZAMAN_ASIMI);

            Properties props = new Properties();
            props.setProperty("user",  this.kullanici.getKullaniciAdi());
            props.setProperty("password", this.kullanici.getParola());
            props.setProperty("encoding", glob.WIN1254);
            this.conn = DriverManager.getConnection(connnectionString, props);

            //this.conn = DriverManager.getConnection(connnectionString, this.kullanici.getKullaniciAdi(), this.kullanici.getParola());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            glob.baglanti_var_mi2=false;

            //Log.e("HATAAAAA:",e.toString());
            Toast.makeText(ctx.getApplicationContext(),ctx.getString(R.string.baglanti_yok),Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean Kapat() {
        try {
           if(!conn.isClosed()) this.conn.close();
            return true;
        } catch (Exception e) {glob.baglanti_var_mi2=false;
           e.printStackTrace();
            return false;
        }
    }

    public Statement createStatement() {

        Statement st=null;
        try {
            st=this.conn.createStatement();
            return st;
        } catch (Exception e) {glob.baglanti_var_mi2=false;
             e.printStackTrace();
            return null;
        }
    }

    public ResultSet Select(String sql) {
        Statement sorgu=this.createStatement();
        PreparedStatement updateSales = null;

        try {
            ResultSet sonuc=sorgu.executeQuery(sql);
            return sonuc;
        } catch (Exception e) {glob.baglanti_var_mi2=false;e.printStackTrace();}
        return null;
    }

    public boolean Update(String sql) {
        Statement sorgu=this.createStatement();
        int sonuc=-1;


        try {
            sonuc=(sorgu.executeUpdate(sql));
            //Log.e("Sonuc:", String.valueOf(sonuc));
            if (sonuc<0) return false;

        }catch (Exception e) {glob.baglanti_var_mi2=false;e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean Update(String sql_cumlesi, ArrayList<String> string_dizi, ArrayList<Double> dbl_dizi) {

        PreparedStatement sorgu =null;
                Log.e("Prepared sorgu0:", "--------------------------------");
        try {

             sorgu = conn.prepareStatement(sql_cumlesi);

            if (dbl_dizi.get(0) ==null) sorgu.setNull(1, java.sql.Types.NULL); else sorgu.setDouble(1, dbl_dizi.get(0));
            if (dbl_dizi.get(1) ==null) sorgu.setNull(2, java.sql.Types.NULL); else sorgu.setDouble(2, dbl_dizi.get(1));
            if (dbl_dizi.get(2) ==null) sorgu.setNull(3, java.sql.Types.NULL); else sorgu.setDouble(3, dbl_dizi.get(2));
            if (dbl_dizi.get(3) ==null) sorgu.setNull(4, java.sql.Types.NULL); else sorgu.setDouble(4, dbl_dizi.get(3));
            if (dbl_dizi.get(4) ==null) sorgu.setNull(5, java.sql.Types.NULL); else sorgu.setDouble(5, dbl_dizi.get(4));

            if (string_dizi.get(0) == null) sorgu.setNull(6, java.sql.Types.NULL); else sorgu.setString(6, string_dizi.get(0));
            if (string_dizi.get(1) == null) sorgu.setNull(7, java.sql.Types.NULL); else sorgu.setString(7, string_dizi.get(1));


            Log.e("Prepared sorgu:", String.valueOf(sorgu));

            int sonuc=-1;

            sonuc=sorgu.executeUpdate();
            Log.e("Sonuc:", String.valueOf(sonuc));
            if (sonuc<0) return false;

        }catch (Exception e) { Log.e("Prepared sorgu:", sorgu.toString());;e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean Delete(String sql) {
        Statement sorgu=this.createStatement();
        boolean sonuc=false;
        try {
            sonuc=sorgu.execute(sql);
        } catch (Exception e) {glob.baglanti_var_mi2=false;
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean callProcedure(String sql) {

        Statement sorgu=this.createStatement();
        boolean sonuc=false;
        try {
            sonuc=sorgu.execute(sql);

        } catch (Exception e) {glob.baglanti_var_mi2=false;
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Connection getConn() {
        return conn;
    }

    public static void setConn(Connection conn) {
        Baglanti.conn = conn;
    }


    public  void showSQLException(java.sql.SQLException e) {
        java.sql.SQLException next = e;
        while (next != null) {
            System.out.println(next.getMessage());
            System.out.println("Error Code: " + next.getErrorCode());
            System.out.println("SQL State: " + next.getSQLState());
            next = next.getNextException();
        }
    }

}
