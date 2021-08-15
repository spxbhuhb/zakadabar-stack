/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.pages

import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import zakadabar.core.frontend.application.ZkAppLayout
import zakadabar.core.frontend.application.application
import zakadabar.core.frontend.resources.css.ZkCssStyleRule

/**
 * Page that displays an entity in some form.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate") // API class
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