package com.projectsova.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.projectsova.data.MapKitInitializer
import com.projectsova.databinding.FragmentOfMapBinding
import com.yandex.mapkit.*
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.*
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.mapkit.traffic.TrafficLayer
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError


class FragmentOfMap : Fragment(), UserLocationObjectListener, DrivingSession.DrivingRouteListener,
    Session.SearchListener, CameraListener {

    lateinit var navController: NavController
    lateinit var binding: FragmentOfMapBinding

    lateinit var mapKit: MapKit
    lateinit var mapView: MapView

    lateinit var userLocLayer: UserLocationLayer
    lateinit var trafficLayer: TrafficLayer

    lateinit var searchManager: SearchManager
    lateinit var searchSession: Session

    lateinit var mapObjects: MapObjectCollection

    lateinit var drivingRouter: DrivingRouter
    lateinit var  drivingSession: DrivingSession

    private lateinit var ROUTE_START_LOCATION: Point
    private lateinit var ROUTE_END_LOCATION: Point
    private lateinit var SCREEN_CENTER: Point

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var address: String
    lateinit var resultLocation: Point

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

        address = arguments?.getString("address")!!

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

        SearchFactory.initialize(requireContext())
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

        mapView.map.addCameraListener(this)

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        mapObjects = mapView.map.mapObjects.addCollection()

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

        submitQuery(address)

        mapView.map.move(
            CameraPosition(Point(59.945933, 30.320045), 12.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
    }

    @SuppressLint("MissingPermission")
    private fun submitRequest() {

        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()

        val requestPoints: ArrayList<RequestPoint> = ArrayList()
        requestPoints.add(
            RequestPoint(
                ROUTE_START_LOCATION,
                RequestPointType.WAYPOINT,
                null
            )
        )
        requestPoints.add(
            RequestPoint(
                ROUTE_END_LOCATION,
                RequestPointType.WAYPOINT,
                null
            )
        )
        drivingSession =
            drivingRouter.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this)
    }

    private fun submitQuery(query:String){
        searchSession = searchManager.submit("город Новосибирск $query", VisibleRegionUtils.toPolygon(mapView.map.visibleRegion), SearchOptions(), this)
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
        for (route in p0){
            mapObjects!!.addPolyline(route.geometry)
        }
    }

    override fun onDrivingRoutesError(p0: Error) {
        Toast.makeText(requireContext(),"Ошибка!", Toast.LENGTH_SHORT).show()
    }

    override fun onSearchResponse(response: Response) {
        mapObjects = mapView.map.mapObjects
        //mapObjects.clear(
        resultLocation = response.collection.children[0].obj!!.geometry[0].point!!
        ROUTE_END_LOCATION = resultLocation
        mapObjects.addPlacemark(resultLocation,ImageProvider.fromResource(requireContext(),com.projectsova.R.drawable.search_result))

        getLocations()

        /*for(searchResult in response.collection.children){
            resultLocation = searchResult.obj!!.geometry[0].point!!

        }*/
    }

    @SuppressLint("MissingPermission")
    private fun getLocations() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: android.location.Location ->
            ROUTE_START_LOCATION = Point(location.latitude.toString().toDouble(), location.longitude.toString().toDouble())
        }.addOnCompleteListener {
            //ROUTE_END_LOCATION = resultLocation

            SCREEN_CENTER = Point(
                (ROUTE_START_LOCATION.latitude + ROUTE_END_LOCATION.latitude) / 2,
                (ROUTE_START_LOCATION.longitude + ROUTE_END_LOCATION.longitude) / 2
            )

            /*mapView.map.move(
                CameraPosition(SCREEN_CENTER, 12.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0F),
                null
            )*/

            submitRequest()
        }
    }

    override fun onSearchError(p0: Error) {
        var errorMessage = "Неизвестная ошибка!"
        if (p0 is RemoteError){
            errorMessage = "Другая ошибка!"
        }else if (p0 is NetworkError){
            errorMessage = "Проблема с соединением!"
        }
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        cameraUpdateReason: CameraUpdateReason,
        finished: Boolean
    ) {
        if(finished){
            submitQuery(address)
        }
    }
}