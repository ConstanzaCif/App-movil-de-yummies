package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.data.dto.PedidoPostDTO;

import java.util.List;

public class DetalleAdapter extends BaseAdapter {
    private Context context;
    private List<PedidoPostDTO.Detalle> detalles;
    private List<String> nombresProductos; // para mostrar el nombre en lugar del id

    public DetalleAdapter(Context context, List<PedidoPostDTO.Detalle> detalles, List<String> nombresProductos) {
        this.context = context;
        this.detalles = detalles;
        this.nombresProductos = nombresProductos;
    }

    @Override
    public int getCount() { return detalles.size(); }

    @Override
    public Object getItem(int position) { return detalles.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        PedidoPostDTO.Detalle detalle = detalles.get(position);
        text1.setText("Producto: " + nombresProductos.get(position));
        text2.setText("Cantidad: " + detalle.cantidad);

        return convertView;
    }
}
