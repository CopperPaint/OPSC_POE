package com.example.opsc_poe

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.opsc_poe.GlobalClass.Companion.DoubleToTime
import com.example.opsc_poe.databinding.ActivityViewDetailsFragmentBinding
import com.example.opsc_poe.databinding.ActivityViewLogsFragmentBinding

class View_Activity_Logs_Fragment : Fragment(R.layout.activity_view_logs_fragment) {
    private var _binding: ActivityViewLogsFragmentBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityViewLogsFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        //create local fragment controller
        val fragmentControl = FragmentHandler()

        //-------------------------------------------------
        //code here

        val activityIDIndex = requireActivity()!!.intent.extras!!.getInt("activityIDIndex")
        var currentActivity = GlobalClass.activities[activityIDIndex]

        //-----------------------------------------------------------------------------------------------------

        var userHasData = false
        for (i in GlobalClass.logs.indices) {

            if (currentActivity.activityID == GlobalClass.logs[i].activityID)
            {
                userHasData = true
                break
            }

        }

        if (userHasData == false)
        {
            GlobalClass.NoUserAppData(binding.llBars, requireActivity(), requireContext(), "Log", activityIDIndex)
        }
        else {


            val activityLayout = binding.llBars
            for (i in GlobalClass.logs.indices) {
                //if the activity belongs to the signed in user
                if (GlobalClass.logs[i].activityID == currentActivity.activityID) {

                    //create new custom activity
                    var newLog = CustomActivity(activity)
                    //set primary text
                    newLog.binding.tvPrimaryText.text = "Start Date"

                    //get activity category
                    // var index = Temp_CategoryDataClass().GetIndex(GlobalClass.activities[i].categoryID, GlobalClass.categories)
                    //  var category = GlobalClass.categories[index]

                    //set secondary text
                    newLog.binding.tvSecondaryText.text = GlobalClass.logs[i].startDate.toString()//category.name

                    //change the text sizes
                    newLog.binding.tvPrimaryText.textSize = 14F
                    newLog.binding.tvSecondaryText.textSize = 20F

                    var catIndex = Temp_CategoryDataClass().GetIndex(currentActivity.categoryID, GlobalClass.categories)
                    var category = GlobalClass.categories[catIndex]

                    //set the activity color shape color
                    val catColour = ColorStateList.valueOf(Color.parseColor(category.colour))

                    //ColorStateList.valueOf(Color.parseColor(category.colour))
                    newLog.binding.llBlockText.backgroundTintList = catColour


                    val barColor = ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.Default_Charcoal_Grey)

                    newLog.binding.vwBar.backgroundTintList = barColor
                    newLog.binding.tvBlockText.text = "Hours Logged"
                    newLog.binding.tvBlockX.text = DoubleToTime(GlobalClass.logs[i].hours.toString())
                    //newActivity.binding.llBlockText.backgroundTintList =  ColorStateList.valueOf(Color.parseColor("#5c37d7"))

                    //add the new view
                    activityLayout.addView(newLog)
                }
            }
        }


            //-----------------------------------------------------------------------------------------------------



                binding.imgBackIndicator.setOnClickListener()
        {

            //set the sign in fragment to be the initial view
            fragmentControl.replaceFragmentAnim(View_Activity_Details_Fragment(), R.id.fcFragmentContainer, parentFragmentManager, "Down", requireContext())

        }


        //-------------------------------------------------


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}