package com.benjaminnwarner.musicianstoolbelt.views.permissions

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.permission_recorder.view.*

class PermissionCheckingFragment(private val permissibleAction: IPermissibleAction) : DialogFragment() {

    private val requestCode = 200
    private lateinit var permissionGrantedCallback: ( (PermissionCheckingFragment) -> Unit)
    private lateinit var cancelledListener: ( (PermissionCheckingFragment) -> Unit)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root =  inflater.inflate(permissibleAction.layoutId, container, false)


        root.get_started.setOnClickListener { requestPermission() }
        root.back.setOnClickListener { cancelledListener.invoke(this) }

        return root
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    private fun missingPermissions(): Array<String>{
        return permissibleAction.permissions.filter { permissionUnGranted(it) }.toTypedArray()
    }

    private fun permissionUnGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        requestPermissions(missingPermissions(), requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            this.requestCode -> {
                if(grantResults.all {it == PackageManager.PERMISSION_GRANTED }){
                    permissionGrantedCallback.invoke(this)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun onPermissionsGranted(callback: ( (PermissionCheckingFragment) -> Unit)) {
        permissionGrantedCallback = callback
    }

    fun onCancelled(callback: ( (PermissionCheckingFragment) -> Unit)) {
        cancelledListener = callback
    }
}
