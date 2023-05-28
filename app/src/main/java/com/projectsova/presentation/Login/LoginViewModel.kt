package com.projectsova.presentation.Login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineExceptionHandler

open class LoginViewModel : ViewModel() {

    private val db = Firebase.firestore

    lateinit var password: String

    private val _stateLogin = MutableLiveData<StateLogin>(StateLogin.Initial)
    val stateLogin: LiveData<StateLogin> = _stateLogin

    val loginVM: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val userName = MutableLiveData("FIO")
    val resultPass: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val errorHandlerLogin = CoroutineExceptionHandler { _, _ ->
        _stateLogin.postValue(StateLogin.Error)
    }

    fun login(login: String = "Ivanov") {
        password = "default"
        val userRef = db.collection("users")
        userRef.get().addOnSuccessListener {
            for (user in it) {
                if (login == user.id) {
                    userName.value = user.get("name").toString()
                    password = user.get("password").toString()
                }
            }
        }.addOnFailureListener{
            Log.w("GetPass", "User not found", it)
        }.addOnCompleteListener {
            if (it.isComplete) {
                resultPass.value = password
                _stateLogin.value = StateLogin.Authorization
            }
        }
    }

    fun startLogin(){
        _stateLogin.value = StateLogin.GettingPassword
    }

    fun getPass(login: String){
        login(login)
        loginVM.value = login
    }

    fun comparingPass(password: String){
        if (resultPass.value == password) {
            Log.w("VMLog", "${resultPass.value} and $password")
            _stateLogin.value = StateLogin.Authorized
        }
        else {
            Log.w("VMLog", "${resultPass.value} and $password")
            _stateLogin.value = StateLogin.NotAuthorized
        }
    }

    fun waitingLogin(){
        _stateLogin.value = StateLogin.Initial
    }
}
