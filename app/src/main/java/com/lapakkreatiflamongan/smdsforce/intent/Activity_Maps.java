package com.lapakkreatiflamongan.smdsforce.intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.lapakkreatiflamongan.smdsforce.R;
import com.lapakkreatiflamongan.smdsforce.adapter.Adapter_PositionMaps;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Position;
import com.lapakkreatiflamongan.smdsforce.schema.Data_Value_Detail_Numbered;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;

public class Activity_Maps extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowLongClickListener {
    private GoogleMap mMap;
    ArrayList<Data_Position> positions = new ArrayList<Data_Position>();
    private Float[] arrColor = new Float[25];

    @BindView(R.id.Maps_Georeverse)
    TextView txtGeo;

    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;

    @BindView(R.id.Maps_List)
    ListView listViewMaps;

    @BindView(R.id.Maps_Info)
    TextView txtInfo;

    private final String TAG_PREF = "SETTING_SUPERVISION_PREF";
    private final String TAG_SPVCODE = "usercode";

    Adapter_PositionMaps adapterPositionMaps;
    BottomSheetBehavior sheetBehavior;
    ArrayList<Data_Position> arrayList = new ArrayList<Data_Position>();

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                arrayList.clear();

                int counter = 0;
                for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()) {
                    Data_Position position = noteSnapshot.getValue(Data_Position.class);
                    Log.e( "onDataChange: ","Hai "+position.getSpvcode());
                    if (position.getSpvcode().toString().replace("/","").equals(getPref(TAG_SPVCODE).toString().replace("/",""))){
                        positions.add(position);
                        arrayList.add(new Data_Position(
                                positions.get(counter).getLatitude(),
                                positions.get(counter).getLongitude(),positions.get(counter).getCreatedate(),counter+"",positions.get(counter).getSpvcode(),positions.get(counter).getSellername()
                        ));
                        counter++;
                    }
                }
                adapterPositionMaps = new Adapter_PositionMaps(Activity_Maps.this,arrayList);
                listViewMaps.setAdapter(adapterPositionMaps);

                listViewMaps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(arrayList.get(position).getLatitude()),Double.parseDouble(arrayList.get(position).getLongitude())),18));
                    }
                });


                ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();

                PolygonOptions polygonOptions = new PolygonOptions().clickable(true);

                for (int i = 0; i < positions.size(); i++) {
                    polygonOptions.add(new LatLng(Double.parseDouble(positions.get(i).getLatitude()),Double.parseDouble(positions.get(i).getLongitude()))).strokeWidth(1);

                    markers.add(
                            new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(positions.get(i).getLatitude()),Double.parseDouble(positions.get(i).getLongitude())))
                                    .title(positions.get(i).getSellername()+" \n "+positions.get(i).getCreatedate())
                                    .icon(BitmapDescriptorFactory.defaultMarker(arrColor[i])));
                    mMap.addMarker(markers.get(i));
                }


                if(positions.size()>0){
                    Polygon polygon1 = mMap.addPolygon(polygonOptions);
                    polygon1.setFillColor(0x7F00FF00);

                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {

                            LinearLayout info = new LinearLayout(Activity_Maps.this);
                            info.setOrientation(LinearLayout.VERTICAL);

                            TextView title = new TextView(Activity_Maps.this);
                            title.setTextColor(Color.BLACK);
                            title.setGravity(Gravity.CENTER);
                            title.setTypeface(null, Typeface.BOLD);
                            title.setText(marker.getTitle());

                            TextView snippet = new TextView(Activity_Maps.this);
                            snippet.setTextColor(Color.GRAY);
                            snippet.setText(marker.getSnippet());

                            TextView desc = new TextView(Activity_Maps.this);
                            desc.setTextColor(Color.GRAY);
                            desc.setText("Hold me to show route");
                            desc.setTextSize(10);

                            info.addView(title);
                            info.addView(snippet);
                            info.addView(desc);

                            return info;
                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }

    public String getPref(String KEY){
        SharedPreferences SettingPref = getSharedPreferences(TAG_PREF, Context.MODE_PRIVATE);
        String Value=SettingPref.getString(KEY, "0");
        return  Value;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_maps,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.maps_refresh:
                Intent intent = new Intent(Activity_Maps.this,Activity_Maps.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:break;
        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_maps_seller);

        ButterKnife.bind(this);

        arrColor[0] = BitmapDescriptorFactory.HUE_AZURE;
        arrColor[1] = BitmapDescriptorFactory.HUE_BLUE;
        arrColor[2] = BitmapDescriptorFactory.HUE_CYAN;
        arrColor[3] = BitmapDescriptorFactory.HUE_GREEN;
        arrColor[4] = BitmapDescriptorFactory.HUE_MAGENTA;
        arrColor[5] = BitmapDescriptorFactory.HUE_ORANGE;
        arrColor[6] = BitmapDescriptorFactory.HUE_RED;
        arrColor[7] = BitmapDescriptorFactory.HUE_ROSE;
        arrColor[8] = BitmapDescriptorFactory.HUE_VIOLET;
        arrColor[9] = BitmapDescriptorFactory.HUE_YELLOW;
        arrColor[10] = 15f;
        arrColor[11] = 25f;
        arrColor[12] = 35f;
        arrColor[13] = 45f;
        arrColor[14] = 55f;
        arrColor[15] = 65f;
        arrColor[16] = 75f;
        arrColor[17] = 85f;
        arrColor[18] = 95f;
        arrColor[20] = 105f;
        arrColor[21] = 115f;
        arrColor[22] = 125f;
        arrColor[23] = 135f;
        arrColor[24] = 145f;


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            String Msg = "Aplikasi Super Vision tidak bisa berjalan jika tidak bisa akses perizinan Telepon, Silahkan aktifkan izin untuk akses telepon";
            new AlertDialog.Builder(Activity_Maps.this)
                    .setTitle("Information")
                    .setMessage(Msg)
                    .setPositiveButton("Setelan", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Activity_Maps.this.finish();
                        }
                    })
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            String Msg = "GPS tidak aktif, silahkan aktifkan dahulu di setting!!!";
            new AlertDialog.Builder(Activity_Maps.this)
                    .setTitle("Information")
                    .setMessage(Msg)
                    .setPositiveButton("Setelan", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                            Activity_Maps.this.finish();
                            Activity_Maps.this.finishAffinity();
                        }
                    })
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setHideable(false);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        txtInfo.setVisibility(View.GONE);
                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        txtInfo.setVisibility(View.VISIBLE);
                        break;
                    }
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        SmartLocation.with(Activity_Maps.this).location().oneFix().start(new OnLocationUpdatedListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationUpdated(Location location) {
                // Add a marker in Sydney and move the camera
                if (location != null){

                    LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Anda sedang disini"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    mMap.setMyLocationEnabled(true);
                    mMap.setMinZoomPreference(8);
                    mMap.setOnInfoWindowLongClickListener(Activity_Maps.this);

                }

            }
        });



    }


    @Override
    public void onInfoWindowLongClick(Marker marker) {
        String nav = "google.navigation:q="+marker.getPosition().latitude+","+marker.getPosition().longitude;
        Uri gmmIntentUri = Uri.parse(nav);
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
}
