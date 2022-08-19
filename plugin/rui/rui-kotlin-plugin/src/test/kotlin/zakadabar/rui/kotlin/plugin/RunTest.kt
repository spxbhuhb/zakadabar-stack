/*
 * Copyright (C) 2020 Brian Norman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zakadabar.rui.kotlin.plugin

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Test
import zakadabar.rui.runtime.RuiAdapterRegistry
import zakadabar.rui.runtime.testing.RuiTestAdapterFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals

class RunTest {

    init {
       RuiAdapterRegistry.register(RuiTestAdapterFactory)
    }

    val sourceDir = "src/test/kotlin/zakadabar/rui/kotlin/plugin/run"

    @Test
    fun basic() = compile("Basic.kt", true)

    @Test
    fun eventHandler() = compile("EventHandler.kt", true)

    fun compile(fileName: String, dumpResult : Boolean = false) {

        // The test source codes are compiled before the tests run. That compilation does not apply
        // the plugin, but the generates the class files. Those class files do not contain the
        // functionality added by the plugin, but those are the class files the class loader below
        // would load if not for the machination with the package and source file name.

        val sourceCode = Files
            .readAllBytes(Paths.get(sourceDir, fileName))
            .decodeToString()
            .replace("package zakadabar.rui.kotlin.plugin.run", "package zakadabar.rui.kotlin.plugin.run.gen")

        val result = KotlinCompilation()
            .apply {
                workingDir = File("./tmp")
                sources = listOf(
                    SourceFile.kotlin(fileName, sourceCode)
                )
                useIR = true
                compilerPlugins = listOf(RuiComponentRegistrar())
                inheritClassPath = true
            }
            .compile()

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

        if (dumpResult) println(result.dump())

        with(result.classLoader.loadClass("zakadabar.rui.kotlin.plugin.run.gen.${fileName.replace(".kt", "Kt")}")) {
            this.declaredMethods.forEach { method ->
                if (method.annotations.firstOrNull { it.annotationClass.simpleName == "RuiTest" } != null) {
                    method.invoke(this)
                }
            }
        }
    }

    fun KotlinCompilation.Result.dump() : String {
        val lines = mutableListOf<String>()

        lines += "exitCode: ${this.exitCode}"

        lines += "======== Messages ========"
        lines += this.messages

        lines += "======== Generated files ========"
        this.generatedFiles.forEach {
            lines += it.toString()
        }

        return lines.joinToString("\n")
    }
}

