package com.happyplaces.databases

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.happyplaces.models.HappyPlaceModel
import java.sql.SQLException

class DataBaseHandler(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object{
        private const val DATABASE_VERSION=1
        private const val DATABASE_NAME="HappyPlacesDb"
        private const val TABLE_HAPP_PLACE="HappyPlaceTable"

        private const val KEY_ID="_id"
        private const val KEY_TITLE="title"
        private const val KEY_IMAGE="image"
        private const val KEY_DESCRIPTION="description"
        private const val KEY_DATE="date"
        private const val KEY_LOCATION="location"
        private const val KEY_LATITUDE="latitiude"
        private const val KEY_LONGITUDE="longitude"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_HAPPY_PLACE_TABLE=("CREATE TABLE "+ TABLE_HAPP_PLACE+"("+ KEY_ID+" INTEGER PRIMARY KEY,"+
                KEY_TITLE+" TEXT,"+ KEY_IMAGE+" TEXT,"+ KEY_DESCRIPTION+" TEXT,"+ KEY_DATE+" TEXT,"+ KEY_LOCATION
                +" TEXT,"+ KEY_LATITUDE+" TEXT,"+ KEY_LONGITUDE+" TEXT)")
        db?.execSQL(CREATE_HAPPY_PLACE_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_HAPP_PLACE")
        onCreate(db)
    }
    fun addHappyPlace(happyPlaceModel: HappyPlaceModel):Long{
        val db=this.writableDatabase
        val contentValues=ContentValues()
        contentValues.put(KEY_TITLE,happyPlaceModel.title)
        contentValues.put(KEY_IMAGE,happyPlaceModel.image)
        contentValues.put(KEY_DESCRIPTION,happyPlaceModel.description)
        contentValues.put(KEY_DATE,happyPlaceModel.date)
        contentValues.put(KEY_LOCATION,happyPlaceModel.location)
        contentValues.put(KEY_LATITUDE,happyPlaceModel.latitiude)
        contentValues.put(KEY_LONGITUDE,happyPlaceModel.longitude)

        val result=db.insert(TABLE_HAPP_PLACE,null,contentValues)
        db.close()
        return result
    }
    fun deleteHappyPlaces(happyPlaceModel: HappyPlaceModel):Int{
        val db=this.writableDatabase
        val sucess=db.delete(TABLE_HAPP_PLACE, KEY_ID+"="+happyPlaceModel.id,null)
        db.close()
        return sucess
    }

    fun updateHappyPlace(happyPlaceModel: HappyPlaceModel):Int{
        val db=this.writableDatabase
        val contentValues=ContentValues()
        contentValues.put(KEY_TITLE,happyPlaceModel.title)
        contentValues.put(KEY_IMAGE,happyPlaceModel.image)
        contentValues.put(KEY_DESCRIPTION,happyPlaceModel.description)
        contentValues.put(KEY_DATE,happyPlaceModel.date)
        contentValues.put(KEY_LOCATION,happyPlaceModel.location)
        contentValues.put(KEY_LATITUDE,happyPlaceModel.latitiude)
        contentValues.put(KEY_LONGITUDE,happyPlaceModel.longitude)

        val result=db.update(TABLE_HAPP_PLACE,contentValues, KEY_ID+"="+happyPlaceModel.id,null)
        db.close()
        return result
    }
    fun getHappyPlacesList():ArrayList<HappyPlaceModel>{
        val happyplaceList=ArrayList<HappyPlaceModel>()
        val selectedquery="SELECT * FROM $TABLE_HAPP_PLACE"
        val db=this.readableDatabase
        try {
            val cursor:Cursor=db.rawQuery(selectedquery,null)
            if(cursor.moveToFirst()){
                do{
                    val place=HappyPlaceModel(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))

                    )
                    happyplaceList.add(place)

                }while (cursor.moveToNext())
            }
            cursor.close()

        }catch (e:SQLException){
            db.execSQL(selectedquery)
            return  happyplaceList
        }
        return happyplaceList

    }


}