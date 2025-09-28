package com.example.myapplication.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.myapplication.data.clases.SessionManager;
import com.example.myapplication.data.clases.Usuario;
import com.example.myapplication.data.database.AppDatabase;
import com.example.myapplication.data.dto.LoginDto;
import com.example.myapplication.data.dto.UsuarioDTO;
import com.example.myapplication.data.network.ApiClient;
import com.example.myapplication.data.network.ApiService;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class UsuarioRepository {

    private final ApiService apiService;
    private final SessionManager sessionManager;

    public UsuarioRepository(Context context) {
        apiService = ApiClient.getClient().create(ApiService.class);
        sessionManager = new SessionManager(context);
    }

    public LiveData<Usuario> login(String username, String password) {

        MutableLiveData<Usuario> result = new MutableLiveData<>();
        LoginDto loginDto = new LoginDto(username, password);
        apiService.login(loginDto).enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body().getUsuario();

                    sessionManager.guardarUsuario(usuario);
                    Log.d("Usuario", "Usuario obtenido "+ usuario.getNombre());
                    result.postValue(usuario);

                } else {
                    result.postValue(null);
                    Log.d("Error", "No se obtuvo el usuario");
                }
            }

            @Override
            public void onFailure(Call<UsuarioDTO> call, Throwable t) {
                result.postValue(null);
            }
        });
        return result;
    }

}
