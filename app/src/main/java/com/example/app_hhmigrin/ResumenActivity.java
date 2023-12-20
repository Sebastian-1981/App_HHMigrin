package com.example.app_hhmigrin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.widget.Chronometer;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class ResumenActivity extends AppCompatActivity {

    // variable privada para Detectar ubicacion
    private LocationManager ubicacion;
    String splantas, ubi, u;

    Button btnLocalizacion, btnIrCrono;
    TextView txtLatitud, txtUsuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);


        txtLatitud=(TextView) findViewById(R.id.txtLatitud);
        txtUsuario=(TextView) findViewById(R.id.txtUser);
        btnLocalizacion=(Button) findViewById(R.id.btnLocalizacion);
        btnIrCrono=(Button) findViewById(R.id.btnActiCrono);

        Intent i= getIntent();
        u=i.getStringExtra("Usuario");
        txtUsuario.setText(u);


        btnLocalizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager=(LocationManager) ResumenActivity.this.getSystemService(Context.LOCATION_SERVICE);
                LocationListener locationListener= new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        txtLatitud.setText("Latitud: "+" "+location.getLatitude()+"  "+"Longitud: "+" "+location.getLongitude());

                    }
                    public void onStatusChanged(String provider,int status,Bundle extras){}
                    public void onProviderEnable(String provider){}
                    public void onProviderDisable(String provider){}
                };
                int permissionCheck= ContextCompat.checkSelfPermission( ResumenActivity.this,android.Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);

                if(txtLatitud.equals("")){

                }else{
                    showAfterDelay3(getApplicationContext(),2800);
                }
            }


        });





        btnIrCrono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            envioDat();
            }

            public void envioDat() {
                String dato= (String) txtLatitud.getText();
                AlertDialog.Builder env = new AlertDialog.Builder(ResumenActivity.this);
                env.setTitle("¡INFORMACION DE INGRESO!");
                env.setMessage("¿Esta seguro de enviar los siguientes datos? Usted esta en: "+splantas+", "+ "Cordenadas: "+dato);
                env.setPositiveButton("SI", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                        ubi= (String) txtLatitud.getText();

                        Intent a;
                        a = new Intent(getApplicationContext(), CronoActivity.class);
                        a.putExtra("Usuario",u);
                        a.putExtra("Planta", splantas);
                        a.putExtra("Ubicacion", ubi);

                        startActivity(a);

                    }
                });

                env.setNeutralButton("CANCELAR", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                });

               env.show();
            }

        });




        int permissionCheck= ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck==PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{

                ActivityCompat.requestPermissions( this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                },1);
            }
        }



    // Arreglo para selecionar plantas.
        final String[] plantas = {"PLANTA EL TURCO", "PLANTA EL PERAL", "PLANTA LAS PIEDRAS"};
        Spinner spinner = findViewById(R.id.spinner);
        if (spinner != null) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, plantas);
            spinner.setAdapter(arrayAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   // Toast.makeText(ResumenActivity.this, getString(R.string.selected_item) + " " + plantas[position], Toast.LENGTH_SHORT).show();
                    splantas = plantas[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }




    }

    private void showAfterDelay3(Context context, int milliseconds){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnIrCrono.setVisibility(View.VISIBLE);
            }

        }, milliseconds);
    }





}



