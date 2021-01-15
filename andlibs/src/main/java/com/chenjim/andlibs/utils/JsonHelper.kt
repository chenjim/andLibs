package com.chenjim.andlibs.utils

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*

/**
 * Created by jimi on 2018/3/27.
 */
class JsonHelper private constructor() {
    companion object {

        @JvmField
        val gson: Gson = GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .serializeNulls()
            //.registerTypeAdapter(MutableList::class.java, ListAdapter())
//                .registerTypeAdapter(String::class.java, StringAdapter())
//                .registerTypeAdapter(Int::class.java, IntegerAdapter())
            .create()

        /**
         * @param json
         * @param type Type type = new TypeToken<List<AccountInfo>>() {}.getType()
         * @param <T>
         * @return
        </T></CheckInItem> */
        @JvmStatic
        fun <T> parseArray(json: String?, type: Type?): List<T>? {
            return try {
                gson.fromJson(json, type)
            } catch (e: JsonSyntaxException) {
                null
            }
        }

        @JvmStatic
        fun <T> parseArray(jsonElement: JsonElement?, type: Type?): List<T>? {
            return try {
                gson.fromJson(jsonElement, type)
            } catch (e: JsonSyntaxException) {
                null
            }
        }

        /**
         * 把json字符串变成map.
         *
         * @param json json
         * @return T
         */
        @JvmStatic
        fun <T> jsonToMap(json: String?): HashMap<String?, T>? {
            val type = object : TypeToken<HashMap<String?, T>?>() {}.type
            return try {
                gson.fromJson(json, type)
            } catch (e: JsonSyntaxException) {
                null
            }
        }

        @JvmStatic
        fun mapToJson(map: Map<*, *>?): String? {
            return try {
                gson.toJson(map)
            } catch (exception: java.lang.Exception) {
                exception.printStackTrace()
                null
            }
        }

        @JvmStatic
        fun <T> parse(json: String?, classOfT: Class<T>?): T? {
            return try {
                gson.fromJson(json, classOfT)
            } catch (e: JsonSyntaxException) {
                null
            }
        }

        @JvmStatic
        fun <T> parse(json: String?, typeOfT: Type?): T? {
            return try {
                gson.fromJson(json, typeOfT)
            } catch (e: JsonSyntaxException) {
                null
            }
        }

        @JvmStatic
        fun <T> parse(jsonElement: JsonElement?, typeOfT: Type?): T? {
            return try {
                gson.fromJson(jsonElement, typeOfT)
            } catch (e: JsonSyntaxException) {
                null
            }
        }

        @JvmStatic
        fun toJson(src: Any?): String {
            return gson.toJson(src)
        }

        @JvmStatic
        fun parseStringAsJsonObject(json: String?): JsonObject {
            return JsonParser.parseString(json).asJsonObject
        }

        @JvmStatic
        fun parseString(jsonData: String?, key: String?): String {
            return try {
                parseStringAsJsonObject(jsonData)[key]?.asString ?: ""
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                ""
            }
        }

        @JvmStatic
        fun parseInt(jsonData: String?, key: String?): Int {
            return try {
                parseStringAsJsonObject(jsonData)[key]?.asInt ?: 0
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }

        @JvmStatic
        fun parseFloat(jsonData: String?, key: String?): Float {
            return try {
                parseStringAsJsonObject(jsonData)[key]?.asFloat ?: 0F
            } catch (e: Exception) {
                e.printStackTrace()
                0F
            }
        }

        @JvmStatic
        fun parseDouble(jsonData: String?, key: String?): Double {
            return try {
                parseStringAsJsonObject(jsonData)[key]?.asDouble ?: 0.0
            } catch (e: Exception) {
                e.printStackTrace()
                0.0
            }
        }

        @JvmStatic
        fun removeFormat(json: String): String? {
            return try {
                JSONObject(json).toString()
            } catch (e: JSONException) {
                null
            }
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}