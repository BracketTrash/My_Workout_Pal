package ie.wit.myworkoutpal.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ie.wit.myworkoutpal.R
import ie.wit.myworkoutpal.main.MainApp
import kotlinx.android.synthetic.main.activity_routine.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import ie.wit.myworkoutpal.models.RoutineModel
import kotlinx.android.synthetic.main.activity_routine.description


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

        if (intent.hasExtra("routine_edit")) {
            edit = true
            routine = intent.extras.getParcelable<RoutineModel>("routine_edit")
            routineTitle.setText(routine.title)
            description.setText(routine.description)
            sets.setText(routine.sets)
        }

        btnAdd.setOnClickListener {
            routine.title = routineTitle.text.toString()
            routine.description = description.text.toString()
            routine.sets = sets.text.toString()
            if (routine.title.isEmpty()) {
                toast(R.string.enter_routine_title)
            } else {
                if (edit) {
                    app.routines.update(routine.copy())
                } else {
                    app.routines.create(routine.copy())
                }
            }
            info("add Button Pressed: $routineTitle")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }
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

}


