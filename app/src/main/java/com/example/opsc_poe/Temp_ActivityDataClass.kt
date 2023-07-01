package com.example.opsc_poe

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory


data class Temp_ActivityDataClass(
    var activityID: Int = 0,
    var userID: Int = 0,
    var categoryID: Int = 0,
    var name: String = "",
    var description: String = "",
    var maxgoalID: Int = 0,
    var mingoalID: Int = 0,
    var photo : Bitmap? = null,   //bitmap?
)

data class ActivitySave(
    var activityID: Int = 0,
    var userID: Int = 0,
    var categoryID: Int = 0,
    var name: String = "",
    var description: String = "",
    var maxgoalID: Int = 0,
    var mingoalID: Int = 0,
    var photo : String? = null,   //bitmap
)


