package com.example.warehouse.activities

import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouse.R
import com.example.warehouse.adapters.InventoryItemAdapter
import com.example.warehouse.models.entities.InventoryItem
import com.example.warehouse.utils.Constants
import com.example.warehouse.utils.Constants.Companion.USERNAME
import com.example.warehouse.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , InventoryItemAdapter.PopupItemListener {

    lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val username = pref.getString(USERNAME, "")

        title = "Welcome, $username!"

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))
            .get(MainViewModel::class.java)
        val adapter = InventoryItemAdapter()
        adapter.popupItemListener = this
        recyclerview_main.adapter = adapter
        recyclerview_main.layoutManager = LinearLayoutManager(this)
        viewModel.items.observe(this, Observer {
          adapter.submitList(it)
        })
    }

    override fun onPopupItemClicked(inventoryItem: InventoryItem, menuItemId: Int, position: Int) {
        when(menuItemId) {
            R.id.menuitem_inv_popup_update -> {
                Toast.makeText(this, "${inventoryItem.name} Updated!", Toast.LENGTH_SHORT).show()
            }
            R.id.menuitem_inv_popup_delete -> {
                Toast.makeText(this, "${inventoryItem.name} Deleted!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
