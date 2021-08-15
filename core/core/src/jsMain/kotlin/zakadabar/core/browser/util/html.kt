/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.browser.util

import org.w3c.dom.DOMTokenList
import org.w3c.dom.HTMLElement
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.resource.css.ZkCssStyleRule
import zakadabar.core.resource.css.percent
import zakadabar.core.util.PublicApi
import kotlin.math.max

operator fun DOMTokenList.plusAssign(token: String?) {
    if (token != null) this.add(token)
}

operator fun DOMTokenList.plusAssign(tokens: Array<out String>) {
    tokens.forEach { this.add(it) }
}

operator fun DOMTokenList.minusAssign(token: String?) {
    if (token != null) this.remove(token)
}

operator fun DOMTokenList.plusAssign(rule: ZkCssStyleRule?) {
    if (rule != null) this.add(rule.cssClassname)
}

operator fun DOMTokenList.plusAssign(rules: Array<out ZkCssStyleRule>) {
    rules.forEach { this.add(it.cssClassname) }
}

operator fun DOMTokenList.minusAssign(rule: ZkCssStyleRule?) {
    if (rule != null) this.remove(rule.cssClassname)
}

infix fun HTMLElement.marginRight(size: Any): HTMLElement {
    this.style.marginRight = if (size is Int) "${size}px" else size.toString()
    return this
}

infix fun HTMLElement.marginBottom(size: Any): HTMLElement {
    this.style.marginBottom = if (size is Int) "${size}px" else size.toString()
    return this
}

infix fun HTMLElement.width(value: Any): HTMLElement {
    if (value == 100.percent) {
        classList += zkLayoutStyles.w100
    } else {
        this.style.width = value.toString()
    }
    return this
}

infix fun HTMLElement.height(value: Any): HTMLElement {
    if (value is String) {
        this.style.height = value
    } else {
        this.style.height = "${value}px"
    }
    return this
}

@Deprecated("EOL: 2021.8.1  -  replace with css")
infix fun HTMLElement.flex(value: String): HTMLElement {
    if (value == "grow") {
        classList += zkLayoutStyles.grow
    } else {
        throw RuntimeException("invalid flex value: $value")
    }
    return this
}

@Deprecated("EOL: 2021.8.1  -  replace with css")
infix fun HTMLElement.style(value: String): HTMLElement {
    style.cssText = value
    return this
}

/**
 * Align the width of two elements. The [calc] function is used to calculate the aligned
 * width from the with of the two elements.
 *
 * Sets inline styles minWidth, width, maxWidth of the.
 *
 * @param  first   First element, does nothing when its null.
 * @param  second  Second element, does nothing when its null.
 * @param  calc    Calculation function, defaults to [max].
 */
@PublicApi
fun alignWidth(first: HTMLElement?, second: HTMLElement?, calc: (Double, Double) -> Double = ::max) {
    if (first == null || second == null) return

    val firstWidth = first.getBoundingClientRect().width
    val secondWidth = second.getBoundingClientRect().width

    val alignedWidthPx = "${calc(firstWidth, secondWidth)}px"

    with(first.style) {
        minWidth = alignedWidthPx
        width = alignedWidthPx
        maxWidth = alignedWidthPx
    }

    with(second.style) {
        minWidth = alignedWidthPx
        width = alignedWidthPx
        maxWidth = alignedWidthPx
    }
}