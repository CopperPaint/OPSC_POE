package com.example.opsc_poe

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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
            if (document.data.getValue("UserID").toString().toInt() == userID) {

                val catID: Int = document.data.getValue("CategoryID").toString().toInt()
                val name: String = document.data.getValue("Name").toString()
                val description: String = document.data.getValue("Description").toString()
                val colour: String = document.data.getValue("Colour").toString()

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
            if (document.data.getValue("UserID").toString().toInt() == userID) {

                var ActID: Int = document.data.getValue("ActivityID").toString().toInt()
                var CatID: Int = document.data.getValue("CategoryID").toString().toInt()
                var Name: String = document.data.getValue("Name").toString()
                var Description: String = document.data.getValue("Description").toString()
                var MaxID: Int = document.data.getValue("MaxGoalID").toString().toInt()
                var MinID: Int = document.data.getValue("MinGoalID").toString().toInt()

                var tempAct = Temp_ActivityDataClass(
                    userID = userID,
                    activityID = ActID,
                    categoryID = CatID,
                    name = Name,
                    description = Description,
                    maxgoalID = MaxID,
                    mingoalID = MinID
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
            if (document.data.getValue("UserID").toString().toInt() == userID) {

                var GoalID: Int = document.data.getValue("GoalID").toString().toInt()
                var Amount: Int = document.data.getValue("Amount").toString().toInt()
                var Interval: String = document.data.getValue("Interval").toString()
                var isSet: Boolean = document.data.getValue("isSet").toString().toBoolean()

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
        val querySnapshot = db.collection("Logs").get().await()
        for (document in querySnapshot) {
            if (document.data.getValue("UserID").toString().toInt() == userID) {

                var LogID: Int = document.data.getValue("LogID").toString().toInt()
                var ActID: Int = document.data.getValue("ActivityID").toString().toInt()
                var startString: String = document.data.getValue("StartDate").toString()
                var StartDate = LocalDate.parse(startString, DateTimeFormatter.ISO_DATE)
                var endString: String = document.data.getValue("EndDate").toString()
                var EndDate = LocalDate.parse(endString, DateTimeFormatter.ISO_DATE)
                var Hours: Double = document.data.getValue("Hours").toString().toDouble()

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


}