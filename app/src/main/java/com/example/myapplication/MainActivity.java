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

import retrofit2.Call;

import com.example.myapplication.adapter.TiendaAdapter;
import com.example.myapplication.data.entities.Tienda;
import com.example.myapplication.data.network.ApiClient;
import com.example.myapplication.data.network.ApiService;
import com.example.myapplication.data.viewmodel.TiendaViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TiendaViewModel viewModel;
    ListView lista;


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

        Log.d("TEST", "MainActivity iniciado 🚀");

        String _tiendas;
        lista = findViewById(R.id.lista);
        TiendaAdapter adapter = new TiendaAdapter(this, new ArrayList<>());
        lista.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(TiendaViewModel.class);



        viewModel.getTodasTiendas().observe(this, tiendas -> {
            adapter.listaTiendas = tiendas;
            adapter.notifyDataSetChanged();
        });

        viewModel.sincronizarTiendas();
    }
}
