package com.example.opsc_poe

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.opsc_poe.databinding.ActivityHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home_Activity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Hide the action bar
        supportActionBar?.hide()

        //set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.Dark_Green)

        //create local fragment controller
        val fragmentControl = FragmentHandler()

        //set the activity view fragment to be the initial view
         fragmentControl.replaceFragment(home_activity_view_fragment(), R.id.fcFragmentContainer, supportFragmentManager)

        binding.tvUserUsername.text = GlobalClass.user.username

        fun CycleHomeFragmentView (arrow : String)
        {

            if (arrow == "Left")
            {
                if (binding.tvSectionTitle.text == "Activities")
                {
                    binding.tvSectionTitle.text = "Categories"
                    /*
                    fragmentControl.replaceFragment(
                        home_category_view_fragment(),
                        R.id.fcFragmentContainer,
                        supportFragmentManager
                    )

                     */
                    fragmentControl.replaceFragmentAnim(home_category_view_fragment(), R.id.fcFragmentContainer, supportFragmentManager, "Left")
                } else
                {
                    binding.tvSectionTitle.text = "Activities"
                    /*
                     fragmentControl.replaceFragment(
                         home_activity_view_fragment(),
                         R.id.fcFragmentContainer,
                         supportFragmentManager
                     )
                     */
                    fragmentControl.replaceFragmentAnim(home_activity_view_fragment(), R.id.fcFragmentContainer, supportFragmentManager, "Left")
                }
            }
            else
            {
                if (binding.tvSectionTitle.text == "Activities")
                {
                    binding.tvSectionTitle.text = "Categories"
                    /*
                    fragmentControl.replaceFragment(
                        home_category_view_fragment(),
                        R.id.fcFragmentContainer,
                        supportFragmentManager
                    )

                     */
                    fragmentControl.replaceFragmentAnim(home_category_view_fragment(), R.id.fcFragmentContainer, supportFragmentManager, "Right")
                } else
                {
                    binding.tvSectionTitle.text = "Activities"
                    /*
                     fragmentControl.replaceFragment(
                         home_activity_view_fragment(),
                         R.id.fcFragmentContainer,
                         supportFragmentManager
                     )
                     */
                    fragmentControl.replaceFragmentAnim(home_activity_view_fragment(), R.id.fcFragmentContainer, supportFragmentManager, "Right")
                }
            }


        }


        binding.imgCycleViewLeft.setOnClickListener {
            CycleHomeFragmentView("Left")
        }

        binding.imgCycleViewRight.setOnClickListener {
            CycleHomeFragmentView("Right")
        }

        //add new entry depending on if category or activity is selected
        binding.imgAddEntry.setOnClickListener{

            if (binding.tvSectionTitle.text == "Activities")
            {
                //load add activity
                var intent = Intent(this, CreateActivity::class.java)
                startActivity(intent)
            } else
            {
                //load add category view
                var intent = Intent(this, CreateCategory::class.java)
                startActivity(intent)
            }

        }

        binding.imgSettingsButton.setOnClickListener()
        {
            var intent = Intent(this, settings_view::class.java)
            intent.putExtra("previousScreen", "Home_View")
            startActivity(intent)
        }
    }
    override fun onBackPressed() {}
}