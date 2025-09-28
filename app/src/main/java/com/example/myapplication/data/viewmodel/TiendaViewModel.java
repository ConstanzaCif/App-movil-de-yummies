package com.example.myapplication.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.data.entities.Tienda;
import com.example.myapplication.data.repositories.TiendaRepository;

import java.util.List;

public class TiendaViewModel extends AndroidViewModel {

    private TiendaRepository repository;
    private LiveData<List<Tienda>> tiendas;

    public TiendaViewModel(@NonNull Application application){
        super(application);
        repository = new TiendaRepository(application.getApplicationContext());
        tiendas = repository.obtenerTiendas();
    }
    public LiveData<List<Tienda>> getTodasTiendas() {
        return tiendas;
    }
    public void sincronizarTiendas(){
        repository.sincronizarTiendas();
    }
}
