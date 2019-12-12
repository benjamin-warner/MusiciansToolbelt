package com.benjaminnwarner.musicianstoolbelt.views.recording

import android.Manifest
import com.benjaminnwarner.musicianstoolbelt.R
import com.benjaminnwarner.musicianstoolbelt.views.permissions.IPermissibleAction


object RecordingPermission : IPermissibleAction {
    override val permissions: Array<String> = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )
    override val layoutId: Int = R.layout.permission_recorder
}
