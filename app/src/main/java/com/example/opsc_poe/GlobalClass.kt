package com.example.opsc_poe

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class GlobalClass : Application()
{

    companion object {


        var activities = arrayListOf<Temp_ActivityDataClass>()
        var categories = arrayListOf<Temp_CategoryDataClass>()
        var goals = arrayListOf<Temp_GoalDataClass>()
        var logs = arrayListOf<Temp_LogDataClass>()
        var user = Temp_UserDataClass()


        fun InformUser(messageTitle: String, messageText: String, context: Context) {
            val alert = AlertDialog.Builder(context)
            alert.setTitle(messageTitle)
            alert.setMessage(messageText)
            alert.setPositiveButton("OK", null)
            alert.show()
        }

        //---------------------------------------------------------------------------------------------------------------------------------------------
        //temporary lists of registered users and their data
        //---------------------------------------------------------------------------------------------------------------------------------------------
        var listUserUserID = arrayListOf(1, 2, 3, 4)
        var listUserEmail = arrayListOf(
            "jake@turtletime.com",
            "jo@turtletime.com",
            "benji@turtletime.com",
            "marker@turtletime.com"
        )
        var listUserUsername = arrayListOf("Jake", "Jo", "Benji", "Mark", "jrtu")
        var listUserPasswordHash = arrayListOf(
            "cb75e237e40ab1e5f8481fb332076d0969cbf9c643d77133e16303730e501858",
            "53866f8bddeafb655d5f5a86599dafbec8c8eaefcd405d25952b957280e889f7",
            "4ba8e3bb0aef5e38db7f6e6dfbeefbd95af8e65035b69efe6e200d929c898b29",
            "434ce1bd62754f195bb35d9beadfd36add5956be76ff000cee6a4225497a93b0"
        )
        var listUserPasswordSalt = arrayListOf("aecd1edc24a0671710d7e92c85342454", "ab966010d04ea07efec8d7fe358ea4fb", "12b97f16dfa05f21f49169333685a243", "38780ed5ef02ad051cc85545302661e2")

        //user account email is first name + @turtletime.com
        //user account password is surname

        //marker email = marker@turtletime.com
        //marker password = marker
        //---------------------------------------------------------------------------------------------------------------------------------------------


        //---------------------------------------------------------------------------------------------------------------------------------------------
        //temporary data for user categories
        //---------------------------------------------------------------------------------------------------------------------------------------------
        var listCategoryCategoryID = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8)
        var listCategoryUserID = arrayListOf(1, 1, 2, 2, 3, 3, 4, 4)
        var listCategoryName = arrayListOf("Category Name 1", "Category Name 2", "Category Name 3", "Category Name 4", "Category Name 5", "Category Name 6", "Category Name 7", "Category Name 8")
        var listCategoryColor = arrayListOf("#5c37d7", "#9c56a7", "#60fabc", "#ed2eb9", "#357afc", "#b92c74", "#f9eb0f", "#6c3470")
        var listCategoryDescription = arrayListOf("Category Description 1", "Category Description 2", "Category Description 3", "Category Description 4", "Category Description 5", "Category Description 6", "Category Description 7", "Category Description 8")
        //---------------------------------------------------------------------------------------------------------------------------------------------

        //---------------------------------------------------------------------------------------------------------------------------------------------
        //temporary data for user goals
        //---------------------------------------------------------------------------------------------------------------------------------------------
        var listGoalGoalID = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24)
        var listGoalUserID = arrayListOf(1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4)
        var listGoalAmount = arrayListOf(1, 11, 1, 11, 10, 20, 2, 12, 2, 12, 2, 12, 3, 13, 3, 13, 3, 13, 4, 14, 4, 14, 4, 14)
        var listGoalInterval = arrayListOf("Daily", "Daily", "Weekly", "Weekly", "Monthly", "Monthly", "Daily", "Daily", "Weekly", "Weekly", "Monthly", "Monthly", "Daily", "Daily", "Weekly", "Weekly", "Monthly", "Monthly", "Daily", "Daily", "Weekly", "Weekly", "Monthly", "Monthly")
        //---------------------------------------------------------------------------------------------------------------------------------------------

        //---------------------------------------------------------------------------------------------------------------------------------------------
        //temporary data for user activities
        //---------------------------------------------------------------------------------------------------------------------------------------------
        var listActivityActivityID = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        var listActivityUserID = arrayListOf(1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4)
        var listActivityCategoryID = arrayListOf(1, 2, 2, 3, 4, 4, 5, 6, 6, 7, 8, 8)
        var listActivityName = arrayListOf("Activity Name 1", "Activity Name 2", "Activity Name 3", "Activity Name 4", "Activity Name 5", "Activity Name 6", "Activity Name 7", "Activity Name 8", "Activity Name 9", "Activity Name 10", "Activity Name 11", "Activity Name 12")
        var listActivityDescription = arrayListOf("Activity Description 1", "Activity Description 2", "Activity Description 3", "Activity Description 4", "Activity Description 5", "Activity Description 6", "Activity Description 7", "Activity Description 8", "Activity Description 9", "Activity Description 10", "Activity Description 11", "Activity Description 12")
        var listActivityMaxGoalID = arrayListOf(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24)
        var listActivityMinGoalID = arrayListOf(1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23)
        //---------------------------------------------------------------------------------------------------------------------------------------------


        //---------------------------------------------------------------------------------------------------------------------------------------------
        //temporary data for user logs
        //---------------------------------------------------------------------------------------------------------------------------------------------
        var listLogLogID = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        var listLogActivityID = arrayListOf(1, 2, 2, 4, 5, 5, 7, 8, 8, 10, 11, 11)
        var listLogUserID = arrayListOf(1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4)

        //dates in form of date-month-year
        var listLogStartDate = arrayListOf("1-6-2023", "2-6-2023", "3-6-2023", "4-6-2023", "5-6-2023", "6-6-2023", "1-7-2023", "2-7-2023", "3-7-2023", "4-7-2023", "5-7-2023", "6-7-2023")//Year-Month-Day
        var listLogEndDate = arrayListOf("2-6-2023", "3-6-2023", "3-6-2023", "5-6-2023", "6-6-2023", "6-6-2023", "2-7-2023", "3-7-2023", "3-7-2023", "5-7-2023", "6-7-2023", "6-7-2023")//Year-Month-Day


        var listLogHours = arrayListOf(2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3, 1)//



        /*
              var listLogStartDate = arrayListOf(2023-6-1,
            2023-6-2,
            2023-6-3,
            2023-6-4,
            2023-6-5,
            2023-6-6,
            2023-7-1,
            2023-7-2,
            2023-7-3,
            2023-7-4,
            2023-7-5,
            2023-7-6)//Year-Month-Day
        var listLogEndDate = arrayListOf(2023-6-2,
            2023-6-3,
            2023-6-3,
            2023-6-5,
            2023-6-6,
            2023-6-6,
            2023-7-2,
            2023-7-3,
            2023-7-3,
            2023-7-5,
            2023-7-6,
            2023-7-6)//Year-Month-Day

         */
        //---------------------------------------------------------------------------------------------------------------------------------------------



    }

    fun LoadLists()
    {
        //data import method
        //method to load the data array lists into the array of objects

        //add the categories
        for(i in listCategoryCategoryID.indices)
        {

            var newCategory = Temp_CategoryDataClass(listCategoryCategoryID[i], listCategoryUserID[i], listCategoryName[i], listCategoryColor[i], listCategoryDescription[i])
            categories.add(newCategory)
        }


        //add the goals
        for(i in listGoalGoalID.indices)
        {

            var newGoal = Temp_GoalDataClass(listGoalGoalID[i], listGoalUserID[i], listGoalAmount[i], listGoalInterval[i])
            goals.add(newGoal)
        }


        //add the activities
        for(i in listActivityActivityID.indices)
        {

            var newActivity = Temp_ActivityDataClass(listActivityActivityID[i], listActivityUserID[i], listActivityCategoryID[i], listActivityName[i], listActivityDescription[i], listActivityMaxGoalID[i], listActivityMinGoalID[i])
            activities.add(newActivity)
        }


        //add the logs
        for(i in listLogLogID.indices)
        {
            var formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
            var newLog = Temp_LogDataClass(listLogLogID[i], listLogActivityID[i], listLogUserID[i], LocalDate.parse(listLogStartDate[i],formatter), LocalDate.parse(listLogEndDate[i], formatter), listLogHours[i])
            logs.add(newLog)
        }
    }

    override fun onCreate() {
        super.onCreate()

        //call the data import method
        LoadLists()
    }




}