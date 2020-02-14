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
    var reminders = mutableListOf<RoutineModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<RoutineModel> {
        return reminders
    }

    override fun create(reminder: RoutineModel) {
        reminder.id = generateRandomId()
        reminders.add(reminder)
        serialize()
    }

    override fun update(reminder: RoutineModel) {
        val remindersList = findAll() as ArrayList<RoutineModel>
        var foundReminder: RoutineModel? = remindersList.find { p -> p.id == reminder.id }
        if (foundReminder != null) {
            foundReminder.title = reminder.title
            foundReminder.description = reminder.description
            foundReminder.image = reminder.image
            foundReminder.lat = reminder.lat
            foundReminder.lng = reminder.lng
            foundReminder.zoom = reminder.zoom
        }
        serialize()
    }

    override fun delete(placemark: RoutineModel) {
        reminders.remove(placemark)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(reminders, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        reminders = Gson().fromJson(jsonString, listType)
    }

}