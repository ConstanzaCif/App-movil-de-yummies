package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;


import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    onBackPressed();
                }
            }
        });
    }
    protected void setupDrawer(int layoutResID) {
        setContentView(R.layout.activity_base);

        FrameLayout frame = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, frame, true);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_ver_tiendas) {
            startActivity(new Intent(this, TiendasActivity.class));
        }
        else if (id == R.id.nav_pre_ventas) {
            startActivity(new Intent(this, PedidosActivity.class));
        }
        else if(id == R.id.nav_ver_productos){
            startActivity(new Intent(this, ProductosActivity.class));
        }
        else if(id == R.id.nav_mi_cuenta){
            startActivity(new Intent(this, CuentaActivity.class));
        }
//        if (id == R.id.nav_mi_cuenta) {
//            startActivity(new Intent(this, MiCuentaActivity.class));
//        } else if (id == R.id.nav_pre_ventas) {
//            startActivity(new Intent(this, PreVentasActivity.class));
//        } else if (id == R.id.nav_ver_tiendas) {
//            startActivity(new Intent(this, VerTiendasActivity.class));
//        } else if (id == R.id.nav_ver_productos) {
//            startActivity(new Intent(this, VerProductosActivity.class));
//        }

//        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



}