package com.benjaminnwarner.musicianstoolbelt.ui.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.benjaminnwarner.musicianstoolbelt.R

abstract class PermissionCheckingFragment : Fragment() {

    private val PERMISSION_REQUEST = 200

    protected fun checkPermissions(permission: String) {
        if(ContextCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
            if(shouldShowRequestPermissionRationale(permission)) {
                showPermissionRationale(permission, reAsking = false)
            } else {
                requestPermissions(arrayOf(permission), PERMISSION_REQUEST)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_REQUEST -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    showPermissionRationale(permissions[0], reAsking = true)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun showPermissionRationale(permission: String, reAsking: Boolean) {
        val rationalLayout = getPermissionRationalLayout(permission)
        val dialog = buildDialog(rationalLayout, permission, reAsking)
        dialog.show()
    }

    private fun getPermissionRationalLayout(permission: String): Int {
        return when (permission) {
            Manifest.permission.RECORD_AUDIO -> R.layout.dialog_permission_mic
            else -> throw IllegalStateException()
        }
    }

    @SuppressLint("InflateParams")
    private fun buildDialog(rationalLayout: Int, permission: String, reAsking: Boolean): AlertDialog {
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }

        val negativeText =  if (reAsking) getString(R.string.im_sure) else getString(R.string.deny)
        val positiveText = if (reAsking) getString(R.string.allow_access) else getString(R.string.allow)
        val builderContent = layoutInflater.inflate(R.layout.dialog_permission_mic, null)
        val rationaleText = builderContent.findViewById(R.id.permission_reason_text) as TextView
        if (reAsking) rationaleText.text = getString(R.string.need_mic_permission_reask)

        builder?.apply {
            setView(builderContent)
            setPositiveButton(positiveText) { dialog, _ ->
                onPermissionGranted(dialog, permission)
            }
            setNegativeButton(negativeText) { dialog, _ ->
                onPermissionDenied(dialog)
            }
        }



        return builder!!.create()
    }

    private fun onPermissionGranted(dialog: DialogInterface?, permission: String) {
        dialog?.dismiss()
        requestPermissions(arrayOf(permission), PERMISSION_REQUEST)
    }

    private fun onPermissionDenied(dialog: DialogInterface?) {
        dialog?.dismiss()
        this.handlePermissionDenial()
    }

    abstract fun handlePermissionDenial()

}
