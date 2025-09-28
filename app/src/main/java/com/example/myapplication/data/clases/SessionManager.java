package com.example.myapplication.data.clases;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SessionManager {
    private static final String pref_name ="sesion_usuario";
    private static final String key_user = "usuario";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public SessionManager(Context context){
        prefs = context.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
        editor = prefs.edit();
        gson = new Gson();
    }

    public void guardarUsuario(Usuario usuario){
        String json = gson.toJson(usuario);
        editor.putString(key_user, json);
        editor.apply();
    }

    public Usuario getUsuario(){
        String json = prefs.getString(key_user, null);
        if(json != null){
            return gson.fromJson(json, Usuario.class);
        }
        return null;
    }

    public void cerrarSesion() {
        editor.remove(key_user);
        editor.apply();
    }

    public boolean estaLoggeado(){
        return getUsuario() != null;
    }

}
