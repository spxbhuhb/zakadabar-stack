/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import zakadabar.lib.blobs.frontend.attachment.ZkAttachmentsField
import zakadabar.lib.blobs.frontend.image.ZkImagesField
import zakadabar.lib.content.data.*
import zakadabar.lib.content.resources.contentStrings
import zakadabar.lib.i18n.data.LocaleBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.application.executor
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementState
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.crud.ZkCrudEditor
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.layout.tabcontainer.ZkTabContainer
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.io
import zakadabar.stack.resources.localizedStrings

class ContentEditor : ZkCrudTarget<ContentBo>() {

    init {
        companion = ContentBo.Companion
        boClass = ContentBo::class
        editorClass = ContentEditorForm::class
    }

    @Serializable
    class Args(
        val master: EntityId<ContentBo>?,
        val locale: EntityId<LocaleBo>?,
    )

    fun openCreate(args: Args) {
        application.changeNavState(this, "create", "args=${Json.encodeToString(Args.serializer(), args)}")
    }

    override suspend fun onBeforeAddedCreate(editor: ZkCrudEditor<ContentBo>) {
        editor as ContentEditorForm

        val navArgs = application.routing.navState.args

        val args = if (navArgs.isNullOrEmpty()) {
            Args(null, null)
        } else {
            Json.decodeFromString(Args.serializer(), navArgs)
        }

        editor.bo.master = args.master
        editor.bo.locale = args.locale

        if (args.master != null) {
            editor.master = ContentBo.read(args.master)
        }
    }

    override suspend fun onBeforeAddedUpdate(editor: ZkCrudEditor<ContentBo>) {
        editor as ContentEditorForm

        editor.bo.master?.let {
            editor.master = ContentBo.read(it)
            editor.master.parent?.let { parentId ->
                editor.parent = ContentBo.read(parentId)
            }
        }
    }

}

class ContentEditorForm : ZkForm<ContentBo>() {

    lateinit var master: ContentBo
    lateinit var parent: ContentBo
    lateinit var textBlocks: ZkElement

    override fun onCreate() {
        super.onCreate()

        // this lets build be called from an IO block after onResume ran
        if (lifeCycleState == ZkElementState.Resumed && setAppTitle) setAppTitleBar()

        + ZkTabContainer {

            + tab(localizedStrings.basics) {

                + fieldGrid {
                    + bo::id
                    + select(bo::status) { StatusBo.all().by { it.name } }
                    if (bo.master == null) + bo::folder

                    if (bo.master == null) {
                        + select(bo::parent) { FolderQuery().execute().map { it.id to it.title } }
                    } else {
                        + constString(localizedStrings["parent"]) { if (::parent.isInitialized) parent.title else "" }
                    }

                    + select(bo::locale) { LocaleBo.all().by { it.name } } readOnly true
                    + textarea(bo::title)
                }

                + buttons()

            }

            + tab(contentStrings.texts) {

                textBlocks = this

                bo.textBlocks.forEach {
                    + ContentTextForm(textBlocks).apply { bo = it }
                }

                + ZkButton(iconSource = ZkIcons.add) {
                    insertFirst(
                        ContentTextForm(textBlocks).apply { bo = default { } }
                    )
                }

                + buttons()

            }

            + tab(contentStrings.imagesAndAttachments) {

                + section(contentStrings.thumbnail) {
                    + ZkImagesField(
                        this@ContentEditorForm,
                        AttachedBlobBo.comm,
                        bo.id,
                        disposition = AttachedBlobDisposition.thumbnail
                    ) {
                        AttachedBlobBo(EntityId(), bo.id, AttachedBlobDisposition.thumbnail, it.name, it.type, it.size.toLong())
                    }
                }

                + section(contentStrings.images) {
                    + ZkImagesField(
                        this@ContentEditorForm,
                        AttachedBlobBo.comm,
                        bo.id,
                        disposition = AttachedBlobDisposition.image
                    ) {
                        AttachedBlobBo(EntityId(), bo.id, AttachedBlobDisposition.image, it.name, it.type, it.size.toLong())
                    }
                }

                + section(contentStrings.attachments) {
                    + ZkAttachmentsField(
                        this@ContentEditorForm,
                        AttachedBlobBo.comm,
                        bo.id,
                        disposition = AttachedBlobDisposition.attachment
                    ) {
                        AttachedBlobBo(EntityId(), bo.id, AttachedBlobDisposition.attachment, it.name, it.type, it.size.toLong())
                    }
                }
            }

        }
    }

    override fun setAppTitleBar(contextElements: List<ZkElement>) {
        io {
            titleText = if (::master.isInitialized) {
                "${master.title} : ${LocaleBo.read(bo.locale!!).name}"
            } else {
                "${bo.title} : ${contentStrings.master}"
            }
            super.setAppTitleBar(contextElements)
        }
    }

    override suspend fun onSubmitStart() {
        super.onSubmitStart()

        bo.modifiedAt = Clock.System.now()
        bo.modifiedBy = executor.account.id

        bo.textBlocks = textBlocks.find<ContentTextForm>().map { it.bo }

    }

    override fun validate(submit: Boolean): Boolean {

        var textOk = true
        val commonOk = super.validate(submit)

        textBlocks.find<ContentTextForm>().forEach {
            textOk = textOk && it.validate(submit)
        }

        return commonOk && textOk

    }

}