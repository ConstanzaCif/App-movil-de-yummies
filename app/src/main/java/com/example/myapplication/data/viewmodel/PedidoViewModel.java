package com.example.myapplication.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.dto.PedidoDTO;
import com.example.myapplication.data.dto.PedidoPostDTO;
import com.example.myapplication.data.entities.DetallePedido;
import com.example.myapplication.data.entities.Pedido;
import com.example.myapplication.data.repositories.PedidoRepository;

import java.util.List;

import retrofit2.Callback;

public class PedidoViewModel extends AndroidViewModel {
    private PedidoRepository repository;
    private LiveData<List<Pedido>> pedidos;
    private final MutableLiveData<Boolean> crearPedidoResult = new MutableLiveData<>();


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
    public LiveData<Boolean> getCrearPedidoResult() {
        return crearPedidoResult;
    }
    public void crearPedido(PedidoPostDTO pedido) {
        repository.crearPedido(pedido, success -> crearPedidoResult.postValue(success));
    }



}
