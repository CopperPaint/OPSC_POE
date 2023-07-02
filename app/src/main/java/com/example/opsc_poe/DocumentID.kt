package com.example.opsc_poe

//data class to store document IDs
data class DocumentID(
    var allUserIDs: ArrayList<String> = ArrayList(),
    var CategoryIDs: ArrayList<String> = ArrayList(),
    var ActivityIDs: ArrayList<String> = ArrayList(),
    var GoalIDs: ArrayList<String> = ArrayList()
)
