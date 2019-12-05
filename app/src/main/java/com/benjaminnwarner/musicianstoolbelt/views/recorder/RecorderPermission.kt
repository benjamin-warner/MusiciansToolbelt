package com.benjaminnwarner.musicianstoolbelt.views.recorder

import android.Manifest
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.views.permissions.IPermissibleAction


object RecorderPermission : IPermissibleAction {
    override val permissions: Array<String> = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )
    override val layoutId: Int = R.layout.permission_recorder
}
