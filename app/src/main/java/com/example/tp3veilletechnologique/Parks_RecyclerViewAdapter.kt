package com.example.tp3veilletechnologique

import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tp3veilletechnologique.parsers.ParseCSV

class ParksRecyclerViewAdapter(private val parks: List<ParseCSV.Parc>,
                                private val onFavoriteClick: (ParseCSV.Parc) -> Unit
) : RecyclerView.Adapter<ParksRecyclerViewAdapter.ParkViewHolder>() {
    class ParkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val parkNameTextView: TextView = view.findViewById(R.id.parkName)
        val parkInfoButton: Button = view.findViewById(R.id.parkInfoButton)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_row, parent, false)
        return ParkViewHolder(view)
    }
    override fun onBindViewHolder(holder: ParkViewHolder, position: Int) {
        val park = parks[position]
        holder.parkNameTextView.text = park.name
        holder.parkInfoButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ParkInfoActivity::class.java)
            intent.putExtra("parkId", park.id)
            intent.putExtra("parkName", park.name)
            intent.putExtra("parkLocation", park.location)
            intent.putExtra("parkLatitude", park.latitude)
            intent.putExtra("parkLongitude", park.longitude)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return parks.size
    }
}