package com.example.app_hhmigrin;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ClearCacheRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CronoActivity extends AppCompatActivity {
    TextView timerText;
    Button stopStartButton, btnEnviar ;
    String hora,minutos,segundos,u,ubi,planta,correo, asunto,mensaje, myTag,lati,longi;
    int dia,mes,ano;
    StringRequest stringRequest2;
    RequestQueue requestQueue2;
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;

    boolean timerStarted = false;

   @Override
    protected void onStop() {
        super.onStop();
        if(stringRequest2!=null){
            requestQueue2.cancelAll(myTag);
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crono);


        timerText = (TextView) findViewById(R.id.timerText);
        stopStartButton = (Button) findViewById(R.id.startStopButton);
        btnEnviar = findViewById(R.id.btnEnviar);

        timer = new Timer();
        
        Time Time = null;
        Time today=new Time(Time.getCurrentTimezone());
        /*Optenemos la fecha actual */
        today.setToNow();
        /*Creamos variables int y llamamos a los atributos de la clase time*/
        dia=today.monthDay;
        mes=today.month;
        ano=today.year;

        Intent i= getIntent();
        u=i.getStringExtra("Usuario");
        ubi=i.getStringExtra("Ubicacion");
        planta=i.getStringExtra("Planta");




        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //onStop();

            enviarDatos();

            }

           public void enviarDatos() {

                correo ="sureta@migrin.cl";
                asunto = "Horas Extras: "+u+" "+dia+"/"+mes+"/"+ano;
                mensaje = " Sr (a): "+u+ "\n"+"Ubicación: "+ubi+ ""+ " \n"+
                        "Planta: "+planta+ "\n"+ " Horas Extras: "+hora+":"+minutos+":"+segundos+".";


                AlertDialog.Builder enviardatos = new AlertDialog.Builder(CronoActivity.this);
                enviardatos.setTitle("Enviar Datos");
                enviardatos.setMessage("¿Desea enviar informacion sobre las horas trabajadas, este proceso no podra modificarse nuevamente?");
                enviardatos.setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                        get();
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailito"));
                        intent.putExtra(Intent.EXTRA_EMAIL,
                                new String[]{correo});
                        intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
                        intent.putExtra(Intent.EXTRA_TEXT, mensaje);
                       intent.setType("message/rfc822");
                        finishAffinity();
                       startActivity(Intent.createChooser(intent, "Elije un cliente de correo:"));
                        showAfterDelay4(getApplicationContext(),600);

                    }
                });

                enviardatos.setNeutralButton("CANCELAR", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                });

                enviardatos.show();

            }


        });


    }

        public void resetTapped(View view)
        {
            AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
            resetAlert.setTitle("Reset Tiempo");
            resetAlert.setMessage("¿Desea restaurar temporizador?");
            resetAlert.setPositiveButton("SI", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    if(timerTask != null)
                    {
                        timerTask.cancel();
                        setButtonUI("INICIAR", R.color.black);
                        time = 0.0;
                        timerStarted = false;
                        timerText.setText(formatTime(0,0,0));

                    }
                }
            });

            resetAlert.setNeutralButton("CANCELAR", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    //do nothing
                }
            });

            resetAlert.show();

        }

        public void startStopTapped(View view)
        {
            if(timerStarted == false)
            {
                timerStarted = true;
                setButtonUI("PARAR", R.color.white);
                stopStartButton.setBackgroundColor(Color.RED);

                startTimer();
            }
            else
            {
                timerStarted = false;
                setButtonUI("INICIAR", R.color.black);
                stopStartButton.setBackgroundColor(Color.GREEN);

                timerTask.cancel();
            }
        }

        private void setButtonUI(String start, int color)
        {
            stopStartButton.setText(start);
            stopStartButton.setTextColor(ContextCompat.getColor(this, color));
        }

        private void startTimer()
        {
            timerTask = new TimerTask()
            {
                @Override
                public void run()
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            time++;
                            timerText.setText(getTimerText());
                        }
                    });
                }

            };
            timer.scheduleAtFixedRate(timerTask, 0 ,1000);
        }


        private String getTimerText()
        {
            int rounded = (int) Math.round(time);

            int seconds = ((rounded % 86400) % 3600) % 60;
            int minutes = ((rounded % 86400) % 3600) / 60;
            int hours = ((rounded % 86400) / 3600);

            hora=Integer.toString(hours);
            minutos=Integer.toString(minutes);
            segundos=Integer.toString(seconds);

            return formatTime(seconds, minutes, hours);
        }

        private String formatTime(int seconds, int minutes, int hours)
        {
            return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
        }





    public void get () {

        String url2= "https://script.google.com/macros/s/AKfycbzWuPwo-WGdIkf_Uz45XZz9tHsV6meCV_mFwAzG2hRVh21uY1MNhN2Y64gHFtIcI0Fe/exec?";
        url2= url2+"vLlave=1"+"&rut="+u+"&planta="+planta+"&fecha="+dia+"/"+mes+"/"+ano+"&latitud="+ubi+"&tiempo="+hora+":"+minutos+":"+segundos;
        stringRequest2=new StringRequest(Request.Method.GET,url2,new Response.Listener<String>(){


            @Override
            public void onResponse(String response) {

                if(response.equals("REGISTRADO")){
                    Toast.makeText(CronoActivity.this,response,Toast.LENGTH_LONG).show();



                }else {
                    Toast.makeText(CronoActivity.this,"Usuario",Toast.LENGTH_LONG).show();

                }



            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CronoActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                // Toast.makeText(InicioActivity.this,"Usuario No Registrado",Toast.LENGTH_LONG).show();

            }
        });


         requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest2);




    }

    public void showAfterDelay4(Context context, int milliseconds){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



            }

        }, milliseconds);
    }


    }
