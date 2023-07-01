package com.example.opsc_poe

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.opsc_poe.GlobalClass.Companion.ReturnToHome
import com.example.opsc_poe.databinding.ActivityCreateBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateActivity : AppCompatActivity()
{
    private lateinit var imageView: ImageView
    private var tempImage : Bitmap? = null

    companion object
    {
        private const val CAMERA_PERMISSION_CODE = 100
        private const val CAMERA_REQUEST_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Hide the action bar
        supportActionBar?.hide()

        //set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.Dark_Green)

        var activityIDIndex = intent.getIntExtra("activityIDIndex", -1)

        binding.tvScreenFunction.text = "Edit"

        //Spinner
        //----------------------------------------------------------------------------------
        val items = arrayListOf<String>()
        val indexes = arrayListOf<Int>()
        //get categories
        for (i in GlobalClass.categories.indices)
        {
            //if category belongs to user
            if (GlobalClass.categories[i].userID == GlobalClass.user.userID)
            {
                items.add(GlobalClass.categories[i].name)
                indexes.add(i)
            }
        }
        //set spinner
        val spinner = findViewById<Spinner>(R.id.spCategory)
        if (spinner != null)
        {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, items)
            spinner.adapter = adapter
        }
        //if spinner has items
        if (items.size <= 0)
        {
            GlobalClass.InformUser("No Categories Available", "Add a category from the home page", this)
        }


        imageView = findViewById(R.id.imgCamera)
        val CameraImage: Button = findViewById(R.id.btnInsertImage)

        //camera
        CameraImage.setOnClickListener {
            try
            {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startCamera()
                } else
                {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
                }
            }
            catch (e: Error)
            {
                GlobalClass.InformUser("Error", e.toString(), this)
                //return user to the home screen
                var intent = Intent(this, MainActivity::class.java) //ViewActivity
                startActivity(intent)
            }
        }

        if (activityIDIndex == -1) //activity does not exist
        {
            try
            {
                binding.tvScreenFunction.text = "Create"
                binding.btnClick.setOnClickListener()
                {
                    if (binding.etActivtyName.text.isNotEmpty())
                    {
                        if (binding.etDescription.text.isNotEmpty())
                        {
                            if (items.size <= 0)
                            {
                                GlobalClass.InformUser("Category Needed", "Add a category from the home page", this)
                            }
                            else
                            {
                                //code to get selected category
                                var selectedItem = spinner.selectedItemPosition
                                var category = GlobalClass.categories[indexes[selectedItem]]
                                //create activity object
                                var activity = Temp_ActivityDataClass(
                                    activityID = GlobalClass.activities.size + 1,
                                    userID = GlobalClass.user.userID,
                                    categoryID = category.categoryID, //get current category ID
                                    name =  binding.etActivtyName.text.toString(),
                                    description = binding.etDescription.text.toString(),
                                    maxgoalID = GlobalClass.goals.size + 1,
                                    mingoalID = GlobalClass.goals.size + 2,
                                    photo = tempImage
                                )
                                //save activity
                                //GlobalClass.activities.add(activity)


                                //create max activity goal
                                var maxgoal = Temp_GoalDataClass(
                                    goalID = GlobalClass.goals.size + 1,
                                    userID = GlobalClass.user.userID,
                                )
                                var mingoal = Temp_GoalDataClass(
                                    goalID = GlobalClass.goals.size + 2,
                                    userID = GlobalClass.user.userID,
                                )
                                //GlobalClass.goals.add(maxgoal)
                                //GlobalClass.goals.add(mingoal)

                                GlobalScope.launch{
                                    var DBmanager = ManageDatabase()
                                    DBmanager.AddActivityToFirestore(activity)
                                    //GlobalClass.goals.add(maxgoal)
                                    DBmanager.AddGoalToFirestore(maxgoal)
                                    //GlobalClass.goals.add(mingoal)
                                    DBmanager.AddGoalToFirestore(mingoal)

                                    //READ DATA
                                    GlobalClass.categories = DBmanager.getCategoriesFromFirestore(GlobalClass.user.userID)
                                    GlobalClass.activities = DBmanager.getActivitesFromFirestore(GlobalClass.user.userID)
                                    GlobalClass.goals = DBmanager.getGoalsFromFirestore(GlobalClass.user.userID)
                                    GlobalClass.logs = DBmanager.getLogsFromFirestore(GlobalClass.user.userID)
                                    GlobalClass.UpdateDataBase = false

                                    withContext(Dispatchers.Main) {
                                        ReturnToHome()
                                    }
                                }
                            }
                        }
                        else
                        {
                            GlobalClass.InformUser("All input fields required", "Please enter an activity description", this)
                        }
                    }
                    else
                    {
                        GlobalClass.InformUser("All input fields required", "Please enter an activity name", this)
                    }
                }
            }
            catch (e: Error)
            {
                GlobalClass.InformUser("Error", e.toString(), this)
                //return user to the sign in screen
                var intent = Intent(this, MainActivity::class.java) //ViewActivity
                startActivity(intent)
            }
        }
        else
        {
            try
            {
                var activity = GlobalClass.activities[activityIDIndex]
                binding.etActivtyName.setText(activity.name)
                binding.etDescription.setText(activity.description)


                //get activity category
                var catIndex = Temp_CategoryDataClass().GetIndex(activity.categoryID, GlobalClass.categories)

                //set spinner
                var spinIndex = 0;
                for (i in indexes.indices)
                {
                    if (indexes[i] == catIndex)
                    {
                        spinIndex = i
                    }
                }
                spinner.setSelection(spinIndex)

                //set image
                binding.imgCamera.setImageBitmap(activity.photo)

                binding.btnClick.setOnClickListener()
                {
                    GlobalClass.activities[activityIDIndex].name = binding.etActivtyName.text.toString()
                    GlobalClass.activities[activityIDIndex].description = binding.etDescription.text.toString()

                    var selectedItem = spinner.selectedItemPosition
                    var category = GlobalClass.categories[indexes[selectedItem]]
                    GlobalClass.activities[activityIDIndex].categoryID = category.categoryID
                    GlobalClass.activities[activityIDIndex].photo = tempImage

                    ReturnToHome()
                }
            }
            catch (e: Error)
            {
                GlobalClass.InformUser("Error", e.toString(), this)
                //return user to the sign in screen
                var intent = Intent(this, MainActivity::class.java) //ViewActivity
                startActivity(intent)
            }
        }

        //need help
        binding.tvNeedHelp.setOnClickListener(){
            try
            {
                var intent = Intent(this, Help::class.java)
                intent.putExtra("previousScreen", "Create_Activity")
                intent.putExtra("activityIDIndex", activityIDIndex)
                startActivity(intent)
            }
            catch (e: Error)
            {
                GlobalClass.InformUser("Error", e.toString(), this)
                //return user to the sign in screen
                var intent = Intent(this, MainActivity::class.java) //ViewActivity
                startActivity(intent)
            }
        }

        //back button
        binding.imgBlackTurtle.setOnClickListener()
        {
            try
            {
                ReturnToHome(this)
            }
            catch (e: Error)
            {
                GlobalClass.InformUser("Error", e.toString(), this)
                //return user to the sign in screen
                var intent = Intent(this, MainActivity::class.java) //ViewActivity
                startActivity(intent)
            }
        }
        }


    private fun ReturnToHome()
    {
        var intent = Intent(this, Home_Activity::class.java) //ViewActivity
        startActivity(intent)
    }


    private fun startCamera()
    {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(this, "Camera is not available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            //Save the image locally
            saveImageLocally(imageBitmap)
        }
    }
    //save image locally
    private fun saveImageLocally(imageBitmap: Bitmap) {
        tempImage = imageBitmap
    }
    override fun onBackPressed() {}

}