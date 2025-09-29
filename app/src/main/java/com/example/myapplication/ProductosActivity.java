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

import com.example.myapplication.adapter.ProductoAdapter;
import com.example.myapplication.adapter.TiendaAdapter;
import com.example.myapplication.data.entities.Producto;
import com.example.myapplication.data.entities.Tienda;
import com.example.myapplication.data.viewmodel.ProductoViewModel;
import com.example.myapplication.data.viewmodel.TiendaViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductosActivity extends BaseActivity {

    ListView lista;
    ProductoViewModel viewModel;

    ArrayList<Producto> listaProductos1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setupDrawer(R.layout.activity_productos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lista = findViewById(R.id.lista_productos);
        ProductoAdapter adapter = new ProductoAdapter(this, new ArrayList<>());
        lista.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        listaProductos1 = new ArrayList<>();



        viewModel.getTodosProductos().observe(this, productos -> {
            adapter.listaProductos = productos;
            listaProductos1.addAll(productos);
            adapter.notifyDataSetChanged();
        });

        viewModel.sincronizarLineas();
        viewModel.sincronizarProductos();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Producto producto = listaProductos1.get(position);

                Intent intent = new Intent(ProductosActivity.this, ProductoActivity.class);

                intent.putExtra("producto", producto.getId_producto());

                startActivity(intent);
            }
        });
    }
}