/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender.frontend

import kotlinx.browser.window
import kotlinx.datetime.Clock
import zakadabar.lib.bender.ClassGenerator
import zakadabar.lib.bender.KtToBoDescriptor
import zakadabar.lib.bender.frontend.property.BoPropertyEditor
import zakadabar.lib.markdown.frontend.MarkdownView
import zakadabar.lib.markdown.frontend.flavour.ZkMarkdownContext
import zakadabar.stack.data.schema.descriptor.BoDescriptor
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.button.buttonPrimary
import zakadabar.stack.frontend.builtin.input.ZkTextInput
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
    private val template: String
) : ZkElement() {

    val descriptor = BoDescriptor("", "", "", emptyList())

    private val packageName = ZkTextInput()
    private val boName = ZkTextInput()
    private val boNamespace = ZkTextInput()

    private val entryContainer = ZkElement()

    private val lastGenerate = ZkElement()

    private var commonSource = ""
    private var browserSource = ""
    private var blSource = ""
    private var paSource = ""

    override fun onCreate() {
        super.onCreate()

        + column(benderStyles.editor) {

            ! "<h2>Bender</h2>"

            + row {
                + div {
                    + div { + "Package" } css benderStyles.editorLabel
                    + packageName
                } marginRight 10
                + div {
                    + div { + "DTO name" } css benderStyles.editorLabel
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

                    + row {
                        + buttonPrimary("Reset") { reset() } marginRight 20
                        + buttonPrimary("Import") { import() } marginRight 20
                        + buttonPrimary("Generate") { generate() } marginRight 20
                    } marginBottom 20

                    + row {
                        + ZkButton(text = "Common", iconSource = ZkIcons.contentCopy, flavour = ZkFlavour.Primary) {
                            window.navigator.clipboard.writeText(commonSource)
                            toastSuccess { "Common source copied to the clipboard!" }
                        }.hide() marginRight 20

                        + ZkButton(text = "Browser", iconSource = ZkIcons.contentCopy, flavour = ZkFlavour.Primary) {
                            window.navigator.clipboard.writeText(browserSource)
                            toastSuccess { "Browser source copied to the clipboard!" }
                        }.hide() marginRight 20

                        + ZkButton(text = "Business Logic", iconSource = ZkIcons.contentCopy, flavour = ZkFlavour.Primary) {
                            window.navigator.clipboard.writeText(blSource)
                            toastSuccess { "Business Logic source copied to the clipboard!" }
                        }.hide() marginRight 20

                        + ZkButton(text = "Persistence API", iconSource = ZkIcons.contentCopy, flavour = ZkFlavour.Primary) {
                            window.navigator.clipboard.writeText(paSource)
                            toastSuccess { "Persistence API source copied to the clipboard!" }
                        }.hide() marginRight 20

                        + lastGenerate css benderStyles.lastGenerated
                    }

                } marginBottom 20
            }
        }
    }

    private fun reset() {
        io {
            val confirmed = ZkConfirmDialog("Reset Bender", "Are you sure? All fields will be lost.").run()
            if (confirmed) {
                packageName.value = ""
                boName.value = ""
                boNamespace.value = ""

                entryContainer.clear()
                entryContainer += BoPropertyEditor(this@BoEditor)
                entryContainer += BoPropertyEditor(this@BoEditor)
            }
        }
    }

    private fun import() {
        io {
            val source = ImportDialog().run() ?: return@io

            if (source.isEmpty() || ! source.contains("package") || ! source.contains("EntityBoCompanion")) {
                toastInfo { "There is nothing to import." }
                return@io
            }

            val descriptor = try {
                KtToBoDescriptor().parse(source)
            } catch (ex: Exception) {
                log(ex)
                toastDanger { "Error during import, check the browser console." }
                return@io
            }

            packageName.value = descriptor.packageName
            boName.value = descriptor.className
            boNamespace.value = descriptor.boNamespace

            entryContainer.clear()

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
            toastWarning(hideAfter = 3000) { "Please set the DTO name!" }
            go = false
        }

        if (! go) return

        find<ZkButton>().forEach { it.show() } // TODO make generate button hide/show smarter

        lastGenerate.clear()
        lastGenerate.innerText = "Generated at: ${formatInstant(Clock.System.now())}"

        if (descriptor.boNamespace.isEmpty()) {
            toastInfo(hideAfter = 5000) { "Namespace is empty, using ${descriptor.className} as default." }
            descriptor.boNamespace = descriptor.className
        }

        classGenerator.boDescriptor = descriptor
        classGenerator.generators = entryContainer.find<BoPropertyEditor>().mapNotNull { it.generator() }

        commonSource = classGenerator.commonGenerator()
        browserSource = classGenerator.browserFrontendGenerator()
        blSource = classGenerator.businessLogicGenerator()
        paSource = classGenerator.exposedPaGenerator()

        val result = template
            .replace("@packageName@", descriptor.packageName)
            .replace("// commonSource", commonSource)
            .replace("// browserSource", browserSource)
            .replace("// blSource", blSource)
            .replace("// paSource", paSource)

        resultContainer.clear()
        resultContainer += MarkdownView(sourceText = result, context = ZkMarkdownContext(toc = false, hashes = false))
    }
}