package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.myapplication.data.viewmodel.ProductoViewModel;

public class ProductoActivity extends BaseActivity {

    TextView txtNombre;
    TextView txtCodigo;
    TextView txtPrecio;
    TextView txtDescripcion;
    TextView txtGramaje;
    TextView txtLinea;

    ProductoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setupDrawer(R.layout.activity_producto2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtNombre = findViewById(R.id.txtNombreProducto2);
        txtCodigo = findViewById(R.id.txtCodigoProducto2);
        txtDescripcion = findViewById(R.id.txtDescripcionProducto2);
        txtLinea = findViewById(R.id.txtLineaProducto2);
        txtPrecio = findViewById(R.id.txtPrecioProducto2);
        txtGramaje = findViewById(R.id.txtGramajeProducto2);
        ImageView imgProducto = findViewById(R.id.imgProducto2);

        viewModel = new ViewModelProvider(this).get(ProductoViewModel.class);

        int id = getIntent().getIntExtra("producto", -1);

        viewModel.getProducto(id).observe(this, productoConLinea -> {
            if (productoConLinea != null) {
                txtNombre.setText(productoConLinea.getProducto().getNombre_producto());
                txtCodigo.setText("Código: " + productoConLinea.getProducto().getId_producto());
                txtPrecio.setText("Precio: " + productoConLinea.getProducto().getPrecio());
                txtDescripcion.setText("Descripción: "+ productoConLinea.getProducto().getDescripcion());
                txtGramaje.setText("Gramos: "+productoConLinea.getProducto().getGramaje());
                txtLinea.setText("Linea del producto: "+productoConLinea.getLineaProducto().getLinea_productos());

                String imageUrl = productoConLinea.getProducto().getImagen_url();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    if (!imageUrl.startsWith("http")) {
                        imageUrl = "http://10.0.2.2:3001" + imageUrl;
                    }
                    Glide.with(this)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_products)
                            .error(R.drawable.ic_error)
                            .into(imgProducto);
                } else {
                    imgProducto.setImageResource(R.drawable.ic_products);
                }
            }
        });
    }
}