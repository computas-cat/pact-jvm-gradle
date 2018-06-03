package no.dervis.spark.app

import com.google.gson.*
import com.google.gson.internal.bind.util.ISO8601Utils
import java.lang.reflect.Type
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ISO8601Date : JsonDeserializer<Date>, JsonSerializer<Date> {
    override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(ISO8601Utils.format(src, false, TimeZone.getDefault()))
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date {
        return Date(ZonedDateTime.parse(json!!.asString, DateTimeFormatter.ISO_DATE_TIME).toInstant().toEpochMilli())
    }
}