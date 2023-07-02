package com.example.opsc_poe

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import java.io.File
import java.net.URI


class ExportUserData (
    var selectedExportItems : ArrayList<Int>,
    private val upperContext : Context

) {

    //val selectedExportItems = ArrayList<Int>()


    fun evaluateOptions() {

        //0, 1
        //0 is first item = activity data
        //1 is second = log data

        var filesToExport = ArrayList<File>()

        if (selectedExportItems.contains(0)) {
            //if the user wants to export activity data

            val activityData = exportActivityData()
            val activityDataFile = generateActivityCSVFileToExport(
                activityData,
                upperContext.getString(R.string.exportActivityFileName),
                upperContext.getString(R.string.exportActivityHeaders)
            )
            filesToExport.add(activityDataFile)

        }

        if (selectedExportItems.contains(1)) {
            //if the user wants to export log data

            val logData = exportLogData()
            val logDataFile = generateLogCSVFileToExport(
                logData,
                upperContext.getString(R.string.exportLogFileName),
                upperContext.getString(R.string.exportLogHeaders)
            )
            filesToExport.add(logDataFile)

        }

        if (!selectedExportItems.isNullOrEmpty()) {
            shareCSVFiles(filesToExport)
        }
    }

    private fun exportActivityData(): ArrayList<ExportActivityData> {

        var userActivityData = arrayListOf<ExportActivityData>()
        var goalCalculator = GoalHourCalculator()

        //loop through activities
        for (i in GlobalClass.activities.indices) {


            //if the activity belongs to the current user
            if (GlobalClass.activities[i].userID == GlobalClass.user.userID) {

                //define or redefine the variable to hold the users export activity data each time an activity matches the current user
                var userActivity = ExportActivityData()

                //set activity name
                userActivity.activityName = GlobalClass.activities[i].name

                //set activity description
                userActivity.activityDescription = GlobalClass.activities[i].description


                //loop through categories
                for (j in GlobalClass.categories.indices) {
                    //if the current activity belongs to the current category
                    if (GlobalClass.categories[j].categoryID == GlobalClass.activities[i].categoryID) {
                        //set category name
                        userActivity.activityCategory = GlobalClass.categories[j].name
                    }
                }


                //loop through logs
                for (k in GlobalClass.logs.indices) {
                    //if the current activity belongs to the current category
                    if (GlobalClass.logs[k].activityID == GlobalClass.activities[i].activityID) {
                        //increment the log count (end of loop signifies that the log count is set)
                        userActivity.activityLogCount++
                    }
                }


                //loop through goals
                for (p in GlobalClass.goals.indices) {
                    //if the current goal is the max goal of the current activity
                    if (GlobalClass.goals[p].goalID == GlobalClass.activities[i].maxgoalID) {

                        //set the activity completed hours
                        userActivity.activityGoalCompletedAmount = goalCalculator.GetHours(
                            GlobalClass.goals[p].interval,
                            GlobalClass.activities[i].activityID
                        )




                        //set the maximum goal interval
                        userActivity.activityMaximumGoalInterval = GlobalClass.goals[p].interval

                        //set the maximum goal time
                        userActivity.activityMaximumGoalTime =
                            GlobalClass.goals[p].amount.toDouble()

                        //set the maximum goal remaining hours
                        userActivity.activityMaximumGoalRemaining =
                            userActivity.activityMaximumGoalTime - userActivity.activityGoalCompletedAmount

                    }

                    //if the current goal is the min goal of the current activity
                    if (GlobalClass.goals[p].goalID == GlobalClass.activities[i].mingoalID) {
                        //set the maximum goal interval
                        userActivity.activityMinimumGoalInterval = GlobalClass.goals[p].interval

                        //set the maximum goal time
                        userActivity.activityMinimumGoalTime =
                            GlobalClass.goals[p].amount.toDouble()

                        //set the maximum goal remaining hours
                        userActivity.activityMinimumGoalRemaining =
                            userActivity.activityMinimumGoalTime - userActivity.activityGoalCompletedAmount

                    }


                }

                userActivityData.add(userActivity)

            }


        }

        return userActivityData

    }

    private fun exportLogData(): ArrayList<ExportLogData> {

        var userLogData = arrayListOf<ExportLogData>()

        //loop through activities
        for (i in GlobalClass.activities.indices) {

            //if the activity belongs to the current user
            if (GlobalClass.activities[i].userID == GlobalClass.user.userID) {


                //loop through activities
                for (j in GlobalClass.logs.indices) {
                    //if the log belongs to the current activity of the current user
                    if (GlobalClass.activities[i].activityID == GlobalClass.logs[j].activityID) {

                        //define or redefine the variable to hold the users export log data each time a log matches the current userand activity
                        var userLog = ExportLogData(upperContext.getString(R.string.defaultDate), upperContext.getString(R.string.dateFormat))

                        //set the name of the activity that the log belongs to
                        userLog.logActivityName = GlobalClass.activities[i].name

                        //set the logged hours for that activities session
                        userLog.logHour = GlobalClass.logs[j].hours

                        //set the date that the log took place on
                        userLog.logDate = GlobalClass.logs[j].startDate

                        //set the date format
                        //userLog.dateFormat = upperContext.getString(R.string.errorTitle)

                        userLogData.add(userLog)
                    }
                }
            }

        }
        return userLogData
    }

    private fun generateActivityCSVFileToExport(
        dataArray: ArrayList<ExportActivityData>,
        fileName: String,
        fileHeaders: String
    ): File {

        //generate the file
        val externalCacheFile = File(upperContext.externalCacheDir, fileName)

        //define the writer
        val filePrintWriter = externalCacheFile.printWriter()

        //write the export file headers
        filePrintWriter.println(fileHeaders)

        //loop through the array and format the data
        for (i in dataArray) {

             var currentCSVRow = i.toCSVRow()

            //write the data to the file
            filePrintWriter.println(currentCSVRow)
        }

        //flush the file
        filePrintWriter.flush()


        //return the complete file
        return File(upperContext.externalCacheDir,fileName)//File(upperContext.getExternalFilesDir(null), fileName)
    }

    private fun generateLogCSVFileToExport(
        dataArray: ArrayList<ExportLogData>,
        fileName: String,
        fileHeaders: String
    ): File {

        //generate the file
        val externalCacheFile = File(upperContext.externalCacheDir, fileName)

        //define the writer
        val filePrintWriter = externalCacheFile.printWriter()

        //write the export file headers
        filePrintWriter.println(fileHeaders)

        //loop through the array and format the data
        for (i in dataArray) {

            var currentCSVRow = i.toCSVRow()

            //write the data to the file
            filePrintWriter.println(currentCSVRow)
        }

        //flush the file
        filePrintWriter.flush()

        //return the complete file
        return File(upperContext.externalCacheDir,fileName)//File(upperContext.getExternalFilesDir(null), fileName)
    }


    private fun shareCSVFiles(fileArray: ArrayList<File>) {

        var fileURIToExport = ArrayList<Uri>()

        for (i in fileArray)
        {
            var fileUri = FileProvider.getUriForFile(upperContext, BuildConfig.APPLICATION_ID + upperContext.getString(R.string.providerAddOn), i)
            //var fileUri = "content://com.example.opsc_poe.provider/external_files/Android/data/com.example.opsc_poe/cache/userActivityData.csv"
            fileURIToExport.add(fileUri)//.toUri())
        }
        //intent to provide share functionality
        //val intent = Intent(Intent.ACTION_SEND)
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        //define file type as csv
        intent.type = upperContext.getString(R.string.exportFileTypeFilter)

        //add extra with the file and prompt
        //intent.putExtra(Intent.EXTRA_STREAM, fileURIToExport)
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileURIToExport)

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        //define share menu intent
        val chooser = Intent.createChooser(intent, upperContext.getString(R.string.sharePrompt))

        upperContext.startActivity(chooser)

        //call the intent to prompt the user with the share menu
        //startActivity(upperContext, chooser, null)
    }
}


