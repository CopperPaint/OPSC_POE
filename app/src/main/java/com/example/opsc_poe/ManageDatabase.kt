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
    //database
    val db = Firebase.firestore

    //READ DATA
    //---------------------------------------------------------------------------------------------
    //get all users from database
    suspend fun getAllUsersFromFirestore(): ArrayList<Temp_UserDataClass> {
        val allUsers = arrayListOf<Temp_UserDataClass>()
        val querySnapshot = db.collection("Users").get().await()
        GlobalClass.documents = DocumentID()
        for (document in querySnapshot) {
            //if (document.data.getValue("UserID").toString().toInt() == userID) {

                val newUserID: Int = document.data.getValue("userID").toString().toInt()
                val newEmail: String = document.data.getValue("email").toString()
                val newUsername: String = document.data.getValue("username").toString()
                val newPasswordHash: String = document.data.getValue("passwordHash").toString()
                val newPasswordSalt: String = document.data.getValue("passwordSalt").toString()

                val tempUser = Temp_UserDataClass(
                    userID = newUserID,
                    email = newEmail,
                    username = newUsername,
                    passwordHash = newPasswordHash,
                    passwordSalt = newPasswordSalt
                )

                allUsers.add(tempUser)
                GlobalClass.documents.allUserIDs.add(document.id)
            //}
        }


        return allUsers
    }

    //get categories from database
    suspend fun getCategoriesFromFirestore(userID: Int): ArrayList<Temp_CategoryDataClass> {
        val categories = arrayListOf<Temp_CategoryDataClass>()
        val querySnapshot = db.collection("Category").get().await()
        GlobalClass.documents = DocumentID()
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
                GlobalClass.documents.CategoryIDs.add(document.id)
            }
        }
        return categories
    }

    //get activities from database
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
                GlobalClass.documents.ActivityIDs.add(document.id)
            }
        }
        return activities
    }

    //get all goals from database
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
                GlobalClass.documents.GoalIDs.add(document.id)
            }
        }
        return goals
    }

    //get all logs from firebase
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


    //ADD DATA
    //---------------------------------------------------------------------------------------------
    //add user to the database
    fun AddUserToFirestore(newUser: Temp_UserDataClass)
    {
        db.collection("Users")
            .add(newUser)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${it.id}")
                GlobalClass.UpdateDataBase = true
            }
    }

    //add category to database
    fun AddCategoryToFirestore(category: Temp_CategoryDataClass)
    {
        db.collection("Category")
            .add(category)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${it.id}")
                GlobalClass.UpdateDataBase = true
            }
    }

    //add activity to database
    fun AddActivityToFirestore(activity: ActivitySave)
    {
        db.collection("Activity")
            .add(activity)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${it.id}")
                GlobalClass.UpdateDataBase = true
            }
    }

    //add goal to firebase
    fun AddGoalToFirestore(goal: Temp_GoalDataClass)
    {
        db.collection("Goals")
            .add(goal)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${it.id}")
                GlobalClass.UpdateDataBase = true
            }
    }

    //add log to database
    fun AddLogToFirestore(log: LogStore)
    {
    db.collection("Logs")
        .add(log)
        .addOnSuccessListener {
            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${it.id}")
            GlobalClass.UpdateDataBase = true
        }
    }


    //UPDATE DATA
    //---------------------------------------------------------------------------------------------
    //update given category in database

    suspend fun updateUserInFirestore(currentUser: Temp_UserDataClass, ID: String) {
        val categoryRef = db.collection("Users").document(ID)
        categoryRef.update(
            mapOf(
                "userID" to currentUser.userID,
                "email" to currentUser.email,
                "username" to currentUser.username,
                "passwordHash" to currentUser.passwordHash,
                "passwordSalt" to currentUser.passwordSalt

            )
        ).await()
    }

    suspend fun updateCategoryInFirestore(category: Temp_CategoryDataClass, ID: String) {
        val categoryRef = db.collection("Category").document(ID)
        categoryRef.update(
            mapOf(
                "name" to category.name,
                "description" to category.description,
                "colour" to category.colour
            )
        ).await()
    }

    //update given activity in database
    suspend fun updateActivityInFirestore(activity: ActivitySave, ID: String) {
        val categoryRef = db.collection("Activity").document(ID)
        categoryRef.update(
            mapOf(
                "categoryID" to activity.categoryID,
                "name" to activity.name,
                "description" to activity.description,
                "photo" to activity.photo
            )
        ).await()
    }

    //update given goal in database
    suspend fun updateGoalInFirestore(goal: Temp_GoalDataClass, ID: String) {
        val categoryRef = db.collection("Goals").document(ID)
        categoryRef.update(
            mapOf(
                "interval" to goal.interval,
                "amount" to goal.amount,
                "set" to goal.isSet
            )
        ).await()
    }
}