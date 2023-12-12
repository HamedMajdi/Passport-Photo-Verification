package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.AlertDialog
import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.OnItemClickListener
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model.UserBAC
import com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.ViewModel.UserBACVeiwModel
import com.example.passportphotocomparisonthesis.databinding.FragmentSelectOrAddPassportBinding
class SelectOrAddPassportFragment : Fragment() {

    private val adapter = RecyclerViewAdapter(arrayListOf(), object : OnItemClickListener {
        override fun onItemClick(user: UserBAC) {
            val action = SelectOrAddPassportFragmentDirections.actionSelectOrAddPassportFragmentToUserMRZFragment(user)
            findNavController().navigate(action)
        }

        override fun onItemLongClick(user: UserBAC) {
            showDeleteConfirmationDialog(user)
        }
    })
    private lateinit var userViewModel: UserBACVeiwModel

    private lateinit var binding: FragmentSelectOrAddPassportBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectOrAddPassportBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewUsers.adapter = adapter
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext())

        userViewModel = ViewModelProvider(this).get(UserBACVeiwModel::class.java)

        userViewModel.getUsers()

        userViewModel.users.observe(viewLifecycleOwner, Observer {
            adapter.updateUsers(it)
        })

        binding.addNewUser.setOnClickListener {
            findNavController().navigate(R.id.action_selectOrAddPassportFragment_to_addDocumentFragment)
        }

    }

    fun showDeleteConfirmationDialog(user: UserBAC) {
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes") { dialog, _ ->
                userViewModel.deleteUser(user)

                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog: androidx.appcompat.app.AlertDialog = builder.create()
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        userViewModel.getUsers()
    }
}