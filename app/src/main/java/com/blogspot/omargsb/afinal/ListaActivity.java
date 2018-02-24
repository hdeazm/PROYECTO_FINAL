package com.blogspot.omargsb.afinal;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class ListaActivity extends AppCompatActivity {

    private ListView lv_alumnos_list;
    private ArrayAdapter adapter;
    private String getAllAlumnosURL = "https://evafinal.herokuapp.com/api_alumnos?user_hash=12345&action=get";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        lv_alumnos_list = (ListView)findViewById(R.id.lv_contacts_list1);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        webServiceRest(getAllAlumnosURL);
        lv_alumnos_list.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        for(int i=0;i<jsonArray.length();i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                id_alumno = jsonObject.getString("id_alumno");
                nom_alum = jsonObject.getString("nom_alum");
                telefono_alum = jsonObject.getString("telefono_alum");
                telefono_tutor = jsonObject.getString("telefono_tutor");
                direccion = jsonObject.getString("direccion");
                adapter.add(id_alumno + " : " + nom_alum);
                Log.i("nom_alum",  "nom_alum");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
