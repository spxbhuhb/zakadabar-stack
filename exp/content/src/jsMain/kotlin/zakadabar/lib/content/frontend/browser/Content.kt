/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import kotlinx.datetime.Clock
import zakadabar.lib.blobs.frontend.attachment.ZkAttachmentsField
import zakadabar.lib.blobs.frontend.image.ZkImagesField
import zakadabar.lib.content.data.*
import zakadabar.lib.content.resources.contentStrings
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.application.executor
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.application.translate
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable

class ContentCrud : ZkCrudTarget<ContentCommonBo>() {
    init {
        companion = ContentCommonBo.Companion
        boClass = ContentCommonBo::class
        editorClass = ContentForm::class
        tableClass = ContentTable::class
    }
}

class ContentForm : ZkForm<ContentCommonBo>() {

    override fun onCreate() {
        super.onCreate()

        build(translate<ContentForm>(), addButtons = false) {
            + section(stringStore.basics) {
                + bo::id
                + select(bo::status) { ContentStatusBo.all().by { it.name } }
                + select(bo::category) { ContentCategoryBo.all().by { it.name } }
                + select(bo::locale) { LocaleBo.all().by { it.name } }
                + bo::title
                + textarea(bo::summary)
                + bo::motto
            }

            + buttons() // here to make difference between form fields and files

            + section(contentStrings.thumbnail) {
                + ZkImagesField(
                    this@ContentForm,
                    ContentBlobBo.comm,
                    bo.id,
                    disposition = ContentBlobDisposition.thumbnail
                ) {
                    ContentBlobBo(EntityId(), bo.id, ContentBlobDisposition.thumbnail, it.name, it.type, it.size.toLong())
                }
            }

            + section(contentStrings.images) {
                + ZkImagesField(
                    this@ContentForm,
                    ContentBlobBo.comm,
                    bo.id,
                    disposition = ContentBlobDisposition.image
                ) {
                    ContentBlobBo(EntityId(), bo.id, ContentBlobDisposition.image, it.name, it.type, it.size.toLong())
                }
            }

            + section(contentStrings.attachments) {
                + ZkAttachmentsField(
                    this@ContentForm,
                    ContentBlobBo.comm,
                    bo.id,
                    disposition = ContentBlobDisposition.attachment
                ) {
                    ContentBlobBo(EntityId(), bo.id, ContentBlobDisposition.attachment, it.name, it.type, it.size.toLong())
                }
            }

        }
    }

    override suspend fun onSubmitStart() {
        super.onSubmitStart()
        bo.modifiedAt = Clock.System.now()
        bo.modifiedBy = executor.account.id
    }
}

class ContentTable : ZkTable<ContentCommonBo>() {

    override fun onConfigure() {

        crud = target<ContentCrud>()

        titleText = translate<ContentTable>()

        add = true
        search = true
        export = true
        
        + ContentCommonBo::id
        + ContentCommonBo::modifiedAt
        // ContentBo::modifiedBy // record id and opt record id is not supported yet 
        // ContentBo::status // record id and opt record id is not supported yet 
        // ContentBo::category // record id and opt record id is not supported yet 
        // ContentBo::parent // record id and opt record id is not supported yet 
        // ContentBo::locale // record id and opt record id is not supported yet 
        + ContentCommonBo::title
        + ContentCommonBo::summary
        + ContentCommonBo::motto
        
        + actions()
    }
}