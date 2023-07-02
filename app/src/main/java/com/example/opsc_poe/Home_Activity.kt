package com.example.opsc_poe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.opsc_poe.databinding.ActivityHomeBinding

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
            try
            {
                if (arrow == getString(R.string.animLeft))
                {
                    if (binding.tvSectionTitle.text == getString(R.string.viewUserActivitiesTitle))
                    {
                        binding.tvSectionTitle.text = getString(R.string.viewUserCategoriesTitle)
                        /*
                        fragmentControl.replaceFragment(
                            home_category_view_fragment(),
                            R.id.fcFragmentContainer,
                            supportFragmentManager
                        )
                         */
                        fragmentControl.replaceFragmentAnim(home_category_view_fragment(), R.id.fcFragmentContainer, supportFragmentManager, getString(R.string.animLeft), this)
                    } else
                    {
                        binding.tvSectionTitle.text = getString(R.string.viewUserActivitiesTitle)
                        /*
                         fragmentControl.replaceFragment(
                             home_activity_view_fragment(),
                             R.id.fcFragmentContainer,
                             supportFragmentManager
                         )
                         */
                        fragmentControl.replaceFragmentAnim(home_activity_view_fragment(), R.id.fcFragmentContainer, supportFragmentManager, getString(R.string.animLeft), this)
                    }
                }
                else
                {
                    if (binding.tvSectionTitle.text == getString(R.string.viewUserActivitiesTitle))
                    {
                        binding.tvSectionTitle.text = getString(R.string.viewUserCategoriesTitle)
                        /*
                        fragmentControl.replaceFragment(
                            home_category_view_fragment(),
                            R.id.fcFragmentContainer,
                            supportFragmentManager
                        )

                         */
                        fragmentControl.replaceFragmentAnim(home_category_view_fragment(), R.id.fcFragmentContainer, supportFragmentManager, getString(R.string.animRight), this)
                    } else
                    {
                        binding.tvSectionTitle.text = getString(R.string.viewUserActivitiesTitle)
                        /*
                         fragmentControl.replaceFragment(
                             home_activity_view_fragment(),
                             R.id.fcFragmentContainer,
                             supportFragmentManager
                         )
                         */
                        fragmentControl.replaceFragmentAnim(home_activity_view_fragment(), R.id.fcFragmentContainer, supportFragmentManager, getString(R.string.animRight), this)
                    }
                }
            }
            catch (e: Error)
            {
                GlobalClass.InformUser(getString(R.string.errorTitle),"${e.toString()}", this)
            }
        }

        binding.imgCycleViewLeft.setOnClickListener {
            CycleHomeFragmentView(getString(R.string.animLeft))
        }

        binding.imgCycleViewRight.setOnClickListener {
            CycleHomeFragmentView(getString(R.string.animRight))
        }

        //add new entry depending on if category or activity is selected
        binding.imgAddEntry.setOnClickListener{

            if (binding.tvSectionTitle.text == getString(R.string.viewUserActivitiesTitle))
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

        //settings button
        binding.imgSettingsButton.setOnClickListener()
        {
            var intent = Intent(this, settings_view::class.java)
            intent.putExtra(getString(R.string.previousScreenKey), getString(R.string.homeView))
            startActivity(intent)
        }
    }
    override fun onBackPressed() {}
}