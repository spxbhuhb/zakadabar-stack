/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

/**
 * This BO is a global empty entity. It is used when we need a BO for type safety
 * but we actually don't want one, most notably in ActionBusinessLogicBase and
 * QueryBusinessLogicBase.
 */
object EmptyEntityBo : EntityBo<EmptyEntityBo> {

    override var id: EntityId<EmptyEntityBo>
        get() = throw IllegalStateException("EmptyEntityBo has no ID.")
        set(value) {
            throw IllegalStateException("EmptyEntityBo has no ID.")
        }

    override fun getBoNamespace(): String {
        throw IllegalStateException("EmptyEntityBo has no namespace.")
    }

    override fun comm(): EntityCommInterface<EmptyEntityBo> {
        throw IllegalStateException("EmptyEntityBo has no comm.")
    }

}