package com.proyect.proyectoboris;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapUserActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton mImageButtonMessage;

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private AuthProvider mAuthProvider;
    private UserProvider mUserProvider;
    private GroupProvider groupPro;

    //private FirebaseAuth firebaseAuth;
    private String name = "Tú estás aquí";
    private String nameUsers = "User Name";
    private String nameGroup = "Group Name";

    private GeofireProvider mGeofireProvider;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;

    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;

    private Marker mMarker;
    private List<Marker> mUsersMarkers = new ArrayList<>();

    private LatLng mCurrentLatLng;

    private boolean mIsFirstTime = true;
    private boolean flag = false;

    private String keyId;

    private ArrayList<String> members;

    //se ejecuta cada vez que el usuario se mueva
    private LocationCallback mlocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {

                    mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    if(mMarker != null){
                        mMarker.remove();
                    }

                    mMarker = mMap.addMarker(new MarkerOptions().position(
                            new LatLng(location.getLatitude(), location.getLongitude())
                            )
                            .title("Tú estás aquí")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location_user))
                    );
                    //OBTENER LA LOCALIZACION DEL USUARIO EN TIEMPO REAL
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(15f)
                                    .build()
                    ));

                    updateLocation();

                    /*if(flag){
                        getMembers();
                    }*/

                    if(mIsFirstTime){
                        mIsFirstTime = false;
                        getActiveUsers();
                    }

                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_user);

        MyToolbar.show(this, "Mapa", false);

        mImageButtonMessage = findViewById(R.id.imageButtonMessage);

        mAuthProvider = new AuthProvider();
        mUserProvider = new UserProvider();
        mGeofireProvider = new GeofireProvider();
        groupPro = new GroupProvider();
        //firebaseAuth = FirebaseAuth.getInstance();
        //name = mUserProvider.getUser(mAuthProvider.getId()).child("name").getKey();

        keyId = mAuthProvider.getId();

       //code = getIntent().getStringExtra("codigo");

        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        Bundle extras = this.getIntent().getExtras();
        if(extras != null){
            //flag = extras.getInt("flag");
            mIsFirstTime = true;
            //flag = true;
            String idE = extras.getString("idgrupo");
            getMembers(idE);
            if(members != null){
                Log.i("flaggg", "aaaaaaaaa");
            }
            //members = extras.getStringArrayList("members");
            //Log.i("mapi",members.get(0));
        }

        //obtenerCodigo();
        //Toast.makeText(this, "code:"+code, Toast.LENGTH_LONG).show();

        //boton del mensajee
        mImageButtonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapUserActivity.this, MessageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getMembers(String idA){
        //ArrayList<String> usuarios = new ArrayList<String>();
        /*groupPro.getGroupId(idA).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Group group = snapshot.getValue(Group.class);
                    members = new ArrayList<>(group.getMembers().keySet());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        groupPro.getGroupId(idA).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Group group = snapshot.getValue(Group.class);
                    members = new ArrayList<>(group.getMembers().keySet());
                    //recuperamos el nombre del grupo y se lo asignamos al toolbar
                    nameGroup = snapshot.child("name").getValue().toString();
                    MyToolbar.show(MapUserActivity.this, nameGroup, false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.crear_grupo){
            Intent intent = new Intent(MapUserActivity.this, RegisterGroupActivity.class);
            //intent.putExtra("codigo", code);
            startActivity(intent);
        }else if(item.getItemId() == R.id.ver_grupos){
            Intent intent = new Intent(MapUserActivity.this, GroupViewActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.unir_grupo){
            Intent intent = new Intent(MapUserActivity.this, EnteredCodeActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
/*
    private void obtenerCodigo(){
        mUserProvider.getUser(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                code = user.code;
                Toast.makeText(MapUserActivity.this, "code: "+code, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    private void updateLocation(){
        if(mAuthProvider.existSession() && mCurrentLatLng != null){
            mGeofireProvider.saveLocation(mAuthProvider.getId(), mCurrentLatLng);
        }
    }

    private void getActiveUsers(){
        mGeofireProvider.getActiveUsers(mCurrentLatLng).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                //AÑADIR LOS MARCADORES DE LOS USUARIOS ACTIVOS
                if(!(key.equals(keyId))) {
                    if (members != null && members.contains(key)) {
                        Log.i("keyee",key);
                        for (Marker marker : mUsersMarkers) {
                            if (marker.getTag() != null) {
                                //key se obtiene cuando se conecta un nuevo usuario
                                if (marker.getTag().equals(key)) {
                                    //esto se hace para que no se vuelva a añadir el marcador
                                    return;
                                }
                            }
                        }

                        getNameUser(key, location);
                        //Log.i("nameee",nameUsers);
                        /*
                        LatLng userLatLng = new LatLng(location.latitude, location.longitude);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(userLatLng).title(nameUsers).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location_members2)));
                        marker.setTag(key);
                        mUsersMarkers.add(marker);*/
                    }
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                //actualizar la posicion de cada usuarioo, este metodo se ejecuta cuando cambia la posicion del usuario
                if(!(key.equals(keyId))) {
                    if (members != null && members.contains(key)) {
                        Log.i("keydos",key);
                        for (Marker marker : mUsersMarkers) {
                            if (marker.getTag() != null) {
                                //key se obtiene cuando se conecta un nuevo usuario
                                if (marker.getTag().equals(key)) {
                                    marker.setPosition(new LatLng(location.latitude, location.longitude));
                                }
                            }
                        }
                    }
                }

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void getNameUser(String key, GeoLocation location){
        mUserProvider.getUser(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    nameUsers = user.getName().toString();

                    LatLng userLatLng = new LatLng(location.latitude, location.longitude);
                    Marker marker = mMap.addMarker(new MarkerOptions().position(userLatLng).title(nameUsers).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location_members2)));
                    marker.setTag(key);
                    mUsersMarkers.add(marker);

                    Log.i("nameee",nameUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.setMyLocationEnabled(true);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);

        startLocation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            //PARA SABER SI EL USUARIO CONCEDIO LOS PERMISOS
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if(gpsActived()){
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mlocationCallback, Looper.myLooper());
                    }else{
                        showAlertDialogNoGPS();
                    }
                } else {
                    checkLocationPermissions();
                }
            } else {
                checkLocationPermissions();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mlocationCallback, Looper.myLooper());
        }else{
            showAlertDialogNoGPS();
        }
    }


    private void showAlertDialogNoGPS(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Activa tu ubicacion para continuar")
                .setPositiveButton("Configuracion", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    private boolean gpsActived(){
        boolean isActived = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            isActived = true;
        }
        return isActived;
    }

    private void startLocation(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if(gpsActived()){
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mlocationCallback, Looper.myLooper());
                }else{
                    showAlertDialogNoGPS();
                }
            }else{
                checkLocationPermissions();
            }
        }else{
            if(gpsActived()){
                mFusedLocation.requestLocationUpdates(mLocationRequest, mlocationCallback, Looper.myLooper());
            }else{
                showAlertDialogNoGPS();
            }
        }
    }

    private void checkLocationPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicaion requiere de los permisos de ubicación")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //PARA HABILITAR LOS PERMISOS DE USO DE LA UBICACION DEL CELULAR
                                ActivityCompat.requestPermissions(MapUserActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }else{
                ActivityCompat.requestPermissions(MapUserActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

}