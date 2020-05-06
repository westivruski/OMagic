package com.example.omagic



import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView


class EventRecyclerAdapter(private val listEvent: List<Event>) : RecyclerView.Adapter<EventRecyclerAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        // inflating recycler item view
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_recycler, parent, false)

        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event: ByteArray? = listEvent[position].KEY_IMAGE

        val bitmap  = BitmapFactory.decodeByteArray(event, 0, event!!.size)

        holder.textViewName.text = listEvent[position].name
        holder.textViewEmail.text = listEvent[position].description
        holder.image.setImageBitmap(bitmap);

        holder.id_location.text = listEvent[position].location
        //holder.desc.text = listEvent[position].description


    }

    override fun getItemCount(): Int {
        return listEvent.size
    }





    /**
     * ViewHolder class
     */
    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {



        var textViewName: AppCompatTextView = view.findViewById(R.id.textViewName)
        var id_location: AppCompatTextView = view.findViewById(R.id.textViewPassword)
        var textViewEmail: AppCompatTextView = view.findViewById(R.id.textViewEmail)
        var image: AppCompatImageView = view.findViewById(R.id.imageView)
        //var noob = imageViewToByte(image)
    }


}