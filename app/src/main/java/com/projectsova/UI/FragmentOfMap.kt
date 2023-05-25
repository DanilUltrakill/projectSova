package com.projectsova.UI

import android.Manifest
import android.R
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.projectsova.data.MapKitInitializer
import com.projectsova.databinding.FragmentOfMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.traffic.TrafficLayer
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider


class FragmentOfMap : Fragment(), UserLocationObjectListener, DrivingSession.DrivingRouteListener {

    lateinit var navController: NavController
    lateinit var binding: FragmentOfMapBinding

    lateinit var mapKit: MapKit
    lateinit var mapView: MapView
    lateinit var userLocLayer: UserLocationLayer
    lateinit var trafficLayer: TrafficLayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitInitializer.initialize(this.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOfMapBinding.inflate(inflater)
        navController = NavHostFragment.findNavController(this)
        mapView = binding.mapview
        mapKit = MapKitFactory.getInstance()

        requestLocPermission()
        mapKit.resetLocationManagerToDefault()

        trafficLayer = mapKit.createTrafficLayer(mapView.mapWindow)
        trafficLayer.isTrafficVisible = true

        userLocLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocLayer.isVisible = true
        userLocLayer.isHeadingEnabled = true

        userLocLayer.setObjectListener(this)

        return binding.root
    }

    private fun requestLocPermission(){
        if (ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        return
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        MapKitFactory.getInstance().onStop()
        mapView.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView.map.move(
            CameraPosition(Point(54.987041, 82.915476), 12.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocLayer.setAnchor(
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()),
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
        )
        userLocationView.arrow.setIcon(
            ImageProvider.fromResource(
                requireContext(), com.projectsova.R.drawable.van
            )
        )

        val pinIcon = userLocationView.pin.useCompositeIcon()

        pinIcon.setIcon(
            "icon",
            ImageProvider.fromResource(requireContext(), com.projectsova.R.drawable.icon),
            IconStyle().setAnchor(PointF(0f, 0f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(0f)
                .setScale(1f)
        )

        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(requireContext(), com.projectsova.R.drawable.search_result),
            IconStyle().setAnchor(PointF(0.5f, 0.5f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(1f)
                .setScale(0.5f)
        )

        userLocationView.accuracyCircle.fillColor = Color.BLUE and -0x66000001
    }

    override fun onObjectRemoved(p0: UserLocationView) {
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentOfMap()
    }

    override fun onDrivingRoutes(p0: MutableList<DrivingRoute>) {
        TODO("Not yet implemented")
    }

    override fun onDrivingRoutesError(p0: Error) {
        TODO("Not yet implemented")
    }
}