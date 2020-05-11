package com.example.warehouse.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouse.R
import com.example.warehouse.adapters.EmptyAdapter
import com.example.warehouse.adapters.WarehouseAdapter
import com.example.warehouse.models.entities.Warehouse
import com.example.warehouse.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_main_warehouses.*

class WarehousesFragment : Fragment(), WarehouseAdapter.ItemClickListener {

    interface WarehousesFragCallback {
        fun onItemSelected(position: Int, warehouse: Warehouse)
    }

    var callback : WarehousesFragCallback? = null
    lateinit var viewModel : MainViewModel


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as WarehousesFragCallback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_main_warehouses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emptyAdapter = EmptyAdapter("No warehouses!", "Ask your admin for permission to use a warehouse")
        val warehouseAdapter = WarehouseAdapter(emptyList()).also {
            it.itemClickListener = this
        }
        recyclerview_warehouses.layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application))
            .get(MainViewModel::class.java)
        viewModel.warehouses.observe(this, Observer {
            if(it.isEmpty()) {
                recyclerview_warehouses.adapter = emptyAdapter
            } else {
                warehouseAdapter.items = it
                warehouseAdapter.notifyDataSetChanged()
                recyclerview_warehouses.adapter = warehouseAdapter
            }
        })
    }

    override fun onItemClicked(position: Int, warehouse: Warehouse) {
        callback?.onItemSelected(position, warehouse)
    }
}