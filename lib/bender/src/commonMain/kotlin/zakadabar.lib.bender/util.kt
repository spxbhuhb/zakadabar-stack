/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender

fun String.withoutDto() = if (this.toLowerCase().endsWith("dto")) this.substring(0, this.length-3) else this
fun String.toTableName() = "${withoutDto()}Table"
fun String.toDaoName() = "${withoutDto()}Dao"
