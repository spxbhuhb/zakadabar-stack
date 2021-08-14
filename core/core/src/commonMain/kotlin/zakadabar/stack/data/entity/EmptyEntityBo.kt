/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.data.entity

import kotlinx.serialization.Serializable

/**
 * This BO is a global empty entity. It is used when we need a BO for type safety
 * but we actually don't want one, most notably in ActionBusinessLogicBase and
 * QueryBusinessLogicBase.
 */
@Serializable
object EmptyEntityBo : EntityBo<EmptyEntityBo> {

    override var id: EntityId<EmptyEntityBo>
        get() = throw IllegalStateException("EmptyEntityBo has no ID.")
        set(_) {
            throw IllegalStateException("EmptyEntityBo has no ID.")
        }

    override fun getBoNamespace(): String {
        throw IllegalStateException("EmptyEntityBo has no namespace.")
    }

    override fun comm(): EntityCommInterface<EmptyEntityBo> {
        throw IllegalStateException("EmptyEntityBo has no comm.")
    }

}