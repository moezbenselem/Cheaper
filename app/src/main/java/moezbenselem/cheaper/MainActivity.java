package moezbenselem.cheaper;

import android.Manifest;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.*;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static int indice =1;
    TextWatcher txtwt;
    ArrayList<Product> listProduct;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    EditText etCode,etPrix;
    Button btScan,btSearch,btX,btAdd,bt_close;
    SurfaceView cameraView;
    CameraSource cameraSource;
    Boolean priceFound=false;
    CardView cardAdd;
    String l,city;
    static int RequestCameraPermissionID = 1001;
    BarcodeDetector barcodeDetector;
    SharedPreferences sharedPref;
    String defaultLang ;
    Spinner spinner;
    public static int state=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        defaultLang = sharedPref.getString("lang", "en");
        System.out.println("langue ===== "+defaultLang);
        Locale locale = new Locale(defaultLang);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(locale);
        } else{
            configuration.locale=locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration,displayMetrics);
        }


        setContentView(R.layout.activity_main);

        try {

            final ArrayList<String> list = new ArrayList<>();
            ArrayList<Integer> images = new ArrayList<>();
            list.add("English");
            list.add("Français");
            list.add("العربية");



            images.add(R.drawable.uk);
            images.add(R.drawable.fr);
            images.add(R.drawable.tn);
            ArrayAdapter<String> adapter = new SpinnerAdapter(getApplicationContext(), R.layout.spinner_layout,list,images);

            MobileAds.initialize(this, "ca-app-pub-7087198421941611~2097574177");
            AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            spinner = (Spinner) findViewById(R.id.spinner);

            spinner.setAdapter(adapter);
            if(defaultLang.equalsIgnoreCase("ar"))
                spinner.setSelection(2);
            if(defaultLang.equalsIgnoreCase("fr"))
                spinner.setSelection(1);
            if(defaultLang.equalsIgnoreCase("en"))
                spinner.setSelection(0);



            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                        if (position == 2 && sharedPref.getString("lang", "en").equalsIgnoreCase("ar")==false) {
                            System.out.println(2);
                            Locale locale = new Locale("ar");
                            Locale.setDefault(locale);
                            Resources resources = getResources();
                            Configuration configuration = resources.getConfiguration();
                            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                configuration.setLocale(locale);
                            } else {
                                configuration.locale = locale;
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                getApplicationContext().createConfigurationContext(configuration);
                            } else {
                                resources.updateConfiguration(configuration, displayMetrics);
                            }
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("lang", "ar");
                            editor.apply();

                            recreate();

                        }

                        if (position == 1 && sharedPref.getString("lang", "en").equalsIgnoreCase("fr")==false) {
                            System.out.println(1);
                            Locale locale = new Locale("fr");
                            Locale.setDefault(locale);
                            Resources resources = getResources();
                            Configuration configuration = resources.getConfiguration();
                            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                configuration.setLocale(locale);
                            } else {
                                configuration.locale = locale;
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                getApplicationContext().createConfigurationContext(configuration);
                            } else {
                                resources.updateConfiguration(configuration, displayMetrics);
                            }
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("lang", "fr");
                            editor.apply();
                            recreate();

                        }

                        if (position == 0 && sharedPref.getString("lang", "en").equalsIgnoreCase("en")==false) {
                            System.out.println(0);
                            Locale locale = new Locale("en");
                            Locale.setDefault(locale);
                            Resources resources = getResources();
                            Configuration configuration = resources.getConfiguration();
                            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                configuration.setLocale(locale);
                            } else {
                                configuration.locale = locale;
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                getApplicationContext().createConfigurationContext(configuration);
                            } else {
                                resources.updateConfiguration(configuration, displayMetrics);
                            }
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("lang", "en");
                            editor.apply();
                            recreate();

                        }

                    }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    /*System.out.println(-1);
                    Locale locale = new Locale("en");
                    Locale.setDefault(locale);
                    Resources resources = getResources();
                    Configuration configuration = resources.getConfiguration();
                    DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                        configuration.setLocale(locale);
                    } else{
                        configuration.locale=locale;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        getApplicationContext().createConfigurationContext(configuration);
                    } else {
                        resources.updateConfiguration(configuration,displayMetrics);
                    }
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("lang","en");
                    editor.apply();
                    recreate();
*/
                }
            });


            Toast.makeText(getApplicationContext(),"Created By BENSELEM MOEZ",Toast.LENGTH_LONG).show();

            recyclerView =
                    (RecyclerView)findViewById(R.id.recycler);



            layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);

            cameraView = (SurfaceView) findViewById(R.id.surfaceView);

            etPrix = (EditText) findViewById(R.id.etPrix);
            etCode = (EditText) findViewById(R.id.etCode);
            txtwt = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().length() == 0)
                        btSearch.setEnabled(false);
                    else if (s.toString().length() >= 4)
                        btSearch.setEnabled(true);
                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            };

            etCode.addTextChangedListener(txtwt);

            btScan = (Button) findViewById(R.id.bt_scan);
            btSearch = (Button) findViewById(R.id.bt_search);
            bt_close = (Button) findViewById(R.id.bt_close);
            btAdd = (Button) findViewById(R.id.bt_add);

            btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try{

                        cardAdd = (CardView) findViewById(R.id.includeAdd);
                        final EditText etCodea = (EditText) cardAdd.findViewById(R.id.etCodeDialog),
                                etLebelle = (EditText) cardAdd.findViewById(R.id.etLebelleDialog),
                                etPrixa = (EditText) cardAdd.findViewById(R.id.etPrixDialog),
                                etVille = (EditText) cardAdd.findViewById(R.id.etVilleDialog),
                                etMagasin = (EditText) cardAdd.findViewById(R.id.etMagasinDialog);

                        Button btValid = (Button) cardAdd.findViewById(R.id.bt_contact);
                        Button btAnnul = (Button) cardAdd.findViewById(R.id.dialog_bt_fav);


                        etCodea.setText(etCode.getText().toString());
                        etCodea.setEnabled(false);
                        etPrixa.setText(etPrix.getText().toString());
                        etPrixa.setEnabled(false);
                        etLebelle.setText(l);
                        etLebelle.setEnabled(false);

                        etMagasin.setText("");
                        etVille.setText("");

                        cardAdd.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);

                        btValid.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                addCode(etCode.getText().toString(),Double.parseDouble(etPrix.getText().toString()),etVille.getText().toString(),etMagasin.getText().toString(),l);
                                cardAdd.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                //etCode.setText("");
                                //etPrix.setText("");

                            }
                        });

                        btAnnul.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                cardAdd.setVisibility(View.INVISIBLE);
                                recyclerView.setVisibility(View.VISIBLE);
                                //etCode.setText("");
                                //etPrix.setText("");

                            }
                        });



                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                }
            });

            btSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sendCode(etCode.getText().toString(),etPrix.getText().toString());

                }
            });


            btScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        RequestCameraPermissionID);
                                return;
                            }
                            //cameraSource.start(cameraView.getHolder());


                        cameraView.setVisibility(View.VISIBLE);
                        bt_close.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        //btX.setVisibility(View.VISIBLE);
                        captureCode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void captureCode() {

        try {

            BarcodeDetector detector =
                    new BarcodeDetector.Builder(getApplicationContext())
                            .setBarcodeFormats(Barcode.ALL_FORMATS).build();
            if (!detector.isOperational()) {
                System.out.println("Could not set up the detector!");
                System.out.println("non functional !");
                return;
            } else if (detector.isOperational()) {
                System.out.println("functional !");

                cameraSource = new CameraSource.Builder(getApplicationContext(), detector)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        //.setRequestedPreviewSize(1280, 1024)
                        .setAutoFocusEnabled(true)
                        .build();
                cameraView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder surfaceHolder) {

                        try {
                            cameraSource.start(cameraView.getHolder());
                        } catch (SecurityException e) {
                            Toast.makeText(getApplicationContext(),"Permession Denied !",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }catch (IOException esp) {
                            esp.printStackTrace();
                        }

                        bt_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                cameraView.setVisibility(View.GONE);
                                bt_close.setVisibility(View.GONE);
                                cameraSource.stop();
                                recyclerView.setVisibility(View.VISIBLE);

                            }
                        });

                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                        cameraSource.stop();
                    }
                });

                detector.setProcessor(new Detector.Processor<Barcode>() {
                    @Override
                    public void release() {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {

                        final SparseArray<Barcode> r = detections.getDetectedItems();
                        if (r.size() > 0) {
                            System.out.println(r.valueAt(0).displayValue);
                            System.out.println("items sizeeeee ==== " + r.size());

                            Toast.makeText(getApplicationContext(), r.valueAt(0).displayValue, Toast.LENGTH_LONG).show();

                            btSearch.post(new Runnable() {
                                @Override
                                public void run() {

                                    //btX.setVisibility(View.GONE);
                                    cameraView.setVisibility(View.GONE);
                                    bt_close.setVisibility(View.GONE);
                                    cameraSource.stop();
                                    recyclerView.setVisibility(View.VISIBLE);
                                    etCode.setText(r.valueAt(0).displayValue);
                                    //Toast.makeText(getApplicationContext(), r.valueAt(0).displayValue, Toast.LENGTH_LONG).show();
                                    //textView.setText(r.valueAt(0).displayValue);
                                    //tvResult.setText(r.valueAt(0).displayValue);
                                }
                            });


                        } else
                            Toast.makeText(getApplicationContext(), "Cannot scan input !", Toast.LENGTH_SHORT).show();
                    }

                    CameraSource.PictureCallback mPictureCallback = new CameraSource.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] bytes) {

                            System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                            Bitmap mBitmap = BitmapFactory
                                    .decodeByteArray(bytes, 0, bytes.length);

                        }

                    };

                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendCode(final String code, final String prix){

        listProduct = new ArrayList<>();
        adapter = new RecyclerProduct(listProduct,getApplicationContext());
        recyclerView.setAdapter(adapter);
        priceFound=false;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://qcmtest.6te.net/cheaper/getProductData.php?code="+code+"&prix="+prix,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            //response = response.replaceAll("<div style=\"text-align: right;position: fixed;top: 5px;right:5px;width: 100%;z-index:999999;cursor: pointer;line-height: 0;display:block;\"><a target=\"_blank\" href=\"https://www.freewebhostingarea.com\" title=\"Free Web Hosting with php7\"><img alt=\"Free Web Hosting\" width=\"350\" height=\"25\" src=\"https://www.freewebhostingarea.com/images/poweredby.gif\" style=\"border-width: 0px;\"></a></div>","");
                            //response = response.replaceAll("<div style=\"text-align: right;position: fixed;bottom: 5px;right:5px;width: 100%;z-index:999999;cursor: pointer;line-height: 0;display:block;\"><a target=\"_blank\" href=\"https://www.freewebhostingarea.com\" title=\"Free Web Hosting with PHP7\"><img alt=\"Free Web Hosting\" width=\"350\" height=\"25\" src=\"https://www.freewebhostingarea.com/images/poweredby.gif\" style=\"border-width: 0px;\"></a></div>","");
                            //response = response.replaceAll("<div style=\"text-align: left;position: fixed;top: 5px;right:5px;width: 100%;z-index:999999;cursor: pointer;line-height: 0;display:block;\"><a target=\"_blank\" href=\"https://www.freewebhostingarea.com\" title=\"Free Web Hosting with php7\"><img alt=\"Free Web Hosting\" width=\"350\" height=\"25\" src=\"https://www.freewebhostingarea.com/images/poweredby.gif\" style=\"border-width: 0px;\"></a></div>","");
                            //response = response.replaceAll("<div style=\"text-align: left;position: fixed;bottom: 5px;right:5px;width: 100%;z-index:999999;cursor: pointer;line-height: 0;display:block;\"><a target=\"_blank\" href=\"https://www.freewebhostingarea.com\" title=\"Free Web Hosting with PHP7\"><img alt=\"Free Web Hosting\" width=\"350\" height=\"25\" src=\"https://www.freewebhostingarea.com/images/poweredby.gif\" style=\"border-width: 0px;\"></a></div>","");
                            if(response.indexOf("/div>")!=-1)
                                response = response.substring(response.lastIndexOf("/div>")+5);
                            System.out.println(response);
                            JSONObject jsonObject;
                            JSONArray json = new JSONArray(response);

                            System.out.println(json.toString());
                            System.out.println(indice);
                            indice++;

                            for (int i = 0;i<json.length();i++){

                                jsonObject = json.getJSONObject(i);

                                String  ville = jsonObject.getString("ville"),
                                        magasin = jsonObject.getString("magasin"),
                                        lebelle = jsonObject.getString("libelle");
                                        l = lebelle;
                                Double  price = jsonObject.getDouble("prix");

                                Product p  = new Product(ville,magasin,lebelle,price);
                                listProduct.add(p);
                                if (Double.parseDouble(etPrix.getText().toString())==price){

                                    priceFound = true;

                                }

                            }
                            CardView cardAdd = (CardView) findViewById(R.id.includeAdd);
                            cardAdd.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter = new RecyclerProduct(listProduct,getApplicationContext());
                            recyclerView.setAdapter(adapter);
                            if (priceFound == false){

                                btAdd.setVisibility(View.VISIBLE);
                                btAdd.setEnabled(true);
                            }else{
                                btAdd.setVisibility(View.INVISIBLE);
                                btAdd.setEnabled(false);
                            }



                        } catch (Exception e) {
                            //e.printStackTrace();
                            System.out.println("NO CHEAPER PRODUCT FOUND !");
                            try{

                                cardAdd = (CardView) findViewById(R.id.includeAdd);
                                final EditText etCode = (EditText) cardAdd.findViewById(R.id.etCodeDialog),
                                        etLebelle = (EditText) cardAdd.findViewById(R.id.etLebelleDialog),
                                        etPrix = (EditText) cardAdd.findViewById(R.id.etPrixDialog),
                                        etVille = (EditText) cardAdd.findViewById(R.id.etVilleDialog),
                                        etMagasin = (EditText) cardAdd.findViewById(R.id.etMagasinDialog);

                                Button btValid = (Button) cardAdd.findViewById(R.id.bt_contact);
                                Button btAnnul = (Button) cardAdd.findViewById(R.id.dialog_bt_fav);

                                etCode.setText(code);
                                etPrix.setText(prix);
                                etPrix.setEnabled(true);
                                etLebelle.setText("");
                                etLebelle.setEnabled(true);
                                etMagasin.setText("");
                                etVille.setText("");

                                cardAdd.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);

                                btValid.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        addCode(code,Double.parseDouble(etPrix.getText().toString()),etVille.getText().toString(),etMagasin.getText().toString(),etLebelle.getText().toString());
                                        cardAdd.setVisibility(View.INVISIBLE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        //etCode.setText("");
                                        //etPrix.setText("");

                                    }
                                });

                                btAnnul.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        cardAdd.setVisibility(View.INVISIBLE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        //etCode.setText("");
                                        //etPrix.setText("");

                                    }
                                });



                            }catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }


                        }


                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        }) {



            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<>();

                params.put("code",code);
                params.put("prix",prix);

                return params;
            }

            /**
             * Passing some request headers
             */

        };


        {
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }


    public void addCode(final String code, final Double prix, final String ville, final String magasin, final String nom){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://qcmtest.6te.net/cheaper/addProduct.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            response = response.replaceAll("<div style=\"text-align: right;position: fixed;top: 5px;right:5px;width: 100%;z-index:999999;cursor: pointer;line-height: 0;display:block;\"><a target=\"_blank\" href=\"https://www.freewebhostingarea.com\" title=\"Free Web Hosting with php7\"><img alt=\"Free Web Hosting\" width=\"350\" height=\"25\" src=\"https://www.freewebhostingarea.com/images/poweredby.gif\" style=\"border-width: 0px;\"></a></div>","");

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("NO CHEAPER PRODUCT FOUND !");



                        }


                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        }) {



            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<>();

                params.put("code",code);
                params.put("ville",ville);
                params.put("prix",String.valueOf(prix));
                params.put("magasin",magasin);
                params.put("lebelle",nom);

                return params;
            }

            /**
             * Passing some request headers
             */

        };


        {
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }


}




