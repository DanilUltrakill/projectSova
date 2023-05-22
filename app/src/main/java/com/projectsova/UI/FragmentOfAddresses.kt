package com.projectsova.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.projectsova.R
import com.projectsova.domain.entity.Card
import com.projectsova.databinding.FragmentContentBinding
import com.projectsova.presentation.AddressVIewModel
import com.projectsova.presentation.StateContent
import java.util.*
import kotlin.collections.ArrayList

class FragmentOfAddresses : Fragment(), CardAdapter.getaddinfoListener {

    lateinit var navController: NavController
    lateinit var binding: FragmentContentBinding
    private lateinit var adapter: CardAdapter

    lateinit var login: String

    private  val viewmodel: AddressVIewModel by viewModels()

    private lateinit var cardList: ArrayList<Card>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navController = NavHostFragment.findNavController(this)
        login = arguments?.getString("name")!!
        binding = FragmentContentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setObservers()
        viewmodel.read(login)
        binding.bottomNavigation.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.action_fragmentOfAddresses_to_fragmentOfLogin -> {
                    navController.navigate(R.id.action_fragmentOfAddresses_to_fragmentOfLogin)
                    return@setOnItemSelectedListener true
                }
                R.id.action_fragmentOfMap_to_fragmentOfAddresses -> {

                    return@setOnItemSelectedListener true
                }
                R.id.action_fragmentOfAddresses_to_fragmentOfMap -> {
                    navController.navigate(R.id.action_fragmentOfAddresses_to_fragmentOfMap)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

    }

    fun setObservers(){
        viewmodel.stateContent.observe(viewLifecycleOwner, ::handleState)
    }

    private fun handleState(state: StateContent) {
        when (state) {
            is StateContent.Initial,
            StateContent.Loading-> renderLoading()

            is StateContent.Content -> renderContent()

            is StateContent.Error -> renderError()
        }
    }

    fun renderLoading(){
        with(binding) {
            recyclerforcards.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    fun renderContent(){
        with(binding) {
            recyclerforcards.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            adapter = CardAdapter(
                viewmodel.cardList as java.util.ArrayList<Card>,this@FragmentOfAddresses
            )
            recyclerforcards.adapter = adapter
            recyclerforcards.layoutManager = LinearLayoutManager(context)
            val itemTouchHelper = ItemTouchHelper(simpleCallback)
            itemTouchHelper.attachToRecyclerView(recyclerforcards)
        }
        var badge = binding.bottomNavigation.getOrCreateBadge(R.id.action_fragmentOfMap_to_fragmentOfAddresses)
        badge.isVisible = true
        // An icon only badge will be displayed unless a number is set:
        badge.number = viewmodel.cardList.size
    }

    fun renderError(){
        Toast.makeText(requireContext(),R.string.error_login, Toast.LENGTH_SHORT).show()
    }

    val simpleCallback = object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN
                                or ItemTouchHelper.START or ItemTouchHelper.END, 0){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.absoluteAdapterPosition
            val toPosition = target.absoluteAdapterPosition
            Collections.swap(viewmodel.cardList, fromPosition, toPosition)
            adapter.notifyItemMoved(fromPosition, toPosition)
            adapter.notifyItemChanged(toPosition)
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            TODO("Not yet implemented")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentOfAddresses()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cardList = adapter.getList()
        viewmodel.sendData(cardList)
    }


    override fun onClick(card: Card) {
        val bundle = Bundle()
        bundle.putString("address", card.address)
        bundle.putString("name", login)
        navController.navigate(R.id.action_fragmentOfAddresses_to_thirdFragment, bundle)
    }
}
