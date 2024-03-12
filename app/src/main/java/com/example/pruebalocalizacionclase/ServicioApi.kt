package com.example.pruebalocalizacionclase

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface ServicioApi {
    @GET("/character")
    /*Esta funcion va a llamar a la web de Disney */
    /*Para que una corutina funcione, es obligatorio poner suspend primero. Esto significa que esta función
    puede parar y reanudar su lógica, en su comunicación con Retrofit. Parece síncrona, pero realmente no lo es*/
    suspend fun conseguirPersonajes():Response<PersonajesResponse>
    /*Se pone Response<Tipo> y no solo PersonajesResponse, para que podamos comprobar si la llamada al servicio
    * web ha sido satisfactoria o no. Si no ponemos Response, no podremos comprobar si ha funcionado la llamada a la web.*/
}
