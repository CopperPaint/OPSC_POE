package com.example.opsc_poe

import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.opsc_poe.GlobalClass.Companion.ReturnToHome
import com.example.opsc_poe.databinding.ActivityAddLogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.round
import kotlin.math.roundToInt

class AddLog : AppCompatActivity()
{
    //timer started
    private var timerStarted = false
    //service
    private lateinit var serviceIntent: Intent
    //current time of stop watch
    private var time = 0.0
    //binding
    private lateinit var binding: ActivityAddLogBinding


    //on create method
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Hide the action bar
        supportActionBar?.hide()

        //set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.Dark_Green)

        binding.dpHours.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS

        //get extra value
        val activityIDIndex = intent.getIntExtra("activityIDIndex", 0)

        //passed activity
        var activity = GlobalClass.activities[activityIDIndex]

        //show activity name
        binding.tvActivityName.text = activity.name

        //set hour picker
        binding.dpHours.setIs24HourView(true)
        var isStopWatch = true

        //DATE PICKER
        //---------------------------------------------------------------------------------
        val calendar = Calendar.getInstance()
        //set start date
        binding.tvStartDate.text = updateLable(calendar)
        //start date picker pop up
        val StartDatePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            var dateText = updateLable(calendar)
            binding.tvStartDate.text = dateText
        }
        //start date button
        binding.btnStartDate.setOnClickListener {
            DatePickerDialog(this, StartDatePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.imgBlackTurtle.setOnClickListener()
        {
            ReturnToHome(this)
        }
        //SPINNER
        //------------------------------------------------------------------------------------
        //set spinner items
        val items = arrayOf("Stopwatch", "Input Hours")
        val spinner = findViewById<Spinner>(R.id.spWatchOption)
        if (spinner != null)
        {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, items)
            spinner.adapter = adapter
        }

        //spinner is changed
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long)
            {
                val selectedItem = parent.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "Stopwatch" -> {
                        binding.llStopWatch.visibility = View.VISIBLE
                        binding.llInputHours.visibility = View.GONE
                        isStopWatch = true
                    }
                    "Input Hours" -> {
                        // Code to execute when "Input Hours" is selected
                        binding.llStopWatch.visibility = View.GONE
                        binding.llInputHours.visibility = View.VISIBLE
                        isStopWatch = false
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to execute when nothing is selected
            }
        }


        //TIMER
        //-------------------------------------------------------------------------------------
        binding.btnStartStop.setOnClickListener() {startStopTimer()}
        binding.btnReset.setOnClickListener() {resetTimer()}

        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))


        //SAVE
        //--------------------------------------------------------------------------------------
        //save goal
        binding.btnAdd.setOnClickListener()
        {
            try
            {
                var inputTime = 0.0
                if (isStopWatch)
                {
                    inputTime = round((time/60.0)/60.0 * 100) / 100
                }
                else
                {
                    val hour = binding.dpHours.hour
                    val min = binding.dpHours.minute
                    val timeInHours = hour + (min / 60.0)
                    inputTime = round(timeInHours * 100) / 100
                }

                if (inputTime == 0.0)
                {
                    GlobalClass.InformUser("Input Error","Cannot have 0 hours. Please set the amount of hours or use the stop watch to time an activity", this)
                }
                else
                {
                    //create new log item
                    var startDate = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()


                    var log = Temp_LogDataClass(
                        logID = GlobalClass.logs.size + 1,
                        activityID = activity.activityID,
                        userID = GlobalClass.user.userID,
                        startDate = startDate,
                        endDate = addHoursToDate(startDate, inputTime),
                        hours = inputTime
                    )
                    //GlobalClass.logs.add(log)

                    GlobalScope.launch {
                        var DBmanager = ManageDatabase()
                        //add log to database
                        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

                        var store = LogStore(
                            logID = log.logID,
                            activityID = log.activityID,
                            userID = log.userID,
                            startDate = log.startDate.format(formatter),
                            endDate = log.endDate.format(formatter),
                            hours = log.hours
                        )
                        DBmanager.AddLogToFirestore(store)

                        //READ DATA
                        GlobalClass.categories = DBmanager.getCategoriesFromFirestore(GlobalClass.user.userID)
                        GlobalClass.activities = DBmanager.getActivitesFromFirestore(GlobalClass.user.userID)
                        GlobalClass.goals = DBmanager.getGoalsFromFirestore(GlobalClass.user.userID)
                        GlobalClass.logs = DBmanager.getLogsFromFirestore(GlobalClass.user.userID)
                        GlobalClass.UpdateDataBase = false

                        withContext(Dispatchers.Main) {
                            ReturnToHome()
                        }
                    }
                }
            }
            catch (e: Error)
            {
                GlobalClass.InformUser("Error", e.toString(), this)
                //return user to the sign in screen
                var intent = Intent(this, MainActivity::class.java) //ViewActivity
                startActivity(intent)
            }
        }
    }


    fun ReturnToHome()
    {
        var intent = Intent(this, Home_Activity::class.java) //ViewActivity
        startActivity(intent)
    }


    fun addHoursToDate(date: LocalDate, hours: Double): LocalDate
    {
        val time = LocalTime.MIDNIGHT
        val dateTime = LocalDateTime.of(date, time)
        val updatedDateTime = dateTime.plusHours(hours.toLong())
        return updatedDateTime.toLocalDate()
    }

    //Date Format Method
    //------------------------------------------------------------------------------------
    private fun updateLable(calendar: Calendar) : String
    {
        var dateText = ""
        try
        {
            val dateFormat = "dd-MM-yyyy"
            val sdf = SimpleDateFormat(dateFormat, Locale.UK)
            dateText = sdf.format(calendar.time)
            return dateText
        }
        catch (e: Error)
        {
            GlobalClass.InformUser("Error", e.toString(), this)
            //return user to the sign in screen
            var intent = Intent(this, MainActivity::class.java) //ViewActivity
            startActivity(intent)
        }
        return dateText
    }

    //TIMER METHODS
    //------------------------------------------------------------------------------------
    //method to reset timer
    private fun resetTimer()
    {
        try
        {
            stopTimer()
            time = 0.0
            binding.tvTime.text = getTimeStringFromDouble(time)
        }
        catch (e: Error)
        {
            GlobalClass.InformUser("Error", e.toString(), this)
            //return user to the sign in screen
            var intent = Intent(this, MainActivity::class.java) //ViewActivity
            startActivity(intent)
        }
    }

    //method to to switch timer state
    private fun startStopTimer()
    {
        try
        {
            if(timerStarted)
                stopTimer()
            else
                startTimer()
        }
        catch (e: Error)
        {
            GlobalClass.InformUser("Error", e.toString(), this)
            //return user to the sign in screen
            var intent = Intent(this, MainActivity::class.java) //ViewActivity
            startActivity(intent)
        }
    }

    //method to start timer
    private fun startTimer()
    {
        try
        {
            serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
            startService(serviceIntent)
            binding.btnStartStop.text = "Stop"
            binding.btnStartStop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pause, 0, 0, 0)
            timerStarted = true
        }
        catch (e: Error)
        {
            GlobalClass.InformUser("Error", e.toString(), this)
            //return user to the sign in screen
            var intent = Intent(this, MainActivity::class.java) //ViewActivity
            startActivity(intent)
        }
    }

    //method to stop timer
    private fun stopTimer()
    {
        try
        {
            stopService(serviceIntent)
            binding.btnStartStop.text = "Start"
            binding.btnStartStop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.play_arrow, 0, 0, 0)
            timerStarted = false
        }
        catch (e: Error)
        {
            GlobalClass.InformUser("Error", e.toString(), this)
            //return user to the sign in screen
            var intent = Intent(this, MainActivity::class.java) //ViewActivity
            startActivity(intent)
        }
    }

    //method to update the time to the service
    private val updateTime: BroadcastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent)
        {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            binding.tvTime.text = getTimeStringFromDouble(time)
        }
    }

    //method to get the time string from a double
    private fun getTimeStringFromDouble(time: Double): CharSequence?
    {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    //method to format a time string
    private fun makeTimeString(hour: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d", hour, min, sec)

    override fun onBackPressed() {}
}

/*
data class LogStore(
    var logID: Int,
    var activityID: Int,
    var userID: Int,
    var startDate: String,
    var endDate: String,
    var hours: Double
)


 */
