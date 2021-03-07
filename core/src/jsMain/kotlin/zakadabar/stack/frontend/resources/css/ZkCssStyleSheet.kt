/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.resources.css

import kotlinx.atomicfu.atomic
import kotlinx.browser.document
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.util.PublicApi
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

open class ZkCssStyleSheet<T : ZkCssStyleSheet<T>>(val theme: ZkTheme) {

    companion object {
        val nextId = atomic(0)
    }

    val id = nextId.getAndIncrement()
    val element = document.createElement("style")

    init {
        element.id = "zkc-$id"
    }

    internal val rules = mutableMapOf<String, ZkCssStyleRule>()

    class CssDelegate(private val cssClassName: String) : ReadOnlyProperty<ZkCssStyleSheet<*>, String> {
        override fun getValue(thisRef: ZkCssStyleSheet<*>, property: KProperty<*>) = cssClassName
    }

    class CssDelegateProvider(val init: ZkCssStyleRule.(ZkTheme) -> Unit) {
        operator fun provideDelegate(thisRef: ZkCssStyleSheet<*>, prop: KProperty<*>): ReadOnlyProperty<ZkCssStyleSheet<*>, String> {
            val cssClassName = "${thisRef::class.simpleName}-${prop.name}-${nextId.getAndIncrement()}"
            val rule = ZkCssStyleRule(thisRef, cssClassName)
            rule.init(thisRef.theme)
            thisRef.rules[cssClassName] = rule
            return CssDelegate(cssClassName)
        }
    }

    fun cssClass(init: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(init)

    @PublicApi
    fun attach(): T {
        element.innerHTML = rules.map { rule ->
            "." + rule.key + " {\n" + rule.value.styles.map { "${it.key}: ${it.value};" }.joinToString("\n") + "}"
        }.joinToString("\n")

        document.body?.appendChild(element)

        @Suppress("UNCHECKED_CAST") // returns with itself, should be OK
        return this as T
    }

    @PublicApi
    fun detach() {
        element.remove()
    }

}