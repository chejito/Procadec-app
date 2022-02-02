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

class PerfilViewModel: ViewModel() {

    private val service = RetrofitService.getRetrofit().create(UsuarioService::class.java)

    fun actualizarUsuario(usuario: Usuario.All): LiveData<Usuario.All> {
        val liveData = MutableLiveData<Usuario.All>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                service.update(usuario.id, usuario).enqueue(object : Callback<Usuario.All> {
                    override fun onResponse(
                        call: Call<Usuario.All>,
                        response: Response<Usuario.All>
                    ) {
                        Log.d("TAG", response.body().toString())
                        liveData.postValue(response.body())
                    }

                    override fun onFailure(call: Call<Usuario.All>, t: Throwable) {
                        Log.e("ERROR", t.message!!)
                        liveData.postValue(null)
                    }
                })
            }
        }
        return liveData
    }

    fun eliminarUsuario(usuario: Usuario.All): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                service.delete(usuario.id).enqueue(object : Callback<Usuario.Delete> {
                    override fun onResponse(
                        call: Call<Usuario.Delete>,
                        response: Response<Usuario.Delete>
                    ) {
                        Log.d("TAG", response.body().toString())
                        liveData.postValue(true)
                    }

                    override fun onFailure(call: Call<Usuario.Delete>, t: Throwable) {
                        Log.e("ERROR", t.message!!)
                        liveData.postValue(false)
                    }
                })
            }
        }
        return liveData
    }
}