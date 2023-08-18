package ke.ac.tukenya.scit.seedclient

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mLocationPermissionGranted = false
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private val DEFAULT_ZOOM = 15

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
            updateLocationUI()
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                    updateLocationUI()
                    getDeviceLocation()
                }
            }
        }
    }

    private fun updateLocationUI() {
        try {
            if (mLocationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                val locationResult = mFusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val mLastKnownLocation = task.result
                        if (mLastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(mLastKnownLocation.latitude, mLastKnownLocation.longitude), DEFAULT_ZOOM.toFloat()))

                            // Add marker for the current location
                            mMap.addMarker(MarkerOptions().position(LatLng(mLastKnownLocation.latitude, mLastKnownLocation.longitude)).title("Current Location"))

                            // Set the address details to EditText
                            setAddressToEditText(mLastKnownLocation.latitude, mLastKnownLocation.longitude)
                        }
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun setAddressToEditText(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(requireContext())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        val editTextAddress: EditText = view?.findViewById(R.id.editTextText3) ?: return
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address = addresses?.get(0)
                val fullAddress = "${address?.getAddressLine(0)}, ${address?.locality}, ${address?.adminArea}, ${address?.countryName}"
                editTextAddress.setText(fullAddress)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocationPermission()
    }
}

