package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val id = VoterInfoFragmentArgs.fromBundle(requireArguments()).argElectionId
        val division = VoterInfoFragmentArgs.fromBundle(requireArguments()).argDivision

        val database = ElectionDatabase.getInstance(requireContext())
        val viewModel: VoterInfoViewModel = ViewModelProvider(
            this,
            VoterInfoViewModelFactory(id.toLong(), division, database)
        ).get(VoterInfoViewModel::class.java)

        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.errorMsg.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(context, "No data is found!", Toast.LENGTH_SHORT).show()
                binding.followBtn.isEnabled = false
            }
        }

        binding.voterInfo = division

        //: Handle loading of URLs
        viewModel.navigateToVotingUrl.observe(viewLifecycleOwner, {
            it?.let { votingLocationUrl ->
                loadUrl(votingLocationUrl)
                viewModel.openVotingLocationComplete()
            }
        })

        viewModel.navigateToBallotUrl.observe(viewLifecycleOwner, {
            it?.let { ballotUrl ->
                loadUrl(ballotUrl)
                viewModel.openBallotUrlComplete()
            }
        })

        //: Handle save button UI state
        //: cont'd Handle save button clicks
        binding.followBtn.setOnClickListener {
            viewModel.toggleButton()
        }

        return binding.root
    }

    //: Create method to load URL intents
    private fun loadUrl(urlStr: String) {
        val uri: Uri = Uri.parse(urlStr)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

}