package com.example.restauapp.DB

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConvertor {
    @TypeConverter
    fun FromAnyToString(atr:Any?):String{
        if (atr == null){
            return ""
        }
        return  atr as String
    }
    @TypeConverter
    fun FromStringToAny(atr:String?):Any{
        if (atr == null){
            return ""
        }
        return  atr
    }

}