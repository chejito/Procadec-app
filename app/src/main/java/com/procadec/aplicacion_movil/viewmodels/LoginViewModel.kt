package com.procadec.aplicacion_movil.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.procadec.aplicacion_movil.application.App
import com.procadec.aplicacion_movil.services.RetrofitService
import com.procadec.aplicacion_movil.services.entities.Usuario
import com.procadec.aplicacion_movil.services.repositories.LoginService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val service = RetrofitService.getRetrofit().create(LoginService::class.java)

    fun login(usuario: Usuario.Login): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                service.login(usuario).enqueue(object : Callback<Usuario.All> {
                    override fun onResponse(
                        call: Call<Usuario.All>,
                        response: Response<Usuario.All>,
                    ) {
                        Log.d("TAG", response.body().toString())
                        if(response.body() == null){
                            liveData.postValue(false)
                        }else{
                            response.body()?.let {
                                App.guardarUsuario(it)
                            }
                            liveData.postValue(true)
                        }
                    }

                    override fun onFailure(call: Call<Usuario.All>, t: Throwable) {
                        Log.e("ERROR", t.message!!)
                        liveData.postValue(false)
                    }
                })
            }
        }
        return liveData
    }

}