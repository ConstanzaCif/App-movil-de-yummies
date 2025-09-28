package com.example.myapplication.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.data.clases.Usuario;
import com.example.myapplication.data.repositories.UsuarioRepository;

public class UsuarioViewModel extends AndroidViewModel {
    private UsuarioRepository repository;

    public UsuarioViewModel(@NonNull Application application) {
        super(application);
        repository = new UsuarioRepository(application);
    }

    public LiveData<Usuario> login(String username, String password) {
       return repository.login(username, password);
    }
}

