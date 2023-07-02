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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class settings_view : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Hide the action bar
        supportActionBar?.hide()

        //set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.Dark_Green)

        //Set view binding
        val binding = ActivitySettingsViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Read Data
        GlobalScope.launch{
            if (GlobalClass.UpdateDataBase == true)
            {
                var DBManger = ManageDatabase()
                GlobalClass.allUsers = DBManger.getAllUsersFromFirestore()
                GlobalClass.categories = DBManger.getCategoriesFromFirestore(GlobalClass.user.userID)
                GlobalClass.activities = DBManger.getActivitesFromFirestore(GlobalClass.user.userID)
                GlobalClass.goals = DBManger.getGoalsFromFirestore(GlobalClass.user.userID)
                GlobalClass.logs = DBManger.getLogsFromFirestore(GlobalClass.user.userID)
                GlobalClass.UpdateDataBase = false
            }
            //withContext(Dispatchers.Main) {
            //    UpdateUI()
            //}
        }

        //get the extra
        var previousScreen = intent.getStringExtra("previousScreen")

        fun SignOut() {
            //return user to the initial view screen
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fun GoBack(previousScreenVar: String) {
            when (previousScreenVar) {
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


        //look over this, the logic is weird with the lists and objects
        fun resetPassword() {
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

                val PasswordManager = ManagePassword(this)
                //check current password
                //for (i in GlobalClass.listUserUserID.indices ) {

                    //if (GlobalClass.listUserUserID[i] == GlobalClass.user.userID) {

                        var currentUserSalt = GlobalClass.user.passwordSalt

                        val attemptedUserPasswordHash = PasswordManager.generateHash(
                            etPopUp.text.toString(),
                            currentUserSalt
                        )

                        if (attemptedUserPasswordHash == GlobalClass.user.passwordHash) {
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
                                    var (validateUserPasswordBool, validateUserPasswordFeedback) = trySignUp.ValidateUserPassword(
                                        etPopUpNew.text.toString(),
                                        this
                                    )

                                    //validate the new password

                                    if (validateUserPasswordBool) {

                                        //hash the new password
                                        val newPasswordHash = PasswordManager.generateHash(
                                            etPopUpNew.text.toString(),
                                            currentUserSalt
                                        )

                                        //set the new password hash back into the list (later DB)
                                        GlobalClass.user.passwordHash = newPasswordHash

                                        GlobalClass.InformUser(
                                            "Success!",
                                            "Your new password has been saved.",
                                            this
                                        )

                                        newPasswordAttempt = true

                                    } else {

                                        passPasswordErrors = validateUserPasswordFeedback
                                        //dialog.dismiss()
                                        GlobalClass.InformUser(
                                            "Invalid Password",
                                            validateUserPasswordFeedback,
                                            this
                                        )
                                        //dialog.cancel()


                                    }


                                })

                            alertReset.setNegativeButton(
                                "Cancel",
                                DialogInterface.OnClickListener { dialog, whichButton ->
                                    newPasswordAttempt = true
                                })

                            alertReset.setOnDismissListener {
                                if (!newPasswordAttempt) {

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

                                    alertPasswordError.setOnDismissListener {
                                        (it as AlertDialog).show()
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


                            //break

                        } else {
                            //show wrong current password
                            GlobalClass.InformUser(
                                "Incorrect Password",
                                "The password entered does not match the existing current password",
                                this
                            )

                            //somehow return to the top of method and try again? maybe loop or while? or repeat?

                            //here needs to be the loop thing

                            //break

                        }
                    //}

                //}

            })

            alert.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialog, whichButton -> })


            alert.show()


/*
            if (attemptPasswordReset == true)
            {

            }

 */

        }

        fun exportUserData() {

            val userDataTypes = arrayOf<String>("Activity Data", "Log Data")
            val selectedItems = ArrayList<Int>() // Where we track the selected items
            val builder = AlertDialog.Builder(this)
            // Set the dialog title
            builder.setTitle("Select the type of data to export")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(userDataTypes, null,
                    DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(which)
                        } else if (selectedItems.contains(which)) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(which)
                        }
                    })
                // Set the action buttons
                .setPositiveButton("Confirm",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK, so save the selectedItems results somewhere
                        // or return them to the component that opened the dialog
                        //...

                        var exportData = ExportUserData(selectedItems, this)
                        exportData.evaluateOptions()
                        //GlobalClass.InformUser("Selected", exportData.selectedExportItems.toString(), this)

                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                       // ...
                    })


            builder.create()
            builder.show()
            //throw IllegalStateException("Activity cannot be null")



        }

        binding.tvExportDataText.setOnClickListener()
        {
            exportUserData()
        }

        binding.imgExportDataIndicator.setOnClickListener()
        {
            exportUserData()
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