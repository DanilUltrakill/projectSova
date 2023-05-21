package com.projectsova.domain.usecases

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class GetData {
    private val db = Firebase.firestore

    private lateinit var password: String
    lateinit var userName: String
    lateinit var addressList: MutableList<String>
    lateinit var phoneNumber: String
    lateinit var time: ArrayList<String>
    lateinit var deliviried: ArrayList<String>
    lateinit var id: ArrayList<String>
    val resultPass: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun login() {
        password = "password"
        userName = "FIO"
        val userRef = db.collection("users")
        userRef.get().addOnSuccessListener { result ->
            for (user in result) {
                if (user.id == "") {
                    userName = user.get("name").toString()
                    password = user.get("password").toString()
                }
            }
            Log.w("Login", "$userName = $password")
        }.addOnFailureListener{
            Log.w("Login", "User not found", it)
        }.addOnCompleteListener(){
            if(it.isSuccessful)
                resultPass.value = password
        }
    }

    val spisok = listOf("Безалкогольные напитки ПЭТ 1,0", "Безалкогольные напитки ПЭТ 1,5", "Безалкогольные напитки ПЭТ 0,5",
            "Безалкогольные напитки, СТЕКЛО 0,5", "Лимонад разливной", "Пиво ПЭТ 1,5 л",
                "Пиво в стеклобутылке, СТЕКЛО 0,5", "Пиво разливное")

    val nonalco10 = mapOf(Pair("Брусника ПЭТ 1,0", "1"), Pair("Бабл-Гам	ПЭТ 1,0", "0"), Pair("Клубника-банан ПЭТ 1,0", 2), Pair("Кола ПЭТ 1,0", 0), Pair("Лимонад ПЭТ 1,0", 0),
    Pair("Манго-Маракуйя ПЭТ 1,0", 2), Pair("Мохито ПЭТ 1,0", 1), Pair("Тархун ПЭТ 1,0", 1))

    val nonalco15 = mapOf(Pair("Алатау ПЭТ 1,5", "0"), Pair("Груша ПЭТ 1,5", "0"), Pair("Квас ПЭТ 1,5", 0), Pair("Кола ПЭТ 1,5", 1), Pair("Лимонад ПЭТ 1,5", 0),
        Pair("Тархун ПЭТ 1,5", 0), Pair("Алатау ПЭТ 5,0", 1))

    val nonalco05 = mapOf(Pair("Кола ПЭТ 0,5", "3"), Pair("Лимонад ПЭТ 0,5", "0"), Pair("Груша ПЭТ 0,5", 0), Pair("Тархун ПЭТ 0,5", 1))

    val nonalco05glass = mapOf(Pair("Брусника с/бут 0,5", "1"), Pair("Бабл-Гам с/бут 0,5", "0"), Pair("Груша с/бут 0,5", 0), Pair("Клубника-банан с/бут 0,5", 0),
        Pair("Кола с/бут 0,5", 0),
        Pair("Лимонад с/бут 0,5", 2), Pair("Манго-Маракуйя с/бут 0,5", 1), Pair("Мохито с/бут 0,5", 1), Pair("Тархун с/бут 0,5", 0))

    val pivo15 = mapOf(Pair("Баварское ПЭТ 1,5", "3"), Pair("Гурмэ ПЭТ 1,5", "0"), Pair("Жигулевское ПЭТ 1,5", 3), Pair("Злата Прага ПЭТ 1,5", 1), Pair("Лагер ПЭТ 1,5", 0),
        Pair("Московское барное ПЭТ 1,5", 0), Pair("Колос Сибири ПЭТ 1,5", 10))

    val pivo05glass = mapOf(Pair("Баварское с/бут 0,5", "3"), Pair("Брюнн с/бут 0,5", "0"), Pair("Жигулевское с/бут 0,5", 2), Pair("Лагер с/бут 0,5", 15),
        Pair("Московское барное с/бут 0,5", 0),
        Pair("СССР с/бут 0,5", 2), Pair("Хмельная шельма/Ш.стаут с/бут 0,5", 7), Pair("Чешское классическое с/бут 0,5", 5), Pair("Колос Сибири с/бут 0,5", "0"))

    val lemonad50 = mapOf(Pair("Брусника КЕГА 50л", "1"), Pair("Груша КЕГА 50л", "0"), Pair("Квас КЕГА 50л", 1), Pair("Клубника-банан КЕГА 50л", 0), Pair("Лимонад КЕГА 50л", 0),
        Pair("Манго-Маракуйя КЕГА 50л", 0), Pair("Мохито КЕГА 50л", 0), Pair("Тархун КЕГА 50л", 0))

    val pivoraz = mapOf(Pair("Баварское КЕГА 50л", "0"), Pair("Брюнн КЕГА 50л", "0"), Pair("Голд Сова КЕГА 50л", 2), Pair("Гурмэ КЕГА 50л", 1),
        Pair("Жигулевское КЕГА 50л", 2),
        Pair("Злата Прага КЕГА 50л", 2), Pair("Лагер КЕГА 50л", 3), Pair("Московское барное КЕГА 50л", 0), Pair("Нефил. Жиг КЕГА 50л", "0"),
        Pair("Танковэ н/ф КЕГА 50л", 1), Pair("Хмельная шельма/Ш.стаут КЕГА 50л", 1), Pair("Чешское классическое КЕГА 50л", 0), Pair("Колос Сибири КЕГА 50л", "0"))

    val map = mapOf(Pair("Kolya", "228"), Pair("a", 3))

    val spisokspiska = listOf(nonalco10, nonalco15, nonalco05, nonalco05glass, pivo15, pivo05glass, lemonad50, pivoraz)

    fun fillData(){
        var count=0
        val userRef = db.collection("users").document("Ivanov").collection("addresses")
        userRef.get().addOnSuccessListener {
            for (doc in it){
                /*userRef.document(doc.id).collection("products").get().addOnSuccessListener { result ->
                    for (res in result){
                        for (i in spisok.indices) {
                            if (spisok[i] == res.id)
                                for ((k,v) in spisokspiska[i])
                                    userRef.document(doc.id).collection("products")
                                        .document(res.id).update(FieldPath.of(k),v)
                        }
                    }
                }*/

                userRef.document(doc.id).update("id", count)
                userRef.document(doc.id).update("time", "")
                userRef.document(doc.id).update("номер телефона", "8999123748$count")
                userRef.document(doc.id).update("deliveried", false)

                /*for (name in spisok)
                    userRef.document(doc.id).collection("products").document(name).set(map)*/

                count++
            }
        }
    }

    fun read(name: String) {
        val addressesRef = db.collection("users").document(name).collection("addresses")
        Log.w("read", "Enter reading")
        addressList = mutableListOf()
        deliviried = arrayListOf()
        time = arrayListOf()
        id = arrayListOf()
        addressesRef.get().addOnSuccessListener { result ->
            Log.w("read", "Enter listener")
            for (address in result) {
                addressList.add(address.id)
                deliviried.add("${address.data["deliviried"]}")
                time.add("${address.data["time"]}")
                id.add("${address.data["id"]}")
            }
        }.addOnFailureListener{
            Log.w("read", "Error getting products", it)
        }.addOnCompleteListener{
            Log.w("read", "Read completed")
        }
        Log.w("read", "$addressList")
    }

    fun getAddInfo(name: String, addressName: String): String {
        Log.w("Map", "Enter")

        val productList: MutableMap<String,String> = mutableMapOf()
        val productString = ""
        db.collection("users").document(name).collection("addresses").document(addressName).get().addOnSuccessListener {
            phoneNumber = it.get("номер телефона").toString()
        }.addOnFailureListener{
            Log.w("Phone", "not working", it)
        }

        val userRef =
            db.collection("users").document(name).collection("addresses").document(addressName).collection("products")
        userRef.get().addOnSuccessListener { result ->
            Log.w("Map", "Enter 2")
            for (product in result) {
                val data = product.data
                Log.w("Map", "Enter 3")
                for ((k, v) in data) {
                    productString.plus("$k, кол-во: $v").plus("\n")
                    productList[k] = v.toString()
                    Log.w("Map", "$k = $v")
                }
            }
            Log.w("List", "$productList")
            Log.w("List", "${productList.size}")
        }.addOnFailureListener {
            Log.w("Pizda", "not working", it)
        }
        return productString
    }
}
