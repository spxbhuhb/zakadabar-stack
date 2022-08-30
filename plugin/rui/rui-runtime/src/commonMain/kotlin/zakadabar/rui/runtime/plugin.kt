/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

object Plugin {
    const val OPTION_NAME_ANNOTATION = "rui-annotation"
    const val OPTION_NAME_DUMP_POINT = "rui-dump-point"
    const val OPTION_NAME_ROOT_NAME_STRATEGY = "rui-root-name-strategy"
    const val OPTION_NAME_TRACE = "rui-trace"
    const val OPTION_NAME_EXPORT_STATE = "rui-export-state"
    const val OPTION_NAME_IMPORT_STATE = "rui-import-state"

    const val PLUGIN_ID = "zakadabar-rui"
    const val PLUGIN_GROUP = "hu.simplexion.zakadabar"
    const val PLUGIN_VERSION = BuildConfig.PLUGIN_VERSION

    const val GRADLE_PLUGIN_NAME = "rui-gradle-plugin"
    const val GRADLE_EXTENSION_NAME = "rui"

    const val KOTLIN_PLUGIN_NAME = "rui-kotlin-plugin"

    const val RUNTIME_NAME = "rui-runtime"

    const val RUI_ANNOTATION = "zakadabar.rui.runtime.Rui"

    val RUI_FRAGMENT_CLASS = listOf("zakadabar", "rui", "runtime", "RuiFragment")
    val RUI_ADAPTER_CLASS = listOf("zakadabar", "rui", "runtime", "RuiAdapter")
    val RUI_BRIDGE_CLASS = listOf("zakadabar", "rui", "runtime", "RuiBridge")
    val RUI_BLOCK_CLASS = listOf("zakadabar", "rui", "runtime", "RuiBlock")
    val RUI_WHEN_CLASS = listOf("zakadabar", "rui", "runtime", "RuiWhen")
    val RUI_FOR_LOOP_CLASS = listOf("zakadabar", "rui", "runtime", "RuiForLoop")
    val RUI_ENTRY_FUNCTION = listOf("zakadabar", "rui", "runtime", "rui")


}