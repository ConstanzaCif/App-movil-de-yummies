package com.example.myapplication.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.data.clases.ProductoConLinea;
import com.example.myapplication.data.entities.LineaProducto;
import com.example.myapplication.data.entities.Producto;
import com.example.myapplication.data.entities.Tienda;
import com.example.myapplication.data.repositories.LineaRepository;
import com.example.myapplication.data.repositories.ProductoRepository;
import com.example.myapplication.data.repositories.TiendaRepository;

import java.util.List;

public class ProductoViewModel extends AndroidViewModel {

    private ProductoRepository repository;
    private LineaRepository repository2;
    private LiveData<List<Producto>> productos;
    private LiveData<List<LineaProducto>> lineas;

    public ProductoViewModel(@NonNull Application application){
        super(application);
        repository = new ProductoRepository(application.getApplicationContext());
        repository2 = new LineaRepository(application.getApplicationContext());
        productos = repository.obtenerProductos();
        lineas = repository2.obtenerLineas();
    }
    public LiveData<List<Producto>> getTodosProductos() {
        return productos;
    }
    public void sincronizarProductos(){
        repository.sincronizarProductos();
    }
    public void sincronizarLineas() {repository2.sincronizarLineas();}

    public LiveData<ProductoConLinea> getProducto(int id){
        return repository.obtenerProducto(id);
    }
}
