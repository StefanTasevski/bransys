package com.tasevski.bransys.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tasevski.bransys.R

internal class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sydney = LatLng(intent.getDoubleExtra("latitude", 0.0) , intent.getDoubleExtra("longitude", 0.0))
        mMap.addMarker(MarkerOptions()
            .position(sydney)
            .snippet(intent.getStringExtra("city")+", "+intent.getStringExtra("country"))
            .title(intent.getStringExtra("name")))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}