package com.example.myapplication.data.dto;


import com.example.myapplication.data.clases.Usuario;

import javax.xml.transform.sax.SAXResult;

public class UsuarioDTO {
    String mensaje;
    public Usuario usuario;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
