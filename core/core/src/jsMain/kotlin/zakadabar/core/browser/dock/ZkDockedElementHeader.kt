/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.dock

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.util.PublicApi

@PublicApi
open class ZkDockedElementHeader(
    title: String = "",
    val icon: ZkElement? = null,
    @PublicApi val titleElement: ZkElement = ZkElement().also { it.build { + title } },
    val tools: List<ZkElement> = emptyList()
) : ZkElement() {

    val toolElement = ZkElement()

    override fun onCreate() {
         + zkDockStyles.header

        this += icon?.apply { classList += zkDockStyles.headerIcon }
        this += titleElement css zkDockStyles.text
        this += toolElement.css(zkDockStyles.extensions)

        toolElement += tools
    }

}