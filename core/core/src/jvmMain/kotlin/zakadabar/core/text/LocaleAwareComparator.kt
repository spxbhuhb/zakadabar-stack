/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.text

import java.text.Collator
import java.util.*

actual class LocaleAwareComparator actual constructor(language: String?) : Comparator<String> {

    private val collator = language?.let{ Collator.getInstance(Locale(it)) } ?: Collator.getInstance()

    override fun compare(a: String, b: String) = collator.compare(a, b)

}