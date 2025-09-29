package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.adapter.DetalleAdapter;
import com.example.myapplication.data.clases.SessionManager;
import com.example.myapplication.data.dto.PedidoPostDTO;
import com.example.myapplication.data.entities.DetallePedido;
import com.example.myapplication.data.entities.Pedido;
import com.example.myapplication.data.entities.Producto;
import com.example.myapplication.data.entities.Tienda;
import com.example.myapplication.data.viewmodel.PedidoViewModel;
import com.example.myapplication.data.viewmodel.ProductoViewModel;
import com.example.myapplication.data.viewmodel.TiendaViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CrearPedidoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Spinner spinnerTienda, spinnerProducto;
    private EditText inputCantidad;
    private Button btnAgregarDetalle, btnGuardarPedido;
    private ListView listViewDetalles;

    private PedidoViewModel pedidoViewModel;
    private TiendaViewModel tiendaViewModel;
    private ProductoViewModel productoViewModel;
    private SessionManager sessionManager;

    private List<Tienda> tiendas = new ArrayList<>();
    private List<Producto> productos = new ArrayList<>();
    private List<DetallePedido> detallesAgregados = new ArrayList<>();
    private ArrayAdapter<String> adapterDetalles;
    private List<String> nombresDetalles = new ArrayList<>();

    private LatLng ubicacionSeleccionada;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private ActivityResultLauncher<String> locationPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pedido);



        // --- Inicializar views ---
        spinnerTienda = findViewById(R.id.spinnerTienda);
        spinnerProducto = findViewById(R.id.spinnerProducto);
        inputCantidad = findViewById(R.id.inputCantidad);
        btnAgregarDetalle = findViewById(R.id.btnAgregarDetalle);
        btnGuardarPedido = findViewById(R.id.btnGuardarPedido);
        listViewDetalles = findViewById(R.id.listViewDetalles);

        sessionManager = new SessionManager(this);

        // --- ViewModels ---
        tiendaViewModel = new ViewModelProvider(this).get(TiendaViewModel.class);
        productoViewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        pedidoViewModel = new ViewModelProvider(this).get(PedidoViewModel.class);
        //pedidoViewModel.enviarPendientes();

        // --- Location client ---
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // --- Cargar datos ---
        cargarTiendas();
        cargarProductos();

        // --- Adapter detalles ---
        adapterDetalles = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nombresDetalles);
        listViewDetalles.setAdapter(adapterDetalles);

        // --- Botón agregar detalle ---
        btnAgregarDetalle.setOnClickListener(v -> agregarDetalle());

        // --- Botón guardar pedido ---
        btnGuardarPedido.setOnClickListener(v -> guardarPedido());



        // --- Permiso ubicación ---
        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) obtenerUbicacionActual();
                    else Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
                }
        );
        solicitarPermisoUbicacion();


    }

    // -------------------
    // CARGAR TIENDAS
    // -------------------
    private void cargarTiendas() {
        tiendaViewModel.getTodasTiendas().observe(this, lista -> {
            if (lista != null) {
                tiendas.clear();
                tiendas.addAll(lista);
                List<String> nombres = new ArrayList<>();
                for (Tienda t : tiendas) nombres.add(t.nombre_tienda);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, nombres);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTienda.setAdapter(adapter);
            }
        });
    }

    // -------------------
    // CARGAR PRODUCTOS
    // -------------------
    private void cargarProductos() {
        productoViewModel.getTodosProductos().observe(this, lista -> {
            if (lista != null) {
                productos.clear();
                productos.addAll(lista);
                List<String> nombres = new ArrayList<>();
                for (Producto p : productos) nombres.add(p.nombre_producto);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, nombres);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProducto.setAdapter(adapter);
            }
        });
    }

    // -------------------
    // AGREGAR DETALLE
    // -------------------
    private void agregarDetalle() {
        int posProducto = spinnerProducto.getSelectedItemPosition();
        if (posProducto < 0 || inputCantidad.getText().toString().isEmpty()) return;

        Producto producto = productos.get(posProducto);
        int cantidad = Integer.parseInt(inputCantidad.getText().toString());
        if(cantidad <= 0) {
            Toast.makeText(this, "Cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show();
            return;
        }

        DetallePedido detalle = new DetallePedido();
        detalle.id_producto = producto.id_producto;
        detalle.cantidad = cantidad;
        detallesAgregados.add(detalle);

        nombresDetalles.add(producto.nombre_producto + " x " + cantidad);
        adapterDetalles.notifyDataSetChanged();
        inputCantidad.setText("");
    }

    // -------------------
    // GUARDAR PEDIDO
    // -------------------
    private void guardarPedido() {
        if (spinnerTienda.getSelectedItemPosition() < 0 || detallesAgregados.isEmpty() || ubicacionSeleccionada == null) {
            Toast.makeText(this, "Completa todos los datos y selecciona ubicación", Toast.LENGTH_SHORT).show();
            return;
        }

        int idTienda = tiendas.get(spinnerTienda.getSelectedItemPosition()).id_tiendas;
        int idUsuario = sessionManager.getUsuario().getId_usuarios();

        PedidoPostDTO pedidoPost = new PedidoPostDTO();
        pedidoPost.tienda = idTienda;
        pedidoPost.id_usuario = idUsuario;
        pedidoPost.latitud = ubicacionSeleccionada.latitude;
        pedidoPost.longitud = ubicacionSeleccionada.longitude;
        pedidoPost.fecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(new Date());

        List<PedidoPostDTO.Detalle> detallesPost = new ArrayList<>();
        for (DetallePedido d : detallesAgregados) {
            detallesPost.add(new PedidoPostDTO.Detalle(d.id_producto, d.cantidad));
        }
        pedidoPost.detalle = detallesPost;

        // Solo una llamada, repository maneja offline/online
        pedidoViewModel.guardarPedido(pedidoPost);

        Toast.makeText(this, "Pedido guardado (offline si no hay internet)", Toast.LENGTH_SHORT).show();
        finish();
    }

    // -------------------
    // MAPA
    // -------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Ubicación por defecto
        LatLng defaultLocation = new LatLng(12.77, -91.12);
        ubicacionSeleccionada = defaultLocation;

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(defaultLocation)
                .draggable(true)
                .title("Arrastra para seleccionar ubicación"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override public void onMarkerDragStart(Marker marker) {}
            @Override public void onMarkerDrag(Marker marker) {}
            @Override
            public void onMarkerDragEnd(Marker marker) {
                ubicacionSeleccionada = marker.getPosition();
            }
        });

        // Obtener ubicación actual si hay permiso
        obtenerUbicacionActual();
    }

    private void solicitarPermisoUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            obtenerUbicacionActual();
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void obtenerUbicacionActual() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                ubicacionSeleccionada = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // -------------------
    // CONEXIÓN
    // -------------------
    private boolean hayConexion() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetwork() != null;
    }
}