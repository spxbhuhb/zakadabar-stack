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

package zakadabar.reactive.kotlin.plugin

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Test
import kotlin.test.assertEquals

val basic = """
    import zakadabar.reactive.core.Reactive
    import zakadabar.reactive.core.ReactiveState
    import zakadabar.reactive.core.ReactiveContext
    import zakadabar.reactive.core.optimize

    @Reactive
    fun d() { 
        c(12) 
    }

    @Reactive
    fun c(a : Int) { 
        println("called c(" + a.toString() + ")")
    }
""".trimIndent()

val ifElseSlotsOnBoth = """
    import zakadabar.reactive.core.Reactive
    import zakadabar.reactive.core.ReactiveState
    import zakadabar.reactive.core.ReactiveContext
    import zakadabar.reactive.core.optimize

    @Reactive
    fun d() {
        if (12 == 13) c(12) else c(13)
    }

    @Reactive
    fun e() {
        if (11 == 12) {
            c(12)
        } else {
            c(13)
        }
    }

    @Reactive
    fun c(a : Int) { 
        println("called c(" + a.toString() + ")")
    }
""".trimIndent()

val whileLoop = """
    import zakadabar.reactive.core.Reactive
    import zakadabar.reactive.core.ReactiveState
    import zakadabar.reactive.core.ReactiveContext
    import zakadabar.reactive.core.optimize

    @Reactive
    fun d() {
        var i = 0
        while (i < 10) {
            i++
            c(i)
        }
    }

    @Reactive
    fun c(a : Int) { 
        println("called c(" + a.toString() + ")")
    }
""".trimIndent()

val closure = """
    fun d() { 
        var i = 0
        class A {
           fun a() {
               i = 2
               println(i)
           }
        }
        A().a()
    }

""".trimIndent()

val dump = """
import zakadabar.reactive.core.ReactiveState
import zakadabar.reactive.core.optimize
import zakadabar.reactive.core.lastChildCurrentToFuture

fun B(value: Int, callSiteOffset: Int, parentState: ReactiveState) {
    val myState = optimize(callSiteOffset, parentState, value)
    if (myState == null) return

    parentState.future.last().handle = "B"

    lastChildCurrentToFuture(parentState)
}
""".trimIndent()

val small2 = """
//    import kotlin.reflect.KProperty0
//
//    annotation class Reactive
//
//    object A {
//        val b = "Hello"
//    }
//
//    class ZkElement {
//        
//    }
//
//    inline operator fun ZkElement.unaryPlus() {
//    
//    }
//
//    inline operator fun KProperty0<String>.unaryPlus() {
//    
//    }
//
//    fun zke(builder : ZkElement.() -> Unit) : ZkElement = ZkElement().also { it.builder() }
//
//    @Reactive
//    fun a() = zke { }
//
//    @Reactive
//    fun b() {
//        + a()
//        + A::b
//    }

      fun d() { c() }

      fun c() {  }
""".trimIndent()

val big = """
    annotation class UiElement

    fun a() : String = ""
    
    @UiElement
    class A(
        val s1 : String,
        var s2 : String,
        val s3 : String?,
        var s4 : String?,
        val s5 : String = "CS5L",
        var s6 : String = "CS5R",
        val s7 : String? = null,
        var s8 : String? = null,
        val s9 : String? = "CS6L",
        var s10 : String? = "CS6R",
        val s11 : String = a()
    )

    val a1 = A("CS1", "CS2", null, null)
    val a2 = A("CS1", "CS2", null, null, s7 = a())
"""

val run = """
    import zakadabar.reactive.core.Reactive
    import zakadabar.reactive.core.ReactiveState
    import zakadabar.reactive.core.ReactiveContext
    import zakadabar.reactive.core.optimize

    @Reactive
    fun d(callSiteOffset : Int, parentState : ReactiveState) { 
        c(12) 
        c(13)
    }

    @Reactive
    fun c(a : Int) { 
        println("called c(" + a.toString() + ")")
    }

    fun main() {
        ReactiveState(ReactiveContext(), "<root>", "<root>", emptyArray()).apply {
            d(0, this)
        }
    }
""".trimIndent()

class IrPluginTest {

    fun compile(source : String) {
        val result = compile(SourceFile.kotlin("pluginTest.kt", source))
        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    @Test
    fun basic() = compile(basic)

    @Test
    fun `if else - slots on both branches`() = compile(ifElseSlotsOnBoth)

    @Test
    fun `while loop`() = compile(whileLoop)

    @Test
    fun closure() = compile(closure)

    // @Test
    fun run() {

        println("%%%%%%%%%%%%%%%%  compile  %%%%%%%%%%%%%%%%")

        val result = compile(SourceFile.kotlin("pluginTest.kt", run))
        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

        println("%%%%%%%%%%%%%%%%  execute  %%%%%%%%%%%%%%%%")

        with(result.classLoader.loadClass("PluginTestKt")) {
            this.declaredMethods.forEach {
                if (it.name == "main" && it.parameters.isEmpty()) {
                    it.invoke(this)
                }
            }
        }

    }
}

fun compile(vararg sourceFiles: SourceFile): KotlinCompilation.Result {
    return KotlinCompilation().apply {
        // workingDir = File("/Users/tiz/src/kotlin-compiler-plugin-poc/plugin-test")
        sources = sourceFiles.toList()
        useIR = true
        compilerPlugins = listOf(ReactiveComponentRegistrar())
        inheritClassPath = true
    }.compile()
}