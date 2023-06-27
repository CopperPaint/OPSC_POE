package com.example.opsc_poe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.opsc_poe.databinding.ActivityGlobalLogsBinding
import com.example.opsc_poe.databinding.ActivitySettingsViewBinding

class Global_Logs : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //Hide the action bar
        supportActionBar?.hide()

        //set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.Dark_Green)

        //Set view binding
        val binding = ActivityGlobalLogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //create local fragment controller
        val fragmentControl = FragmentHandler()

        //set the activity view fragment to be the initial view
        fragmentControl.replaceFragment(global_logs_list_fragment(), R.id.fcFragmentContainer, supportFragmentManager)

        fun CycleHomeFragmentView (arrow : String)
        {



            if (arrow == "Left")
            {
                if (binding.tvSectionTitle.text == "List")
                {
                    binding.tvSectionTitle.text = "Category"
                    fragmentControl.replaceFragmentAnim(
                        global_logs_category_fragment(),
                        R.id.fcFragmentContainer,
                        supportFragmentManager,
                        "Left"
                    )
                } else
                {
                    binding.tvSectionTitle.text = "List"
                    fragmentControl.replaceFragmentAnim(
                        global_logs_list_fragment(),
                        R.id.fcFragmentContainer,
                        supportFragmentManager,
                        "Left"
                    )
                }
            }
            else
            {
                if (binding.tvSectionTitle.text == "List")
                {
                    binding.tvSectionTitle.text = "Category"
                    fragmentControl.replaceFragmentAnim(
                        global_logs_category_fragment(),
                        R.id.fcFragmentContainer,
                        supportFragmentManager,
                        "Right"
                    )
                } else
                {
                    binding.tvSectionTitle.text = "List"
                    fragmentControl.replaceFragmentAnim(
                        global_logs_list_fragment(),
                        R.id.fcFragmentContainer,
                        supportFragmentManager,
                        "Right"
                    )
                }
            }

        }

        fun ExitLogs()
        {
            //return user to the home view screen
            var intent = Intent(this, Home_Activity::class.java) //ViewActivity
            startActivity(intent)
        }

        binding.imgCycleViewLeft.setOnClickListener()
        {
            CycleHomeFragmentView("Left")
        }

        binding.imgCycleViewRight.setOnClickListener()
        {
            CycleHomeFragmentView("Right")
        }

        binding.tvBackText.setOnClickListener()
        {
            ExitLogs()
        }

        binding.imgBackIndicator.setOnClickListener()
        {
            ExitLogs()
        }
    }
    override fun onBackPressed() {}
}