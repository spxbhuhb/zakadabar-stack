/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.schema

object Formats {
    // source: https://stackoverflow.com/questions/1819142/how-should-i-validate-an-e-mail-address
    val EMAIL = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+"


    // source : https://www.baeldung.com/java-regex-validate-phone-numbers (+ optional extension)
    val PHONE_NUMBER = Regex("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}(/\\d{1,5})?$")

}