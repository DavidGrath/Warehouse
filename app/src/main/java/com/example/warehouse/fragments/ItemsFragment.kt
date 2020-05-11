package com.example.warehouse.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouse.R
import com.example.warehouse.activities.AddEditItemActivity
import com.example.warehouse.adapters.EmptyAdapter
import com.example.warehouse.adapters.InventoryItemAdapter
import com.example.warehouse.models.entities.InventoryItem
import com.example.warehouse.models.entities.Warehouse
import com.example.warehouse.utils.Constants
import com.example.warehouse.utils.Result
import com.example.warehouse.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_main_items.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemsFragment : Fragment(), InventoryItemAdapter.PopupItemListener{

    interface ItemsFragCallback {

    }

    var callback : ItemsFragCallback? = null

    lateinit var viewModel : MainViewModel
    lateinit var warehouse: Warehouse
    val handler = Handler(Looper.getMainLooper())

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as ItemsFragCallback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_main_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(requireActivity().application))
            .get(MainViewModel::class.java)
        val pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val token = pref.getString(Constants.TOKEN, "")!!
        val username = pref.getString(Constants.USERNAME, "")

        InventoryItemAdapter.token = token
        val adapter = InventoryItemAdapter()
        val emptyAdapter = EmptyAdapter("No warehouses!", "Ask your admin for permission to use a warehouse")
        adapter.popupItemListener = this

        recyclerview_items.layoutManager = LinearLayoutManager(requireContext())

        viewModel.username = username
        viewModel.token = token
        viewModel.items.observe(this, Observer {
            if(it.isEmpty()) {
                recyclerview_items.adapter = emptyAdapter
            } else {
                recyclerview_items.adapter = adapter
                adapter.submitList(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menuitem_main_add_item -> {
                startActivity(Intent(requireActivity(), AddEditItemActivity::class.java).also {
                    it.putExtra("ITEM_ID", -1L)
                    it.putExtra("WAREHOUSE", warehouse.name)
                    it.putExtra("WAREHOUSE_ID", warehouse.uuid)
                })
                return true
            }
            else -> {
                return false
            }
        }
    }

    override fun onPopupItemClicked(inventoryItem: InventoryItem, menuItemId: Int, position: Int) {
        when(menuItemId) {
            R.id.menuitem_inv_popup_update -> {
                startActivity(Intent(requireContext(), AddEditItemActivity::class.java).also {
                    it.putExtra("ITEM_ID", inventoryItem.id)
                    it.putExtra("WAREHOUSE", warehouse.name)
                    it.putExtra("WAREHOUSE_ID", warehouse.uuid)
                })
            }
            R.id.menuitem_inv_popup_delete -> {
                GlobalScope.launch {
                    val result = viewModel.deleteItem(inventoryItem.uuid, warehouse.name)
                    when(result) {
                        is Result.Success -> {

                        }
                        is Result.Failure -> {
                            handler.post{
                                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}