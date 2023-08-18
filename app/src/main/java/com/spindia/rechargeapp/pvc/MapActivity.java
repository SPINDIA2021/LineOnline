package com.spindia.rechargeapp.pvc;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.spindia.rechargeapp.R;

public class MapActivity extends AppCompatActivity {

    GoogleMap googleMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_map);
       //  GoogleMap googleMap = ((MapView) findViewById(R.id.map)).g();

    }
}
