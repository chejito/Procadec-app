package com.procadec.aplicacion_movil.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.procadec.aplicacion_movil.services.RetrofitService
import com.procadec.aplicacion_movil.services.entities.Producto
import com.procadec.aplicacion_movil.services.repositories.ProductoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductosViewModel : ViewModel() {
    private val service = RetrofitService.getRetrofit().create(ProductoService::class.java)

    fun cargarProductos(): LiveData<List<Producto.All>> {
        val liveData = MutableLiveData<List<Producto.All>>()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                service.getAll().enqueue(object : Callback<List<Producto.All>> {
                    override fun onResponse(
                        call: Call<List<Producto.All>>,
                        response: Response<List<Producto.All>>,
                    ) {
                        liveData.postValue(response.body())
                    }

                    override fun onFailure(call: Call<List<Producto.All>>, t: Throwable) {
                        Log.e("ERROR", t.message!!)
                        liveData.postValue(null)
                    }
                })
            }
        }
        return liveData
    }

    fun crearProducto(producto: Producto.Create): LiveData<Producto.All> {
        val liveData = MutableLiveData<Producto.All>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                service.create(producto).enqueue(object : Callback<Producto.All> {
                    override fun onResponse(
                        call: Call<Producto.All>,
                        response: Response<Producto.All>,
                    ) {
                        liveData.postValue(response.body())
                    }

                    override fun onFailure(call: Call<Producto.All>, t: Throwable) {
                        Log.e("ERROR", t.message!!)
                        liveData.postValue(null)
                    }
                })
            }
        }
        return liveData
    }

    fun actualizarProducto(producto: Producto.All): LiveData<Producto.All> {
        val liveData = MutableLiveData<Producto.All>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                service.update(producto.id, producto).enqueue(object : Callback<Producto.All> {
                    override fun onResponse(
                        call: Call<Producto.All>,
                        response: Response<Producto.All>
                    ) {
                        liveData.postValue(response.body())
                    }

                    override fun onFailure(call: Call<Producto.All>, t: Throwable) {
                        Log.e("ERROR", t.message!!)
                        liveData.postValue(null)
                    }
                })
            }
        }
        return liveData
    }

    fun eliminarProducto(producto: Producto.All): LiveData<String> {
        val liveData = MutableLiveData<String>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                service.delete(producto.id).enqueue(object : Callback<Producto.Delete> {
                    override fun onResponse(
                        call: Call<Producto.Delete>,
                        response: Response<Producto.Delete>
                    ) {
                        liveData.postValue(response.body().toString())
                    }

                    override fun onFailure(call: Call<Producto.Delete>, t: Throwable) {
                        Log.e("ERROR", t.message!!)
                        liveData.postValue(null)
                    }
                })
            }
        }
        return liveData
    }
}