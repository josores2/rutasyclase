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
import android.widget.FrameLayout
import android.widget.TextView
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

class BlankFragment : Fragment(){

    private lateinit var textView: TextView
    private lateinit var frameLayout: FrameLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_blank, container, false)

        textView = root.findViewById(R.id.textViewPersonajes)
        frameLayout = root.findViewById(R.id.frameLayoutImagen)
        llamaDisney()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun llamaDisney(){
        /*Las llamadas a internet no se pueden hacer en el hilo principal, hay que hacerlo en corutinas
        para ello ejecutamos CoroutineScope(Dispatcher)  */

        CoroutineScope(Dispatchers.IO).launch {
            /*Lanzamos el hilo a través de la interfaz que hemos creado y la func conseguirRuta de la misma*/
            val llamada = getRetrofit().create(ServicioApi::class.java).conseguirPersonajes()
            /*getRetrofit() puede devolver codigos de error si nos hemos equivocado al construir la url, si la web no está
            disponible, etc. En ese caso, no entraría en el siguiente if:*/
            if(llamada.isSuccessful){
                /*Si la llamada ha recogido la ruta, ya la tengo en esta variable*/
                /*Necesitamos permiso de internet en el manifest, para poder acceder a la web de rutas*/
                Log.i("PEPE TENGO PERSONAJES", "OK")
                // withContext(Dispatchers.Main) {
                val data = llamada.body()?.data

                listarPersonajes(data)

            }else{
                Log.i("PEPE TENGO PERSONAJES:","NO")
            }
        }//FIN DE LA CORRUTINA

    }

    fun listarPersonajes(valores: List<Data>?){

        val stringBuilder = StringBuilder()
        if (valores != null) {
            valores.forEach { pepe ->
                stringBuilder.append("Personaje: ${pepe.name} \n")
            }
        }
        // withContext(Dispatchers.Main) {
        activity?.runOnUiThread {
            textView.text = stringBuilder.toString()
        }
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.disneyapi.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun mostrarImagen(imageUrl: String) {
        // Aquí cargarías la imagen desde la URL y la mostrarías en un ImageView dentro del FrameLayout
        // Por simplicidad, aquí solo imprimimos la URL
        Log.i("Imagen URL:", imageUrl)
    }
}