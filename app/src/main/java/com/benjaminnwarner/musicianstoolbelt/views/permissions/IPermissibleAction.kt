package com.benjaminnwarner.musicianstoolbelt.views.permissions

interface IPermissibleAction {
    val permissions: Array<String>
    val layoutId: Int
}
