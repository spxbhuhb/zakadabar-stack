/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

enum class RuiRootNameStrategy(
    val optionValue : String
) {
    /**
     * Do not add any postfix after "RuiRoot". Intended for unit tests where
     * it is hard to use the call site as it may easily change (by reformatting
     * the code or adding a comment for example).
     */
    NoPostfix("no-postfix"),

    /**
     * Use the file name and start offset of the "rui" function call this root
     * belongs to. Intended for production use, generated root classes are
     * guaranteed to have unique ID.
     *
     * The generated id **is not** persisted between different software versions.
     */
    StartOffset("start-offset");

    companion object {
        fun optionValues(): List<String> = values().map { it.optionValue }
        fun fromOption(value : String) : RuiRootNameStrategy? = values().firstOrNull { it.optionValue == value }
    }
}
