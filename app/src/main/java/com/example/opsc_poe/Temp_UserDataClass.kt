package com.example.opsc_poe

import android.content.Context
import android.text.TextUtils
import android.util.Patterns


class Temp_UserDataClass{

    var userID: Int = 0
    var email: String = ""
    var username: String = ""
    //var password: String = ""

    fun ValidateUser(userEmail: String, userPassword: String, context: Context): Boolean
    {
        val PasswordManager = ManagePassword()

        //loop through users
        for (i in GlobalClass.listUserEmail.indices) {
            //if the entered email matches an existing email
            if (userEmail == GlobalClass.listUserEmail[i]) {

                //if user exists

                val attemptedUserPasswordHash = PasswordManager.generateHash(
                    userPassword,
                    GlobalClass.listUserPasswordSalt[i]
                )

                if (attemptedUserPasswordHash == GlobalClass.listUserPasswordHash[i]) {
                    //if the user password is correct
                    userID = GlobalClass.listUserUserID[i]
                    email = userEmail
                    username = GlobalClass.listUserUsername[i]



                    //assign the user data to the global class to share its information
                    GlobalClass.user.userID = userID
                    GlobalClass.user.email = email
                    GlobalClass.user.username = username




                   // tester = "hi"
                   // GlobalClass().InformUser("Unable to Sign In",tester , context)


                    //issue is with the global class not saving the user information to its own object


                    //exit loop
                    break
                }


            }
        }

        if (userID == 0)
        {

            //user doesn't exist code goes here
            GlobalClass.InformUser("Unable to Sign In", "Cannot find user with the given data", context)

            //return the user exists boolean as false
            return false

        }
        else{

            //return the user exists boolean as true
            return true

        }



    }



    fun ValidateUserEmail(attemptedEmail: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(attemptedEmail)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(attemptedEmail).matches()
        }
    }



        fun ValidateUserPassword(attemptedPassword : String): Pair<Boolean, String>
        {

            var validationErrors = ArrayList<String>()




                 if (attemptedPassword.length < 8)
                 {
                    validationErrors.add("Password too short, needs to be 8 or more characters")
                 }

                 if (attemptedPassword.count(Char::isDigit) == 0)
                 {
                     validationErrors.add("Password needs to contain at least 1 digit")
                 }

                if (attemptedPassword.any(Char::isLowerCase))
                {

                }
                else
                {
                    validationErrors.add("Password needs to contain at least 1 lower case character")
                }

                if (attemptedPassword.any(Char::isUpperCase))
                {

                }
            else
                {
                    validationErrors.add("Password needs to contain at least 1 upper case character")
                }


                if (attemptedPassword[0].isUpperCase())
                    {

                    }
            else
                {
                    validationErrors.add("Password needs start with an upper case character")
                }


                if (attemptedPassword.any { it in "-?!@#£$%&+=" })
                {

                }
            else
                {
                    validationErrors.add("Password needs to include one of the following special characters: -?!@#£$%&+=")
                }


            if (validationErrors.isEmpty())
            {
                return Pair(true, "")//true
            }
            else
            {
                var passwordErrors = ""
                for (i in validationErrors) {
                    passwordErrors+= "$i\n"
                }

                //GlobalClass.InformUser("Invalid Password", passwordErrors, context)
                return Pair(false, passwordErrors)//false
            }


        }

        fun RegisterUser(userEmail: String, userUsername : String, userPassword: String, context: Context)
        {
            val PasswordManager = ManagePassword()
            var userExists = false


            //loop through users
            for(i in GlobalClass.listUserEmail.indices)
            {
                //check to see if there is already a user with the given information
                if (userEmail == GlobalClass.listUserEmail[i] || userUsername == GlobalClass.listUserUsername[i])
                {
                    //if the user already exists

                    //set the user exists variable to true
                    userExists = true

                    //infrom user that the entered information is already registered to another user
                    GlobalClass.InformUser("Unable to Sign Up", "A user with the provided data already exists", context)

                    //exit loop
                    break

                }


            }

            //check if the user matching the given data exists or conflicts
            if (userExists == false) {
                //if the user doesn't conflict with existing data then register the user

                //create new user password salt
                val newUserPasswordSalt = PasswordManager.generateRandomSalt()

                //create new user password hash
                val newUserPasswordHash = PasswordManager.generateHash(
                    userPassword,
                    newUserPasswordSalt
                )

                val currentLastUserIDIndex = GlobalClass.listUserUserID.last()
                var newUserUserIDIndex = currentLastUserIDIndex + 1

                //add the new user to the user data lists
                GlobalClass.listUserUserID.add(newUserUserIDIndex)
                GlobalClass.listUserEmail.add(userEmail)
                GlobalClass.listUserUsername.add(userUsername)
                GlobalClass.listUserPasswordHash.add(newUserPasswordHash)
                GlobalClass.listUserPasswordSalt.add(newUserPasswordSalt)

                //inform the user that their account was successfully created
                GlobalClass.InformUser("Account Created", "Your account has been registered, Please Log In", context)

                //temp method used to get the new user password hash and salt text values to hard code into application
                /*
                val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
                clipboard?.setPrimaryClip(ClipData.newPlainText("", newUserPasswordHash + "-" + newUserPasswordSalt))

                 */

            }


        }





}