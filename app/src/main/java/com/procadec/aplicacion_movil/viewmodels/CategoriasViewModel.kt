package com.procadec.aplicacion_movil.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.procadec.aplicacion_movil.services.RetrofitService
import com.procadec.aplicacion_movil.services.entities.Categoria
import com.procadec.aplicacion_movil.services.repositories.CategoriaService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriasViewModel : ViewModel() {
    private val service = RetrofitService.getRetrofit().create(CategoriaService::class.java)

    fun cargarCategorias(): LiveData<List<Categoria.All>>{
        val liveData = MutableLiveData<List<Categoria.All>>()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                service.getAll().enqueue(object : Callback<List<Categoria.All>>{
                    override fun onResponse(
                        call: Call<List<Categoria.All>>,
                        response: Response<List<Categoria.All>>,
                    ) {
                        liveData.postValue(response.body())
                    }

                    override fun onFailure(call: Call<List<Categoria.All>>, t: Throwable) {
                        Log.e("ERROR", t.message!!)
                        liveData.postValue(null)
                    }
                })
            }
        }
        return liveData
    }

    fun crearCategoria(categoria: Categoria.Create): LiveData<Categoria.All> {
        val liveData = MutableLiveData<Categoria.All>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                service.create(categoria).enqueue(object : Callback<Categoria.All> {
                    override fun onResponse(
                        call: Call<Categoria.All>,
                        response: Response<Categoria.All>,
                    ) {
                        liveData.postValue(response.body())
                    }

                    override fun onFailure(call: Call<Categoria.All>, t: Throwable) {
                        Log.e("ERROR", t.message!!)
                        liveData.postValue(null)
                    }
                })
            }
        }
        return liveData
    }

    fun actualizarCategoria(categoria: Categoria.All): LiveData<Categoria.All> {
        val liveData = MutableLiveData<Categoria.All>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                service.update(categoria.id, categoria).enqueue(object : Callback<Categoria.All> {
                    override fun onResponse(
                        call: Call<Categoria.All>,
                        response: Response<Categoria.All>
                    ) {
                        liveData.postValue(response.body())
                    }

                    override fun onFailure(call: Call<Categoria.All>, t: Throwable) {
                        Log.e("ERROR", t.message!!)
                        liveData.postValue(null)
                    }
                })
            }
        }
        return liveData
    }

    fun eliminarCategoria(categoria: Categoria.All): LiveData<String> {
        val liveData = MutableLiveData<String>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                service.deleteById(categoria.id).enqueue(object : Callback<Categoria.Delete> {
                    override fun onResponse(
                        call: Call<Categoria.Delete>,
                        response: Response<Categoria.Delete>
                    ) {
                        liveData.postValue(response.body().toString())
                    }

                    override fun onFailure(call: Call<Categoria.Delete>, t: Throwable) {
                        Log.e("ERROR", t.message!!)
                        liveData.postValue(null)
                    }
                })
            }
        }
        return liveData
    }
}