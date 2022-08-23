/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.config.CompilerConfigurationKey

object RuiConfigurationKeys {
    val ANNOTATION: CompilerConfigurationKey<List<String>> = CompilerConfigurationKey.create("qualified annotation name")
    val DUMP: CompilerConfigurationKey<List<RuiDumpPoint>> = CompilerConfigurationKey.create("points where the compiler plugin should dump it's progress")
    val ROOT_NAME_STRATEGY: CompilerConfigurationKey<RuiRootNameStrategy> = CompilerConfigurationKey.create("strategy for naming generated root fragment classes")
    val TRACE: CompilerConfigurationKey<Boolean> = CompilerConfigurationKey.create("add trace output to the generated code")
    val EXPORT_STATE: CompilerConfigurationKey<Boolean> = CompilerConfigurationKey.create("generate state export functions")
    val IMPORT_STATE: CompilerConfigurationKey<Boolean> = CompilerConfigurationKey.create("generate state import functions")
}

