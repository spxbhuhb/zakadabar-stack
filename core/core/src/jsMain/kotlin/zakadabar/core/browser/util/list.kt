/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.util

import zakadabar.core.data.EntityBo
import zakadabar.core.text.sortedWithLocaleBy

fun <T : EntityBo<T>> List<T>.by(field: (it: T) -> String) =
    map { it.id to field(it) }.sortedWithLocaleBy { it.second }

fun <T : EntityBo<T>> List<T>.by(language: String, field: (it: T) -> String) =
    map { it.id to field(it) }.sortedWithLocaleBy(language) { it.second }