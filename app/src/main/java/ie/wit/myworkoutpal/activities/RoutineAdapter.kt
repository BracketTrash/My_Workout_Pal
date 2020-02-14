package ie.wit.myworkoutpal.activities


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.myworkoutpal.R
import ie.wit.myworkoutpal.helpers.readImageFromPath
import ie.wit.myworkoutpal.models.RoutineModel
import kotlinx.android.synthetic.main.card_routine.view.*

interface RoutineListener {
    fun onRoutineClick(routine: RoutineModel)
}

class RoutineAdapter constructor(private var routines: List<RoutineModel>,
                                 private val listener: RoutineListener) : RecyclerView.Adapter<RoutineAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_routine, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val routine = routines[holder.adapterPosition]
        holder.bind(routine, listener)
    }

    override fun getItemCount(): Int = routines.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(routine: RoutineModel,  listener : RoutineListener) {
            itemView.routineTitle.text = routine.title
            itemView.description.text = routine.description
            itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, routine.image))
            itemView.setOnClickListener { listener.onRoutineClick(routine) }
        }
    }
}