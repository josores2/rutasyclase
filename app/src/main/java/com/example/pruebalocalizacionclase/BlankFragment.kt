package com.example.pruebalocalizacionclase
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BlankFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationClickListener{

    private lateinit var map: GoogleMap
    private lateinit var loki:LocHandler
    private var start:String = ""
    private var end:String = ""
    var poly : Polyline? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enchufoElMapa()
    }

    private fun enchufoElMapa() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(nico: GoogleMap) {
        map=nico
        /*nos subscribimos al listener*/
        map.setOnMyLocationClickListener(this)
        loki = LocHandler(requireContext(),requireActivity(),map)
        loki.activarLocalizacion()
    }

    fun calcularRuta(valor: Int) {
        if(valor==1){
            poly?.remove()
            poly = null
            end = "-0.3282783, 39.47694"
            crearRuta()
        }else if (valor==2) {
            poly?.remove()
            poly = null
            end = "-0.3552569, 39.4021309"
            crearRuta()
        }else{
            poly?.remove()
            poly = null
            end = "-0.3345141, 39.4895194"
            crearRuta()
        }
    }

    fun crearRuta(){
        Log.i("PEPE CREAR RUTA START: ",start)
        Log.i("PEPE CREAR RUTA END: ",end)
        /*Las llamadas a internet no se pueden hacer en el hilo principal, hay que hacerlo en corutinas
        para ello ejecutamos CoroutineScope(Dispatcher)  */

        CoroutineScope(Dispatchers.IO).launch {
            /*Lanzamos el hilo a través de la interfaz que hemos creado y la func conseguirRuta de la misma*/
            val llamada = getRetrofit().create(ServicioApi::class.java).conseguirRuta("5b3ce3597851110001cf62482f4f1876a2644f81ac0aaf6d860f35de",start,end)
            /*getRetrofit() puede devolver codigos de error si nos hemos equivocado al construir la url, si la web no está
            disponible, etc. En ese caso, no entraría en el siguiente if:*/
            if(llamada.isSuccessful){
                /*Si la llamada ha recogido la ruta, ya la tengo en esta variable*/
                /*Necesitamos permiso de internet en el manifest, para poder acceder a la web de rutas*/
                Log.i("PEPE TENGO RUTA:", "OK")
                // withContext(Dispatchers.Main) {
                activity?.runOnUiThread{
                    Toast.makeText(requireContext(),"Llamada dentro de la corrutina exisoto", Toast.LENGTH_SHORT).show()
                }
                dibujarRuta(llamada.body())
            }else{
                Log.i("PEPE TENGO RUTA:","NO")
            }
        }//FIN DE LA CORRUTINA
    }

    private fun dibujarRuta(respuestaDeRuta: RespuestaDeRuta?) {
        /*Rellenamos el poliline con los datos que nos ha devuelto el servicio*/
        val polilineOptions = PolylineOptions()

        respuestaDeRuta?.features?.first()?.geometry?.coordinates?.forEach{
            /*para cada linea de la listaagregamos a la variable polilineOptions, el valor capturado(lati,longit)*/
            polilineOptions.add(LatLng(it[1],it[0]))
            /*Ponemos 1 primero, pq la api devuelve primero la latitud y no la longitud*/
        }
        //withContext(Dispatchers.Main){
        activity?.runOnUiThread{
            /*Ejecutamos la acción de dibujado en la corutina*/
            polilineOptions.color(Color.RED)
            poly = map.addPolyline(polilineOptions)
        }
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(requireContext(),"Estás en ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_SHORT).show()
        start = "${p0.longitude},${p0.latitude}"
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}