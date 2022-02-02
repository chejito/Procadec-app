package com.procadec.aplicacion_movil.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

    companion object {
        private var _retrofit: Retrofit? = null
        fun getRetrofit():Retrofit{
            if(_retrofit==null){
                _retrofit=Retrofit.Builder()
                    .baseUrl("https://procadec-pruebas.herokuapp.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return _retrofit!!
        }
    }
}