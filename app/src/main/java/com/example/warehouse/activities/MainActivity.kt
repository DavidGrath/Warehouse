package com.example.warehouse.activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouse.R
import com.example.warehouse.adapters.EmptyAdapter
import com.example.warehouse.adapters.InventoryItemAdapter
import com.example.warehouse.fragments.ItemsFragment
import com.example.warehouse.fragments.WarehousesFragment
import com.example.warehouse.models.entities.InventoryItem
import com.example.warehouse.models.entities.Warehouse
import com.example.warehouse.utils.Constants
import com.example.warehouse.utils.Constants.Companion.TOKEN
import com.example.warehouse.utils.Constants.Companion.USERNAME
import com.example.warehouse.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , WarehousesFragment.WarehousesFragCallback, ItemsFragment.ItemsFragCallback {

    lateinit var viewModel : MainViewModel
    lateinit var warehousesFrag : WarehousesFragment
    lateinit var itemsFrag : ItemsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val username = pref.getString(USERNAME, "")

        title = "Welcome, $username!"

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))
            .get(MainViewModel::class.java)
        if(savedInstanceState == null) {
            warehousesFrag = WarehousesFragment()
            itemsFrag = ItemsFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_main_activity, warehousesFrag)
                .commit()
        }
    }


    override fun onItemSelected(position: Int, warehouse: Warehouse) {
        viewModel.setInvItems(warehouse.uuid)
        itemsFrag.warehouse = warehouse
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_main_activity, itemsFrag)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}
