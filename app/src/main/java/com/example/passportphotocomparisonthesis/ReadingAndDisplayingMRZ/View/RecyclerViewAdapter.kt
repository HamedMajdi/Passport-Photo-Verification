package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.OnItemClickListener
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBAC
import com.example.passportphotocomparisonthesis.Utils.IconGenerator.DocTypeImageGenerator
import com.example.passportphotocomparisonthesis.Utils.IconGenerator.GenderImageGenerator

class RecyclerViewAdapter(
    var users: ArrayList<UserBAC>,
    private val listener: OnItemClickListener,
) : RecyclerView.Adapter<RecyclerViewAdapter.UserViewHolder>() {

    fun updateUsers(newUsers: List<UserBAC>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_custom_cell, parent, false)

        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class UserViewHolder(view: View) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener,
        View.OnLongClickListener {

        private val tvID = view.findViewById<TextView>(R.id.tvDocumentID)
        private val tvName = view.findViewById<TextView>(R.id.tvNameSurname)
        private val ivGender = view.findViewById<ImageView>(R.id.ivGender)
        private val ivFlag = view.findViewById<ImageView>(R.id.ivFlag)
        private val ivDocType = view.findViewById<ImageView>(R.id.ivDocumentType)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)

        }

        fun bind(user: UserBAC) {
            tvID.text = user.documentID
            tvName.text = user.nameSurname

            val genderImageGenerator = user.gender?.let { GenderImageGenerator.fromId(it) }
            if (genderImageGenerator != null) {
                ivGender.setImageResource(genderImageGenerator.imageResId)
            }

            Glide.with(itemView.context)
                .load("https://flagcdn.com/w80/${user.nationalityFirst2Digits?.lowercase()}.png")
                .into(ivFlag)

            val docType = user.travelDocumentType?.let { DocTypeImageGenerator.fromId(it) }

            if (docType != null) {
                ivDocType.setImageResource(docType.imageResId)
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(users[position])
            }

        }

        override fun onLongClick(v: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemLongClick(users[position])
            }
            return true
        }

    }

}

