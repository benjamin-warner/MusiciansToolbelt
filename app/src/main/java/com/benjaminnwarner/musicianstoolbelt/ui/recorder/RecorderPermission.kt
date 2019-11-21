package com.benjaminnwarner.musicianstoolbelt.ui.recorder

import android.Manifest
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.ui.permissions.IPermissibleAction


object RecorderPermission : IPermissibleAction {
    override val permissions: Array<String> = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    override val layoutId: Int = R.layout.permission_recorder
}
