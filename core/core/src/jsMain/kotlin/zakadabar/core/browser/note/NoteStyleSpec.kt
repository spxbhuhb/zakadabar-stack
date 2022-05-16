/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.note

import zakadabar.core.resource.css.CssStyleSpec
import zakadabar.core.resource.css.ZkCssStyleRule

interface NoteStyleSpec : CssStyleSpec {
    val noteOuter : ZkCssStyleRule
    val noteInner : ZkCssStyleRule
    val titleOuter : ZkCssStyleRule
    val titleIcon : ZkCssStyleRule
    val contentOuter : ZkCssStyleRule
    val separator : ZkCssStyleRule
    val primaryInner : ZkCssStyleRule
    val primaryTitle : ZkCssStyleRule
    val secondaryInner  : ZkCssStyleRule
    val secondaryTitle : ZkCssStyleRule
    val successInner : ZkCssStyleRule
    val successTitle : ZkCssStyleRule
    val warningInner : ZkCssStyleRule
    val warningTitle : ZkCssStyleRule
    val dangerInner : ZkCssStyleRule
    val dangerTitle : ZkCssStyleRule
    val infoInner : ZkCssStyleRule
    val infoTitle : ZkCssStyleRule
}