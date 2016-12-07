package com.onewaree.sispu;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.onewaree.sispu.Fragments.NoConnectionFragment;
import com.onewaree.sispu.Gerenciador.ControleComentarios;
import com.onewaree.sispu.POJO.Comentario;
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
    ControleComentarios controleComentarios;

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


        //if(hasActiveInternetConnection(this)){
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
        /*}else{
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame, new NoConnectionFragment()).commit();
        }*/
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
        marcarPontos();

        controleComentarios = new ControleComentarios();
        getComentarios();

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
                                marcarPontos();

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
                                marcarPontos();
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
                        marcarPontos();
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

                final Ponto p = controlePontos.getPonto(marker.getPosition());
                TextView tv_titulo = (TextView) aboutMarker.findViewById(R.id.seeMore_titulo);
                tv_titulo.setText(p.getTitulo());


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

                        //ALERT DIALOG
                        LayoutInflater inflater = getLayoutInflater();
                        final View see_about_point = inflater.inflate(R.layout.dialog_about_marker, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(p.getTitulo());
                        builder.setView(see_about_point);

                        final TextView tv_autor = (TextView) see_about_point.findViewById(R.id.tv_autor);
                        tv_autor.setText(p.getAutor());

                        TextView tv_data = (TextView) see_about_point.findViewById(R.id.tv_data);
                        tv_data.setText(p.getData());

                        TextView tv_descricao = (TextView) see_about_point.findViewById(R.id.tv_descricao);
                        tv_descricao.setText(p.getDescricao());


                        builder.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });


                        builder.show();

                        final LinearLayout llAboutMarker = (LinearLayout) see_about_point.findViewById(R.id.llAboutMarker);
                        Button bntAddComentario = (Button) llAboutMarker.findViewById(R.id.buttonAddComment);
                        final LinearLayout content_coments = new LinearLayout(MainActivity.this);

                        showComents(content_coments, p);

                        bntAddComentario.setOnClickListener(new View.OnClickListener()
                        {
                            public void onClick(View v){
                                EditText nomeUser = (EditText) llAboutMarker.findViewById(R.id.nomeUser);
                                EditText comentUser = (EditText) llAboutMarker.findViewById(R.id.comentarioUser);
                                TextView textView1 = new TextView(MainActivity.this);
                                textView1.setWidth(tv_autor.getWidth());
                                textView1.setText(nomeUser.getText());
                                TextView textView2 = new TextView(MainActivity.this);
                                textView2.setWidth(tv_autor.getWidth());
                                textView2.setText(comentUser.getText());
                                content_coments.addView(textView1);
                                content_coments.addView(textView2);

                                Comentario novo = new Comentario();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                String currentDate = sdf.format(new Date());
                                novo.setData(currentDate);
                                novo.setMensagem(comentUser.getText().toString());
                                novo.setAutor(nomeUser.getText().toString());
                                novo.setCodDemanda(p.getCodigo());

                                controleComentarios.addComentario(novo);
                            }
                        });
                        llAboutMarker.addView(content_coments);
                    }
                });

                return true;
            }

        });
    }

    public void marcarPontos(){
        for(Ponto p: controlePontos.getPontos() ){
            map.addMarker(new MarkerOptions()
                    .position(p.getLatlng())
                    .title(p.getTitulo()));
        }
    }

    public void getComentarios(){
        for(Comentario c : controleComentarios.getComentarios()){
            Ponto p = controlePontos.getPontoByID(c.getCodDemanda());
            p.addComentario(c);
        }
    }

    public void showComents(LinearLayout content_coments, Ponto p){
        for(Comentario c : p.getComentarios()){
            TextView textView1 = new TextView(MainActivity.this);
            textView1.setText("Autor: " + c.getAutor());
            TextView textView2 = new TextView(MainActivity.this);
            textView2.setText("Comentario: " + c.getMensagem());
            content_coments.addView(textView1);
            content_coments.addView(textView2);
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
