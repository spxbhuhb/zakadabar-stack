/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.resources.css

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet.Companion.styleSheets
import zakadabar.stack.util.PublicApi
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <S : ZkCssStyleSheet> cssStyleSheet(sheet: S) = CssStyleSheetDelegate(sheet)

class CssStyleSheetDelegate<S : ZkCssStyleSheet>(
    var sheet: S?
) {

    init {
        sheet?.let { styleSheets.add(it) }

        if (::application.isInitialized) {
            sheet?.attach()
        } else {
            sheet?.attachOnRefresh = true
        }
    }

    operator fun getValue(thisRef: Nothing?, property: KProperty<*>): S {
        return sheet ?: throw IllegalStateException("style sheet not initialized yet")
    }

    operator fun setValue(thisRef: Nothing?, property: KProperty<*>, value: S) {
        sheet?.detach()
        sheet = value
        value.attach()
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): S {
        return sheet ?: throw IllegalStateException("style sheet not initialized yet")
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: S) {
        sheet?.detach()
        sheet = value
        sheet?.theme?.onResume() // FIXME make this sheet specific
        value.attach()
    }

}


/**
 * Represents a CSS style sheet in the browser. Provides programmatic
 * tools to build, maintain and merge style sheets. Uses [theme]
 * to build the styles.
 *
 * I decided to add a type parameter because that makes themes easily extendable.
 * However, the theme in the application is not typed, so this is not a perfect
 * solution. We can go with this for now and then figure out how to resolve
 * this conflict.
 */
open class ZkCssStyleSheet {

    companion object {
        var nextId = 0

        /**
         * When true style names will be like "zks233" where the number
         * is the id assigned to the ZkCssStyleRule. When false, the class
         * names will contain the style class name, the property name and the id.
         */
        var shortNames = true

        val styleSheets = mutableListOf<ZkCssStyleSheet>()
    }

    val id = nextId ++

    /**
     * When true, the next refresh attaches this style sheet.
     * Style sheets have to wait until the application is set.
     */
    open var attachOnRefresh = false

    val element = document.createElement("style")

    init {
        element.id = "zk-${this::class.simpleName}-$id"
    }

    open val theme: ZkTheme
        get() = zakadabar.stack.frontend.resources.theme

    internal val rules = mutableMapOf<String, ZkCssStyleRule>() // key is property name

    /**
     * Provides the ZkCssStyleRule as a delegate. I decided to go this way because I think it is important
     * for the rule to know the property name and that's not possible with a simple assignment. Performance
     * overhead should be minimal.
     */
    class CssDelegateProvider(val name: String? = null, val selector: String? = null, val builder: ZkCssStyleRule.(ZkTheme) -> Unit) {

        operator fun provideDelegate(thisRef: ZkCssStyleSheet, prop: KProperty<*>): ReadOnlyProperty<ZkCssStyleSheet, ZkCssStyleRule> {

            val cssClassName = name ?: if (shortNames) "zks${nextId++}" else "${thisRef::class.simpleName}-${prop.name}-${nextId ++}"

            val rule = ZkCssStyleRule(thisRef, prop.name, cssClassName, selector, builder)

            thisRef.rules[prop.name] = rule

            return rule
        }

    }

    fun cssImport(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(null, "@import", builder)

    @PublicApi
    fun cssRule(selector: String, builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(null, selector, builder)

    fun cssClass(builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(null, null, builder)

    fun cssClass(name: String? = null, builder: ZkCssStyleRule.(ZkTheme) -> Unit) = CssDelegateProvider(name, null, builder)

    @PublicApi
    fun attach() {

        if (document.getElementById(element.id) != null) {
            refresh()
            return
        }

        if (this !in styleSheets) {
            styleSheets += this
        }

        var sheets = document.getElementById("zk-styles")

        if (sheets == null) {
            sheets = document.createElement("div") as HTMLElement
            sheets.id = "zk-styles"
            document.body?.appendChild(sheets)
        }

        sheets.appendChild(element)

        refresh()
    }

    @PublicApi
    fun detach() {
        element.remove()
        styleSheets -= this
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

    fun onThemeChange() {
        attach()
    }

}