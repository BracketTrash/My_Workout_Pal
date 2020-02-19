package ie.wit.myworkoutpal.main

import android.app.Application
import ie.wit.myworkoutpal.models.RoutineJSONStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import ie.wit.myworkoutpal.models.RoutineStore

class MainApp : Application(), AnkoLogger {

    lateinit var routines: RoutineStore

    override fun onCreate() {
        super.onCreate()
        routines = RoutineJSONStore(applicationContext)
        info("MyWorkourPal started")
    }
}