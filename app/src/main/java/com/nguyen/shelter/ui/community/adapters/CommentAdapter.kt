package com.nguyen.shelter.ui.community.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.shelter.databinding.ItemCommentBinding
import com.nguyen.shelter.model.Comment
import java.text.SimpleDateFormat

class CommentAdapter(private val comments: ArrayList<Comment>, private val deleteOnClick: (Comment) -> Unit): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SimpleDateFormat")
        fun bind(comment: Comment){

            val dateString = SimpleDateFormat("dd MMM yyyy").format(comment.date)
            val timeString = SimpleDateFormat("HH:mm").format(comment.date)

            binding.dateString = dateString
            binding.timeString = timeString

            binding.comment = comment

            binding.deleteButton.setOnClickListener {
                deleteOnClick.invoke(comment)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    fun addItem(comment: Comment) {
        comments.add(0, comment)
        notifyItemInserted(0)
    }

    fun deleteItem(commentId: String) {
        var position: Int? = null
        for(i in 0 until  comments.size){
            if(comments[i].id == commentId) {
                comments.removeAt(i)
                position = i
                break
            }
        }
        position?.let{
            notifyItemRemoved(it)
        }
    }

    fun clearComments(){
        comments.clear()
        notifyDataSetChanged()
    }

}