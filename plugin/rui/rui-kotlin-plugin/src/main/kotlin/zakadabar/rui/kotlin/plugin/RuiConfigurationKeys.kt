/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */
package zakadabar.rui.kotlin.plugin

import org.jetbrains.kotlin.config.CompilerConfigurationKey

object RuiConfigurationKeys {
    val ANNOTATION: CompilerConfigurationKey<List<String>> = CompilerConfigurationKey.create("annotation qualified name")
    val DUMP: CompilerConfigurationKey<List<String>> = CompilerConfigurationKey.create("data dump points")

}
