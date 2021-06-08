/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.resources

import zakadabar.stack.resources.ZkStringStore


val i18nStrings = Strings()

@Suppress("unused")
class Strings : ZkStringStore() {
    val localeCrud by "Locales"
    val translationCrud by "Translations"
}