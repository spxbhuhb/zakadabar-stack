/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.frontend.browser

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementState
import zakadabar.core.browser.application.application
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.crud.ZkCrudEditor
import zakadabar.core.browser.crud.ZkCrudTarget
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.tabcontainer.ZkTabContainer
import zakadabar.core.browser.util.default
import zakadabar.core.browser.util.io
import zakadabar.core.data.EntityId
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.localizedStrings
import zakadabar.lib.blobs.browser.attachment.ZkAttachmentsField
import zakadabar.lib.blobs.browser.image.ZkImagesField
import zakadabar.lib.content.data.*
import zakadabar.lib.content.resources.contentStrings
import zakadabar.lib.i18n.data.LocaleBo

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
                    + bo::status query { StatusBo.all().by { it.name } }
                    if (bo.master == null) + bo::folder

                    if (bo.master == null) {
                        + bo::parent query { FolderQuery().execute().map { it.id to it.title } }
                    }

                    + constString(localizedStrings["parent"]) { if (::parent.isInitialized) parent.title else "" }

                    + bo::locale query { LocaleBo.all().by { it.name } } readOnly true
                    + bo::title.asTextArea()
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
                        comm = AttachedBlobBo.comm,
                        blobCountMax = 1,
                        reference = bo.id,
                        disposition = AttachedBlobDisposition.thumbnail,
                        blobClass = AttachedBlobBo::class
                    )
                }

                + section(contentStrings.images) {
                    + ZkImagesField(
                        this@ContentEditorForm,
                        comm = AttachedBlobBo.comm,
                        reference = bo.id,
                        disposition = AttachedBlobDisposition.image,
                        blobClass = AttachedBlobBo::class
                    )
                }

                + section(contentStrings.attachments) {
                    + ZkAttachmentsField(
                        this@ContentEditorForm,
                        comm = AttachedBlobBo.comm,
                        reference = bo.id,
                        disposition = AttachedBlobDisposition.attachment,
                        blobClass = AttachedBlobBo::class
                    )
                }
            }

        }
    }

    override fun setAppTitleBar(contextElements: List<ZkElement>) {
        io {
            titleText = if (::master.isInitialized) {
                "${master.title} : ${LocaleBo.read(bo.locale !!).name}"
            } else {
                "${bo.title} : ${contentStrings.master}"
            }
            super.setAppTitleBar(contextElements)
        }
    }

    override suspend fun onSubmitStart() {
        super.onSubmitStart()
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