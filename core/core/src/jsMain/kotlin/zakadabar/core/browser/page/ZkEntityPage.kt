/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.page

import zakadabar.core.browser.application.ZkAppLayout
import zakadabar.core.browser.application.application
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import zakadabar.core.resource.css.ZkCssStyleRule

/**
 * Page that displays an entity in some form.
 */
@Suppress("unused") // API class
open class ZkEntityPage<T : EntityBo<T>>(
    layout: ZkAppLayout? = null,
    css: ZkCssStyleRule? = null
) : ZkPage(layout, css) {

    val entityId : EntityId<T>
       get() = EntityId(application.routing.navState.segments.last())

    @Deprecated(
        "ZkEntityPage.open throws NotImplementedError, use open(id) instead",
        replaceWith = ReplaceWith("open(id)"),
        level = DeprecationLevel.ERROR
    )
    override fun open() = throw NotImplementedError("ZkEntityPage")

    open fun open(id : EntityId<T>) {
        application.changeNavState(this, "$id")
    }

}