package com.projectsova.presentation.AddInfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import kotlinx.coroutines.CoroutineExceptionHandler

class AddInfoViewModel: ViewModel() {

    private val db = Firebase.firestore

    val resultProductString: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val phoneNumber: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    lateinit var productString: String

    private val _stateAddInfo = MutableLiveData<StateAddInfo>(StateAddInfo.Initial)
    val stateAddInfo: LiveData<StateAddInfo> = _stateAddInfo

    private val errorHandlerAddInfo = CoroutineExceptionHandler { _, _ ->
        _stateAddInfo.postValue(StateAddInfo.Error)
    }

    fun getAddInfo(name: String, addressName: String) {
        Log.w("vm3", "Enter")

        val productList: MutableMap<String,String> = mutableMapOf()
        productString = ""
        db.collection("users").document(name).collection("addresses").document(addressName).get().addOnSuccessListener {
            phoneNumber.value = it.get("номер телефона").toString()
        }.addOnFailureListener{
            Log.w("vm3", "not working", it)
        }

        val userRef =
            db.collection("users").document(name).collection("addresses").document(addressName).collection("products")
        userRef.get().addOnSuccessListener { result ->
            for (product in result) {
                val data = product.data
                for ((k, v) in data) {
                    if (v.toString()!="0") {
                        productString = "$productString$k, кол-во: $v\n"
                        productList[k] = v.toString()
                        Log.w("vm3", "$k = $v")
                        Log.w("vm3", productString)
                    }
                }
            }
        }.addOnFailureListener {
            Log.w("vm3", "not working", it)
        }.addOnCompleteListener {
            if (!it.isSuccessful) _stateAddInfo.value = StateAddInfo.Error
            if(it.isComplete){
                resultProductString.value = productString
                _stateAddInfo.value = StateAddInfo.Content
            }
        }
    }
}