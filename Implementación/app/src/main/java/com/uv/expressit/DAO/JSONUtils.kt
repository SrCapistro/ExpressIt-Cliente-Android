package com.uv.expressit.DAO

class JSONUtils {
    companion object{
        fun parsearJson(json: String): String{
            var json = json.replace("[", "")
            json = json.replace("]", "")
            return json
        }
    }

}