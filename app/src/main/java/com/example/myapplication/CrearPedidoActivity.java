package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.adapter.DetalleAdapter;
import com.example.myapplication.data.clases.SessionManager;
import com.example.myapplication.data.dto.PedidoPostDTO;
import com.example.myapplication.data.entities.Producto;
import com.example.myapplication.data.entities.Tienda;
import com.example.myapplication.data.viewmodel.PedidoViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearPedidoActivity extends AppCompatActivity {
    private Spinner spinnerTienda, spinnerProducto;
    private EditText inputCantidad;
    private Button btnAgregarDetalle, btnGuardarPedido;
    private ListView listViewDetalles;

    private PedidoViewModel pedidoViewModel;
    private SessionManager sessionManager;

    private List<Tienda> tiendas = new ArrayList<>();
    private List<Producto> productos = new ArrayList<>();
    private List<PedidoPostDTO.Detalle> listaDetalles = new ArrayList<>();
    private List<String> nombresProductos = new ArrayList<>();
    private DetalleAdapter detalleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_pedido);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
    }


}