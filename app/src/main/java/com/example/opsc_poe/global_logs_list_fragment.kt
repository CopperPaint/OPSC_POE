package com.example.opsc_poe

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.opsc_poe.databinding.ActivityGlobalLogsListFragmentBinding
import com.example.opsc_poe.databinding.ActivityViewDetailsFragmentBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class global_logs_list_fragment : Fragment(R.layout.activity_global_logs_list_fragment) {

        private var _binding: ActivityGlobalLogsListFragmentBinding? = null
        // This property is only valid between onCreateView and
// onDestroyView.
        private val binding get() = _binding!!
        var StartDate: LocalDate? = null
        var EndDate: LocalDate? = null

        @SuppressLint("Range")
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View?
        {
            _binding = ActivityGlobalLogsListFragmentBinding.inflate(inflater, container, false)
            val view = binding.root

            var userHasData = false
            for (i in GlobalClass.logs.indices)
            {
                //if user has logs
                if (GlobalClass.logs[i].userID  == GlobalClass.user.userID)
                {
                    userHasData = true
                    //break
                }
            }

            if (userHasData == false) //if user has no logs
            {
                GlobalClass.NoUserAppData(binding.llLogContainer, requireActivity(), requireContext(), "Logs", 0)
            }
            else //if user has logs
            {
                //generate full list
                try {
                    GenerateLogList()
                }
                catch (e: Error)
                {
                    GlobalClass.InformUser("Error", "${e.toString()}", requireContext())
                }
            }

            //Date Pickers
            //-----------------------------------------------------------------------------------------
            val calendar = Calendar.getInstance()
            //Start date picker
            val StartdatePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                var dateText = updateTable(calendar)
                binding.tvStartDate.text = dateText
                StartDate = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                GenerateLogList()
            }

            binding.btnStartDate.setOnClickListener {
                var userHasData = false
                for (i in GlobalClass.logs.indices) {
                    if (GlobalClass.logs[i].userID == GlobalClass.user.userID)
                    {
                        userHasData = true
                        break
                    }
                }

                if (userHasData == false) //if user has no data
                {
                    GlobalClass.InformUser(
                        "No Logs Exist",
                        "Go to an activity to add a log",
                        requireContext()
                    )
                }
                else
                {
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(requireContext(), StartdatePicker,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show()
                }
            }

            //End Date Picker
            val EnddatePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                var dateText = updateTable(calendar)
                binding.tvEndDate.text = dateText
                EndDate = calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                GenerateLogList()
            }

            binding.btnEndDate.setOnClickListener {

                var userHasData = false
                for (i in GlobalClass.logs.indices) {
                    if (GlobalClass.logs[i].userID == GlobalClass.user.userID)
                    {
                        userHasData = true
                        break
                    }
                }

                if (userHasData == false)
                {
                    GlobalClass.InformUser(
                        "No Logs Exist",
                        "Go to an activity to add a log",
                        requireContext()
                    )
                }
                else
                {
                    val calendar2 = Calendar.getInstance()
                    calendar2.set(2023, Calendar.JUNE, 1)

                    DatePickerDialog(requireContext(), EnddatePicker,
                        calendar2.get(Calendar.YEAR),
                        calendar2.get(Calendar.MONTH),
                        calendar2.get(Calendar.DAY_OF_MONTH)).show()
                }
            }
            //------------------------------------------------------
            return view
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }


    private fun GenerateLogList()
    {
        binding.llLogContainer.removeAllViews()
        var loglist = GlobalClass.logs
        loglist.sortBy { it.startDate }

        for (i in loglist.indices)
        {
            if (loglist[i].userID == GlobalClass.user.userID)
            {
                var activityIndex = 0
                //get activity index
                for (j in GlobalClass.activities.indices)
                {
                    if (loglist[i].activityID == GlobalClass.activities[j].activityID)
                    {
                        activityIndex = j
                    }
                }
                var activity = GlobalClass.activities[activityIndex]

                if (StartDate != null)
                {
                    if (EndDate != null) //both dates
                    {
                        if (loglist[i].startDate.isAfter(StartDate) && (loglist[i].endDate.isBefore(EndDate)))
                        {
                            AddLogView(loglist[i], activity)
                        }
                    }
                    else //start only
                    {
                        if (loglist[i].startDate.isAfter(StartDate))
                        {
                            AddLogView(loglist[i], activity)
                        }
                    }
                }
                else
                {
                    if (EndDate != null) //End Date Only
                    {
                        if ((loglist[i].endDate.isBefore(EndDate)))
                        {
                            AddLogView(loglist[i], activity)
                        }
                    }
                    else //start only
                    {
                        AddLogView(loglist[i], activity)
                    }
                }
            }
        }

        if (binding.llLogContainer.childCount == 0)
        {
            GlobalClass.NoUserAppData(binding.llLogContainer, requireActivity(), requireContext(), "LogsData", 0)
        }
    }

    //Update Date Text
    private fun updateTable(calendar: Calendar) : String {
        val dateFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(dateFormat, Locale.UK)
        var dateText = sdf.format(calendar.time)
        return dateText
    }

    //Add Log to View
    @SuppressLint("Range")
    private fun AddLogView(log: Temp_LogDataClass, activity: Temp_ActivityDataClass)
    {
        //create new custom activity
        var newLog = CustomActivity(requireActivity())

        val logParam: ViewGroup.MarginLayoutParams = newLog.binding.vwBar.layoutParams as ViewGroup.MarginLayoutParams
        logParam.setMargins(28, logParam.topMargin, logParam.rightMargin, logParam.bottomMargin)
        newLog.binding.vwBar.layoutParams = logParam

        //set primary text
        newLog.binding.tvPrimaryText.text = activity.name

        //set secondary text
        newLog.binding.tvSecondaryText.text = log.startDate.toString()

        //change the text sizes
        newLog.binding.tvPrimaryText.textSize = 14F
        newLog.binding.tvSecondaryText.textSize = 20F

        var catIndex = Temp_CategoryDataClass().GetIndex(
            activity.categoryID,
            GlobalClass.categories
        )
        var category = GlobalClass.categories[catIndex]
        //set the activity color shape color
        val catColour = ColorStateList.valueOf(Color.parseColor(category.colour))
        //ColorStateList.valueOf(Color.parseColor(category.colour))
        newLog.binding.llBlockText.backgroundTintList = catColour

        //set bar color
        val barColor = ContextCompat.getColorStateList(
            requireContext(),
            R.color.Default_Charcoal_Grey
        )
        newLog.binding.vwBar.backgroundTintList = barColor

        newLog.binding.tvBlockText.text = "Hours Logged"

        newLog.binding.tvBlockX.text = GlobalClass.DoubleToTime(log.hours.toString())

        binding.llLogContainer.addView(newLog)
    }
}
