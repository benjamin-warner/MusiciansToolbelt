package com.benjaminnwarner.musicianstoolbelt.ui.permissions

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


abstract class PermissionFragment(private val permissibleAction: IPermissibleAction): Fragment() {

    private val permissionCheckingFragmentTag = "PermissionCheckingFragment"
    private val permissionCheckingFragment = PermissionCheckingFragment(permissibleAction)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!actionPermissible()){
            permissionCheckingFragment.show(activity?.supportFragmentManager?.beginTransaction()!!, permissionCheckingFragmentTag)

            permissionCheckingFragment.onPermissionsGranted { it.dismiss() }

            permissionCheckingFragment.onCancelled {
                it.dismiss()
                findNavController().popBackStack()
            }
        }
    }

    private fun actionPermissible(): Boolean {
        return !permissibleAction.permissions.any {
            ContextCompat.checkSelfPermission(context!!, it) != PackageManager.PERMISSION_GRANTED
        }
    }
}
