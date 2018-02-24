package com.blogspot.omargsb.afinal;



import android.app.AlertDialog;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;


public class BuscarActivity extends AppCompatActivity {

    private String URL = "https://evafinal.herokuapp.com/api_alumnos?user_hash=12345&action=get&";
    private String getProductosURL = "";
    private String queryParams = "";
    private Button btn_buscar;
    private EditText et_id_alumno;
    private TextView tv_nom_alum;
    private TextView tv_telefono_alum;
    private TextView tv_telefono_tutor;
    private TextView tv_direccion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        et_id_alumno = (EditText) findViewById(R.id.et_id_alumno);
        tv_nom_alum = (TextView) findViewById(R.id.tv_nom_alum);
        tv_telefono_alum = (TextView) findViewById(R.id.tv_telefono_alum);
        tv_telefono_tutor = (TextView) findViewById(R.id.tv_telefono_tutor);
        tv_direccion = (TextView) findViewById(R.id.tv_direccion);

        btn_buscar = (Button) findViewById(R.id.btn_buscar);
        btn_buscar.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btn_buscar)
                btn_buscar_onClick();
        }
    };


    private void btn_buscar_onClick() {
        String id_alumno = et_id_alumno.getText().toString();
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("id_alumno", id_alumno);
        queryParams = builder.build().getEncodedQuery();
        getProductosURL = URL;
        getProductosURL += queryParams;
        Log.d("Parametros", queryParams);
        Log.d("Consulta", getProductosURL);
        webServiceRest(getProductosURL);
    }

    private void webServiceRest(String requestURL){
        try{
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            String webServiceResult="";
            while ((line = bufferedReader.readLine()) != null){
                webServiceResult += line;
            }
            bufferedReader.close();
            parseInformation(webServiceResult);
            Log.d("datos", webServiceResult);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void parseInformation(String jsonResult){
        JSONArray jsonArray = null;
        String id_alumno;
        String nom_alum;
        String telefono_alum;
        String telefono_tutor;
        String direccion;
        try{
            jsonArray = new JSONArray(jsonResult);
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (jsonArray != null){
            Log.d("jsonArray ",""+jsonArray.length());
            for(int i=0;i<jsonArray.length();i++){
                try{
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    id_alumno = jsonObject.getString("id_alumno");
                    nom_alum = jsonObject.getString("nom_alum");
                    telefono_alum = jsonObject.getString("telefono_alum");
                    telefono_tutor = jsonObject.getString("telefono_tutor");
                    direccion = jsonObject.getString("direccion");

                    tv_nom_alum.setText(nom_alum);
                    tv_telefono_alum.setText(telefono_alum);
                    tv_telefono_tutor.setText(telefono_tutor);
                    tv_direccion.setText(direccion);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }else{
            tv_nom_alum.setText("No encontrado");
            tv_telefono_alum.setText("No encontrado");
            tv_telefono_tutor.setText("No encontrado");
            tv_direccion.setText("No encontrado");
            Message("Error","Registro no encontrado");
        }
    }

    private void Message(String title, String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.show();
    }
}