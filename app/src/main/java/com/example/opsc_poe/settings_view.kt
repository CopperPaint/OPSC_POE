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
        GlobalScope.launch {
           // if (GlobalClass.UpdateDataBase == true) {

                var DBManger = ManageDatabase()
                GlobalClass.allUsers = DBManger.getAllUsersFromFirestore()
                GlobalClass.categories = DBManger.getCategoriesFromFirestore(GlobalClass.user.userID)
                GlobalClass.activities = DBManger.getActivitesFromFirestore(GlobalClass.user.userID)
                GlobalClass.goals = DBManger.getGoalsFromFirestore(GlobalClass.user.userID)
                GlobalClass.logs = DBManger.getLogsFromFirestore(GlobalClass.user.userID)
                GlobalClass.UpdateDataBase = false
           // }
            //withContext(Dispatchers.Main) {
            //    UpdateUI()
            //}
        }

        var userIDIndex = 0

        //find the user ID Index
        for (i in GlobalClass.allUsers.indices)
        {
            if (GlobalClass.allUsers[i].userID == GlobalClass.user.userID)
            {
                userIDIndex = i
                break
            }
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

        fun resetPassword() {



            val etPopUp = EditText(this)
            etPopUp.hint = "Current Password"

            var alert = AlertDialog.Builder(this)
            alert.setTitle("Forgot Password? (Coming Soon)")
            alert.setMessage("Enter your current password below")

            alert.setView(etPopUp)

            alert.setPositiveButton("Send", DialogInterface.OnClickListener { dialog, whichButton ->

                val PasswordManager = ManagePassword(this)

                var currentUserSalt = GlobalClass.user.passwordSalt

                val attemptedUserPasswordHash = PasswordManager.generateHash(
                    etPopUp.text.toString(),
                    currentUserSalt
                )

                //if the correct password is entered
                if (attemptedUserPasswordHash == GlobalClass.user.passwordHash) {

                    val etPopUpNew = EditText(this)

                    var alertReset = AlertDialog.Builder(this)



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

                            if (validateUserPasswordBool) {

                                //if the new password is valid
                                //hash the new password

                                val newPasswordHash = PasswordManager.generateHash(
                                    etPopUpNew.text.toString(),
                                    currentUserSalt
                                )

                                var updatedUser = Temp_UserDataClass(GlobalClass.user.userID, GlobalClass.user.email, GlobalClass.user.username, newPasswordHash, GlobalClass.user.passwordSalt)

                                //update the current user in the db and global class
                                //GlobalClass.user.passwordHash = newPasswordHash






                                //Read Data
                                GlobalScope.launch {
                                    //if (GlobalClass.UpdateDataBase == true) {



                                        //Update Data
                                        var documentID =
                                            GlobalClass.documents.allUserIDs[userIDIndex]
                                        var DBmanager = ManageDatabase()
                                        DBmanager.updateUserInFirestore(
                                            updatedUser,
                                            documentID
                                        )

                                        var DBManger = ManageDatabase()
                                        GlobalClass.allUsers = DBManger.getAllUsersFromFirestore()
                                        GlobalClass.categories =
                                            DBManger.getCategoriesFromFirestore(GlobalClass.user.userID)
                                        GlobalClass.activities =
                                            DBManger.getActivitesFromFirestore(GlobalClass.user.userID)
                                        GlobalClass.goals =
                                            DBManger.getGoalsFromFirestore(GlobalClass.user.userID)
                                        GlobalClass.logs =
                                            DBManger.getLogsFromFirestore(GlobalClass.user.userID)
                                        GlobalClass.UpdateDataBase = false
                                    //}
                                    //withContext(Dispatchers.Main) {
                                    //    UpdateUI()
                                    //}
                                }


                                GlobalClass.InformUser(
                                    "Password Updated",
                                    "Your new password has been set",
                                    this
                                )


                            } else {
                                //if the new password is invalid
                                GlobalClass.InformUser(
                                    "Invalid Password",
                                    validateUserPasswordFeedback,
                                    this
                                )

                            }

                        })
                    alertReset.show()

                }
                else
                {
                    //if entered password does not match current
                    GlobalClass.InformUser(
                        "Invalid Password",
                        "The password you entered does not match your current password",
                        this
                    )
                }


            })

            alert.show()
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