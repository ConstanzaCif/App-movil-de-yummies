package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.Tienda;

import java.util.List;

public class TiendaAdapter extends BaseAdapter {
    Context context;
    public List<Tienda> listaTiendas;

    public TiendaAdapter(Context context,List<Tienda> listaTiendas){
        this.context = context;
        this.listaTiendas = listaTiendas;
    }


    @Override
    public int getCount() {
        return listaTiendas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaTiendas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tienda, parent, false);
        }
        TextView txtTienda = convertView.findViewById(R.id.txtTienda);

        Tienda tiendaItem = listaTiendas.get(position);

        txtTienda.setText(tiendaItem.getNombre_tienda());

        return convertView;
    }

}
