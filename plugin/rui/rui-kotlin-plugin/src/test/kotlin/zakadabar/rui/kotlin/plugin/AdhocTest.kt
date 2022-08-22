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
import java.io.File
import kotlin.test.assertEquals

class AdhocTest {

    val sourceDir = "src/test/kotlin/zakadabar/rui/kotlin/plugin/adhoc"

    @Test
    fun test() = compile("Adhoc.kt")

    fun compile(fileName: String, dumpResult : Boolean = false) {
        val result = KotlinCompilation()
            .apply {
                workingDir = File("./tmp")
                sources = listOf(
                    SourceFile.fromPath(File(sourceDir, fileName))
                )
                useIR = true
                compilerPlugins = listOf(RuiComponentRegistrar.withAll())
                inheritClassPath = true
            }
            .compile()

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

        if (dumpResult) println(result.dump())

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

