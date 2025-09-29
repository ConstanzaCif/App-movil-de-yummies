package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.entities.DetallePedido;

import java.util.List;

public class DetallePedidoAdapter extends BaseAdapter {
    private final Context context;
    public List<DetallePedido> listaDetalles;

    public DetallePedidoAdapter(Context context, List<DetallePedido> listaDetalles) {
        this.context = context;
        this.listaDetalles = listaDetalles;
    }

    @Override
    public int getCount() {
        return listaDetalles.size();
    }

    @Override
    public Object getItem(int position) {
        return listaDetalles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_detalle, parent, false);
        }

        TextView txtIdProducto = convertView.findViewById(R.id.txtIdProducto);
        TextView txtCantidad = convertView.findViewById(R.id.txtCantidad);
        TextView txtPrecioUnitario = convertView.findViewById(R.id.txtPrecioUnitario);
        TextView txtSubtotal = convertView.findViewById(R.id.txtSubtotal);

        DetallePedido detalleItem = listaDetalles.get(position);

        txtIdProducto.setText("Producto #" + String.valueOf(detalleItem.getId_producto()));
        txtCantidad.setText("Cantidad: " + String.valueOf(detalleItem.getCantidad()));
        txtPrecioUnitario.setText("Precio Unitario: Q " + String.valueOf(detalleItem.getPrecio_unitario()));
        txtSubtotal.setText("Subtotal: Q " + String.valueOf(detalleItem.getSubtotal()));

        return convertView;
    }
}
