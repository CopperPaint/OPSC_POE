package com.example.opsc_poe

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.text.IDNA.Info
import android.view.ContentInfo
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


class GlobalClass : Application()
{
    companion object
    {
        var InitialRead: Boolean = false

        var activities = arrayListOf<Temp_ActivityDataClass>()
        var categories = arrayListOf<Temp_CategoryDataClass>()
        var goals = arrayListOf<Temp_GoalDataClass>()
        var logs = arrayListOf<Temp_LogDataClass>()
        var user = Temp_UserDataClass()

        fun ReturnToHome(context: Context)
        {
            var intent = Intent(context, Home_Activity::class.java)
            context.startActivity(intent)
        }

        fun NoUserAppData(barLayout: LinearLayout, barActivity: FragmentActivity?, barContext : Context, screenFunction: String, dataID : Int)
        {

            var barNoData = CustomActivity(barActivity)
            barNoData.binding.tvPrimaryText.text = "No $screenFunction Data"
            barNoData.binding.tvSecondaryText.text = "Click Here to Add $screenFunction Data"
            barNoData.binding.vwBar.backgroundTintList = ContextCompat.getColorStateList(barContext, R.color.Default_Charcoal_Grey)
            barNoData.binding.llBlockText.backgroundTintList = ContextCompat.getColorStateList(barContext, R.color.Muted_Green)
            barNoData.binding.tvBlockText.text = "Click Here"
            barNoData.binding.tvBlockX.text = "+"
            barNoData.binding.tvBlockX.textSize = 36F

            barNoData.isClickable = true

            //set the + text to be closer to the Click Me Text
            val barParam: ViewGroup.MarginLayoutParams = barNoData.binding.tvBlockX.layoutParams as ViewGroup.MarginLayoutParams
            barParam.setMargins(barParam.leftMargin, -40, barParam.rightMargin, barParam.bottomMargin)
            barNoData.binding.tvBlockX.layoutParams = barParam


            when (screenFunction)
            {
                "LogsData" ->
                {

                        val logParam: ViewGroup.MarginLayoutParams = barNoData.binding.vwBar.layoutParams as ViewGroup.MarginLayoutParams
                        logParam.setMargins(28, logParam.topMargin, logParam.rightMargin, logParam.bottomMargin)
                        barNoData.binding.vwBar.layoutParams = logParam

                        barNoData.binding.tvPrimaryText.text = "No Logs Found"
                        barNoData.binding.tvSecondaryText.text = "Try a different set of filters"

                        barNoData.binding.tvBlockText.text = ""
                        barNoData.binding.tvBlockX.text = "\uD83D\uDCC5"
                        barNoData.binding.tvBlockX.height = 210


                }
                "Logs" ->
                {

                    val logParam: ViewGroup.MarginLayoutParams = barNoData.binding.vwBar.layoutParams as ViewGroup.MarginLayoutParams
                    logParam.setMargins(28, logParam.topMargin, logParam.rightMargin, logParam.bottomMargin)

                    barNoData.binding.vwBar.layoutParams = logParam
                    barNoData.binding.tvSecondaryText.text = "Go to an activity to add a log"

                    barNoData.binding.tvBlockText.text = ""
                    barNoData.binding.tvBlockX.text = "\uD83D\uDCC5"
                    barNoData.binding.tvBlockX.height = 210
                }
                "Log" ->
                {
                    barNoData.setOnClickListener()
                    {
                        //load add activity
                        var intent = Intent(barContext, AddLog::class.java)
                        intent.putExtra("activityIDIndex", dataID)
                        barContext.startActivity(intent)
                    }
                }
                "Activity" ->
                {
                    barNoData.setOnClickListener()
                    {

                        //check if any categories exist first

                        var userHasData = false
                        for (i in categories.indices)
                        {
                            if (categories[i].userID == user.userID)
                            {
                                //load add activity
                               userHasData = true
                                break
                            }
                        }

                        if (userHasData == true)
                        {
                            var intent = Intent(barContext, CreateActivity::class.java)
                            barContext.startActivity(intent)
                        }
                        else
                        {
                            InformUser("No Categories Available", "Add a category from the home page", barContext)
                        }


                    }
                }
                "Category" ->
                {
                    barNoData.setOnClickListener()
                    {
                        //load add category view
                        var intent = Intent(barContext, CreateCategory::class.java)
                        barContext.startActivity(intent)
                    }

                }
            }


            barLayout.addView(barNoData)

        }



        fun InformUser(messageTitle: String, messageText: String, context: Context) {
            val alert = AlertDialog.Builder(context)
            alert.setTitle(messageTitle)
            alert.setMessage(messageText)
            alert.setPositiveButton("OK", null)
            alert.show()
        }

        fun DoubleToTime(currentDouble: String): String {

            if (currentDouble.toDoubleOrNull() == null) {
                return currentDouble

            }
            else
            {
                var splitCurrentDouble = currentDouble.split(".")
                var currentHours = splitCurrentDouble[0].toInt()
                var minutesFraction = "0." + splitCurrentDouble[1]
                var currentMinutes = (minutesFraction.toDouble() * 60)
                return "$currentHours:${currentMinutes.roundToInt()}"
            }

            //GlobalClass.InformUser("", "Hours Split: $currentHours Minutes Split: ${currentMinutes.roundToInt()}", requireContext())
            //GlobalClass.InformUser("", "", requireContext())
            //GlobalClass.InformUser("", "Hours Split: $tt", requireContext())

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
        //@turtletime.com
        var listUserUsername = arrayListOf("Jake", "Jo", "Benji", "Mark")
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
        var listGoalAmount = arrayListOf(4, 6, 1, 11, 10, 20, 2, 12, 2, 12, 2, 12, 3, 13, 3, 13, 3, 13, 4, 14, 4, 14, 4, 14)
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


        var listLogHours = arrayListOf(2.0, 3.0, 1.0, 2.0, 3.0, 1.0, 2.0, 3.0, 1.0, 2.0, 3.0, 1.0)//



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
            var newGoal = Temp_GoalDataClass(listGoalGoalID[i], listGoalUserID[i], listGoalAmount[i], listGoalInterval[i], isSet = true)
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



    override fun onCreate()
    {
        super.onCreate()
        //call the data import method
        //LoadLists()

        //add images 3.6.9.12
        /*
        activities[2].photo = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.imgstockbike);
        activities[5].photo = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.imgstockdriving);
        activities[8].photo = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.imgstockboileggs);
        activities[11].photo = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.imgstockhacking);
        */
    }

}