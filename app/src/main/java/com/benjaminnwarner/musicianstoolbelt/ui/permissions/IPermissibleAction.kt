package com.benjaminnwarner.musicianstoolbelt.ui.permissions

interface IPermissibleAction {
    val permissions: Array<String>
    val layoutId: Int
}
