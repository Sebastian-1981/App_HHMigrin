package com.example.app_hhmigrin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class InicioActivity extends AppCompatActivity {

    ProgressBar pb;
    TextView txt1,txt2,txt3;
    EditText iRut;
    Button btnIngreso;
    String info, myTag;
    StringRequest stringRequest;
    RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        pb= findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);
        Inicio();

    }

    public void Inicio() {
        btnIngreso = findViewById(R.id.btnIngreso);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        iRut = findViewById(R.id.etxtRut);
        showAfterDelay1(getApplicationContext(),2500);

    }

        public void showAfterDelay1(Context context, int milliseconds){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txt1.setVisibility(View.VISIBLE);
                txt2.setVisibility(View.VISIBLE);
                txt3.setVisibility(View.VISIBLE);
                btnIngreso.setVisibility(View.VISIBLE);
                iRut.setVisibility(View.VISIBLE);



                btnIngreso.setOnClickListener(new View.OnClickListener() {
                    @Override
                   public void onClick(View view) {
                        pb.setVisibility(View.VISIBLE);
                        showAfterDelay2(getApplicationContext(),1000);
                   }

                });
            }

        }, milliseconds);
    }



    public void login() {
        String Arut= iRut.getText().toString();
        String url= "https://script.google.com/macros/s/AKfycbwCyvJ_gySYB9Hy08zPcUTkO76r5e_ngMbI5B1ra38Q9qH-CqRQCtPfeFFzrl3zJolK/exec?";
        url= url+"rut="+Arut;
        stringRequest=new StringRequest(Request.Method.GET,url,new Response.Listener<String>(){


            @Override
            public void onResponse(String response) {

                if(response.equals("Bienvenido")){
                    Toast.makeText(InicioActivity.this,"¡Bienvenido a Ingreso de horas extras Migrin!",Toast.LENGTH_SHORT).show();
                    String usuario=Arut;
                    Intent i;
                    i = new Intent(getApplicationContext(), ResumenActivity.class);
                    i.putExtra("Usuario",usuario);
                    startActivity(i);
                    close();

                }else {
                    Toast.makeText(InicioActivity.this,"Usuario No Registrado",Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.INVISIBLE);
                    iRut.setText("");            }



            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InicioActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });


        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);




    }
    public void showAfterDelay2(Context context,int milliseconds){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                login();

            }
        }, milliseconds);

    }


    public  void close(){
        if(stringRequest!=null){
            requestQueue.cancelAll(myTag);
        }

    }

}

