package com.pastelyazilim.depo.UIActivity;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;

import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.pastelyazilim.depo.Global.GlobalClass;
import com.pastelyazilim.depo.ListViewAdapter.DepolarAdapter;
import com.pastelyazilim.depo.R;
import com.pastelyazilim.depo.Veritabani.Baglanti;
import com.pastelyazilim.depo.Veritabani.Database_baglanti;
import com.pastelyazilim.depo.Veritabani.Depo;
import com.pastelyazilim.depo.Veritabani.Stok;
import com.pastelyazilim.depo.zbar.FullScannerFragmentActivity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static com.pastelyazilim.depo.Global.GlobalClass.PASTEL_VERSIYON;

public class LoginActivity extends Activity {

    private Activity act = this;
    private Spinner spin_depolar;
    private List<Depo> depoList;
    private GlobalClass glob = new GlobalClass();
    private String depoID;


    private EditText barkod, stokKodu, stokAdi, prgBakiye, sayimBakiye, fiyatS1, fiyatS2, fiyatS3, fiyatS4, fiyatS5;
    private TextView alisKDV, alisKDVsiz;
    Baglanti bag;
    Stok stk;

    private String[] permissions = {
            Manifest.permission.CAMERA
    };


    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        act = this;

        barkod = (EditText) findViewById(R.id.edt_barkod);
        stokKodu = (EditText) findViewById(R.id.edt_stokKodu);
        stokAdi = (EditText) findViewById(R.id.edt_stokAdi);
        prgBakiye = (EditText) findViewById(R.id.edt_prgBakiye);
        sayimBakiye = (EditText) findViewById(R.id.edt_sayBakiye);
        fiyatS1 = (EditText) findViewById(R.id.edt_fiyatS1);
        fiyatS2 = (EditText) findViewById(R.id.edt_fiyatS2);
        fiyatS3 = (EditText) findViewById(R.id.edt_fiyatS3);
        fiyatS4 = (EditText) findViewById(R.id.edt_fiyatS4);
        fiyatS5 = (EditText) findViewById(R.id.edt_fiyatS5);
        alisKDV = (TextView) findViewById(R.id.edt_alisKDVli);
        alisKDVsiz = (TextView) findViewById(R.id.edt_alisKDVsiz);

        barkod.setImeActionLabel("Stok Ara", KeyEvent.KEYCODE_ENTER);

        barkod.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEND:
                    case EditorInfo.IME_ACTION_GO:
                    case EditorInfo.IME_ACTION_DONE:
                        Log.e("actionId-----2", "actionId");
                        ara(true);
                        break;
                }
                return false;
            }
        });

        barkod.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    ara(true);
                    return true;
                }
                return false;
            }
        });


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
        spin_depolar = (Spinner) findViewById(R.id.spin_depolar);

        spin_depolar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                //Log.e("Depo Bilgileri-> ", "Depo ID:" + depoList.get(pos).getID() + " - Depo Adı:" + depoList.get(pos).getAdi());
                depoID = depoList.get(pos).getID();

                barkod.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(barkod, InputMethodManager.SHOW_IMPLICIT);


            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new BağlanmayiDene().execute();

    }


    public void tara(View sender) {

        Permissions.check(LoginActivity.this, permissions, null, null, new PermissionHandler() {

            @Override
            public void onGranted() {

                try {
                    mClss = FullScannerFragmentActivity.class;
                    Intent intent = new Intent(LoginActivity.this, mClss);
                    startActivityForResult(intent, 1);

                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "Hata...", LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
            }

            @Override
            public boolean onBlocked(Context context, ArrayList<String> blockedList) {
                return super.onBlocked(context, blockedList);
            }

            @Override
            public void onJustBlocked(Context context, ArrayList<String> justBlockedList, ArrayList<String> deniedPermissions) {
                super.onJustBlocked(context, justBlockedList, deniedPermissions);
            }
        });

        /*
         if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, mClss);
            startActivityForResult(intent,1);
        }

         */

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivityForResult(intent, 1);
                    }
                } else {
                    Toast.makeText(this, "Barkod okuma için kamera erişimine izin veriniz.", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            try {
                barkod.setText(data.getStringExtra("barkodDegeri"));
                ara(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void ayarlar(View sender) {
        Intent i = new Intent(LoginActivity.this, ConnectDialogActivity.class);
        startActivity(i);
    }

    public void kapat(View sender) {
        setResult(RESULT_OK, this.getIntent());
        System.exit(0);
    }

    Snackbar snackbar;
    public void etiketYazdir(View sender) {


        try {

            glob.baglanti.Baglan(act);

            ResultSet sonuc2= glob.baglanti.Select("SELECT * FROM PROGAYARLAR");
            try {
                while (sonuc2.next()) {
                    PASTEL_VERSIYON=sonuc2.getInt("VERSIYON");
                }
            } catch (Exception e) {e.printStackTrace();}

            try {glob.baglanti.Kapat();} catch (Exception ex) {ex.printStackTrace();}



            if ((PASTEL_VERSIYON<6300)) {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("SÜRÜM BİLGİSİ");
                alertDialog.setMessage("Bu özelliği kullanabilmeniz için Ticari programınızın versiyonu v6.3 ve üzeri olmalıdır. Mevcut Versiyon: "+ PASTEL_VERSIYON);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "TAMAM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return;
            }

            if(barkod.getText().toString().isEmpty()||stokKodu.getText().toString().isEmpty()){
                snackbar = Snackbar
                        .make(findViewById(android.R.id.content), "Önce ürün çağırınız.", Snackbar.LENGTH_LONG)
                        .setAction("Tamam", view -> snackbar.dismiss());

                snackbar.show();
                return;
            }

            new EtiketYazdir().execute();
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.veritabaniAyarlari:
                MesajGizle();
                Intent i = new Intent(LoginActivity.this, ConnectDialogActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class BağlanmayiDene extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MesajGoster(getString(R.string.sunucuya_baglaniliyor));
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean son = false;
            try {
                if (glob.baglanti.Baglan(act)) {
                    son = true;
                    glob.baglanti_var_mi2 = true;
                } else {
                    glob.baglanti_var_mi2 = false;
                    son = false;
                }
            } catch (Exception e) {
                son = false;
                e.printStackTrace();
            }

            try {
                glob.baglanti.Kapat();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (son) {
                depoList = new ArrayList<Depo>();

                depoList = new Depo().depolariYukle();
            }

            return son;
        }

        @Override
        protected void onPostExecute(Boolean sonuc) {
            super.onPostExecute(sonuc);


            if (sonuc) {
                Baglanti lisansBaglanti = new Baglanti();

                //bunu aktif etmeyi unutma

                if (!lisansBaglanti.Baglan(glob.getLisansVeritabaniYolu())) {

                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
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


                ResultSet DEPOSAYIMLISANS = lisansBaglanti.Select("SELECT DEPOSAYIMLISANS FROM TERMINAL");


                try {

                    while (DEPOSAYIMLISANS.next()) {
                        try {
                            //Log.e("DEPOSAYIMLISANS:", String.valueOf(DEPOSAYIMLISANS.getInt("DEPOSAYIMLISANS")));
                            if (DEPOSAYIMLISANS.getInt("DEPOSAYIMLISANS") == 0) {

                                Toast.makeText(getApplicationContext(), getString(R.string.lisans_hatasi), LENGTH_LONG).show();
                                MesajGizle();

                                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                alertDialog.setTitle("LİSANS HATASI");
                                alertDialog.setMessage("LİSANS HATASI");
                                alertDialog.setCancelable(false);
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ÇIKIŞ",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                System.exit(0);
                                            }
                                        });
                                alertDialog.show();

                            } else {
                                DepolarAdapter adapter = new DepolarAdapter(act, R.layout.lay_spin_depolar, depoList);
                                spin_depolar.setAdapter(adapter);
                            }

                        } catch (Exception e) {

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {

                Toast.makeText(getApplicationContext(), getString(R.string.baglanti_saglanamadi), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, ConnectDialogActivity.class);
                startActivity(i);
            }

            MesajGizle();

        }
    }

    private ProgressDialog mProgressDialog;

    protected void MesajGoster(String mesaj) {
        try {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(mesaj);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            // Dialog_omru(glob.ZAMAN_ASIMI, mProgressDialog);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void MesajGizle() {

        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Dialog_omru(long time, final Dialog d) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                d.dismiss();
            }
        }, time * 1000);
    }

    public void temizle() {
        barkod.setText("");
        stokKodu.setText("");
        stokAdi.setText("");
        prgBakiye.setText("");
        sayimBakiye.setText("");
        fiyatS1.setText("");
        fiyatS2.setText("");
        fiyatS3.setText("");
        fiyatS4.setText("");
        fiyatS5.setText("");
        alisKDV.setText("");
        alisKDVsiz.setText("");

        barkod.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(barkod, InputMethodManager.SHOW_IMPLICIT);
    }

    public void ara(boolean clear) {
        stk = new Stok();
        stk = stk.stokBul(depoID, barkod.getText().toString());
        if (stk.getBarkod().equals("-1")) {


            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("BAĞLANTI HATASI");
            alertDialog.setMessage("Pastel veritabanı ile bağlantı kurulamadı...");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "TEMİZLE VE KAPAT",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(clear) temizle();
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "KAPAT",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else if (stk.getBarkod().equals("-2")) {

            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("ÜRÜN BULANAMADI");
            alertDialog.setMessage("Aradığınız ürün bulunamadı...");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "TEMİZLE VE KAPAT",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(clear) temizle();
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "KAPAT",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else {

            stokKodu.setText(stk.getStokKodu());
            stokAdi.setText(stk.getStokAdi());
            prgBakiye.setText(String.format("%." + String.valueOf(glob.KURUS_HESABI) + "f", stk.getPrgBakiye()));
            // sayimBakiye.setText(stk.getSayimBakiye());
            if (stk.getFiyatS1() > 0)
                fiyatS1.setText(String.format("%." + String.valueOf(glob.KURUS_HESABI) + "f", stk.getFiyatS1()));
            else fiyatS1.setText("");
            if (stk.getFiyatS2() > 0)
                fiyatS2.setText(String.format("%." + String.valueOf(glob.KURUS_HESABI) + "f", stk.getFiyatS2()));
            else fiyatS2.setText("");
            if (stk.getFiyatS3() > 0)
                fiyatS3.setText(String.format("%." + String.valueOf(glob.KURUS_HESABI) + "f", stk.getFiyatS3()));
            else fiyatS3.setText("");
            if (stk.getFiyatS4() > 0)
                fiyatS4.setText(String.format("%." + String.valueOf(glob.KURUS_HESABI) + "f", stk.getFiyatS4()));
            else fiyatS4.setText("");
            if (stk.getFiyatS5() > 0)
                fiyatS5.setText(String.format("%." + String.valueOf(glob.KURUS_HESABI) + "f", stk.getFiyatS5()));
            else fiyatS5.setText("");
            if (stk.getAlisKDV() > 0)
                alisKDV.setText(String.format("%." + String.valueOf(glob.KURUS_HESABI) + "f", stk.getAlisKDV()) + " " + stk.getParaSembol());
            else alisKDV.setText("");
            if (stk.getAlisKDVsiz() > 0)
                alisKDVsiz.setText(String.format("%." + String.valueOf(glob.KURUS_HESABI) + "f", stk.getAlisKDVsiz()) + " " + stk.getParaSembol());
            else alisKDVsiz.setText("");

            sayimBakiye.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(sayimBakiye, InputMethodManager.SHOW_IMPLICIT);

        }

        // Log.e("MSG ", "Depo ID:" + depoID + "Barkod:" + barkod.getText().toString());
    }

    public void onBtnAra(View v) {
        ara(true);
    }

    public void onBtnTemizle(View v) {
        temizle();
    }

    public void onBtnGuncelle(View v) {
        new Guncelle().execute();
    }

    public class EtiketYazdir extends AsyncTask<String, Void, Boolean> {

        int dgr = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MesajGoster("Etiket Yazdırılıyor...");
        }

        @Override
        protected Boolean doInBackground(String... params) {

            String s_barkod, s_stokKodu;

            s_barkod = barkod.getText().toString();
            s_stokKodu = stokKodu.getText().toString();

            bag = new Baglanti();

            boolean sonuc = true;
            boolean sorgu_sonucu_SQL_1 = false;

            try {
                if (!bag.Baglan(act)) {
                    dgr = -1;
                    return false;
                }
                bag.conn.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            String SQL_1 = "INSERT INTO BARKOD (" +
                    " STOK_ID," +
                    " BARKODNO," +
                    " MIKTAR" +
                    ") " +
                    "VALUES (" + stk.getStokID() + ", '" + s_barkod + "'," +
                    " 1 )";

            try {
                return bag.Update(SQL_1);

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean sonuc) {
            super.onPostExecute(sonuc);

            MesajGizle();

            if (sonuc) {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("BAŞARILI");
                alertDialog.setMessage("Ürün Etiket Kuyruğuna Eklendi");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "TAMAM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                temizle();
            } else {
                if (dgr == -1) {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("BAĞLANTI HATASI");
                    alertDialog.setMessage("Pastel veritabanı ile bağlantı kurulamadı....");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "TAMAM",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    dgr = 0;
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("BAŞARISIZ");
                    alertDialog.setMessage("Etiket Yazdırma başarısız");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "TAMAM",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    dgr = 0;

                }
            }
        }
    }


    public class Guncelle extends AsyncTask<String, Void, Boolean> {

        int dgr = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MesajGoster("Değişiklikler Uygulanıyor...");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            dgr = 0;
            //Log.e("Başladı ", "--------------------------------------------");
            String s_barkod, s_stokKodu, s_stokAdi, s_fiyatS1, s_fiyatS2, s_fiyatS3, s_fiyatS4, s_fiyatS5;
            double s_prgBakiye, s_sayimBakiye;

            ArrayList<String> str_dizi = new ArrayList<String>();
            ArrayList<Double> dbl_dizi = new ArrayList<Double>();

            if (stokAdi.getText().toString().isEmpty()) s_stokAdi = null;
            else s_stokAdi = stokAdi.getText().toString();

            if (fiyatS1.getText().toString().isEmpty()) s_fiyatS1 = null;
            else s_fiyatS1 = (fiyatS1.getText().toString());
            if (fiyatS2.getText().toString().isEmpty()) s_fiyatS2 = null;
            else s_fiyatS2 = (fiyatS2.getText().toString());
            if (fiyatS3.getText().toString().isEmpty()) s_fiyatS3 = null;
            else s_fiyatS3 = (fiyatS3.getText().toString());
            if (fiyatS4.getText().toString().isEmpty()) s_fiyatS4 = null;
            else s_fiyatS4 = (fiyatS4.getText().toString());
            if (fiyatS5.getText().toString().isEmpty()) s_fiyatS5 = null;
            else s_fiyatS5 = (fiyatS5.getText().toString());

            //Log.e("s_fiyatS1",s_fiyatS1);


            s_barkod = barkod.getText().toString();
            s_stokKodu = stokKodu.getText().toString();

            s_prgBakiye = Double.parseDouble(prgBakiye.getText().toString().replace(",", "."));


            // Log.e("s_prgBakiye - s_sayimBakiye", String.valueOf(s_prgBakiye) + " - " + String.valueOf(s_sayimBakiye));

            bag = new Baglanti();

            boolean sonuc = true;
            boolean sorgu_sonucu_SQL_1 = false;
            boolean sorgu_sonucu_SQL_2 = false;
            boolean sorgu_sonucu_SQL_3 = false;
            boolean sorgu_sonucu_SQL_4 = false;
            boolean sorgu_sonucu_SQL_5 = false;

            try {
                if (!bag.Baglan(act)) {

                    dgr = -1;
                    return false;
                }
                bag.conn.setAutoCommit(false);
            } catch (Exception e) {
                dgr = -1;
                e.printStackTrace();
                return false;
            }

            //String SQL_5 = "UPDATE STOKKARTLARI SET SATISFIYATI="+ s_fiyatS1 +", SATISFIYATI2="+ s_fiyatS2 + ", SATISFIYATI3="+ s_fiyatS3 + ", SATISFIYATI4="+ s_fiyatS4 + ", SATISFIYATI5="+ s_fiyatS5 + " , STOKADI='"+ s_stokAdi + "'  WHERE STOKKODU='"+ s_stokKodu +"'";

            String SQL_5 = "UPDATE STOKKARTLARI SET " +
                    "SATISFIYATI=?, " +
                    "SATISFIYATI2=?, " +
                    "SATISFIYATI3=?, " +
                    "SATISFIYATI4=?, " +
                    "SATISFIYATI5=? , " +
                    "STOKADI=?  " +
                    "WHERE STOKKODU=?";

            try {
                dbl_dizi.add(Double.parseDouble(s_fiyatS1.replace(",", ".")));
            } catch (Exception e) {
                dbl_dizi.add(null);
                e.printStackTrace();
            }
            try {
                dbl_dizi.add(Double.parseDouble(s_fiyatS2.replace(",", ".")));
            } catch (Exception e) {
                dbl_dizi.add(null);
                e.printStackTrace();
            }
            try {
                dbl_dizi.add(Double.parseDouble(s_fiyatS3.replace(",", ".")));
            } catch (Exception e) {
                dbl_dizi.add(null);
                e.printStackTrace();
            }
            try {
                dbl_dizi.add(Double.parseDouble(s_fiyatS4.replace(",", ".")));
            } catch (Exception e) {
                dbl_dizi.add(null);
                e.printStackTrace();
            }
            try {
                dbl_dizi.add(Double.parseDouble(s_fiyatS5.replace(",", ".")));
            } catch (Exception e) {
                dbl_dizi.add(null);
                e.printStackTrace();
            }

            try {
                str_dizi.add(s_stokAdi);
            } catch (Exception e) {
                str_dizi.add(null);
                e.printStackTrace();
            }
            try {
                str_dizi.add(s_stokKodu);
            } catch (Exception e) {
                str_dizi.add(null);
                e.printStackTrace();
            }


            Log.e("SQL_5", SQL_5);
            if (sayimBakiye.getText().toString().isEmpty()) {

                // sorgu_sonucu_SQL_5=bag.Update(SQL_5);
                sorgu_sonucu_SQL_5 = bag.Update(SQL_5, str_dizi, dbl_dizi);

                if (sorgu_sonucu_SQL_5) {
                    try {
                        bag.conn.commit();
                    } catch (Exception e) {
                        try {
                            bag.conn.rollback();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                        return false;
                    }
                } else {
                    try {
                        bag.conn.rollback();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    return false;
                }
                return true;
            } else {

                s_sayimBakiye = Double.parseDouble(sayimBakiye.getText().toString().replace(",", "."));

            }

            if (s_prgBakiye > s_sayimBakiye) {
                String SQL_1 = "INSERT INTO STOKHAREKETI (STOKKODU, DEPOID, TARIH, MIKTAR, HAREKETTURU, F, KULLANICIID, BIRIMID, DAH, ISLEMTURU, ACIKLAMA) VALUES('" + s_stokKodu + "', " + depoID + ", '" + new java.sql.Date(Calendar.getInstance().getTime().getTime()) + "', " + String.valueOf(s_prgBakiye - s_sayimBakiye) + ", 'SATIŞ', 'FALSE','ELTERMİNALİ', 1, -1, 'DEPO HAREKETİ', 'Depo sayımındaki fazlalık atıldı')";
                String SQL_2 = "INSERT INTO STOKHAREKETI (STOKKODU, DEPOID, TARIH, MIKTAR, HAREKETTURU, F, KULLANICIID, BIRIMID, DAH, ISLEMTURU, ACIKLAMA) VALUES('" + s_stokKodu + "', -2, '" + new java.sql.Date(Calendar.getInstance().getTime().getTime()) + "', " + String.valueOf(s_prgBakiye - s_sayimBakiye) + ", 'ALIŞ', 'FALSE', 'ELTERMİNALİ', 1, -1, 'DEPO HAREKETİ', 'Depo sayımındaki fazlalık aktarıldı')";

                try {
                    sorgu_sonucu_SQL_1 = bag.Update(SQL_1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (sorgu_sonucu_SQL_1) try {
                    sorgu_sonucu_SQL_2 = bag.Update(SQL_2);
                } catch (Exception e) {
                    try {
                        bag.conn.rollback();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
                if (sorgu_sonucu_SQL_2) try {
                    //sorgu_sonucu_SQL_5=bag.Update(SQL_5);
                    sorgu_sonucu_SQL_5 = bag.Update(SQL_5, str_dizi, dbl_dizi);
                } catch (Exception e) {
                    try {
                        bag.conn.rollback();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
                if (sorgu_sonucu_SQL_1 & sorgu_sonucu_SQL_2 & sorgu_sonucu_SQL_5) {
                    try {
                        bag.conn.commit();
                    } catch (Exception e) {
                        try {
                            bag.conn.rollback();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                        return false;
                    }
                } else {
                    try {
                        bag.conn.rollback();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    return false;
                }

                /*
                Log.e("SQL_1: ", SQL_1);
                Log.e("SQL_2: ", SQL_2);
                Log.e("SQL_5: ", SQL_5);

                Log.e("SQL_1_sorgu_sonucu: ", String.valueOf(sorgu_sonucu_SQL_1));
                Log.e("SQL_2_sorgu_sonucu: ", String.valueOf(sorgu_sonucu_SQL_2));
                Log.e("SQL_5_sorgu_sonucu: ", String.valueOf(sorgu_sonucu_SQL_3));
*/
            }

            if (s_prgBakiye < s_sayimBakiye) {
                String SQL_3 = "INSERT INTO STOKHAREKETI (STOKKODU, DEPOID, TARIH, MIKTAR, HAREKETTURU, F, KULLANICIID, BIRIMID, DAH, ISLEMTURU, ACIKLAMA) VALUES('" + s_stokKodu + "', " + depoID + ", '" + new java.sql.Date(Calendar.getInstance().getTime().getTime()) + "', " + String.valueOf(s_sayimBakiye - s_prgBakiye) + ", 'ALIŞ','FALSE','ELTERMİNALİ',1,-1,'DEPO HAREKETİ','Depo sayımındaki eksiklik tamamlandı')";
                String SQL_4 = "INSERT INTO STOKHAREKETI (STOKKODU, DEPOID, TARIH, MIKTAR, HAREKETTURU, F, KULLANICIID, BIRIMID, DAH, ISLEMTURU, ACIKLAMA) VALUES('" + s_stokKodu + "', -1, '" + new java.sql.Date(Calendar.getInstance().getTime().getTime()) + "', " + String.valueOf(s_prgBakiye - s_sayimBakiye) + ", 'SATIŞ', 'FALSE', 'ELTERMİNALİ', 1, -1, 'DEPO HAREKETİ', 'Depo sayımındaki eksiklik tamamlandı')";

                try {
                    sorgu_sonucu_SQL_3 = bag.Update(SQL_3);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (sorgu_sonucu_SQL_3) try {
                    sorgu_sonucu_SQL_4 = bag.Update(SQL_4);
                } catch (Exception e) {
                    try {
                        bag.conn.rollback();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }

                if (sorgu_sonucu_SQL_4) try {
                    //sorgu_sonucu_SQL_5=bag.Update(SQL_5);
                    sorgu_sonucu_SQL_5 = bag.Update(SQL_5, str_dizi, dbl_dizi);
                } catch (Exception e) {
                    try {
                        bag.conn.rollback();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }

                if (sorgu_sonucu_SQL_3 & sorgu_sonucu_SQL_4 & sorgu_sonucu_SQL_5) {
                    try {
                        bag.conn.commit();
                    } catch (Exception e) {
                        try {
                            bag.conn.rollback();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                        return false;
                    }
                } else {
                    try {
                        bag.conn.rollback();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    return false;
                }

              /*  Log.e("SQL_3: ", SQL_3);
                Log.e("SQL_4: ", SQL_4);
                Log.e("SQL_5: ", SQL_5);

                Log.e("SQL_3_sorgu_sonucu: ", String.valueOf(sorgu_sonucu_SQL_3));
                Log.e("SQL_4_sorgu_sonucu: ", String.valueOf(sorgu_sonucu_SQL_4));
                Log.e("SQL_5_sorgu_sonucu: ", String.valueOf(sorgu_sonucu_SQL_5));
                */
            }

            // Log.e("Bitti ", "--------------------------------------------");

            return true;
        }

        @Override
        protected void onPostExecute(Boolean sonuc) {
            super.onPostExecute(sonuc);

            MesajGizle();

            if (sonuc) {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("BAŞARILI");
                alertDialog.setMessage("Güncelleme Başarılı");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "TAMAM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                temizle();
            } else {
                if (dgr == -1) {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("BAĞLANTI HATASI");
                    alertDialog.setMessage("Pastel veritabanı ile bağlantı kurulamadı....");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "TAMAM",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    dgr = 0;
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("BAŞARISIZ");
                    alertDialog.setMessage("Güncelleme başarısız");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "TAMAM",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    dgr = 0;

                }
            }
        }
    }

}

