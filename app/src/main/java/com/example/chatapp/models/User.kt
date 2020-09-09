package com.example.chatapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val userName: String, val profileImageURL: String) : Parcelable{
    constructor(): this ("","","")

}