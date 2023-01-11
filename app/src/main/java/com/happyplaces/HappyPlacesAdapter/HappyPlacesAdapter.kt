package com.happyplaces.HappyPlacesAdapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.happyplaces.R
import com.happyplaces.activities.AddHappyPlaceActivity
import com.happyplaces.activities.MainActivity
import com.happyplaces.databases.DataBaseHandler
import com.happyplaces.models.HappyPlaceModel
import kotlinx.android.synthetic.main.itemhappy_places.view.*

open class HappyPlacesAdapter(
    private val context: Context,
    private var list: ArrayList<HappyPlaceModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClicklistner:onClickListner?=null

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.itemhappy_places,
                parent,
                false
            )
        )
    }
    fun setOnclickLitner(onClickListner: onClickListner){
        this.onClicklistner=onClickListner
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.iv_place_image.setImageURI(Uri.parse(model.image))
            holder.itemView.tvTitle.text = model.title
            holder.itemView.tvDescription.text = model.description
            holder.itemView.setOnClickListener{
                if(onClicklistner!=null){
                    onClicklistner!!.onClick(position,model)
                }
            }
        }
    }
    fun removeAt(postion:Int){
        val dbHandler=DataBaseHandler(context)
        val isDelete=dbHandler.deleteHappyPlaces(list[postion])
        if(isDelete>0){
            list.removeAt(postion)
            notifyItemRemoved(postion)
        }
    }
    fun notifyEditItem(activity:Activity,position: Int,requestCode:Int){
        var intent=Intent(context,AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS,list[position])
        activity.startActivityForResult(intent,requestCode)
        notifyItemChanged(position)

    }


    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }
    interface onClickListner{
        fun onClick(position: Int,model: HappyPlaceModel)
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}