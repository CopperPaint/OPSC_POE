package com.example.opsc_poe

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings.Global
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.opsc_poe.databinding.ActivityMainBinding
import com.example.opsc_poe.databinding.ActivitySettingsViewBinding

class settings_view : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //Hide the action bar
        supportActionBar?.hide()

        //set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.Dark_Green)

        //Set view binding
        val binding = ActivitySettingsViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get the extra
        var previousScreen = intent.getStringExtra("previousScreen")

        fun SignOut()
        {
            //return user to the initial view screen
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fun GoBack(previousScreenVar: String)
        {
            when (previousScreenVar)
            {
                "Home_View" -> {
                    //return user to the initial view screen
                    var intent = Intent(this, Home_Activity::class.java)
                    startActivity(intent)
                }
                "Category_View" -> {
                    var returningCategoryID = intent.getIntExtra("currentDataID", 0)

                    //GlobalClass.InformUser("", returningCategoryID.toString(), this)

                    //return user to the initial view screen
                    var intent = Intent(this, CategoryName::class.java)
                    intent.putExtra("categoryIDIndex", returningCategoryID)
                    startActivity(intent)
                }
                "Activity_View" -> {
                    var returningActivityID = intent.getIntExtra("currentDataID", 0)

                    //GlobalClass.InformUser("", returningActivityID.toString(), this)

                    //return user to the initial view screen
                    var intent = Intent(this, ViewActivity::class.java)
                    intent.putExtra("activityIDIndex", returningActivityID)
                    startActivity(intent)
                }
            }
        }

        fun resetPassword()
        {
            //var attemptPasswordReset = false

            var passPasswordErrors = ""

            val etPopUp = EditText(this)
            etPopUp.hint = "Current Password"

            var alert = AlertDialog.Builder(this)
            alert.setTitle("Forgot Password? (Coming Soon)")
            alert.setMessage("Enter your current password below")

            alert.setView(etPopUp)

            alert.setPositiveButton("Send", DialogInterface.OnClickListener { dialog, whichButton ->

                //attemptPasswordReset = true

                //disable multi line for the edit texts
                //maybe check if new attempted password is the same as the old password?

                val PasswordManager = ManagePassword()
                //check current password
                //docode here to chek if current oasswoed is the same and then prompt to choose new password
                for (i in GlobalClass.listUserEmail.indices)
                {

                    if (GlobalClass.listUserEmail[i] == GlobalClass.user.email)
                    {
                        var currentUserSalt = GlobalClass.listUserPasswordSalt[i]

                        val attemptedUserPasswordHash = PasswordManager.generateHash(
                            etPopUp.text.toString(),
                            currentUserSalt
                        )

                        if (attemptedUserPasswordHash == GlobalClass.listUserPasswordHash[i])
                        {
                            //if password matches show new alert

                            var newPasswordAttempt = false



                                val etPopUpNew = EditText(this)

                                var alertReset = AlertDialog.Builder(this)


                                //maybe make the current password display as dots
                                etPopUpNew.hint = "New Password"
                                alertReset.setTitle("Reset Password")
                                alertReset.setMessage("Enter a new password")

                                alertReset.setView(etPopUpNew)

                                alertReset.setPositiveButton(
                                    "Confirm",
                                    DialogInterface.OnClickListener { dialog, whichButton ->

                                        val trySignUp = Temp_UserDataClass()
                                        var (validateUserPasswordBool, validateUserPasswordFeedback) = trySignUp.ValidateUserPassword(etPopUpNew.text.toString())

                                        //validate the new password

                                        if (validateUserPasswordBool)
                                         {

                                            //hash the new password
                                            val newPasswordHash = PasswordManager.generateHash(
                                                etPopUpNew.text.toString(),
                                                currentUserSalt
                                            )

                                            //set the new password hash back into the list (later DB)
                                            GlobalClass.listUserPasswordHash[i] = newPasswordHash

                                            GlobalClass.InformUser(
                                                "Success!",
                                                "Your new password has been saved.",
                                                this
                                            )

                                            newPasswordAttempt = true

                                        }
                                        else
                                        {

                                            passPasswordErrors = validateUserPasswordFeedback
                                            //dialog.dismiss()
                                            GlobalClass.InformUser("Invalid Password", validateUserPasswordFeedback, this)
                                            //dialog.cancel()



                                        }


                                    })

                                alertReset.setNegativeButton(
                                    "Cancel",
                                    DialogInterface.OnClickListener { dialog, whichButton -> newPasswordAttempt = true})

                            alertReset.setOnDismissListener{
                                if (!newPasswordAttempt)
                                {

                                    //while (!GlobalClass.alertInformUserShowing)
                                    //{

                                    val alertPasswordError = AlertDialog.Builder(this)


                                    alertPasswordError.setTitle("Invalid Password")
                                    alertPasswordError.setMessage(passPasswordErrors)
                                    alertPasswordError.setNegativeButton("OK", null)

                                   // alertPasswordError.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, whichButton ->
                                        //(it as AlertDialog).show()
                                       // dialog.dismiss()
                                        //dialog.cancel()
                                        //(it as AlertDialog).show()

                                   // })

                                    val parentDialogInterface = it

                                    alertPasswordError.setOnDismissListener{
                                        //(it as AlertDialog).show()
                                        Handler().postDelayed({ it.dismiss() }, 1000)
                                        (parentDialogInterface as AlertDialog).show()
                                    }

                                    alertPasswordError.show()


                                    //}


                                }
                            }

                            alertReset.show()

/*
                            while (!newPasswordAttempt) {

                                alertReset = AlertDialog.Builder(this)
                                alertReset.show()

                            }

 */


                            break

                        }
                        else
                        {
                            //show wrong current password
                            GlobalClass.InformUser("Incorrect Password","The password entered does not match the existing current password", this)

                            //somehow return to the top of method and try again? maybe loop or while? or repeat?

                            //here needs to be the loop thing

                            //break

                        }
                    }

                }

            })

            alert.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, whichButton -> })


            alert.show()


/*
            if (attemptPasswordReset == true)
            {

            }

 */

        }

        binding.tvResetPassword.setOnClickListener()
        {
            resetPassword()
        }

        binding.imgResetPasswordIndicator.setOnClickListener()
        {
            resetPassword()
        }

        binding.tvBackText.setOnClickListener()
        {

            if (previousScreen != null) {
                GoBack(previousScreen)
            }

        }

        binding.imgBackIndicator.setOnClickListener()
        {
            if (previousScreen != null) {
                GoBack(previousScreen)
            }
        }

        binding.tvViewLogsText.setOnClickListener()
        {
            var intent = Intent(this, Global_Logs::class.java)
            startActivity(intent)
        }

        binding.imgViewLogsIndicator.setOnClickListener()
        {
            var intent = Intent(this, Global_Logs::class.java)
            startActivity(intent)
        }


        binding.tvSignOutText.setOnClickListener()
        {
            SignOut()
        }
        binding.imgSignOutIndicator.setOnClickListener()
        {
            SignOut()
        }
    }
    override fun onBackPressed() {}
}