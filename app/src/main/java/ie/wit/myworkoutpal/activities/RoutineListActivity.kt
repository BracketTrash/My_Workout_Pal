package ie.wit.myworkoutpal.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import ie.wit.myworkoutpal.R
import ie.wit.myworkoutpal.main.MainApp
import ie.wit.myworkoutpal.models.RoutineModel
import kotlinx.android.synthetic.main.activity_routine_list.*

class RoutineListActivity : AppCompatActivity(), RoutineListener {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_list)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        loadRoutines()

      //        setSupportActionBar(toolbarMain)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<RoutineActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRoutineClick(routine: RoutineModel) {
        startActivityForResult(intentFor<RoutineActivity>().putExtra("routine_edit", routine), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadRoutines()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadRoutines(){
        showRoutines(app.routines.findAll())
    }

    fun showRoutines(routines: List<RoutineModel>){
        recyclerView.adapter = RoutineAdapter(routines, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}


