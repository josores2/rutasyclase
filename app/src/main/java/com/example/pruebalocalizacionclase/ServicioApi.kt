package com.example.pruebalocalizacionclase

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface ServicioApi {
    @GET("/character")
    suspend fun conseguirPersonajes():Response<PersonajesResponse>
}
