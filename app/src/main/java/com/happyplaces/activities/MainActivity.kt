package com.happyplaces.activities

import SwipeToDeleteCallback
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.happyplaces.HappyPlacesAdapter.HappyPlacesAdapter
import com.happyplaces.R
import com.happyplaces.databases.DataBaseHandler
import com.happyplaces.models.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_main.*
import pl.kitek.rvswipetodelete.SwipeToEditCallback

class MainActivity : AppCompatActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)

        // This is used to align the xml view to this class
        setContentView(R.layout.activity_main)

        // Setting an click event for Fab Button and calling the AddHappyPlaceActivity.
        fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this@MainActivity, AddHappyPlaceActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        getHappyPlaceListFromLocalDb()


    }
    private fun setUpRecyleView(happyPlacesList:ArrayList<HappyPlaceModel>){
        rv_places_list.layoutManager=LinearLayoutManager(this)
        val placeAdapter=HappyPlacesAdapter(this,happyPlacesList)
        rv_places_list.adapter=placeAdapter
        placeAdapter.setOnclickLitner(object :HappyPlacesAdapter.onClickListner{
            override fun onClick(position: Int, model: HappyPlaceModel) {
                var intent=Intent(this@MainActivity,HappyPlacesDetailsActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS,model)

                startActivity(intent)

            }

        })
        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // TODO (Step 5: Call the adapter function when it is swiped)
                // START
                val adapter = rv_places_list.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(this@MainActivity,viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE)
            }
        }
        var editItemTouchHelper=ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_places_list)
        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // TODO (Step 5: Call the adapter function when it is swiped)
                // START
                val adapter = rv_places_list.adapter as HappyPlacesAdapter

                adapter.removeAt(viewHolder.adapterPosition)
                getHappyPlaceListFromLocalDb()
            }
        }
        var deleteItemTouchHelper=ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_places_list)




    }

    private fun getHappyPlaceListFromLocalDb(){
        val dbHandler=DataBaseHandler(this)
        val getHappyPlcaeList : ArrayList<HappyPlaceModel>
        getHappyPlcaeList=dbHandler.getHappyPlacesList()
        if(getHappyPlcaeList.size>0){
            rv_places_list.visibility=View.VISIBLE
            tv_no_records_avaliable.visibility=View.GONE
            setUpRecyleView(getHappyPlcaeList)
        }else{
            rv_places_list.visibility=View.GONE
            tv_no_records_avaliable.visibility=View.VISIBLE

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== ADD_PLACE_ACTIVITY_REQUEST_CODE){
            if(resultCode==Activity.RESULT_OK){
                getHappyPlaceListFromLocalDb()
            }
        }
    }
    companion object{
        var ADD_PLACE_ACTIVITY_REQUEST_CODE=1
        var EXTRA_PLACE_DETAILS="extra_place_details"
    }
}