package com.example.myapplication.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.data.entities.Pedido;
import com.example.myapplication.data.repositories.PedidoRepository;

import java.util.List;

public class PedidoViewModel extends AndroidViewModel {
    private PedidoRepository repository;
    private LiveData<List<Pedido>> pedidos;

    public PedidoViewModel(@NonNull Application application){
        super(application);
        repository = new PedidoRepository(application.getApplicationContext());
        pedidos = repository.obtenerPedidos();
    }
    public LiveData<List<Pedido>> getAllPedidos(){return pedidos;}
    public LiveData<List<Pedido>> sincronizarPedidos(int usuario)
    {
        return repository.sincronizarPedidos(usuario);
    }
}
