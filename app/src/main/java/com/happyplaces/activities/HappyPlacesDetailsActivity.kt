package com.happyplaces.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.happyplaces.R
import com.happyplaces.models.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_add_happy_place.*
import kotlinx.android.synthetic.main.activity_add_happy_place.iv_place_image
import kotlinx.android.synthetic.main.activity_happy_places_details.*
import pl.kitek.rvswipetodelete.SwipeToEditCallback

class HappyPlacesDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_happy_places_details)
        var happyPlaceDetailMode:HappyPlaceModel?=null
        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){

            happyPlaceDetailMode=intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModel
        }
        iv_place_image.setImageURI(Uri.parse(happyPlaceDetailMode!!.image))
        tv_description.text=happyPlaceDetailMode.description
         tv_location.text=happyPlaceDetailMode.location
        if(happyPlaceDetailMode!=null) {
            setSupportActionBar(toolbar_happy_place_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = happyPlaceDetailMode.title
            toolbar_happy_place_detail.setNavigationOnClickListener {
                onBackPressed()
            }

            //
            // tv_description.text=happyPlaceDetailMode.description
            // tv_location.text=happyPlaceDetailMode.location

        }

    }
}