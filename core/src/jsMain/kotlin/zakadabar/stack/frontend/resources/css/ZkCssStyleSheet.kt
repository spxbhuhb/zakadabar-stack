/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.resources.css

import kotlinx.atomicfu.atomic
import kotlinx.browser.document
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.util.PublicApi
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T : ZkTheme, S : ZkCssStyleSheet<T>> cssStyleSheet(sheet: S) = CssStyleSheetDelegate(sheet)

class CssStyleSheetDelegate<T : ZkTheme, S : ZkCssStyleSheet<T>>(
    var sheet: S?
) {

    init {
        sheet?.attach()
    }

    operator fun getValue(thisRef: Nothing?, property: KProperty<*>): S {
        return sheet ?: throw IllegalStateException("style sheet not initialized yet")
    }

    operator fun setValue(thisRef: Nothing?, property: KProperty<*>, value: S) {
        sheet?.detach()
        sheet = value
        value.attach()
    }

}


/**
 * Represents a CSS style sheet in the browser. Provides programmatic
 * tools to build, maintain and merge style sheets. Uses [ZkApplication.theme]
 * to build the styles.
 *
 * I decided to add a type parameter because that makes themes easily extendable.
 * However, the theme in the application is not typed, so this is not a perfect
 * solution. We can go with this for now and then figure out how to resolve
 * this conflict.
 */
open class ZkCssStyleSheet<T : ZkTheme> {

    companion object {
        val nextId = atomic(0)
    }

    val id = nextId.getAndIncrement()
    val element = document.createElement("style")

    init {
        element.id = "zk-${this::class.simpleName}-$id"
    }

    @Suppress("UNCHECKED_CAST") // TODO figure out if we can remove this, maybe doesn't even worth it
    open var theme: T = ZkApplication.theme as T

    internal val rules = mutableMapOf<String, ZkCssStyleRule>() // key is property name

    class CssDelegate(private var cssClassName: String) : ReadOnlyProperty<ZkCssStyleSheet<*>, String> {
        override fun getValue(thisRef: ZkCssStyleSheet<*>, property: KProperty<*>) = cssClassName
    }

    class CssDelegateProvider(val name: String? = null, val selector: String? = null, val builder: ZkCssStyleRule.(ZkTheme) -> Unit) {
        operator fun provideDelegate(thisRef: ZkCssStyleSheet<*>, prop: KProperty<*>): ReadOnlyProperty<ZkCssStyleSheet<*>, String> {

            val cssClassName = name ?: "${thisRef::class.simpleName}-${prop.name}-${nextId.getAndIncrement()}"

            thisRef.rules[prop.name] = ZkCssStyleRule(thisRef, prop.name, cssClassName, selector, builder)

            return CssDelegate(cssClassName)
        }
    }

    fun cssImport(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(null, "@import", builder)

    fun cssRule(selector: String, builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(null, selector, builder)

    fun cssClass(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(null, null, builder)

    fun cssClass(name: String? = null, builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(name, null, builder)

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
    fun merge(from: ZkCssStyleSheet<*>) {
        from.rules.forEach { entry ->

            val fromRule = entry.value
            val toRule = rules[fromRule.propName] ?: return@forEach

            toRule.builder = fromRule.builder
        }

        refresh()
    }

    @Suppress("UNCHECKED_CAST") // TODO figure out if we can remove this, maybe doesn't even worth it
    fun onThemeChange(newTheme: ZkTheme) {
        theme = newTheme as T
        refresh()
    }

}