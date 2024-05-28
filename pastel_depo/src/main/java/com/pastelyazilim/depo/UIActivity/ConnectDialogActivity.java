package com.pastelyazilim.depo.UIActivity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pastelyazilim.depo.Global.GlobalClass;
import com.pastelyazilim.depo.ListViewAdapter.DepolarAdapter;
import com.pastelyazilim.depo.R;
import com.pastelyazilim.depo.Veritabani.Baglanti;
import com.pastelyazilim.depo.Veritabani.Database_baglanti;
import com.pastelyazilim.depo.Veritabani.Kullanici;

public class ConnectDialogActivity extends Activity {

    private EditText edtHost;
    private EditText edtUserName;
    private EditText edtPassword;
    private EditText edtPort;
    private EditText edtDatabaseFilePath;

    private Kullanici kullanici;
    private GlobalClass glob = new GlobalClass();

    private boolean baglanti_sonucu=false;

    Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_dialog);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); StrictMode.setThreadPolicy(policy);


        edtHost=(EditText)findViewById(R.id.edtHost);
        edtUserName=(EditText)findViewById(R.id.edtUserName);
        edtPassword=(EditText)findViewById(R.id.edtPassword);
        edtPort=(EditText)findViewById(R.id.edtPort);
        edtDatabaseFilePath=(EditText)findViewById(R.id.edtDatabaseFilePath);


        glob.otomatikKontrol=false;
        act=this;

        Database_baglanti db = new Database_baglanti(getApplicationContext());
        ArrayList<HashMap<String, String>> kayit_liste = db.kayitlar();

        if (db.kayitlar().size() == 0) {
            db.kayitEkle(glob.getKullaniciAdi(), glob.getVeritabaniYolu(), glob.getPort(), glob.getSunucu());
        } else {
            glob.setVeritabaniYolu(kayit_liste.get(0).get("veritabaniYolu"));
            glob.setSunucu(kayit_liste.get(0).get("sunucu"));
            glob.setPort(kayit_liste.get(0).get("port"));
            glob.setId(Integer.parseInt(kayit_liste.get(0).get("id")));

        }


        glob.kullaniciOlustur();


        //local
        edtHost.setText(glob.getSunucu());
        edtPort.setText(glob.getPort());
        edtUserName.setText(glob.getKullaniciAdi());
        edtPassword.setText(glob.getParola());
        edtDatabaseFilePath.setText(glob.getVeritabaniYolu());
    }

    public void btn_test_et(View sender) {

       test();
    }

    public void test(){
        Database_baglanti db_baglanti = new Database_baglanti(getApplicationContext());



        ArrayList<HashMap<String, String>> kayit_liste= db_baglanti.kayitlar();
        if(db_baglanti.kayitlar().size()==0)
            db_baglanti.kayitEkle(glob.getKullaniciAdi(),glob.getVeritabaniYolu(),glob.getPort(),glob.getSunucu());
        else
            db_baglanti.kayitDuzenle(edtUserName.getText().toString(),edtDatabaseFilePath.getText().toString(),edtPort.getText().toString(),edtHost.getText().toString(),Integer.parseInt(kayit_liste.get(0).get("id")));






        kullanici=new Kullanici();
        kullanici.setSunucu(edtHost.getText().toString());
        kullanici.setKullaniciAdi(edtUserName.getText().toString());
        kullanici.setParola(edtPassword.getText().toString());
        kullanici.setVeriTabaniYolu(edtDatabaseFilePath.getText().toString());
        kullanici.setPort(Integer.parseInt(edtPort.getText().toString()));
        glob.setKullanici(kullanici);

        final BağlanmayiDene baglan=new  BağlanmayiDene();
        baglan.execute();
    }

    public void btn_finish(View sender){

        glob.otomatikKontrol=true;
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.connect_dialog, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
    public void guncelleme(View sender) {
        Intent i = new Intent(ConnectDialogActivity.this, LoginActivity.class);
        startActivityForResult(i,2);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2) {
            finish();
        }


    }

    public class BağlanmayiDene extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MesajGoster(getString(R.string.sunucuya_baglaniliyor));
            //Toast.makeText(getApplicationContext(), getString(R.string.sunucuya_baglaniliyor), Toast.LENGTH_SHORT).show();
            glob.setSunucu(edtHost.getText().toString());
            glob.setKullaniciAdi(edtUserName.getText().toString());
            glob.setParola(edtPassword.getText().toString());
            glob.setVeritabaniYolu(edtDatabaseFilePath.getText().toString());
            glob.setPort(edtPort.getText().toString());
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean son=false;
            try {
                if(glob.baglanti.Baglan(act))  {
                    son= true;
                    glob.baglanti_var_mi2 =true;
                }
                else{
                    glob.baglanti_var_mi2=false;
                    son= false;
                }
            }
            catch (Exception e) {
                son= false;
                e.printStackTrace();
            }

            try {glob.baglanti.Kapat();} catch (Exception ex) {ex.printStackTrace();}
            return son;
        }

        @Override
        protected void onPostExecute(Boolean sonuc) {
            super.onPostExecute(sonuc);
            baglanti_sonucu=sonuc;
            MesajGizle();

            if(sonuc){

                Baglanti lisansBaglanti = new Baglanti();


                  if(!lisansBaglanti.Baglan(glob.getLisansVeritabaniYolu())){

                    AlertDialog alertDialog = new AlertDialog.Builder(ConnectDialogActivity.this).create();
                    alertDialog.setTitle("VERİTABANI GÜNCEL DEĞİL");
                    alertDialog.setMessage("Pastel veritabanınız güncel değil, program devam edemiyor.");
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ÇIKIŞ",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    System.exit(0);
                                }
                            });
                    alertDialog.show();
                }



                ResultSet DEPOSAYIMLISANS= lisansBaglanti.Select("SELECT DEPOSAYIMLISANS FROM TERMINAL");

                try {
                    while (DEPOSAYIMLISANS.next()) {
                        try {
                            //Log.e("DEPOSAYIMLISANS:", String.valueOf(DEPOSAYIMLISANS.getInt("DEPOSAYIMLISANS")));
                            if (DEPOSAYIMLISANS.getInt("DEPOSAYIMLISANS")==0) {

                                Toast.makeText(getApplicationContext(), getString(R.string.lisans_hatasi), Toast.LENGTH_LONG).show();
                                MesajGizle();

                                AlertDialog alertDialog = new AlertDialog.Builder(ConnectDialogActivity.this).create();
                                alertDialog.setTitle("LİSANS HATASI");
                                alertDialog.setMessage("LİSANS HATASI:");
                                alertDialog.setCancelable(false);
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ÇIKIŞ",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                System.exit(0);
                                            }
                                        });
                                alertDialog.show();

                            }
                            else {
                                glob.baglanti_var_mi2=true;
                                Baglanti bag = new Baglanti();
                                bag.Baglan();
                                ResultSet kurus= bag.Select("Select stokkartkurushanesi from progayarlar");
                                while (kurus.next())
                                    glob.KURUS_HESABI=kurus.getInt("stokkartkurushanesi");


                                Toast.makeText(getApplicationContext(), getString(R.string.baglanti_saglandi), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e) {}
                    }
                } catch (Exception e) {e.printStackTrace();}

            }
            else{
                glob.baglanti_var_mi2=false;
                Toast.makeText(getApplicationContext(), getString(R.string.baglanti_saglanamadi), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ProgressDialog mProgressDialog;
    protected void MesajGoster(String mesaj) {
        try{
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(mesaj);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            Dialog_omru(glob.ZAMAN_ASIMI,mProgressDialog);
            mProgressDialog.show();
        }catch (Exception e){e.printStackTrace();}
    }
    protected void MesajGizle() {

        try{mProgressDialog.dismiss();}catch (Exception e){e.printStackTrace();}
    }
    public void Dialog_omru(long time, final Dialog d){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                d.dismiss();
            }
        }, time*1000);
    }

}
