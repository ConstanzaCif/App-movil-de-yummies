package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.data.clases.SessionManager;
import com.example.myapplication.data.clases.Usuario;
import com.example.myapplication.data.dto.UsuarioDTO;
import com.example.myapplication.data.repositories.UsuarioRepository;
import com.example.myapplication.data.viewmodel.PedidoViewModel;
import com.example.myapplication.data.viewmodel.ProductoViewModel;
import com.example.myapplication.data.viewmodel.TiendaViewModel;
import com.example.myapplication.data.viewmodel.UsuarioViewModel;

public class MainActivity extends AppCompatActivity {
    Button btnInicio;
    EditText txtUsuario;
    EditText txtPassword;

    SessionManager sessionManager;

    UsuarioViewModel viewModel;
    UsuarioDTO usuarioDTO;

    TiendaViewModel tiendaViewModel;
    ProductoViewModel productoViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        PedidoViewModel pedidoViewModel = new ViewModelProvider(this).get(PedidoViewModel.class);
        pedidoViewModel.enviarPendientes();

        btnInicio = findViewById(R.id.btnInicio);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPassword);
        usuarioDTO = new UsuarioDTO();


        sessionManager = new SessionManager(this);

        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        tiendaViewModel = new ViewModelProvider(this).get(TiendaViewModel.class);
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);


        Log.d("TEST", "MainActivity iniciado");

        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = txtUsuario.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if(usuario.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Ingrese su usuario y contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.login(usuario, password).observe(MainActivity.this, user -> {
                    if (user != null)
                    {
                        Toast.makeText(MainActivity.this, "Bienvenido " +user.getNombre(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, InicioActivity.class);
                        startActivity(intent);
                        Log.d("Login", "El usuario ha iniciado sesion: " +sessionManager.getUsuario().getUsename());
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


//        String _tiendas;
//        lista = findViewById(R.id.lista);
//        TiendaAdapter adapter = new TiendaAdapter(this, new ArrayList<>());
//        lista.setAdapter(adapter);
//
//        viewModel = new ViewModelProvider(this).get(TiendaViewModel.class);
//
//
//
//        viewModel.getTodasTiendas().observe(this, tiendas -> {
//            adapter.listaTiendas = tiendas;
//            adapter.notifyDataSetChanged();
//        });
//
//        viewModel.sincronizarTiendas();
    }
}
