package ie.wit.myworkoutpal.activities

import android.os.Bundle
import android.app.DatePickerDialog
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ie.wit.myworkoutpal.R
import ie.wit.myworkoutpal.main.MainApp
import kotlinx.android.synthetic.main.activity_routine.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import ie.wit.myworkoutpal.helpers.readImage
import ie.wit.myworkoutpal.helpers.readImageFromPath
import ie.wit.myworkoutpal.helpers.showImagePicker
import ie.wit.myworkoutpal.models.Location
import ie.wit.myworkoutpal.models.RoutineModel
import kotlinx.android.synthetic.main.activity_routine.description
import java.text.SimpleDateFormat
import java.util.*


class RoutineActivity : AppCompatActivity(), AnkoLogger {

    var routine = RoutineModel()
    var edit = false
    val IMAGE_REQUEST = 1
    lateinit var app: MainApp
    val LOCATION_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)
        app = application as MainApp

        if(intent.hasExtra("routine_edit")){
            edit = true
            routine = intent.extras.getParcelable<RoutineModel>("routine_edit")
            routineTitle.setText(routine.title)
            description.setText(routine.description)
            //Saves Date
            showDate.setText(routine.date)
            btnAdd.setText(R.string.save_routine)
            routineImage.setImageBitmap(readImageFromPath(this, routine.image))
            if(routine.image != null){
                chooseImage.setText(R.string.change_routine_image)
            }
        }

        btnAdd.setOnClickListener {
            routine.title = routineTitle.text.toString()
            routine.description = description.text.toString()
            routine.date = showDate.text.toString()
            if (routine.title.isEmpty()) {
                toast(R.string.enter_routine_title)
            }else{
                if(edit){
                    app.routines.update(routine.copy())
                }else{
                    app.routines.create(routine.copy())
                }
            }
            info("add Button Pressed: $routineTitle")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }

        //Date Picker  (https://stackoverflow.com/questions/45842167/how-to-use-datepickerdialog-in-kotlin#45844018)
        var cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val format = "dd.MM.yyy"
            val sdf = SimpleDateFormat(format, Locale.UK)
            showDate.text = sdf.format(cal.time)
        }

        //Show Date Picker
        pickDate.setOnClickListener{
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        routineLocation.setOnClickListener{
            val location = Location(52.245696, -7.139102, 15f)
            if(routine.zoom != 0f){
                location.lat = routine.lat
                location.lng = location.lng
                location.zoom  = location.zoom
            }
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }

        toolbarAdd.title = title
//        setSupportActionBar(toolbarAdd)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.menu_routine, menu)
        if (edit && menu != null) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                app.routines.delete(routine)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    routine.image = data.getData().toString()
                    routineImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseImage.setText(R.string.change_routine_image)
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras.getParcelable<Location>("location")
                    routine.lat = location.lat
                    routine.lng = location.lng
                    routine.zoom = location.zoom
                }
            }
        }
    }
}


