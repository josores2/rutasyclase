package com.example.pruebalocalizacionclase

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.GoogleMap

class LocHandler(var cont:Context, var actv:Activity, var map:GoogleMap) : FragmentActivity() {
/*Hemos tenido que extender la clase a FragmentActivity() para poder acceder a los override de
onRequestPermissionResult y onResume*/

    companion object{
        const val REQUEST_CODE_LOCATION = 0
    }
   /***********************************************************************************/
    /*Esta funcion comprueba si el permiso de localización está activada o no*/
    fun localizacionPermitida():Boolean{
        /*Comprobamos si está el permiso concedido o no. Pasamos requireContext en vez de this,
        ya que estamos dentro de un fragment y necesitamos el contexto*/

        return ContextCompat.checkSelfPermission(cont,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    /***********************************************************************************/

    fun activarLocalizacion(){
        /*si el mapa no ha sido inicializado, salimos, ya que aun no podemos pedir nada*/
        if(map==null) return

        if(localizacionPermitida()){
            /*el usuario ha permitido la localizacion*/
            map.isMyLocationEnabled = true
        }else{
            /*El usuario no ha permitido la localizacion, entonces se lo pedimos*/
            pedirLocalizacion()
        }
    }

    /***********************************************************************************/

    private fun pedirLocalizacion(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(actv,Manifest.permission.ACCESS_FINE_LOCATION)){
            /*Se pidió el permiso, pero el usuario no lo aceptó*/
            Toast.makeText(cont,"Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        }else{
            /*Es la primera vez que se piden los permisos. Le pasamos el ultimo argumento, con una constante que hemos creado como
            * companion object de esta clase, para almacenar si el usuario acepta o no el permiso*/
            ActivityCompat.requestPermissions(actv, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    /***********************************************************************************/
    /*En esta funcion tenemos que capturar la respuesta del usuario que acepta los permisos.
        * La buscamos en Code*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        /*Aqui implementamos la lógica para ver si el permiso ha sido aceptado*/
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled=true
            }else{
                /*El usuario ha rechazado el permiso*/
                Toast.makeText(cont,"Para activar la ubicación, ve a ajustes y acepta el permiso", Toast.LENGTH_SHORT).show()
            }
            /*Si se hubiera pasado otro permiso que no fuera el de ubicacion, que no va a pasar*/
            else -> {}
        }
    }
    /***********************************************************************************/
    /*Tengo que crear esta función por si el usuario ha minimizado nuestra app y ha revocado los permisos*/
    override fun onResume() {
        super.onResume()
        /*Primero comprobamos si el mapa está cargado*/
        if(map==null) return
        if(!localizacionPermitida()){
            map.isMyLocationEnabled = false
            Toast.makeText(cont,"Para activar la ubicación, ve a ajustes y acepta el permiso", Toast.LENGTH_SHORT).show()
        }
    }

}