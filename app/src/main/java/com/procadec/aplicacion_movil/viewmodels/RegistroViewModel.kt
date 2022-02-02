package com.procadec.aplicacion_movil.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.procadec.aplicacion_movil.services.RetrofitService
import com.procadec.aplicacion_movil.services.entities.Usuario
import com.procadec.aplicacion_movil.services.repositories.UsuarioService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroViewModel: ViewModel() {
    private val service = RetrofitService.getRetrofit().create(UsuarioService::class.java)

    fun registro(usuario: Usuario.Register): LiveData<Int> {
        val liveData = MutableLiveData<Int>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                service.create(usuario).enqueue(object : Callback<Usuario.All> {
                    override fun onResponse(
                        call: Call<Usuario.All>,
                        response: Response<Usuario.All>,
                    ) {
                        Log.d("TAG", response.body().toString())
                        if(response.body() == null){
                            liveData.postValue(0)
                        }else{
                            liveData.postValue(1)
                        }
                    }

                    override fun onFailure(call: Call<Usuario.All>, t: Throwable) {
                        Log.e("ERROR", t.message!!)
                        liveData.postValue(-1)
                    }
                })
            }
        }
        return liveData
    }

}