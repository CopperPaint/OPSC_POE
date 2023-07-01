package com.example.opsc_poe

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import java.io.File




class ExportUserData (
    var selectedExportItems : ArrayList<Int>,
    private val upperContext : Context

)
{

 //val selectedExportItems = ArrayList<Int>()


    fun evaluateOptions()
    {

        //0, 1
        //0 is first item = activity data
        //1 is second = log data

        var filesToExport = ArrayList<File>()

        if (selectedExportItems.contains(0))
        {
            //if the user wants to export activity data

            val activityData = exportActivityData()
            val activityDataFile = generateCSVFileToExport(activityData, upperContext.getString(R.string.exportActivityFileName), upperContext.getString(R.string.exportActivityHeaders))
            filesToExport.add(activityDataFile)

        }

        if (selectedExportItems.contains(1))
        {
            //if the user wants to export log data

            val logData = exportLogData()
            val logDataFile = generateCSVFileToExport(logData, upperContext.getString(R.string.exportLogFileName), upperContext.getString(R.string.exportLogHeaders))
            filesToExport.add(logDataFile)

        }

        if (!selectedExportItems.isNullOrEmpty())
        {
            shareCSVFiles(filesToExport)
        }

    }

    private fun exportActivityData(): ArrayList<ExportActivityData>
    {

        var userActivityData = arrayListOf<ExportActivityData>()
        var goalCalculator = GoalHourCalculator()

        //loop through activities
        for (i in GlobalClass.activities.indices)
        {


            //if the activity belongs to the current user
            if (GlobalClass.activities[i].userID == GlobalClass.user.userID) {

                //define or redefine the variable to hold the users export activity data each time an activity matches the current user
                var userActivity = ExportActivityData()

                //set activity name
                userActivity.activityName = GlobalClass.activities[i].name

                //set activity description
                userActivity.activityDescription = GlobalClass.activities[i].description



                //loop through categories
                for (j in GlobalClass.categories.indices)
                {
                    //if the current activity belongs to the current category
                    if (GlobalClass.categories[j].categoryID == GlobalClass.activities[i].categoryID)
                    {
                        //set category name
                        userActivity.activityCategory = GlobalClass.categories[j].name
                    }
                }



                //loop through logs
                for (k in GlobalClass.logs.indices)
                {
                    //if the current activity belongs to the current category
                    if (GlobalClass.logs[k].activityID == GlobalClass.activities[i].activityID)
                    {
                        //increment the log count (end of loop signifies that the log count is set)
                        userActivity.activityLogCount++
                    }
                }



                //loop through goals
                for (p in GlobalClass.goals.indices)
                {
                    //if the current goal is the max goal of the current activity
                    if (GlobalClass.goals[p].goalID == GlobalClass.activities[i].maxgoalID)
                    {

                        //set the activity completed hours
                        userActivity.activityGoalCompletedAmount = goalCalculator.GetHours(GlobalClass.goals[p].interval, GlobalClass.activities[i].activityID)



                        //set the maximum goal interval
                        userActivity.activityMaximumGoalInterval = GlobalClass.goals[p].interval

                        //set the maximum goal time
                        userActivity.activityMaximumGoalTime = GlobalClass.goals[p].amount.toDouble()

                        //set the maximum goal remaining hours
                        userActivity.activityMaximumGoalRemaining = userActivity.activityMaximumGoalTime - userActivity.activityGoalCompletedAmount

                    }

                    //if the current goal is the min goal of the current activity
                    if (GlobalClass.goals[p].goalID == GlobalClass.activities[i].mingoalID)
                    {
                        //set the maximum goal interval
                        userActivity.activityMinimumGoalInterval = GlobalClass.goals[p].interval

                        //set the maximum goal time
                        userActivity.activityMinimumGoalTime = GlobalClass.goals[p].amount.toDouble()

                        //set the maximum goal remaining hours
                        userActivity.activityMinimumGoalRemaining = userActivity.activityMinimumGoalTime - userActivity.activityGoalCompletedAmount

                    }


                }

                userActivityData.add(userActivity)

            }


        }

        return userActivityData

    }

    private fun exportLogData(): ArrayList<ExportLogData>
    {

        var userLogData = arrayListOf<ExportLogData>()

        //loop through activities
        for (i in GlobalClass.activities.indices) {

            //if the activity belongs to the current user
            if (GlobalClass.activities[i].userID == GlobalClass.user.userID) {



                //loop through activities
                for (j in GlobalClass.logs.indices)
                {
                    //if the log belongs to the current activity of the current user
                    if (GlobalClass.activities[i].activityID == GlobalClass.logs[j].activityID)
                    {

                        //define or redefine the variable to hold the users export log data each time a log matches the current userand activity
                        var userLog = ExportLogData()

                        //set the name of the activity that the log belongs to
                        userLog.logActivityName = GlobalClass.activities[i].name

                        //set the logged hours for that activities session
                        userLog.logHour = GlobalClass.logs[j].hours

                        //set the date that the log took place on
                        userLog.logDate = GlobalClass.logs[j].startDate

                        userLogData.add(userLog)


                    }
                }


            }

        }

        return userLogData

    }
/*
    private fun generateActivityCSVFile(userActivityDataArray: ArrayList<ExportActivityData>): File {

        //list of activities and data to be exported (change with the populated list from previous method)
        //var userActivityDataArray = arrayListOf<ExportActivityData>()

        /*
        //-----------------------------------------------
        val internalPath = parentContext.filesDir.path + "/Export"
        val fileCSV = File(internalPath,"UserActivityData.csv")

        val filePrintWriter = fileCSV.printWriter()

        filePrintWriter.println(activityHeaders)

        for (i in userActivityDataArray)
        {
            //var formattedCSVLine = "\"${i.activityName}\", \"${i.activityDescription}\", \"${i.activityCategory}\", ${i.activityLogCount}, ${i.activityGoalCompletedAmount}, \"${i.activityMaximumGoalInterval}\", ${i.activityMaximumGoalTime}, ${i.activityMaximumGoalRemaining}, \"${i.activityMinimumGoalInterval}\", ${i.activityMinimumGoalTime}, ${i.activityMinimumGoalRemaining}"
            var currentCSVRow = i.toCSVRow()
            filePrintWriter.println(currentCSVRow)
        }


        filePrintWriter.flush()
        //-----------------------------------------------

 */

        val activityHeaders = "Activity Name, Description, Category, Number of Logs, Total Hours Logged (Hours), Maximum Goal Interval, Maximum Goal (Hours), Maximum Goal Remaining Time (Hours), Minimum Goal Interval, Minimum Goal (Hours), Minimum Goal Remaining Time (Hours)"

        val externalCacheFile = File(parentContext.externalCacheDir, "UserActivityData.csv")

        val filePrintWriter = externalCacheFile.printWriter()

        filePrintWriter.println(activityHeaders)

        for (i in userActivityDataArray)
        {
            //var formattedCSVLine = "\"${i.activityName}\", \"${i.activityDescription}\", \"${i.activityCategory}\", ${i.activityLogCount}, ${i.activityGoalCompletedAmount}, \"${i.activityMaximumGoalInterval}\", ${i.activityMaximumGoalTime}, ${i.activityMaximumGoalRemaining}, \"${i.activityMinimumGoalInterval}\", ${i.activityMinimumGoalTime}, ${i.activityMinimumGoalRemaining}"
            var currentCSVRow = i.toCSVRow()
            filePrintWriter.println(currentCSVRow)
        }


        filePrintWriter.flush()



        return File(parentContext.getExternalFilesDir(null), "UserActivityData.csv")


    }

 */

/*
    private fun generateCSVFileToExport(dataArray: ArrayList<ExportActivityData>, fileName: String, fileHeaders: String): File {


        val externalCacheFile = File(upperContext.externalCacheDir, fileName)

        val filePrintWriter = externalCacheFile.printWriter()

        //write the export file headers
        filePrintWriter.println(fileHeaders)

        for (i in dataArray)
        {
            //var formattedCSVLine = "\"${i.activityName}\", \"${i.activityDescription}\", \"${i.activityCategory}\", ${i.activityLogCount}, ${i.activityGoalCompletedAmount}, \"${i.activityMaximumGoalInterval}\", ${i.activityMaximumGoalTime}, ${i.activityMaximumGoalRemaining}, \"${i.activityMinimumGoalInterval}\", ${i.activityMinimumGoalTime}, ${i.activityMinimumGoalRemaining}"
            var currentCSVRow = i.toCSVRow()
            filePrintWriter.println(currentCSVRow)
        }


        filePrintWriter.flush()



        return File(upperContext.getExternalFilesDir(null), fileName)


    }

    private fun generateCSVFileToExport(dataArray: ArrayList<ExportLogData>, fileName: String, fileHeaders: String): File {


        val externalCacheFile = File(upperContext.externalCacheDir, fileName)

        val filePrintWriter = externalCacheFile.printWriter()

        //write the export file headers
        filePrintWriter.println(fileHeaders)

        for (i in dataArray)
        {
            //var formattedCSVLine = "\"${i.activityName}\", \"${i.activityDescription}\", \"${i.activityCategory}\", ${i.activityLogCount}, ${i.activityGoalCompletedAmount}, \"${i.activityMaximumGoalInterval}\", ${i.activityMaximumGoalTime}, ${i.activityMaximumGoalRemaining}, \"${i.activityMinimumGoalInterval}\", ${i.activityMinimumGoalTime}, ${i.activityMinimumGoalRemaining}"
            var currentCSVRow = i.toCSVRow()
            filePrintWriter.println(currentCSVRow)
        }


        filePrintWriter.flush()



        return File(upperContext.getExternalFilesDir(null), fileName)


    }

 */

    private fun generateCSVFileToExport(dataArray: ArrayList<out Any>, fileName: String, fileHeaders: String): File {


        val externalCacheFile = File(upperContext.externalCacheDir, fileName)

        val filePrintWriter = externalCacheFile.printWriter()

        //write the export file headers
        filePrintWriter.println(fileHeaders)

        for (i in dataArray)
        {
            //var formattedCSVLine = "\"${i.activityName}\", \"${i.activityDescription}\", \"${i.activityCategory}\", ${i.activityLogCount}, ${i.activityGoalCompletedAmount}, \"${i.activityMaximumGoalInterval}\", ${i.activityMaximumGoalTime}, ${i.activityMaximumGoalRemaining}, \"${i.activityMinimumGoalInterval}\", ${i.activityMinimumGoalTime}, ${i.activityMinimumGoalRemaining}"
            var currentCSVRow = i.toCSVRow()
            filePrintWriter.println(currentCSVRow)
        }


        filePrintWriter.flush()



        return File(upperContext.getExternalFilesDir(null), fileName)


    }



    private fun shareCSVFiles(fileArray: ArrayList<File>)
    {


        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/csv"
        intent.putExtra("Share this", fileArray)

        val chooser = Intent.createChooser(intent, "Share using...")
        startActivity(upperContext, chooser, null)
    }

    private fun Any.toCSVRow() : Any{

        val genericClass = Any::class.java
/*
        when (Any::class.java) {
            ExportActivityData::class -> ExportActivityData().toCSVRow()
            ExportLogData::class -> ExportLogData().toCSVRow()
            else -> throw java.lang.IllegalStateException("Unsupported type")
        }

 */

        if (genericClass.isInstance(ExportActivityData::class.java)) {
            return ExportActivityData().toCSVRow()
        } else {
            if (genericClass.isInstance(ExportLogData::class.java)) {
                return ExportLogData().toCSVRow()
            } else {
                throw java.lang.IllegalStateException("Unsupported type")
            }


        }


    }



}


