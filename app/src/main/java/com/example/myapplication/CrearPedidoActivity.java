package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CrearPedidoActivity extends BaseActivity  {

    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private ImageView imagePreview;
    private Bitmap imagenBitmap;

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
        setupDrawer(R.layout.activity_crear_pedido);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
        }

        imagePreview = findViewById(R.id.imagePreview);
        Button btnTomarFoto = findViewById(R.id.btnTomarFoto);

        btnTomarFoto.setOnClickListener(v -> abrirCamara());

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imagenBitmap = (Bitmap) extras.get("data");
            imagePreview.setImageBitmap(imagenBitmap);
        }
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

    private void abrirCamara() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            lanzarIntentCamara();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lanzarIntentCamara();
            } else {
                Toast.makeText(this, "Se necesita permiso de cámara para tomar fotos", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void lanzarIntentCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
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
// CONVERTIR IMAGEN A BASE64
// -------------------
    private String convertirImagenABase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.NO_WRAP);
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


        String imagenBase64 = null;
        if (imagenBitmap != null) {
            imagenBase64 = convertirImagenABase64(imagenBitmap);
        }
        pedidoPost.imagen = imagenBase64;



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