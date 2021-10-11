package com.example.heads_up

import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @Headers("Content-Type: application/json")
    @GET("celebrities/")
    fun getdat(): Call<List<celep.dat>>


    @Headers("Content-Type: application/json")
    @POST("celebrities/")
    fun adddat(@Body data: celep.dat): Call<celep.dat>

    @Headers("Content-Type: application/json")
    @PUT("celebrities/{id}")
    fun updatedat(@Path("id")id:Int, @Body data: celep.dat): Call<celep.dat>

    @Headers("Content-Type: application/json")
    @DELETE("celebrities/{id}")
    fun deldat(@Path("id")id:Int): Call<celep.dat>
}