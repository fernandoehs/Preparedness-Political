package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener

class ElectionsFragment: Fragment() {


    private lateinit var viewModel: ElectionsViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val database = ElectionDatabase.getInstance(requireContext())
        viewModel = ViewModelProvider(
            this,
            ElectionsViewModelFactory(database)
        ).get(ElectionsViewModel::class.java)

        val binding = FragmentElectionBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.navigateToVoterInfoFragment.observe(viewLifecycleOwner) {
            it?.let { election ->
                this.findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        election.id,
                        election.division
                    )
                )
                viewModel.onNavigationComplete()

            }
        }

        val electionAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.onElectionClicked(election)
        })
        binding.upcomingElectionRc.adapter = electionAdapter

        val savedElectionAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.onElectionClicked(election)
        })
        binding.savedElectionRc.adapter = savedElectionAdapter

        return binding.root

    }
    override fun onResume() {
        super.onResume()
        viewModel.getAllSavedElectionsFromDB()
    }



}