/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.text

expect class LocaleAwareComparator(language: String? = null) : Comparator<String>

fun  Iterable<String>.sortedWithLocale(language: String? = null) : List<String> = sortedWith(LocaleAwareComparator(language)::compare)

fun <T> Iterable<T>.sortedWithLocaleBy(language: String? = null, selector: (T)->String) : List<T> =
    LocaleAwareComparator(language).let { sortedWith { a, b -> it.compare(selector(a), selector(b)) } }