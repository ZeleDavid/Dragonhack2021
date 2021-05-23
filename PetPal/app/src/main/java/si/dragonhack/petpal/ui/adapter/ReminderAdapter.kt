package si.dragonhack.petpal.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_reminder.view.*
import kotlinx.android.synthetic.main.add_reminder.view.reminder_date
import kotlinx.android.synthetic.main.add_reminder.view.reminder_name
import kotlinx.android.synthetic.main.add_reminder.view.reminder_place
import kotlinx.android.synthetic.main.fact_item_layout.view.*
import kotlinx.android.synthetic.main.reminder_layout_row.view.*
import si.dragonhack.petpal.R
import si.dragonhack.petpal.data.models.Fact
import si.dragonhack.petpal.data.models.Reminder

class ReminderAdapter(list: List<Reminder>): RecyclerView.Adapter<RemindersViewHolder>(){
    val reminders = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindersViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.reminder_layout_row, parent, false)
        return RemindersViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return reminders.size
    }

    override fun onBindViewHolder(holder: RemindersViewHolder, position: Int) {
        holder.bind(reminders[position])
    }
}
class RemindersViewHolder(val view: View): RecyclerView.ViewHolder(view){
    fun bind(reminder: Reminder){
        view.reminder_name_row.text = reminder.name
        view.reminder_place_row.setText(reminder.place)
        view.reminder_date_row.setText(reminder.date)
    }
}