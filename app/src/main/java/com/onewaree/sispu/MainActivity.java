package com.onewaree.sispu;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.onewaree.sispu.POJO.Ponto;
import com.onewaree.sispu.Fragments.ImportFragment;
import com.onewaree.sispu.Fragments.HomeFragment;
import com.onewaree.sispu.Fragments.FormFragment;

import com.onewaree.sispu.Gerenciador.ControlePontos;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    SupportMapFragment sMapfragment;
    GoogleMap map;
    ControlePontos controlePontos;

    FloatingActionButton add_marker;
    FloatingActionButton rmv_marker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sMapfragment = SupportMapFragment.newInstance().newInstance();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //OCULTAR BOTÃO REMOVE MARKER
        rmv_marker = (FloatingActionButton) findViewById(R.id.rmv_marker);
        rmv_marker.setVisibility(View.INVISIBLE);
        //OCULTAR BOTÃO ADD MARKER
        add_marker = (FloatingActionButton) findViewById(R.id.add_marker);
        add_marker.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();


        sMapfragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
       FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        int id = item.getItemId();

        //OCULTAR BOTÃO ADD_MARKER
        add_marker.setVisibility(View.INVISIBLE);

        //SE CLICAR EM OUTRO MENU, OCULTA O MAPA
        if(sMapfragment.isAdded())
            sFm.beginTransaction().hide(sMapfragment).commit();

        if (id == R.id.nav_home) {
            fm.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
        } else if (id == R.id.nav_mapa) {
            add_marker.setVisibility(View.VISIBLE);//MOSTRAR ADD_MARKER
            if(!sMapfragment.isAdded())
                sFm.beginTransaction().add(R.id.map, sMapfragment).commit();
            else
                sFm.beginTransaction().show(sMapfragment).commit();
        } else if (id == R.id.nav_import) {
            fm.beginTransaction().replace(R.id.content_frame, new ImportFragment()).commit();
        } else if (id == R.id.nav_form) {
            fm.beginTransaction().replace(R.id.content_frame, new FormFragment()).commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        //Disable Map Toolbar:
        map.getUiSettings().setMapToolbarEnabled(false);

        final LatLng center = new LatLng(-4.9702514, -39.0166022);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 12.0f));

        //MARCANDO OS PONTOS CADASTRADOS NO MAPA
        controlePontos = new ControlePontos();
        marcarPontos(controlePontos);

        // AO CLICLICAR EM ADICIONAR PONTO (+)
        add_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_marker.setVisibility(View.INVISIBLE);
                rmv_marker.setVisibility(View.VISIBLE);

                //AO CLICAR EM UM PONTO NO MAPA
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(final LatLng latLng) {
                        // Creating a marker
                        MarkerOptions markerOptions = new MarkerOptions();

                        // Setting the position for the marker
                        markerOptions.position(latLng);

                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                        // Clears the previously touched position
                        map.clear();

                        // Animating to the touched position
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));

                        // Placing a marker on the touched position
                        map.addMarker(markerOptions);

                        //ALERT DIALOG
                        LayoutInflater inflater = getLayoutInflater();
                        final View form_addMarker = inflater.inflate(R.layout.dialog_form, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Title");
                        builder.setView(form_addMarker);

                        // Set up the buttons
                        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String autor = ((EditText) form_addMarker.findViewById(R.id.addPnt_autor)).getText().toString();
                                String titulo = ((EditText) form_addMarker.findViewById(R.id.addPnt_titulo)).getText().toString();
                                String descricao = ((EditText) form_addMarker.findViewById(R.id.addPnt_descricao)).getText().toString();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                String currentDate = sdf.format(new Date());

                                controlePontos.addPonto(titulo, autor, descricao, latLng, currentDate);

                                add_marker.setVisibility(View.VISIBLE);
                                rmv_marker.setVisibility(View.INVISIBLE);

                                map.clear();
                                map.setOnMapClickListener(null);
                                marcarPontos(controlePontos);

                                dialog.dismiss();

                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 12.0f));
                                //m_Text = input.getText().toString();
                            }
                        });

                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                map.clear();
                                dialog.cancel();
                                marcarPontos(controlePontos);
                            }
                        });

                        builder.show();
                    }
                });

                rmv_marker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add_marker.setVisibility(View.VISIBLE);
                        rmv_marker.setVisibility(View.INVISIBLE);

                        map.clear();
                        map.setOnMapClickListener(null);
                        marcarPontos(controlePontos);
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 12.0f));
                    }
                });
            }
        });


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.hideInfoWindow();
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 12.0f));

                View aboutMarker = getLayoutInflater().inflate(R.layout.iw_about_marker, null);


                final PopupWindow pwindo = new PopupWindow(aboutMarker,480,250,true);
                pwindo.showAtLocation(aboutMarker, Gravity.CENTER, 0, -160);
                pwindo.setOutsideTouchable(true);

                Button bntCloseIW_AM = (Button) aboutMarker.findViewById(R.id.bntCloseIW_AM);

                bntCloseIW_AM.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        pwindo.dismiss();
                    }
                });

                Button bntSeeMoreIW_AM = (Button) aboutMarker.findViewById(R.id.seeMore);

                bntSeeMoreIW_AM.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        pwindo.dismiss();
                    }
                });




                return true;
            }

        });



        // Setting a custom info window adapter for the google map
        /*map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }


            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {

                // Getting view from the layout file info_window_layout
                View aboutMarker = getLayoutInflater().inflate(R.layout.iw_about_marker, null);

                // Getting the position from the marker
                LatLng latLng = marker.getPosition();

                Button bnt = (Button) aboutMarker.findViewById(R.id.seeMore);
                bnt.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        Log.d("Mensagem ", "Clicou em ver mais");
                    }
                });

                // Returning the view containing InfoWindow contents
                return aboutMarker;
            }
        });*/
    }

    public void marcarPontos(ControlePontos controlepontos){
        for(Ponto p: controlepontos.getPontos() ){
            map.addMarker(new MarkerOptions()
                    .position(p.getLatlng())
                    .title(p.getTitulo()));
        }
    }
}
/*AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle(autor);
                                alertDialog.setMessage("Alert message to be shown");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();*/
