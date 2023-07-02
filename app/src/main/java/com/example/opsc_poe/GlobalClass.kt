package com.example.opsc_poe

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


class GlobalClass : Application()
{
    companion object
    {
        var UpdateDataBase: Boolean = true
        var documents = DocumentID()

        var activities = arrayListOf<Temp_ActivityDataClass>()
        var categories = arrayListOf<Temp_CategoryDataClass>()
        var goals = arrayListOf<Temp_GoalDataClass>()
        var logs = arrayListOf<Temp_LogDataClass>()
        var allUsers = arrayListOf<Temp_UserDataClass>()
        var user = Temp_UserDataClass()

        fun ReturnToHome(context: Context)
        {
            var intent = Intent(context, Home_Activity::class.java)
            context.startActivity(intent)
        }

        fun NoUserAppData(barLayout: LinearLayout, barActivity: FragmentActivity?, barContext : Context, screenFunction: String, dataID : Int)
        {

            var barNoData = CustomActivity(barActivity)
            barNoData.binding.tvPrimaryText.text = "${barContext.getString(R.string.textNo)} $screenFunction ${barContext.getString(R.string.textData)}" //"No $screenFunction Data"
            barNoData.binding.tvSecondaryText.text = "${barContext.getString(R.string.promptIndicatorToAdd)} $screenFunction ${barContext.getString(R.string.textData)}"//"Click Here to Add $screenFunction Data"
            barNoData.binding.vwBar.backgroundTintList = ContextCompat.getColorStateList(barContext, R.color.Default_Charcoal_Grey)
            barNoData.binding.llBlockText.backgroundTintList = ContextCompat.getColorStateList(barContext, R.color.Muted_Green)
            barNoData.binding.tvBlockText.text = barContext.getString(R.string.clickIndicator)
            barNoData.binding.tvBlockX.text = barContext.getString(R.string.addSymbol)
            barNoData.binding.tvBlockX.textSize = 36F

            barNoData.isClickable = true

            //set the + text to be closer to the Click Me Text
            val barParam: ViewGroup.MarginLayoutParams = barNoData.binding.tvBlockX.layoutParams as ViewGroup.MarginLayoutParams
            barParam.setMargins(barParam.leftMargin, -40, barParam.rightMargin, barParam.bottomMargin)
            barNoData.binding.tvBlockX.layoutParams = barParam


            when (screenFunction)
            {
                barContext.getString(R.string.logsDataScreenFunction) ->
                {

                        val logParam: ViewGroup.MarginLayoutParams = barNoData.binding.vwBar.layoutParams as ViewGroup.MarginLayoutParams
                        logParam.setMargins(28, logParam.topMargin, logParam.rightMargin, logParam.bottomMargin)
                        barNoData.binding.vwBar.layoutParams = logParam

                        barNoData.binding.tvPrimaryText.text = barContext.getString(R.string.noLogsFound)
                        barNoData.binding.tvSecondaryText.text = barContext.getString(R.string.noLogsFoundMessage)

                        barNoData.binding.tvBlockText.text = ""
                        barNoData.binding.tvBlockX.text = barContext.getString(R.string.calSymbol) //"\uD83D\uDCC5"
                        barNoData.binding.tvBlockX.height = 210


                }
                barContext.getString(R.string.logsScreenFunction) ->
                {

                    val logParam: ViewGroup.MarginLayoutParams = barNoData.binding.vwBar.layoutParams as ViewGroup.MarginLayoutParams
                    logParam.setMargins(28, logParam.topMargin, logParam.rightMargin, logParam.bottomMargin)

                    barNoData.binding.vwBar.layoutParams = logParam
                    barNoData.binding.tvSecondaryText.text = barContext.getString(R.string.promptToGoAddLog)

                    barNoData.binding.tvBlockText.text = ""
                    barNoData.binding.tvBlockX.text = barContext.getString(R.string.calSymbol) //"\uD83D\uDCC5"
                    barNoData.binding.tvBlockX.height = 210
                }
                barContext.getString(R.string.logScreenFunction) ->
                {
                    barNoData.setOnClickListener()
                    {
                        //load add activity
                        var intent = Intent(barContext, AddLog::class.java)
                        intent.putExtra(barContext.getString(R.string.activityIdentityIndex), dataID)
                        barContext.startActivity(intent)
                    }
                }
                barContext.getString(R.string.textActivity) ->
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
                            InformUser(barContext.getString(R.string.noCategoriesTitle), barContext.getString(R.string.noCategoriesMessage), barContext)
                        }


                    }
                }
                barContext.getString(R.string.viewLogCategory)->
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
            alert.setPositiveButton(context.getString(R.string.alertOK), null)

            alert.show()
        }


        fun DoubleToTime(currentDouble: String, context : Context): String {

            if (currentDouble.toDoubleOrNull() == null) {
                return currentDouble

            }
            else
            {
                var splitCurrentDouble = currentDouble.split(context.getString(R.string.timeDelimiter))
                var currentHours = splitCurrentDouble[0].toInt()
                var minutesFraction = context.getString(R.string.minutesFractionStart) + splitCurrentDouble[1]
                var currentMinutes = (minutesFraction.toDouble() * 60)
                return "$currentHours:${currentMinutes.roundToInt()}"
            }

            //GlobalClass.InformUser("", "Hours Split: $currentHours Minutes Split: ${currentMinutes.roundToInt()}", requireContext())
            //GlobalClass.InformUser("", "", requireContext())
            //GlobalClass.InformUser("", "Hours Split: $tt", requireContext())

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