package com.example.tp3veilletechnologique

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentsRecyclerViewAdapter(private val comments: List<ParkInfoActivity.Comment>) :
    RecyclerView.Adapter<CommentsRecyclerViewAdapter.CommentViewHolder>() {

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val commentTextView: TextView = view.findViewById(R.id.commentText)
    }

    init {
        Log.d("CommentsAdapter", "Received ${comments.size} comments")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.commentTextView.text = comment.commentText
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}