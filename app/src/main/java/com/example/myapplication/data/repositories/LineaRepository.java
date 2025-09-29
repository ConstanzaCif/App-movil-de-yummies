package com.example.myapplication.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.dao.LineaProductoDao;
import com.example.myapplication.data.dao.TiendaDao;
import com.example.myapplication.data.database.AppDatabase;
import com.example.myapplication.data.dto.LineaDTO;
import com.example.myapplication.data.dto.ProductoDTO;
import com.example.myapplication.data.dto.TiendaDTO;
import com.example.myapplication.data.entities.LineaProducto;
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

public class LineaRepository {
    private final AppDatabase db;
    private final ApiService apiService;

    public LineaRepository(Context context) {
        db = AppDatabase.getInstance(context);
        apiService = ApiClient.getClient().create(ApiService.class);
    }
    public void sincronizarLineas() {
        apiService.obtenerLineas().enqueue(new Callback<LineaDTO>() {
            @Override
            public void onResponse(Call<LineaDTO> call, Response<LineaDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LineaProducto> lineas = response.body().data;
                    if (lineas != null && !lineas.isEmpty()) {
                        Log.d("LineasRepository", "Lineas recibidas de API: " + lineas.size());
                        LineaProducto primera = lineas.get(0);
                        Log.d("LineasRepository", "Lineas ejemplo: id=" + primera.getId_linea_productos() + ", nombre=" + primera.getLinea_productos());
                        Executors.newSingleThreadExecutor().execute(() -> {
                            try {
                                db.lineaDao().insertarLineas(lineas);
                                Log.d("LineasRepository", "Lineas insertadas en DB: " + lineas.size());
                                List<LineaProducto> lineasEnBd = db.lineaDao().obtenerLineas().getValue();
                                Log.d("LineasRepository", "Lineas en DB después de insert: " + (lineasEnBd != null ? lineasEnBd.size() : 0));
                            } catch (Exception e) {
                                Log.e("LineasRepository", "Error al insertar/leer lineas: " + e.getMessage());
                                e.printStackTrace();
                            }
                        });
                    } else {
                        Log.e("LineasRepository", "Lista de lineas vacía o nula de la API");
                    }
                } else {
                    Log.e("LineasRepository", "Respuesta no exitosa o body nulo: " + (response != null ? response.code() : "Respuesta nula"));
                }
            }
            @Override
            public void onFailure(Call<LineaDTO> call, Throwable throwable) {
                Log.e("LineaRepository", "Error en API: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }
    public LiveData<List<LineaProducto>> obtenerLineas() {
        return db.lineaDao().obtenerLineas();
    }

    public LiveData<LineaProducto> obtenerLinea(int id) {
        return  db.lineaDao().obtenerLineaPorId(id);
    }
}
