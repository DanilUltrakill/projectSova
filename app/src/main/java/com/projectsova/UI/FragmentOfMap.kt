package com.projectsova.UI

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.projectsova.databinding.FragmentOfMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView


class FragmentOfMap : Fragment() {

    lateinit var navController: NavController
    lateinit var binding: FragmentOfMapBinding

    private val apiKey = "2f085e5d-4400-4f49-91fa-be91163cbf86"
    lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        MapKitFactory.setApiKey("2f085e5d-4400-4f49-91fa-be91163cbf86")
        binding = FragmentOfMapBinding.inflate(inflater)
        navController = NavHostFragment.findNavController(this)
        mapView = binding.mapview

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MapKitFactory.initialize(requireContext())

        mapView.map.move(
            CameraPosition(Point(54.987041, 82.915476), 5.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 300F),
            null
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentOfMap()
    }
}