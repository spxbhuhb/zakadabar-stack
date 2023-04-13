/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import zakadabar.core.resource.locales.DefaultLocalizedFormats
import zakadabar.core.resource.locales.HuLocalizedFormats
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

/**
 * Stores localized version of application strings (messages, labels, etc).
 */
var localizedStrings: ZkBuiltinStrings = ZkBuiltinStrings()
    set(value) {
        value.addMissing(field)
        field = value
    }

// this is for legacy applications
var skipLocaleFormats = false

fun setLocalizedFormats(locale : String) {
    if (skipLocaleFormats) {
        localizedFormats = DefaultLocalizedFormats
        return
    }

    localizedFormats = when (locale.lowercase()) {
        "hu" -> HuLocalizedFormats
        else -> DefaultLocalizedFormats
    }
}
/**
 * Stores an instance that is able to format and parse different data types
 * according to the current locale.
 */
lateinit var localizedFormats: LocalizedFormats

/**
 * Translates the normalized name of the given type from the string store.
 * Returns with the type name if there is no translation in the string store.
 */
inline fun <reified T> localized() = localizedStrings.getNormalized(T::class.simpleName !!)

/**
 * Translates the given string to the localized version from the string store.
 */
inline val String.localized
    get() = localizedStrings[this]

/**
 * Translates the normalized name of the given class from the string store.
 * Returns with the class name if there is no translation in the string store.
 */
inline val KClass<*>.localized
    get() = localizedStrings.getNormalized(this)

/**
 * Translates the normalized name of the given property from the string store.
 * Returns with the property name if there is no translation in the string store.
 */
inline val KMutableProperty<*>.localized
    get() = localizedStrings.getNormalized(this.name)

// ---- Int ----

inline val Int.localized: String
    get() = localizedFormats.format(this)

// ---- Int ----

inline val Long.localized: String
    get() = localizedFormats.format(this)

// ---- Double ----

inline val Double.localized: String
    get() = localizedFormats.format(this)

// ---- Instant ----

inline val Instant.localized: String
    get() = localizedFormats.format(this)

inline val String.toInstant: Instant
    get() = localizedFormats.toInstant(this)

inline val String.toInstantOrNull: Instant?
    get() = localizedFormats.toInstantOrNull(this)

// ---- LocalDate ----

inline val LocalDate.localized: String
    get() = localizedFormats.format(this)

inline val String.toLocalDate: LocalDate
    get() = localizedFormats.toLocalDate(this)

inline val String.toLocalDateOrNull: LocalDate?
    get() = localizedFormats.toLocalDateOrNull(this)

// ---- LocalDateTime ----

inline val LocalDateTime.localized: String
    get() = localizedFormats.format(this)

inline val String.toLocalDateTime: LocalDateTime
    get() = localizedFormats.toLocalDateTime(this)

inline val String.toLocalDateTimeOrNull: LocalDateTime?
    get() = localizedFormats.toLocalDateTimeOrNull(this)