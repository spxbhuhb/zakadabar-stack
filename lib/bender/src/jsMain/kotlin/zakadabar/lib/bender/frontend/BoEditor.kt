/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender.frontend

import kotlinx.browser.window
import kotlinx.datetime.Clock
import zakadabar.lib.bender.ClassGenerator
import zakadabar.lib.bender.EntityIdPropertyGenerator
import zakadabar.lib.bender.KtToBoDescriptor
import zakadabar.lib.bender.PropertyGenerator
import zakadabar.lib.bender.frontend.property.BoPropertyEditor
import zakadabar.lib.markdown.frontend.MarkdownModal
import zakadabar.lib.markdown.frontend.MarkdownView
import zakadabar.lib.markdown.frontend.flavour.ZkMarkdownContext
import zakadabar.stack.data.schema.descriptor.BoDescriptor
import zakadabar.stack.data.schema.descriptor.EntityIdBoProperty
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.button.buttonPrimary
import zakadabar.stack.frontend.builtin.input.ZkTextInput
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.modal.ZkConfirmDialog
import zakadabar.stack.frontend.builtin.toast.toastDanger
import zakadabar.stack.frontend.builtin.toast.toastInfo
import zakadabar.stack.frontend.builtin.toast.toastSuccess
import zakadabar.stack.frontend.builtin.toast.toastWarning
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkFormatters.formatInstant
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.log
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.frontend.util.marginRight

class BoEditor(
    private val classGenerator: ClassGenerator,
    private val resultContainer: ZkElement,
    private val template: String,
    private val markdownContext: () -> ZkMarkdownContext
) : ZkElement() {

    var descriptor = BoDescriptor("", "", "", emptyList())

    private val packageName = ZkTextInput()
    private val boName = ZkTextInput()
    private val boNamespace = ZkTextInput()

    val entryContainer = ZkElement()

    private val copyContainer = ZkElement()
    private val lastGenerate = ZkElement()

    private var commonSource = ""
    private var browserSource = ""
    private var blSource = ""
    private var paSource = ""

    override fun onCreate() {
        super.onCreate()

        + column(benderStyles.editor) {

            + row(benderStyles.header) {
                + h2 { + "Bender" } marginRight 10

                // these links are pretty dirty, but i don't want to think about it right now

                + ZkButton(
                    "documentation",
                    ZkFlavour.Custom,
                    url = "/en/Documentation/guides/tools/Bender.md",
                    capitalize = false,
                ) css benderStyles.headerLink

                + ZkButton(
                    "change log",
                    ZkFlavour.Custom,
                    capitalize = false
                ) {
                    io {
                        MarkdownModal(
                            title = "Bender ChangeLog",
                            url = "/api/content/guides/tools/BenderChanges.md",
                            context = ZkMarkdownContext(toc = false)
                        ).run()
                    }
                } css benderStyles.headerLink
            }

            + row {
                + buttonPrimary("Reset") { reset() } marginRight 20
                + buttonPrimary("Import") { import() } marginRight 20
                + buttonPrimary("Generate") { generate() } marginRight 20
                + lastGenerate css benderStyles.lastGenerated
            } marginBottom 30

            + row {
                + div {
                    + div { + "Package" } css benderStyles.editorLabel
                    + packageName css benderStyles.extraLargeInput
                } marginRight 10
                + div {
                    + div { + "BO name" } css benderStyles.editorLabel
                    + boName
                } marginRight 10
                + div {
                    + div { + "Namespace" } css benderStyles.editorLabel
                    + boNamespace
                } marginRight 10
            } marginBottom 20

            + column {

                + entryContainer marginBottom 10

                entryContainer += BoPropertyEditor(this@BoEditor)
                entryContainer += BoPropertyEditor(this@BoEditor)

                + ZkButton(flavour = ZkFlavour.Success, iconSource = ZkIcons.add, fill = true, buttonSize = 18) {
                    val new = BoPropertyEditor(this@BoEditor)
                    entryContainer += new
                    new.name.focus()
                } marginBottom 20

                + column {

                    + copyContainer build {

                        + zkLayoutStyles.hidden
                        + zkLayoutStyles.row

                        + ZkButton(text = "Common", iconSource = ZkIcons.contentCopy, flavour = ZkFlavour.Primary) {
                            window.navigator.clipboard.writeText(commonSource)
                            toastSuccess { "Common source copied to the clipboard!" }
                        } marginRight 20

                        + ZkButton(text = "Browser", iconSource = ZkIcons.contentCopy, flavour = ZkFlavour.Primary) {
                            window.navigator.clipboard.writeText(browserSource)
                            toastSuccess { "Browser source copied to the clipboard!" }
                        } marginRight 20

                        + ZkButton(text = "Persistence API", iconSource = ZkIcons.contentCopy, flavour = ZkFlavour.Primary) {
                            window.navigator.clipboard.writeText(paSource)
                            toastSuccess { "Persistence API source copied to the clipboard!" }
                        } marginRight 20

                        + ZkButton(text = "Business Logic", iconSource = ZkIcons.contentCopy, flavour = ZkFlavour.Primary) {
                            window.navigator.clipboard.writeText(blSource)
                            toastSuccess { "Business Logic source copied to the clipboard!" }
                        } marginRight 20
                    }

                } marginBottom 20
            }
        }
    }

    private fun reset() {
        io {
            val confirmed = ZkConfirmDialog("Reset Bender", "Are you sure? All fields will be lost.").run()
            if (confirmed) {
                doReset()
                entryContainer += BoPropertyEditor(this@BoEditor)
                entryContainer += BoPropertyEditor(this@BoEditor)
            }
        }
    }

    private fun doReset() {
        packageName.value = ""
        boName.value = ""
        boNamespace.value = ""

        descriptor = BoDescriptor("", "", "", emptyList())

        entryContainer.clear()
        copyContainer.hide()

        resultContainer.clear()
    }

    private fun import() {
        io {

            val source = ImportDialog().run() ?: return@io

            if (source.isEmpty() || ! source.contains("package") || ! source.contains("EntityBoCompanion")) {
                toastInfo { "There is nothing to import." }
                return@io
            }

            doReset()

            val descriptor = try {
                KtToBoDescriptor().parse(source)
            } catch (ex: Exception) {
                log(ex)
                toastDanger { "Error during import, check the browser console." }
                return@io
            }

            packageName.value = descriptor.packageName.let { if (it.endsWith(".data")) it.substringBeforeLast(".data") else it }
            boName.value = descriptor.className
            boNamespace.value = descriptor.boNamespace

            descriptor.properties.forEach {
                entryContainer += BoPropertyEditor(this, it)
            }
        }
    }


    private fun generate() {

        var go = true

        descriptor.packageName = packageName.value
        descriptor.className = boName.value
        descriptor.boNamespace = boNamespace.value

        if (descriptor.packageName.isEmpty()) {
            toastWarning(hideAfter = 3000) { "Please set the package name!" }
            go = false
        }

        if (descriptor.className.isEmpty()) {
            toastWarning(hideAfter = 3000) { "Please set the BO name!" }
            go = false
        }

        if (! go) return

        lastGenerate.clear()
        lastGenerate.innerText = "Generated at: ${formatInstant(Clock.System.now())}"

        if (descriptor.boNamespace.isEmpty()) {
            toastInfo(hideAfter = 5000) { "Namespace is empty, using ${descriptor.className} as default." }
            descriptor.boNamespace = descriptor.className
        }

        val generators = entryContainer.find<BoPropertyEditor>().mapNotNull { it.generator() }.toMutableList()
        if (generators.firstOrNull { it.property.name == "id" } == null) {
            addId(generators)
        }

        classGenerator.boDescriptor = descriptor
        classGenerator.generators = generators


        commonSource = classGenerator.commonGenerator()
        browserSource = classGenerator.browserFrontendGenerator()
        blSource = classGenerator.businessLogicGenerator()
        paSource = classGenerator.exposedPaGenerator()

        val result = template
            .replace("@packageName@", descriptor.packageName.replace(".", "/"))
            .replace("className", descriptor.className)
            .replace("businessLogicName", classGenerator.businessLogicName)
            .replace("browserCrudName", classGenerator.browserCrudName)
            .replace("// commonSource", commonSource)
            .replace("// browserSource", browserSource)
            .replace("// blSource", blSource)
            .replace("// paSource", paSource)

        resultContainer.clear()
        resultContainer += MarkdownView(sourceText = result, context = ZkMarkdownContext(toc = false, hashes = false))

        copyContainer.show()
    }

    private fun addId(generators: MutableList<PropertyGenerator>) {
        generators.add(
            0, EntityIdPropertyGenerator(
                descriptor,
                EntityIdBoProperty(
                    name = "id",
                    optional = false,
                    constraints = emptyList(),
                    kClassName = descriptor.className,
                    defaultValue = null,
                    value = null
                )
            )
        )
    }
}