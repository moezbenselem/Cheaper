package moezbenselem.cheaper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import moezbenselem.cheaper.R;

/**
 * Created by Moez on 24/05/2018.
 */

public class CustomDialog extends Dialog  {

    public Context c;
    public Dialog d;

    public Button contacter, fav;
    Double prix;
    String code;


    public CustomDialog(Context act, String code , Double prix) {
        super(act);
        this.c = act;
        this.code = code;
        this.prix = prix;



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_layout);



        contacter = (Button) findViewById(R.id.bt_contact);
        contacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText    etVille = (EditText) findViewById(R.id.etVilleDialog),
                        etMagasin = (EditText) findViewById(R.id.etMagasinDialog),
                        etLebelle = (EditText) findViewById(R.id.etLebelle);

                String  ville = etVille.getText().toString(),
                        magasin = etMagasin.getText().toString(),
                        lebelle = etLebelle.getText().toString();
                sendCode(code,prix,ville,magasin,lebelle);

            }
        });
        fav = (Button) findViewById(R.id.dialog_bt_fav);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomDialog.this.dismiss();
            }
        });

    }

    public void sendCode(String code, Double prix,String ville,String magasin,String lebelle){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.137.1/addProduct.php?code="+code+"&prix="+prix+"&lebelle="+lebelle+"&magasin="+magasin+"&ville="+ville,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {



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
            RequestQueue requestQueue = Volley.newRequestQueue(c);
            requestQueue.add(stringRequest);
        }
    }

}
