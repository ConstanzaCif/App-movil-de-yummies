package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.adapter.PedidoAdapter;
import com.example.myapplication.data.clases.SessionManager;
import com.example.myapplication.data.viewmodel.PedidoViewModel;

import java.util.ArrayList;

public class PedidosActivity extends BaseActivity {
    ListView lstViewPedidos;
    PedidoViewModel pedidoViewModel;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setupDrawer(R.layout.activity_pedidos);
        sessionManager = new SessionManager(this);
        Log.d("Usuario", "logueado "+sessionManager.getUsuario().getId_usuarios());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        int usuario = sessionManager.getUsuario().getId_usuarios();
        lstViewPedidos = findViewById(R.id.listViewPedidos);
        PedidoAdapter pedidoAdapter = new PedidoAdapter(this, new ArrayList<>());
        lstViewPedidos.setAdapter(pedidoAdapter);
        pedidoViewModel = new ViewModelProvider(this).get(PedidoViewModel.class);
    
        pedidoViewModel.getAllPedidos().observe(this, pedidos -> {
            pedidoAdapter.listaPedidos = pedidos;
            pedidoAdapter.notifyDataSetChanged();
        });
        pedidoViewModel.sincronizarPedidos(usuario);




    }

}