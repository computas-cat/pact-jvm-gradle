package no.dervis.spark.app

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY

/**
 * Custom Error class. Enables Json-conversion of only
 * the annotated properties. (otherwise we get the full conversion
 * of the Throwable class with stacktrace)
 */

@JsonAutoDetect(
        fieldVisibility = Visibility.NONE,
        setterVisibility = Visibility.NONE,
        getterVisibility = Visibility.NONE,
        isGetterVisibility = Visibility.NONE,
        creatorVisibility = Visibility.NONE
)
open class Error (
        @JsonProperty(value ="message", access = READ_ONLY) val errorMessage: String,
        @JsonProperty(value = "error", access = READ_ONLY) val error: String? = "",
        @JsonIgnore val exception: Exception?) : Exception(exception) {
    constructor() : this("", "", null)
    constructor(errorMessage: String, error: String?) : this(errorMessage, error, null)
    constructor(errorMessage: String) : this(errorMessage, "", null)
    constructor(errorMessage: String, exception: Exception) : this(errorMessage, exception.message, exception)
}

@JsonAutoDetect(
        fieldVisibility = Visibility.NONE,
        setterVisibility = Visibility.NONE,
        getterVisibility = Visibility.NONE,
        isGetterVisibility = Visibility.NONE,
        creatorVisibility = Visibility.NONE
)
class ItemNotFoundError : Error {
    constructor() : super()
    constructor(@JsonProperty(value ="message", access = READ_ONLY) errorMessage: String) : super(errorMessage)
    constructor(
            @JsonProperty(value ="message", access = READ_ONLY) errorMessage: String,
            @JsonProperty(value = "error", access = READ_ONLY) error: String? = "") : super(errorMessage, error)

    constructor(
            @JsonProperty(value ="message", access = READ_ONLY) errorMessage: String,
            exception: Exception) : super(errorMessage, exception)
}