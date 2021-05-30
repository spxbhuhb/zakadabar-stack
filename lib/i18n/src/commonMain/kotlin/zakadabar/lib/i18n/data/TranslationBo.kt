/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.i18n.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

/**
 * A string in a given locale.
 *
 * @property  id      Id of the string locale record.
 * @property  locale  The locale of the string like "hu-HU".
 * @property  name    Name of the string resource as defined in the string store.
 * @property  value   Value of the string resource.
 */
@Serializable
data class TranslationBo(
    override var id: EntityId<TranslationBo>,
    var name: String,
    var locale: String,
    var value: String,
) : EntityBo<TranslationBo> {

    companion object : EntityBoCompanion<TranslationBo>("translation")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::name min 1 max 100 blank false
        + ::locale min 2 max 20 blank false
        + ::value
    }
}