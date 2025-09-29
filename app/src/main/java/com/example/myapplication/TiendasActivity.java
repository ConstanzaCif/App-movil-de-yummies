package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.adapter.TiendaAdapter;
import com.example.myapplication.data.entities.Tienda;
import com.example.myapplication.data.viewmodel.TiendaViewModel;

import java.util.ArrayList;
import java.util.List;

public class TiendasActivity extends BaseActivity {

    ListView lista;
    TiendaViewModel viewModel;

    ArrayList<Tienda> listaTiendas1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setupDrawer(R.layout.activity_tiendas);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lista = findViewById(R.id.lista_tiendas);
        TiendaAdapter adapter = new TiendaAdapter(this, new ArrayList<>());
        lista.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(TiendaViewModel.class);
        listaTiendas1 = new ArrayList<>();



        viewModel.getTodasTiendas().observe(this, tiendas -> {
            adapter.listaTiendas = tiendas;
            listaTiendas1.addAll(tiendas);
            adapter.notifyDataSetChanged();
        });

        viewModel.sincronizarTiendas();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tienda tienda = listaTiendas1.get(position);

                Intent intent = new Intent(TiendasActivity.this, TiendaActivity.class);

                intent.putExtra("tienda", tienda.getId_tiendas());

                startActivity(intent);
            }
        });
    }
}