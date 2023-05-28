package com.projectsova.presentation.Addresses

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.projectsova.domain.entity.Card
import kotlinx.coroutines.CoroutineExceptionHandler

class AddressVIewModel: ViewModel() {

    val cardList = mutableListOf<Card>()

    private val _stateContent = MutableLiveData<StateContent>(StateContent.Initial)
    val stateContent: LiveData<StateContent> = _stateContent

    private val loginVM: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val errorHandlerContent = CoroutineExceptionHandler { _, _ ->
        _stateContent.postValue(StateContent.Error)
    }

    private val db = Firebase.firestore

    lateinit var addressList: MutableList<String>
    lateinit var time: ArrayList<String>
    lateinit var deliviried: ArrayList<String>
    lateinit var id: ArrayList<String>

    fun read(name: String) {
        loginVM.value = name
        val addressesRef = db.collection("users").document(loginVM.value.toString()).collection("addresses")
        addressList = mutableListOf()
        deliviried = arrayListOf()
        time = arrayListOf()
        id = arrayListOf()
        addressesRef.get().addOnSuccessListener { result ->
            for (address in result) {
                addressList.add(address.id)
                deliviried.add("${address.data["deliveried"]}")
                time.add("${address.data["time"]}")
                id.add("${address.data["id"]}")
            }
        }.addOnFailureListener{
            Log.w("read", "Error getting products", it)
        }.addOnCompleteListener {
            if (!it.isSuccessful) _stateContent.value = StateContent.Error
            if(it.isComplete) getAddresses()
        }
    }

    fun getAddresses(){
        cardList.clear()
        for (i in addressList.indices) {
            val card = Card(id[i].toInt(),addressList[i],deliviried[i].toBoolean(), time[i])
            cardList.add(card)
        }
        cardList.sortWith(compareBy{it.id})
        _stateContent.value = StateContent.Content
    }

    fun getFirstAddress(): String{
        for (addresses in cardList){
            if (!addresses.arrived)
                return addresses.address
        }
        return ""
    }

    fun sendData(cardList: ArrayList<Card>){
        val addressesRef = db.collection("users").document(loginVM.value.toString()).collection("addresses")

        cardList.forEach{ card ->
            addressesRef.document(card.address).update("deliveried", card.arrived)
            addressesRef.document(card.address).update("id", card.id)
            addressesRef.document(card.address).update("time", card.time)
        }
    }
}