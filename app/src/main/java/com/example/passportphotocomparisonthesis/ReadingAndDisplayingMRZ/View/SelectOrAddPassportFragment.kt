package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.fragment.app.Fragment
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
//        val userBAC = UserBAC(
//            "P52838259",
//            "240424",
//            "200295",
//            "HAMED MAJDI",
//            "M",
//            "IRAN",
//            "IR",
//            3
//        )
//
//        userViewModel.addUser(userBAC)
        userViewModel.getUsers()

        userViewModel.users.observe(viewLifecycleOwner, Observer {
            adapter.updateUsers(it)
        })

        binding.addNewUser.setOnClickListener {
            findNavController().navigate(R.id.action_selectOrAddPassportFragment_to_addDocumentFragment)
        }

    }

    override fun onResume() {
        super.onResume()
        userViewModel.getUsers()
    }
}