package com.example.myapplication.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.clases.ProductoConLinea;
import com.example.myapplication.data.dao.TiendaDao;
import com.example.myapplication.data.database.AppDatabase;
import com.example.myapplication.data.dto.ProductoDTO;
import com.example.myapplication.data.dto.TiendaDTO;
import com.example.myapplication.data.entities.Producto;
import com.example.myapplication.data.entities.Tienda;
import com.example.myapplication.data.network.ApiClient;
import com.example.myapplication.data.network.ApiService;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoRepository {
    private final AppDatabase db;
    private final ApiService apiService;

    public ProductoRepository(Context context) {
        db = AppDatabase.getInstance(context);
        apiService = ApiClient.getClient().create(ApiService.class);
    }
    public void sincronizarProductos() {
        apiService.obtenerProductos().enqueue(new Callback<ProductoDTO>() {
            @Override
            public void onResponse(Call<ProductoDTO> call, Response<ProductoDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Producto> productos = response.body().data;
                    if (productos != null && !productos.isEmpty()) {
                        Log.d("ProductoRepository", "Productos recibidas de API: " + productos.size());
                        Producto primera = productos.get(0);
                        Log.d("ProductoRepository", "Producto ejemplo: id=" + primera.getId_producto() + ", nombre=" + primera.getNombre_producto());
                        Executors.newSingleThreadExecutor().execute(() -> {
                            try {
                                db.productoDao().insertarProductos(productos);
                                Log.d("ProductoRepository", "Productoss insertados en DB: " + productos.size());
                                List<Producto> productosEnBd = db.productoDao().obtenerProductos().getValue();
                                Log.d("ProductoRepository", "Productos en DB después de insert: " + (productosEnBd != null ? productosEnBd.size() : 0));
                            } catch (Exception e) {
                                Log.e("ProductoRepository", "Error al insertar/leer productos: " + e.getMessage());
                                e.printStackTrace();
                            }
                        });
                    } else {
                        Log.e("ProductoRepository", "Lista de productos vacía o nula de la API");
                    }
                } else {
                    Log.e("ProductoRepository", "Respuesta no exitosa o body nulo: " + (response != null ? response.code() : "Respuesta nula"));
                }
            }
            @Override
            public void onFailure(Call<ProductoDTO> call, Throwable throwable) {
                Log.e("ProductoRepository", "Error en API: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }
    public LiveData<List<Producto>> obtenerProductos() {
        return db.productoDao().obtenerProductos();
    }

    public LiveData<ProductoConLinea> obtenerProducto(int id) {
        return  db.productoDao().obtenerProductoConLinea(id);
    }
}
