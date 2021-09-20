package com.example.android.politicalpreparedness.utils

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?) {
    data?.let {
        val adapter = recyclerView.adapter as ElectionListAdapter
        adapter.submitList(data)
    }
}


@BindingAdapter("toggleText")
fun bindTextViewToggleText(buttonView: Button, isElectionSaved: Boolean) {
    val context = buttonView.context
    if (isElectionSaved) {
        buttonView.text = context.getString(R.string.unfollow_election)
    } else {
        buttonView.text = context.getString(R.string.follow_election)
    }
}

@BindingAdapter("dateFormatter")
fun bindDateFormat(textView: TextView, date: Date?) {
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    date?.let {
        textView.text = dateFormat.format(it)
    }
}

@BindingAdapter("apiStatus")
fun bindAPILoadingStatus(statusImage: ImageView, status: Constants.ApiStatus?) {
    when (status) {
        Constants.ApiStatus.LOADING -> {
            statusImage.visibility = View.VISIBLE
            statusImage.setImageResource(R.drawable.loading_animation)
        }
        Constants.ApiStatus.ERROR -> {
            statusImage.visibility = View.VISIBLE
            statusImage.setImageResource(R.drawable.ic_connection_error)
        }
        Constants.ApiStatus.DONE -> {
            statusImage.visibility = View.GONE
        }
    }
}

@BindingAdapter("hasData")
fun bindHasDataVisibility(textView: TextView, hasData: Boolean) {
    if (hasData) {
        textView.visibility = View.GONE
    } else {
        textView.visibility = View.VISIBLE
    }
}

@BindingAdapter("hasString")
fun bindHasStringVisibility(tView: View, hasString: String?) {
    if (hasString.isNullOrEmpty()) {
        tView.visibility = View.GONE
    } else {
        tView.visibility = View.VISIBLE
    }
}


