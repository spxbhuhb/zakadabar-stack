/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.markdown.frontend

@JsModule("highlight.js/lib/core")
@JsNonModule
external object hljs {
    fun highlightElement(element: dynamic)
    fun registerLanguage(name : String, language: dynamic)
}

@JsModule("highlight.js/lib/languages/plaintext")
@JsNonModule
external object hljsText

@JsModule("highlight.js/lib/languages/kotlin")
@JsNonModule
external object hljsKotlin

@JsModule("highlight.js/lib/languages/yaml")
@JsNonModule
external object hljsYaml

@JsModule("highlight.js/lib/languages/sql")
@JsNonModule
external object hljsSql

@JsModule("highlight.js/lib/languages/xml")
@JsNonModule
external object hljsXml
