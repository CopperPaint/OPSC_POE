package com.example.opsc_poe

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ManageDatabase
{
    val db = Firebase.firestore

    suspend fun getCategoriesFromFirestore(userID: Int): ArrayList<Temp_CategoryDataClass> {
        val categories = arrayListOf<Temp_CategoryDataClass>()
        val querySnapshot = db.collection("Category").get().await()
        for (document in querySnapshot) {
            if (document.data.getValue("userID").toString().toInt() == userID) {

                val catID: Int = document.data.getValue("categoryID").toString().toInt()
                val name: String = document.data.getValue("name").toString()
                val description: String = document.data.getValue("description").toString()
                val colour: String = document.data.getValue("colour").toString()

                val tempCat = Temp_CategoryDataClass(
                    userID = userID,
                    categoryID = catID,
                    name = name,
                    description = description,
                    colour = colour
                )
                categories.add(tempCat)
            }
        }
        return categories
    }

    suspend fun getActivitesFromFirestore(userID: Int): ArrayList<Temp_ActivityDataClass> {
        val activities = arrayListOf<Temp_ActivityDataClass>()
        val querySnapshot = db.collection("Activity").get().await()
        for (document in querySnapshot) {
            if (document.data.getValue("userID").toString().toInt() == userID) {

                var ActID: Int = document.data.getValue("activityID").toString().toInt()
                var CatID: Int = document.data.getValue("categoryID").toString().toInt()
                var Name: String = document.data.getValue("name").toString()
                var Description: String = document.data.getValue("description").toString()
                var MaxID: Int = document.data.getValue("maxgoalID").toString().toInt()
                var MinID: Int = document.data.getValue("mingoalID").toString().toInt()
                var filePath: String = document.data.getValue("photo").toString()
                var bitmap: Bitmap?

                if (filePath.isNotEmpty())
                {
                    var file = File(filePath)
                    bitmap = BitmapFactory.decodeFile(file.absolutePath)
                }
                else
                {
                    bitmap = null
                }

                var tempAct = Temp_ActivityDataClass(
                    userID = userID,
                    activityID = ActID,
                    categoryID = CatID,
                    name = Name,
                    description = Description,
                    maxgoalID = MaxID,
                    mingoalID = MinID,
                    photo = bitmap
                )
                activities.add(tempAct)
            }
        }
        return activities
    }

    suspend fun getGoalsFromFirestore(userID: Int): ArrayList<Temp_GoalDataClass> {
        val goals = arrayListOf<Temp_GoalDataClass>()
        val querySnapshot = db.collection("Goals").get().await()
        for (document in querySnapshot) {
            if (document.data.getValue("userID").toString().toInt() == userID) {

                var GoalID: Int = document.data.getValue("goalID").toString().toInt()
                var Amount: Int = document.data.getValue("amount").toString().toInt()
                var Interval: String = document.data.getValue("interval").toString()
                var isSet: Boolean = document.data.getValue("set").toString().toBoolean()

                var tempGoal = Temp_GoalDataClass(
                    userID = userID,
                    goalID = GoalID,
                    amount = Amount,
                    interval = Interval,
                    isSet = isSet
                )
                goals.add(tempGoal)
            }
        }
        return goals
    }

    suspend fun getLogsFromFirestore(userID: Int): ArrayList<Temp_LogDataClass> {
        val logs = arrayListOf<Temp_LogDataClass>()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val querySnapshot = db.collection("Logs").get().await()
        for (document in querySnapshot) {
            if (document.data.getValue("userID").toString().toInt() == userID) {

                var LogID: Int = document.data.getValue("logID").toString().toInt()
                var ActID: Int = document.data.getValue("activityID").toString().toInt()
                var startString: String = document.data.getValue("startDate").toString()
                var StartDate = LocalDate.parse(startString, formatter)
                var endString: String = document.data.getValue("endDate").toString()
                var EndDate = LocalDate.parse(endString, formatter)
                var Hours: Double = document.data.getValue("hours").toString().toDouble()

                var tempLog = Temp_LogDataClass(
                    userID = userID,
                    logID = LogID,
                    activityID = ActID,
                    startDate = StartDate,
                    endDate = EndDate,
                    hours = Hours
                )
                logs.add(tempLog)
            }
        }
        return logs
    }


    suspend fun AddCategoryToFirestore(category: Temp_CategoryDataClass)
    {
        db.collection("Category")
            .add(category)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${it.id}")
                GlobalClass.UpdateDataBase = true
            }
    }

    suspend fun AddActivityToFirestore(activity: ActivitySave)
    {
        db.collection("Activity")
            .add(activity)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${it.id}")
                GlobalClass.UpdateDataBase = true
            }
    }

    suspend fun AddGoalToFirestore(goal: Temp_GoalDataClass)
    {
        db.collection("Goals")
            .add(goal)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${it.id}")
                GlobalClass.UpdateDataBase = true
            }
    }

    suspend fun AddLogToFirestore(log: LogStore)
    {
    db.collection("Logs")
        .add(log)
        .addOnSuccessListener {
            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${it.id}")
            GlobalClass.UpdateDataBase = true
        }
    }

}