package com.example.opsc_poe

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Temp_UserDataClass
    (

    var userID: Int = 0,
    var email: String = "",
    var username: String = "",
    var passwordHash: String = "",
    var passwordSalt: String = ""
    //var password: String = ""
)

    {
        /*
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
                    passwordHash = GlobalClass.listUserPasswordHash[i]
                    passwordSalt = GlobalClass.listUserPasswordSalt[i]


                    //assign the user data to the global class to share its information
                    GlobalClass.user.userID = userID
                    GlobalClass.user.email = email
                    GlobalClass.user.username = username
                    GlobalClass.user.passwordHash = passwordHash
                    GlobalClass.user.passwordSalt = passwordSalt




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



         */


        fun ValidateUser(userEmail: String, userPassword: String, context: Context): Boolean
        {



            val PasswordManager = ManagePassword()

            //loop through users
            for (i in GlobalClass.allUsers.indices) {

                //if the entered email matches an existing email
                if (userEmail == GlobalClass.allUsers[i].email) {

                    //if user exists

                    val attemptedUserPasswordHash = PasswordManager.generateHash(
                        userPassword,
                        GlobalClass.allUsers[i].passwordSalt
                    )

                    if (attemptedUserPasswordHash == GlobalClass.allUsers[i].passwordHash) {
                        //if the user password is correct
                        userID = GlobalClass.allUsers[i].userID//GlobalClass.listUserUserID[i]
                        email = userEmail
                        username = GlobalClass.allUsers[i].username//GlobalClass.listUserUsername[i]
                        passwordHash = GlobalClass.allUsers[i].passwordHash//GlobalClass.listUserPasswordHash[i]
                        passwordSalt = GlobalClass.allUsers[i].passwordSalt//GlobalClass.listUserPasswordSalt[i]


                        //assign the user data to the global class to share its information
                        GlobalClass.user.userID = userID
                        GlobalClass.user.email = email
                        GlobalClass.user.username = username
                        GlobalClass.user.passwordHash = passwordHash
                        GlobalClass.user.passwordSalt = passwordSalt




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
            for(i in GlobalClass.allUsers.indices)
            {
                //check to see if there is already a user with the given information
                if (userEmail == GlobalClass.allUsers[i].email || userUsername == GlobalClass.allUsers[i].username)
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

                val currentLastUserIDIndex = GlobalClass.allUsers.last().userID//GlobalClass.listUserUserID.last()
                var newUserUserIDIndex = currentLastUserIDIndex + 1

                //add the new user to the user data lists
                /*GlobalClass.listUserUserID.add(newUserUserIDIndex)
                GlobalClass.listUserEmail.add(userEmail)
                GlobalClass.listUserUsername.add(userUsername)
                GlobalClass.listUserPasswordHash.add(newUserPasswordHash)
                GlobalClass.listUserPasswordSalt.add(newUserPasswordSalt)

                 */
                var newUser = Temp_UserDataClass(newUserUserIDIndex, userEmail, userUsername, newUserPasswordHash, newUserPasswordSalt)

                //GlobalClass.allUsers.add(newUser)

                //save to global class
                //GlobalClass.categories.add(category)

                GlobalScope.launch(){
                    var DBmanager = ManageDatabase()
                    //add to database
                    DBmanager.AddUserToFirestore(newUser)

                    //READ DATA
                    GlobalClass.allUsers = DBmanager.getAllUsersFromFirestore()
                    GlobalClass.categories = DBmanager.getCategoriesFromFirestore(GlobalClass.user.userID)
                    GlobalClass.activities = DBmanager.getActivitesFromFirestore(GlobalClass.user.userID)
                    GlobalClass.goals = DBmanager.getGoalsFromFirestore(GlobalClass.user.userID)
                    GlobalClass.logs = DBmanager.getLogsFromFirestore(GlobalClass.user.userID)
                    GlobalClass.UpdateDataBase = false
                }

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