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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.kalex.sp_aplication.camara.Permission
import com.kalex.sp_aplication.domain.model.ItemOfice
import com.kalex.sp_aplication.presentation.composables.Drawer
import com.kalex.sp_aplication.presentation.viewModels.OficesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@Composable
fun VerOficinas(
    navController: NavHostController,
    viewModel: OficesViewModel = hiltViewModel(),
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
                                },
                            )
                        },
                    ) {
                        Text("Open Settings")
                    }
                    // If they don't want to grant permissions, this button will result in going back
                }
            }
        },
    ) {
        getDeviceLocation(context, viewModel)
        GetOficinas(viewModel, navController)
    }
}

private fun getDeviceLocation(context: Context, viewModel: OficesViewModel) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        try {
            val locationResult = fusedLocationProviderClient.lastLocation

            locationResult.addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    val lastKnownLocation = task.result

                    if (lastKnownLocation != null) {
                        val location = LatLng(
                            lastKnownLocation.altitude,
                            lastKnownLocation.longitude,
                        )
                        println("UserLocation : $location")
                        viewModel.userLocation = location
                    }
                } else {
                    Log.d("Exception", " Current User location is null")
                }
            }
        } catch (e: SecurityException) {
            Log.d("Exception", "Exception:  $e.message.toString()")
        }
    } else {
        Log.d("Exception", "Permission not granted")
    }
}

@Composable
fun GetOficinas(viewModel: OficesViewModel, navController: NavHostController) {
    // para menu lateral
    val scaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    )
    val scope = rememberCoroutineScope()

    val resp = viewModel.state.value

    if (resp.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(0.1f),
            )
        }
    }
    if (!resp.isLoading) {
        // coordenadas de cada oficina
        val coordenadas = ArrayList<ItemOfice>()
        for (coordenada in resp.ofices?.Items!!) {
            coordenadas.add(coordenada)
        }

        ToolBarOfice(navController, scope, scaffoldState, coordenadas, viewModel)
    }
}

@Composable
fun ToolBarOfice(
    navController: NavHostController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    coordenadas: ArrayList<ItemOfice>,
    viewModel: OficesViewModel,
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
                            contentDescription = "go back",
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
                            contentDescription = "menu hamburgesa",
                        )
                    }
                },
                backgroundColor = Color.White,
            )
        },
        drawerContent = { Drawer(scope, scaffoldState, navController) },
        drawerGesturesEnabled = false,
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            MyMap(coordenadas, viewModel.userLocation)
        }
    }
}

@Composable
fun MyMap(
    coordenadas: ArrayList<ItemOfice>,
    userLocation: LatLng = LatLng(6.217, -75.567),
) {
    val mapView = rememberMapViewWithLifeCycle()

    AndroidView(
        { mapView },
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            val map = mapView.awaitMap()
            map.uiSettings.isZoomControlsEnabled = true
            Log.d("locacion", "UserLocation : $userLocation")
            val medellin = LatLng(6.217, -75.567)
            Log.d("locacion", "$medellin")

            val zoomLevel = 12f
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    userLocation,
                    zoomLevel,
                ),
            )

            for (coordenada in coordenadas) {
                map.addMarker(
                    MarkerOptions()
                        .position(LatLng(coordenada.Latitud.toDouble(), coordenada.Longitud.toDouble()))
                        .title(coordenada.Nombre)
                        .snippet(coordenada.Ciudad),
                )
            }
        }
    }
}

@Composable
fun rememberMapViewWithLifeCycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = com.google.maps.android.ktx.R.id.map_frame
        }
    }
    val lifeCycleObserver = rememberMapLifecycleObserver(mapView)
    val lifeCycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifeCycle) {
        lifeCycle.addObserver(lifeCycleObserver)
        onDispose {
            lifeCycle.removeObserver(lifeCycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }
