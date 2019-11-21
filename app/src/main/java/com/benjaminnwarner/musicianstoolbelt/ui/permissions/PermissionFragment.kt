package com.benjaminnwarner.musicianstoolbelt.ui.permissions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


abstract class PermissionFragment(permissibleAction: IPermissibleAction): Fragment() {

    private val permissionCheckingFragmentTag = "PermissionCheckingFragment"
    private val permissionCheckingFragment = PermissionCheckingFragment(permissibleAction)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionCheckingFragment.show(activity?.supportFragmentManager?.beginTransaction()!!, permissionCheckingFragmentTag)

        permissionCheckingFragment.onPermissionsGranted { it.dismiss() }

        permissionCheckingFragment.onCancelled {
            it.dismiss()
            findNavController().popBackStack()
        }
    }

    protected fun actionPermissible(): Boolean {
        return permissionCheckingFragment.actionPermissible()
    }
}
