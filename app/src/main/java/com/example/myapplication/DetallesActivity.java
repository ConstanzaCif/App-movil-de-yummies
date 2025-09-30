package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.adapter.DetallePedidoAdapter;
import com.example.myapplication.data.viewmodel.DetallePedidoViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetallesActivity extends AppCompatActivity {
    ListView lstViewDetalles;
    DetallePedidoAdapter adapter;
    DetallePedidoViewModel viewModel;
    int idPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lstViewDetalles = findViewById(R.id.listViewDetallePedido);

        adapter = new DetallePedidoAdapter(this, new ArrayList<>());
        lstViewDetalles.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(DetallePedidoViewModel.class);

        // Recuperamos el ID del pedido enviado desde PedidosActivity
        idPedido = getIntent().getIntExtra("ID_PEDIDO", -1);

        // Observamos los detalles del pedido
        viewModel.getDetallesByPedido(idPedido).observe(this, detalles -> {
            adapter.listaDetalles = detalles;
            adapter.notifyDataSetChanged();
        });
    }
}