/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("TestFunctionName")

package zakadabar.rui.runtime.test.original

import kotlin.reflect.KProperty0

interface InputContext<T : Any> {
    val bo : T
}

open class ReactiveForm<T : Any>  : InputContext<T> {
    override lateinit var bo : T
}

fun <T : Any> Form(builder : InputContext<T>.() -> Unit) {

}

/**
 * [Documentation](https://zakadabar.io)
 * [Recipes](https://zakadabar.io)
 *
 * Section of an input context (e.g. form).
 *
 */
fun <T : Any> InputContext<T>.Section(builder : InputContext<T>.() -> Unit) {

}

fun <T : Any> InputContext<T>.Field(field : KProperty0<String>) {

}

class Registration(
    val accountName : String,
    val password : String,
    val email : String,
    val mobile : String
)

fun Register1() {
    Form<Registration> {
        Field(bo::accountName)
        Field(bo::password)
        Field(bo::email)
        Field(bo::mobile)
    }
}

fun title(calc : () -> String) {

}

fun hint(calc : () -> String) {

}

infix fun Unit.label(text : String) {

}

fun Register2() {
    Form<Registration> {
        Section {
            title { "account" }
            hint { "These are the default account parameters" }
            Field(bo::accountName) label "Account"
            Field(bo::password)
        }
        Section {
            Field(bo::email)
            Field(bo::mobile)
        }
    }
}

fun Register3() {
    Form<Registration> {
        AccountSection()
        ContactSection()
    }
}

fun InputContext<Registration>.AccountSection() {
    Section {
        Field(bo::accountName)
        Field(bo::password)
    }
}

fun InputContext<Registration>.ContactSection() {
    Section {
        Field(bo::email)
        Field(bo::mobile)
    }
}

class Register4 : ReactiveForm<Registration>() {

    fun main() {
        bo = Registration("a", "b", "c", "d")
        AccountSection()
        ContactSection()
    }

    fun AccountSection() {
        Section {
            Field(bo::accountName)
            Field(bo::password)
        }
    }

    fun ContactSection() {
        Section {
            Field(bo::email)
            Field(bo::mobile)
        }
    }
}