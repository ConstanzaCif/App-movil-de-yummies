package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.data.clases.SessionManager;
import com.example.myapplication.data.clases.Usuario;

public class CuentaActivity extends BaseActivity {

    SessionManager sessionManager;
    TextView txtUser;
    TextView txtNombre;

    Button btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setupDrawer(R.layout.activity_cuenta);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = new SessionManager(this);
        txtUser = findViewById(R.id.txtUsername2);
        txtNombre = findViewById(R.id.txtNombre2);
        btnCerrarSesion = findViewById(R.id.btnLogout);
        Usuario usuario = new Usuario();

        usuario = sessionManager.getUsuario();
        Log.d("Usaurio", "Usuario en mi sesion "+ usuario.getUsename());

        txtUser.setText("Usuario: "+ usuario.getUsename());
        txtNombre.setText("Empleado: "+ usuario.getNombre()+" "+usuario.getApellido());

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.cerrarSesion();
                startActivity(new Intent(CuentaActivity.this, MainActivity.class));
            }
        });

    }
}