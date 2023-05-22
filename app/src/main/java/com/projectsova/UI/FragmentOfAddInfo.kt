package com.projectsova.UI

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.projectsova.R
import com.projectsova.domain.entity.Card
import com.projectsova.databinding.FragmentAddinfoBinding
import com.projectsova.presentation.AddInfoViewModel
import com.projectsova.presentation.StateAddInfo

class FragmentOfAddInfo : Fragment(), CardAdapter.getaddinfoListener{
    private lateinit var binding: FragmentAddinfoBinding
    lateinit var address: String
    lateinit var login: String

    private  val viewmodel: AddInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        address = arguments?.getString("address")!!
        login = arguments?.getString("name")!!
        binding = FragmentAddinfoBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()


    }

    fun setObservers(){
        viewmodel.stateAddInfo.observe(viewLifecycleOwner, ::handleState)
    }

    private fun handleState(state: StateAddInfo) {
        when (state) {
            is StateAddInfo.Initial,
            StateAddInfo.Loading-> getAddinfo()

            is StateAddInfo.Content -> renderContent()

            is StateAddInfo.Error -> renderError()
        }
    }

    fun getAddinfo(){
        viewmodel.getAddInfo(login, address)
    }

    @SuppressLint("SetTextI18n")
    fun renderContent(){
        with(binding) {
            txtPhone.text = "номер телефона: ${viewmodel.phoneNumber.value}"
            textaddinfoaddress.text = address
            txtProducts.text = viewmodel.resultProductString.value
        }
    }

    fun renderError(){
        Toast.makeText(requireContext(), R.string.error_login, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(card: Card) {
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentOfAddInfo()
    }
}
