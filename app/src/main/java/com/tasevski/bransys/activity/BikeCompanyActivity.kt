package com.tasevski.bransys.activity

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tasevski.bransys.R
import com.tasevski.bransys.adapter.BikeCompaniesAdapter
import com.tasevski.bransys.databinding.ActivityBinding
import com.tasevski.bransys.databinding.PopUpBinding
import com.tasevski.bransys.util.Resource
import com.tasevski.bransys.view_model.BikeCompaniesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class BikeCompanyActivity : AppCompatActivity() {
    private val viewModel: BikeCompaniesViewModel by viewModels()
    private var prefs: SharedPreferences? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun isNetworkAvailable(context: Context) =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val dialog = Dialog(this)
        val layout: PopUpBinding = PopUpBinding.inflate(layoutInflater)
        dialog.setContentView(layout.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        layout.apply {
            editTextId.setText(prefs?.getLong("time", 15)?.toString())
            submitButton.setOnClickListener {
                val minutes = Integer.parseInt(layout.editTextId.text.toString())
                if(minutes in 15..60) {
                    prefs?.edit()
                        ?.putLong("time", layout.editTextId.text.toString().toLong())
                        ?.apply()
                    dialog.onBackPressed()
                }
            }
        }
        dialog.show()
        return super.onOptionsItemSelected(item)
    }

    private fun checkExpiredData(context: Context) {
        if(isNetworkAvailable(context)) {
            if (prefs?.contains("timestamp") == true) {
                if (LocalDateTime.parse(
                        prefs?.getString(
                            "timestamp", LocalDateTime.MIN.format(
                                DateTimeFormatter.ISO_DATE_TIME
                            )
                        ), DateTimeFormatter.ISO_DATE_TIME
                    ).plusMinutes(prefs?.getLong("time", "15".toLong()) ?: "15".toLong())
                        .isBefore(LocalDateTime.now())
                ) {
                    lifecycleScope.launch {
                        viewModel.deleteAll()
                    }
                }
            }
        }
    }

    private fun setExpiryDate() {
        prefs?.edit()?.putString("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))?.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bikeCompaniesAdapter = BikeCompaniesAdapter()
        prefs = getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        checkExpiredData(this)
        binding.apply {
            recyclerView.apply {
                adapter = bikeCompaniesAdapter
                layoutManager = LinearLayoutManager(this@BikeCompanyActivity)
            }

            viewModel.bikeCompanies?.observe(this@BikeCompanyActivity) { result ->
                if(result is Resource.Loading)
                    setExpiryDate()

                bikeCompaniesAdapter.submitList(result.data)
                progressBar.isVisible = result is Resource.Loading && result.data.isNullOrEmpty()
                textViewError.isVisible = result is Resource.Error && result.data.isNullOrEmpty()
                textViewError.text = result.error?.localizedMessage
            }
        }
    }
}