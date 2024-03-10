package com.example.pruebalocalizacionclase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.model.LatLng

class MainActivity : AppCompatActivity(), OnButtonClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = BlankFragment()

        //Capturamos los botones
        val botonRuta1=findViewById<Button>(R.id.botonRuta1)
        val botonRuta2=findViewById<Button>(R.id.botonRuta2)
        val botonRuta3=findViewById<Button>(R.id.botonRuta3)

        // Configuramos los listeners de los botones
        botonRuta1.setOnClickListener { onButtonClick(1) }
        botonRuta2.setOnClickListener { onButtonClick(2) }
        botonRuta3.setOnClickListener { onButtonClick(3) }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onButtonClick(valor: Int) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as BlankFragment
        fragment.calcularRuta(valor)
    }

}

/*Definimos una interfaz para capturar el botón que se pulsa en la mainActivity*/
interface OnButtonClickListener {
    fun onButtonClick(valor: Int)
}
