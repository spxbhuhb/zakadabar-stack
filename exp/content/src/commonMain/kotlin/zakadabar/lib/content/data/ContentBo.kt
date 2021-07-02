/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema
import zakadabar.stack.data.schema.descriptor.CustomBoConstraint

/**
 * Content entity. There are master versions and localized versions.
 *
 * Master versions:
 *
 * - form a tree, the tree structure is stored in the [parent] property
 * - have `null` as [locale]
 * - have `null` as [master]
 * - may have attachments, images shared between localized versions
 *
 * Localized versions:
 *
 * - are linked to the master by the [master] property
 * - have `null` as [parent]
 * - have `non-null` [locale]
 *
 * @param   id
 * @param   status            Status of the content.
 * @param   parent            If of the parent master content (if any). Null for localized versions and for top-level masters.
 * @param   master            Id of the master content. Null for masters, non-null for localized versions.
 * @param   position          Order of the master between the children of a parent.
 * @param   locale            Locale of a localized version, null for masters.
 * @param   title             Title of the content.
 * @param   seoTitle          Localized, SEO ready version of the title.
 * @param   textBlocks        Text blocks of the content.
 */
@Serializable
class ContentBo(

    override var id: EntityId<ContentBo>,
    var status: EntityId<StatusBo>,
    var parent: EntityId<ContentBo>?,
    var master: EntityId<ContentBo>?,
    var folder: Boolean,
    var position: Long,
    var locale: EntityId<LocaleBo>?,
    var title: String,
    var seoTitle: String,
    var textBlocks: List<TextBlockBo>

) : EntityBo<ContentBo> {

    companion object : EntityBoCompanion<ContentBo>("zkl-content-common")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::locale
        + ::status
        + ::folder
        + ::parent
        + ::master
        + ::position
        + ::title max 100
        + ::seoTitle max 100
        + ::textBlocks

        + custom("locale is not null when master") { name, report ->
            if (master == null) {
                if (locale != null) report.fail(::locale, CustomBoConstraint(name = name))
            }
        }

        + custom("locale is null when localized") { name, report ->
            if (master != null) {
                if (locale == null) report.fail(::locale, CustomBoConstraint(name = name))
            }
        }

        + custom("parent is not null when localized") { name, report ->
            if (master != null) {
                if (parent != null) report.fail(::parent, CustomBoConstraint(name = name))
            }
        }
    }

}