package com.example.warehouse.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.warehouse.R
import com.example.warehouse.bind.AddEditItemForm
import com.example.warehouse.databinding.ActivityAddEditItemBinding
import com.example.warehouse.models.entities.InventoryItem
import com.example.warehouse.utils.Constants.Companion.TOKEN
import com.example.warehouse.utils.GeneralUtils
import com.example.warehouse.utils.Result
import com.example.warehouse.viewmodels.AddEditViewModel
import com.example.warehouse.viewmodels.factories.AddEditViewModelFactory
import kotlinx.android.synthetic.main.activity_add_edit_item.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddEditItemActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var viewModel: AddEditViewModel
    lateinit var item : InventoryItem
    val IMAGE_REQ_CODE = 100
    var pictureChanged = false
    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityAddEditItemBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit_item)
        setSupportActionBar(toolbar_add_edit)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        button_choose_pic.setOnClickListener(this)
        val id = intent.extras.getLong("ITEM_ID", -1L)
        val warehouseName = intent.extras.getString("WAREHOUSE", "")
        val warehouseId = intent.extras.getString("WAREHOUSE_ID", "")
        viewModel = ViewModelProvider(this, AddEditViewModelFactory(application, warehouseName, warehouseId))
            .get(AddEditViewModel::class.java)
        val pref = PreferenceManager.getDefaultSharedPreferences(this@AddEditItemActivity)
        val token = pref.getString(TOKEN, "")
        if(id != -1L) {
            GlobalScope.launch {
                viewModel.getItem(id).also { item ->
                    viewModel.addEditForm = AddEditItemForm(
                        warehouseName,
                        item.warehouseId,
                        item.id,
                        item.uuid,
                        item.name,
                        item.quantity.toString(),
                        item.unitPrice.toString(),
                        item.imageUri
                    )
                    binding.addEditForm = viewModel.addEditForm
                    viewModel.addEditForm.imageUrl?.let {
                        handler.post {
                            Glide.with(application)
                                .load(
                                    GlideUrl(
                                        GeneralUtils.convertUrlToMobile(it),
                                        LazyHeaders.Builder()
                                            .addHeader("Authorization", token)
                                            .build()
                                    )
                                )
                                .placeholder(R.drawable.ic_image_not_found)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(imageview_item_picture)
                        }
                    }
                }
            }
        } else {
            binding.addEditForm = viewModel.addEditForm
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_edit_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        item?.let{
            when(it.itemId) {
                R.id.menuitem_done_adding_item-> {
                    GlobalScope.launch {
                        val bytes = if(pictureChanged) {
                            contentResolver.openInputStream(Uri.parse(viewModel.addEditForm.imageUrl))
                                .readBytes()
                        } else {
                            ByteArray(0)
                        }
                        val pref = PreferenceManager.getDefaultSharedPreferences(this@AddEditItemActivity)
                        val token = pref.getString(TOKEN, "")
                        val newItem = viewModel.addEditForm.itemId == null
                        val result = if(newItem) {
                            viewModel.addItem(token, spinner_item_curr_code.selectedItem as String, bytes)
                        } else {
                            viewModel.updateItem(token, spinner_item_curr_code.selectedItem as String, bytes, pictureChanged)
                        }
                        when(result) {
                            is Result.Success -> {
                                handler.post {
                                    val text = if(newItem) "Item Added Successfully"
                                                else "Item Updated Successfully"
                                    Toast.makeText(this@AddEditItemActivity, text, Toast.LENGTH_SHORT).show()
                                }
                                finish()
                            }
                            is Result.Failure -> {
                                handler.post {
                                    Toast.makeText(this@AddEditItemActivity, "Error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                else ->{

                }
            }
        }
        return true
    }

    override fun onClick(v: View?) {
        v?.let {
            when(it) {
                button_choose_pic -> {
                    val anIntent = Intent().apply{
                        setType("image/*")
                        setAction(Intent.ACTION_GET_CONTENT)
                    }
                    startActivityForResult(anIntent, IMAGE_REQ_CODE)
                }
                else -> {

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK) {
            when(requestCode) {
                IMAGE_REQ_CODE -> {
                    pictureChanged = true
                    viewModel.addEditForm.imageUrl = data!!.dataString
                    Glide.with(application)
                        .load(viewModel.addEditForm.imageUrl)
                        .placeholder(R.drawable.ic_image_not_found)
                        .into(imageview_item_picture)
                }
            }
        }
    }
}
