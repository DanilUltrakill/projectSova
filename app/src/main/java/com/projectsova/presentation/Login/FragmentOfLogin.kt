package com.projectsova.presentation.Login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.projectsova.R
import com.projectsova.databinding.FragmentLoginBinding
import com.projectsova.domain.usecases.GetData

class FragmentOfLogin : Fragment() {

    private var showpass = false
    val getData = GetData()
    lateinit var binding: FragmentLoginBinding
    lateinit var navController: NavController
    private  val viewmodel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        navController = NavHostFragment.findNavController(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setObservers()

        with (binding) {
            btnLogin.setOnClickListener {
                login()
            }
            imShowpass.setOnClickListener {
                showPassword()
            }
        }
    }
    
    fun setObservers(){
        viewmodel.stateLogin.observe(viewLifecycleOwner, ::handleState)
    }

    private fun clearfields() {
        binding.txtPassword.text.clear()
        binding.txtLogin.text.clear()
    }

    private fun moveToContent() {
        Toast.makeText(requireContext(),"Здарвствуйте, ${viewmodel.userName.value}", Toast.LENGTH_SHORT).show()

        val bundle = Bundle()
        bundle.putString("name", viewmodel.loginVM.value)
        navController.navigate(R.id.action_fragmentOfLogin_to_fragmentOfAddresses, bundle)

        viewmodel.waitingLogin()
    }

    private fun handleState(state: StateLogin) {
        when (state) {
            is StateLogin.Initial -> clearfields()

            is StateLogin.GettingPassword -> getPass()

            is StateLogin.Authorization -> comparingPass()

            is StateLogin.Authorized -> moveToContent()

            is StateLogin.NotAuthorized,
            StateLogin.Error-> tryAgain()
        }
    }

    private fun comparingPass() {
        viewmodel.comparingPass(binding.txtPassword.text.toString())
    }

    private fun tryAgain() {
        Toast.makeText(requireContext(),R.string.error_login, Toast.LENGTH_SHORT).show()
        viewmodel.waitingLogin()
    }

    fun showPassword(){
        if (showpass){
            binding.txtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            showpass = false
        }
        else{
            binding.txtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            showpass = true
        }
    }

    fun getPass(){
        viewmodel.getPass(binding.txtLogin.text.toString())
    }

    fun login(){
        viewmodel.startLogin()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentOfLogin()
    }
}
