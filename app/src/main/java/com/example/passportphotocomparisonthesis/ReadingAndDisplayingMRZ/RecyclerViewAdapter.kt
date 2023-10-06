package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBAC
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Utilities.DocTypeImageGenerator
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Utilities.GenderImageGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecyclerViewAdapter(var users: ArrayList<UserBAC>): RecyclerView.Adapter<RecyclerViewAdapter.UserViewHolder>() {

    fun updateUsers(newUsers: List<UserBAC>){
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_custom_cell, parent, false)

        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val tvID =  view.findViewById<TextView>(R.id.tvDocumentID)
        private val tvName = view.findViewById<TextView>(R.id.tvNameSurname)
        private val ivGender = view.findViewById<ImageView>(R.id.ivGender)
        private val ivFlag = view.findViewById<ImageView>(R.id.ivFlag)
        private val ivDocType = view.findViewById<ImageView>(R.id.ivDocumentType)

        fun bind(user: UserBAC){
            tvID.text = user.documentID
            tvName.text = user.nameSurname

            val genderImageGenerator = GenderImageGenerator.fromId(user.gender)
            if (genderImageGenerator != null) {
                ivGender.setImageResource(genderImageGenerator.imageResId)
            }

            Glide.with(ivFlag.context)
                .load("https://flagcdn.com/w80/${user.nationalityFirst2Digits}.png")

            val docType = DocTypeImageGenerator.fromId(user.travelDocumentType)

            if (docType != null) {
                ivDocType.setImageResource(docType.imageResId)
            }

        }
    }

}

