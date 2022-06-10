/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.text

actual class LocaleAwareComparator actual constructor(language: String?) : Comparator<String> {

    private val collator = language?.let { Intl.Collator(it) } ?: Intl.Collator()

    override fun compare(a: String, b: String) = collator.compare(a, b)

}

//this class is missing in Kotlin/JS, however browsers support it
private external class Intl {
    class Collator() {
        constructor(language: String)
        fun compare(a: String, b: String) : Int
    }
}