package com.benjaminnwarner.musicianstoolbelt.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.widget.ConfirmationDialog
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)


        root.home_recorder.setReRecordListener {
            val dialog = ConfirmationDialog.getInstance(getString(R.string.abandon_unsaved), getString(R.string.discard), getString(R.string.cancel)){
                root.home_recorder.setRecordingMode()
            }
            dialog.show(fragmentManager!!, ConfirmationDialog.CONFIRM_DIALOG_TAG)
        }

        return root
    }
}
