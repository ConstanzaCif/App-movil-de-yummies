package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.data.viewmodel.TiendaViewModel;

public class TiendaActivity extends BaseActivity {

    TextView txtNombre;
    TextView txtCodigo;
    TextView txtDireccion;
    TextView txtEncargado;

    TextView txtTelefono;
    TiendaViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setupDrawer(R.layout.activity_tienda);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtNombre = findViewById(R.id.txtTituloTienda);
        txtCodigo = findViewById(R.id.txtCodTienda2);
        txtDireccion = findViewById(R.id.txtDireccionTienda2);
        txtEncargado = findViewById(R.id.txtEncargadoTienda2);
        txtTelefono = findViewById(R.id.txtTelefonoTienda2);
        viewModel = new ViewModelProvider(this).get(TiendaViewModel.class);
        int id = getIntent().getIntExtra("tienda", -1);

        viewModel.getTienda(id).observe(this, tienda -> {
            txtNombre.setText(tienda.getNombre_tienda());
            txtCodigo.setText("Código: "+ tienda.getId_tiendas());
            txtTelefono.setText("Teléfono: "+tienda.getTelefono());
            txtDireccion.setText("Dirección: "+tienda.getDireccion());
            txtEncargado.setText("Encargado: "+tienda.getNombre_encargado());
        });
    }


}