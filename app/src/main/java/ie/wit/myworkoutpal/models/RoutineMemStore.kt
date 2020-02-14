package ie.wit.myworkoutpal.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class RoutineMemStore : RoutineStore, AnkoLogger{

    val routines = ArrayList<RoutineModel>()

    override fun findAll(): List<RoutineModel>{
        return routines
    }

    override fun create(routine: RoutineModel) {
        routines.add(routine)
        logAll()
    }

    override fun update(routine: RoutineModel) {
        var foundRoutine: RoutineModel? = routines.find { p -> p.id == routine.id }
        if (foundRoutine != null) {
            foundRoutine.title = routine.title
            foundRoutine.description = routine.description
            foundRoutine.date = routine.date
            foundRoutine.image = routine.image
            foundRoutine.lat = routine.lat
            foundRoutine.lng = routine.lng
            foundRoutine.zoom = routine.zoom
            logAll()
        }
    }

    override fun delete(routine: RoutineModel) {
        routines.remove(routine)
    }

    fun logAll(){
        routines.forEach{ info("${it}")}
    }
}
