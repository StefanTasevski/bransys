package com.tasevski.bransys.adapter

import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tasevski.bransys.R
import com.tasevski.bransys.activity.MapsActivity
import com.tasevski.bransys.data.model.BikeCompany
import com.tasevski.bransys.databinding.BikeCompanyItemBinding

class BikeCompaniesAdapter :
    ListAdapter<BikeCompany, BikeCompaniesAdapter.BikeCompanyViewHolder>(BikeCompanyComparator()) {
    private var parentObject: ViewGroup? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeCompanyViewHolder {
        parentObject = parent
        val binding =
            BikeCompanyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BikeCompanyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BikeCompanyViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
        holder.itemView.setOnClickListener {
            if(parentObject!=null) {
                val intent = Intent(parentObject?.context, MapsActivity::class.java)
                intent.putExtra("name", currentItem.name)
                intent.putExtra("latitude", currentItem.location.latitude)
                intent.putExtra("longitude", currentItem.location.longitude)
                intent.putExtra("city", currentItem.location.city)
                intent.putExtra("country", currentItem.location.country)
                startActivity(parentObject!!.context, intent, null)
            }
        }
    }


    class BikeCompanyViewHolder(private val binding: BikeCompanyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bikeCompany: BikeCompany) {
            binding.apply {
                companyName.text = bikeCompany.name
                companyCity.text = bikeCompany.location.city+", "+bikeCompany.location.country
            }
        }
    }

    class BikeCompanyComparator : DiffUtil.ItemCallback<BikeCompany>() {
        override fun areItemsTheSame(oldItem: BikeCompany, newItem: BikeCompany) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: BikeCompany, newItem: BikeCompany) =
            oldItem == newItem
    }
}