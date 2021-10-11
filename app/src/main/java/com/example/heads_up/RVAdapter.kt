package com.example.heads_up

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class RVAdapter(private val rv: ArrayList<celep.dat>,var cont: Context): RecyclerView.Adapter<RVAdapter.ItemViewHolder>()  {
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVAdapter.ItemViewHolder {
        return RVAdapter.ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rvlist,parent,false )
        )
    }

    override fun onBindViewHolder(holder: RVAdapter.ItemViewHolder, position: Int) {
        val rvv = rv[position]

        holder.itemView.apply {
            var rvlisting= findViewById<TextView>(R.id.rvlisting)
            rvlisting.text = rvv.name
            rvlisting.setOnClickListener {
                var intent = Intent(cont, editdelet::class.java)
                var i=rv[position]
                intent.putExtra("na",i.name)
                intent.putExtra("ta1",i.taboo1)
                intent.putExtra("ta2",i.taboo2)
                intent.putExtra("ta3",i.taboo3)
                intent.putExtra("id",i.pk)
                context.startActivity(intent)
            }


        }


    }

    override fun getItemCount() = rv.size
}
