package com.kalex.sp_aplication.presentation.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.kalex.sp_aplication.presentation.composables.Drawer
import kotlinx.coroutines.CoroutineScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kalex.sp_aplication.camara.Permission
import com.kalex.sp_aplication.domain.model.ItemOfice
import com.kalex.sp_aplication.presentation.viewModels.OficesViewModel


import kotlinx.coroutines.launch


@ExperimentalPermissionsApi
@Composable
fun VerOficinas(navController: NavHostController,
                viewModel : OficesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    // pedir permisos
    Permission(
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        rationale = "Quieres ver las oficinas , debo acceder a tu ubicacion",
        permissionNotAvailableContent = {
            // Si no quiso dar permisos , Aparece esta pantalla
            Column() {
                Text("O noes! No permiso no Oficinas!")
                Spacer(modifier = Modifier.height(8.dp))
                Row {

                    Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            context.startActivity(
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            )
                        }
                    ) {
                        Text("Open Settings")
                    }
                    // If they don't want to grant permissions, this button will result in going back
                    /*Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            //TODO:
                        }
                    ) {
                        Text("Use Camera")
                    }*/
                }
            }
        },
    ) {
        val context = LocalContext.current

        getDeviceLocation(context,viewModel)
        GetOficinas(viewModel,navController)
    }

}


private  fun getDeviceLocation(context: Context, viewModel: OficesViewModel) {

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    {
        println("Accedio a getDeviceLocation")
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        try {
            val locationResult = fusedLocationProviderClient.lastLocation

            locationResult.addOnCompleteListener {
                    task ->
                if (task.isSuccessful){
                    val lastKnownLocation = task.result

                    if (lastKnownLocation != null){

                        val location = LatLng(
                            lastKnownLocation.altitude,
                            lastKnownLocation.longitude
                        )
                        println("ultima locacion del user : " + location)
                       viewModel.userLocation = location

                    }
                }else{
                    Log.d("Exception"," Current User location is null")
                }
            }



        }catch (e: SecurityException){
            Log.d("Exception", "Exception:  $e.message.toString()")
        }

    }else{
        Log.d("Exception", "Permission not granted")
    }


}


@Composable
fun GetOficinas(viewModel: OficesViewModel, navController: NavHostController) {
    //para menu lateral
    val scaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    )
    val scope = rememberCoroutineScope()

    val resp = viewModel.state.value

    if (resp.isLoading){
        Box(modifier = Modifier.fillMaxSize()
            , contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(modifier = Modifier
                .fillMaxSize(0.1f)
            )

        }
    }
    if (!resp.isLoading){

        val coordenadas = ArrayList<ItemOfice>()
        for (coordenada in resp.ofices?.Items!!) {
                coordenadas.add(coordenada)
        }

        ToolBarOfice(navController,scope,scaffoldState,coordenadas,viewModel)
    }
}

@Composable
fun ToolBarOfice(
    navController: NavHostController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    coordenadas: ArrayList<ItemOfice>,
    viewModel: OficesViewModel
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "Oficinas") },
                navigationIcon =
                {
                    IconButton(
                        onClick = { navController.popBackStack() },

                        ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "go back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "menu hamburgesa"
                        )
                    }
                },
                backgroundColor = Color.White
            )
        },
        drawerContent = { Drawer(scope, scaffoldState, navController,) },
        drawerGesturesEnabled = false
    ) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            MyMap(coordenadas,viewModel.userLocation) {}

        }

    }
}

@Composable
fun MyMap(coordenadas: ArrayList<ItemOfice>,
          userLocation : LatLng,
          onReady:(GoogleMap)-> Unit) {
    val context = LocalContext.current
    val mapView = remember{MapView(context)}
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    lifecycle.addObserver(rememberMapLifeCycle(map = mapView))
println("userLocation  : "  + userLocation)
    AndroidView(
        factory = {
            mapView.apply {
                mapView.getMapAsync{googleMap ->
                    val zoomLevel = 12f
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        LatLng(userLocation.latitude, userLocation.longitude),
                        zoomLevel))


                    for (coordenada in coordenadas) {
                        googleMap.addMarker(MarkerOptions()
                            .position(LatLng(coordenada.Latitud.toDouble(),coordenada.Longitud.toDouble()))
                            .title(coordenada.Nombre)
                            .snippet(coordenada.Ciudad))
                    }

                }
            }
        })
}

@Composable
fun rememberMapLifeCycle(map: MapView): LifecycleObserver {
    return remember {
        LifecycleEventObserver{source, event ->
            when(event){
                Lifecycle.Event.ON_CREATE -> map.onCreate(Bundle())
                Lifecycle.Event.ON_START -> map.onStart()
                Lifecycle.Event.ON_RESUME -> map.onResume()
                Lifecycle.Event.ON_PAUSE -> map.onPause()
                Lifecycle.Event.ON_STOP -> map.onStop()
                Lifecycle.Event.ON_DESTROY -> map.onDestroy()
                Lifecycle.Event.ON_ANY -> throw IllegalStateException()
            }
        }
    }
}