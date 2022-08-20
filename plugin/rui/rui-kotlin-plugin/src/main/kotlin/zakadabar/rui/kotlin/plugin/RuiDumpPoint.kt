/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin

enum class RuiDumpPoint(
    val optionValue : String
) {
    Before("before"),
    After("after"),
    RuiTree("rui-tree"),
    KotlinLike("kotlin-like");

    fun dump(ruiPluginContext: RuiPluginContext, dumpFunc: () -> Unit) {
        if (this in ruiPluginContext.dumpPoints) dumpFunc()
    }

    companion object {
        fun optionValues(): List<String> = values().map { it.optionValue }
        fun fromOption(value : String) : RuiDumpPoint? = values().firstOrNull { it.optionValue == value }
    }
}