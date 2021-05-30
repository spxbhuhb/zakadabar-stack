package zakadabar.lib.i18n.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema


/**
 * Business Object of TranslationBo.
 *
 * Generated with Bender at 2021-05-30T09:38:55.857Z.
 *
 * Please do not implement business logic in this class. If you add fields,
 * please check the frontend table and form, and also the persistence API on
 * the backend.
 */
@Serializable
class TranslationBo(

    override var id: EntityId<TranslationBo>,
    var locale : EntityId<LocaleBo>,
    var key : String,
    var value : String

) : EntityBo<TranslationBo> {

    companion object : EntityBoCompanion<TranslationBo>("translation")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::locale
        + ::key blank false min 1 max 100
        + ::value
    }

}