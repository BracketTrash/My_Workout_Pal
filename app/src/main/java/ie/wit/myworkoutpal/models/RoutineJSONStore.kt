package ie.wit.myworkoutpal.models

import android.content.Context
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.jetbrains.anko.AnkoLogger
import ie.wit.myworkoutpal.helpers.*
import java.util.*


val JSON_FILE = "reminder.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<RoutineModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class RoutineJSONStore : RoutineStore, AnkoLogger{

    val context: Context
    var routines = mutableListOf<RoutineModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<RoutineModel> {
        return routines
    }

    override fun create(routine: RoutineModel) {
        routine.id = generateRandomId()
        routines.add(routine)
        serialize()
    }

    override fun update(routine: RoutineModel) {
        val routineList = findAll() as ArrayList<RoutineModel>
        var foundRoutine: RoutineModel? = routineList.find { p -> p.id == routine.id }
        if (foundRoutine != null) {
            foundRoutine.title = routine.title
            foundRoutine.description = routine.description
            foundRoutine.sets = routine.sets
        }
        serialize()
    }

    override fun delete(placemark: RoutineModel) {
        routines.remove(placemark)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(routines, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        routines = Gson().fromJson(jsonString, listType)
    }

}