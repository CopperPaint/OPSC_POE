package com.example.opsc_poe

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.content.ContextCompat.startActivity
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*
import kotlin.math.roundToInt

//private var data = GlobalClass

class GoalHourCalculator
{
    //preset bar colors
    var yellow = "#fcef5d"
    var red = "#e81e1e"
    var green = "#40bf2a"

    //calculate goal hours if both goals exists
    fun CalculateHours(minIndex: Int, maxIndex: Int, activityID: Int): Triple<String, String, String>
    {
        var mingoal = GlobalClass.goals[minIndex]
        var maxgoal = GlobalClass.goals[maxIndex]
        var calcHour: String
        var goalText: String
        var barColour: String

        //check min
        val (minHour, minText, minColor) = CheckGoal(mingoal.interval, mingoal.amount, activityID)
        if (minText.equals("Overtime")) //if over mingoal hours
        {
            //check max
            val (maxHour, maxText, maxColor) = CheckGoal(maxgoal.interval, maxgoal.amount, activityID) //here
            if (maxText.equals("Hours to Go!")) //if under maxgoal hours
            {
                calcHour = "✔"
                goalText = "Goal Reached!"
                barColour = green
            }
            else //if over or if equal to max
            {
                calcHour = maxHour
                goalText = maxText
                barColour = maxColor
            }
        }
        else //if over to equal to min
        {
            calcHour = minHour
            goalText = minText
            barColour = minColor
        }
        //return goal data
        return Triple(calcHour, goalText, barColour)
    }

    //method to get the total hours logged in the specified interval
    fun GetHours(interval: String, activityID: Int): Double
    {
        var total: Double = 0.0

        try
        {
            if (interval.equals("Daily")) //Daily
            {
                //logs
                for (log in GlobalClass.logs.indices)
                {
                    //if log belongs to activity
                    if (GlobalClass.logs[log].activityID == activityID)
                    {
                        //if log start date is today
                        if (GlobalClass.logs[log].startDate == LocalDate.now())
                        {
                            //add hours
                            total = total + GlobalClass.logs[log].hours
                        }
                    }
                }
            }
            else if (interval.equals("Weekly")) //Weekly
            {
                //get current week
                val weekFields = WeekFields.of(Locale.UK)
                val currentWeek = LocalDate.now().get(weekFields.weekOfWeekBasedYear())
                //logs
                for (log in GlobalClass.logs.indices)
                {
                    //if log belongs to activity
                    if (GlobalClass.logs[log].activityID == activityID)
                    {
                        //get log week
                        val logWeek = GlobalClass.logs[log].startDate.get(weekFields.weekOfWeekBasedYear())
                        //if log in in week
                        if (logWeek == currentWeek)
                        {
                            //add hour
                            total = total + GlobalClass.logs[log].hours
                        }
                    }
                }
            }
            else if (interval.equals("Monthly")) //Monthly
            {
                //get current date
                val currentDate = LocalDate.now()
                //logs
                for (log in GlobalClass.logs.indices)
                {
                    //if log belongs to activiyt
                    if (GlobalClass.logs[log].activityID == activityID)
                    {
                        //if log in this month of this year
                        if (GlobalClass.logs[log].startDate.month == currentDate.month && GlobalClass.logs[log].startDate.year == currentDate.year)
                        {
                            //add hours
                            total = total + GlobalClass.logs[log].hours
                        }
                    }
                }
            }
        }
        catch (e: Error)
        {

        }
        return total
    }

    //method to check the hours of an individual goal
    public fun CheckGoal(interval: String, amount: Int, activityID: Int): Triple<String, String, String>
    {
        var hourToGo: String = ""
        var hourText: String = ""
        var barColor: String = ""
        try
        {
            val total = GetHours(interval, activityID)
            if (total == amount.toDouble()) //if goal reached
            {
                hourToGo = "✔"
                hourText = "Goal Reached!"
                barColor = green
            }
            else if (total > amount.toDouble()) //if over goal
            {
                hourToGo = (total - amount).toString()
                hourText = "Overtime"
                barColor = red
            }
            else //if under goal
            {
                hourToGo = (amount.toDouble() - total).toString()
                hourText = "Hours to Go!"
                barColor = yellow
            }
        }
        catch (e: Error)
        {

        }
        //return goal data
        return Triple(hourToGo, hourText, barColor)
    }
}




