/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.resources.css

import kotlinx.atomicfu.atomic
import kotlinx.browser.document
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.util.PublicApi
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Represents a CSS style sheet in the browser. Provides programmatic
 * tools to build, maintain and merge style sheets. Uses [ZkApplication.theme]
 * to build the styles.
 */
open class ZkCssStyleSheet {

    companion object {
        val nextId = atomic(0)
    }

    val id = nextId.getAndIncrement()
    val element = document.createElement("style")

    init {
        element.id = "zk-${this::class.simpleName}-$id"
    }

    var theme = ZkApplication.theme

    internal val rules = mutableMapOf<String, ZkCssStyleRule>() // key is property name

    class CssDelegate(private var cssClassName: String) : ReadOnlyProperty<ZkCssStyleSheet, String> {
        override fun getValue(thisRef: ZkCssStyleSheet, property: KProperty<*>) = cssClassName
    }

    class CssDelegateProvider(val name: String? = null, val builder: ZkCssStyleRule.(ZkTheme) -> Unit) {
        operator fun provideDelegate(thisRef: ZkCssStyleSheet, prop: KProperty<*>): ReadOnlyProperty<ZkCssStyleSheet, String> {

            val cssClassName = name ?: "${thisRef::class.simpleName}-${prop.name}-${nextId.getAndIncrement()}"

            thisRef.rules[prop.name] = ZkCssStyleRule(thisRef, prop.name, cssClassName, builder)

            return CssDelegate(cssClassName)
        }
    }

    fun cssClass(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(null, builder)

    fun cssClass(name: String? = null, builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(name, builder)

    @PublicApi
    fun attach() {
        if (this in ZkApplication.styleSheets) return

        ZkApplication.styleSheets.add(this)

        document.body?.appendChild(element)

        refresh()
    }

    @PublicApi
    fun detach() {
        element.remove()

        ZkApplication.styleSheets.remove(this)
    }

    private fun refresh() {
        element.innerHTML = rules.map { it.value.compile() }.joinToString("\n")
    }

    @PublicApi
    fun merge(from: ZkCssStyleSheet) {
        from.rules.forEach { entry ->

            val fromRule = entry.value
            val toRule = rules[fromRule.propName] ?: return@forEach

            toRule.builder = fromRule.builder
        }

        refresh()
    }

    fun onThemeChange(newTheme: ZkTheme) {
        theme = newTheme
        refresh()
    }

}