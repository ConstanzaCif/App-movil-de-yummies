package com.example.myapplication.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.data.entities.DetallePedido;
import com.example.myapplication.data.repositories.PedidoRepository;

import java.util.List;

public class DetallePedidoViewModel extends AndroidViewModel {
    private final PedidoRepository repository;

    public DetallePedidoViewModel(@NonNull Application application) {
        super(application);
        repository = new PedidoRepository(application);
    }

    public LiveData<List<DetallePedido>> getDetallesByPedido(int idPedido) {
        return repository.obtenerDetallesPorPedido(idPedido);
    }
}
