package com.benjaminnwarner.musicianstoolbelt.widget

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class ConfirmationDialog: DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(arguments?.getString(MESSAGE))
            .setPositiveButton(arguments?.getString(POSITIVE_BTN)) { _, _ ->
                callback?.invoke()
                dismiss()
            }
            .setNegativeButton(arguments?.getString(NEGATIVE_BTN)) { _, _ ->
                dismiss()
            }
            .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val CONFIRM_DIALOG_TAG = "CONFIRM_DIALOG_TAG"
        private const val MESSAGE = "MESSAGE"
        private const val POSITIVE_BTN = "POSITIVE_BTN"
        private const val NEGATIVE_BTN = "NEGATIVE_BTN"

        private var callback: (() -> Unit)? = null

        fun getInstance(msg: String, positiveBtn: String, negativeBtn: String, callback: (() -> Unit)): ConfirmationDialog {
            this.callback = callback
            val args = Bundle()
            args.putString(MESSAGE, msg)
            args.putString(POSITIVE_BTN, positiveBtn)
            args.putString(NEGATIVE_BTN, negativeBtn)
            val fragment = ConfirmationDialog()
            fragment.arguments = args
            return fragment
        }
    }
}
