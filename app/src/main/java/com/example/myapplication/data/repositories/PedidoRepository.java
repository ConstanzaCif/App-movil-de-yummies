package com.example.myapplication.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.myapplication.data.database.AppDatabase;
import com.example.myapplication.data.dto.LoginDto;
import com.example.myapplication.data.dto.PedidoDTO;
import com.example.myapplication.data.dto.PedidoRequestDTO;
import com.example.myapplication.data.entities.Pedido;
import com.example.myapplication.data.entities.Tienda;
import com.example.myapplication.data.network.ApiClient;
import com.example.myapplication.data.network.ApiService;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoRepository {
    private final AppDatabase db;
    private final ApiService apiService;
    public PedidoRepository(Context context){
        db = AppDatabase.getInstance(context);
        apiService = ApiClient.getClient().create(ApiService.class);
    }
    public LiveData<List<Pedido>> sincronizarPedidos(int usuario){
        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO(usuario);
        apiService.obtenerPedidos(pedidoRequestDTO).enqueue(new Callback<PedidoDTO>() {
            @Override
            public void onResponse(Call<PedidoDTO> call, Response<PedidoDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pedido> pedidos = response.body().pedidos;
                    if (pedidos != null && !pedidos.isEmpty()) {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            try {
                                db.pedidoDao().insertarPedidos(pedidos);
                                Log.d("PedidoRepository", "Pedidos insertadas en DB: " + pedidos.size());
                            } catch (Exception e) {
                                Log.e("PedidoRepository", "Error al insertar pedidos: " + e.getMessage());
                            }
                        });
                    }
                } else {
                    Log.e("PedidoRepository", "Respuesta no exitosa o body nulo");
                }
            }


            @Override
            public void onFailure(Call<PedidoDTO> call, Throwable throwable) {
            Log.e("TiendaRepository", "Error en API: " + throwable.getMessage());
            throwable.printStackTrace();
            }
        });
        return db.pedidoDao().obtenerPedidos();
    }
    public LiveData<List<Pedido>> obtenerPedidos(){return db.pedidoDao().obtenerPedidos();}
}
