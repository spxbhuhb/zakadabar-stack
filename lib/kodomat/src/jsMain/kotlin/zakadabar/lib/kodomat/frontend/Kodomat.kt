/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import io.ktor.util.*
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.datetime.Clock
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import zakadabar.lib.markdown.frontend.MarkdownView
import zakadabar.lib.markdown.frontend.flavour.ZkMarkdownContext
import zakadabar.stack.data.schema.descriptor.DescriptorDto
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.input.ZkTextInput
import zakadabar.stack.frontend.builtin.toast.toastDanger
import zakadabar.stack.frontend.builtin.toast.toastInfo
import zakadabar.stack.frontend.builtin.toast.toastSuccess
import zakadabar.stack.frontend.builtin.toast.toastWarning
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkFormatters.formatInstant
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.frontend.util.marginRight

class Kodomat(
    private val classGenerator: ClassGenerator,
    private val templateUrl: String,
) : ZkElement() {

    private val resultContainer = ZkElement()
    lateinit var template: String

    override fun onCreate() {
        super.onCreate()

        + Editor()
        + resultContainer

        io {
            template = window.fetch(templateUrl).await().text().await()
            resultContainer += MarkdownView(sourceText = template, context = ZkMarkdownContext(toc = false, hashes = false))
        }

    }

    inner class Editor : ZkElement() {

        val descriptor = DescriptorDto("", "", "", emptyList())

        private val packageName = ZkTextInput()
        private val dtoClassName = ZkTextInput()
        private val dtoNamespace = ZkTextInput()

        private val entryContainer = ZkElement()

        private val lastGenerate = ZkElement()

        private var commonSource = ""
        private var browserSource = ""
        private var backendSource = ""

        override fun onCreate() {
            super.onCreate()

            ! "<h2>Kod-o-mat</h2>"

            + row {
                + div {
                    + div { + "Package" } css kodomatStyles.editorLabel
                    + packageName
                } marginRight 10
                + div {
                    + div { + "DTO name" } css kodomatStyles.editorLabel
                    + dtoClassName
                } marginRight 10
                + div {
                    + div { + "Namespace" } css kodomatStyles.editorLabel
                    + dtoNamespace
                } marginRight 10
            } marginBottom 20

            + column {

                + entryContainer marginBottom 10

                entryContainer += EditorEntry(this@Editor)
                entryContainer += EditorEntry(this@Editor)

                + ZkButton(flavour = ZkFlavour.Success, iconSource = ZkIcons.add, fill = true, buttonSize = 18) {
                    val new = EditorEntry(this@Editor)
                    entryContainer += new
                    new.name.focus()
                } marginBottom 20

                + row {
                    + ZkButton(text = "Generate", flavour = ZkFlavour.Primary) {
                        generate()
                    } marginRight 20

                    + ZkButton(text = "Common", iconSource = ZkIcons.contentCopy, flavour = ZkFlavour.Primary) {
                        window.navigator.clipboard.writeText(commonSource)
                        toastSuccess { "Common source copied to the clipboard!"}
                    }.hide() marginRight 20

                    + ZkButton(text = "Browser", iconSource = ZkIcons.contentCopy, flavour = ZkFlavour.Primary) {
                        window.navigator.clipboard.writeText(browserSource)
                        toastSuccess { "Browser source copied to the clipboard!"}
                    }.hide() marginRight 20

                    + ZkButton(text = "Backend", iconSource = ZkIcons.contentCopy, flavour = ZkFlavour.Primary) {
                        window.navigator.clipboard.writeText(backendSource)
                        toastSuccess { "Backend source copied to the clipboard!"}
                    }.hide() marginRight 20

                    + lastGenerate css kodomatStyles.lastGenerated

                } marginBottom 20

            }
        }

        private fun generate() {

            var go = true

            descriptor.packageName = packageName.value
            descriptor.kClassName = dtoClassName.value
            descriptor.dtoNamespace = dtoNamespace.value

            if (descriptor.packageName.isEmpty()) {
                toastWarning(hideAfter = 3000) { "Please set the package name!" }
                go = false
            }

            if (descriptor.kClassName.isEmpty()) {
                toastWarning(hideAfter = 3000) { "Please set the DTO name!" }
                go = false
            }

            if (! go) return

            find<ZkButton>().forEach { it.show() } // TODO make generate button hide/show smarter

            lastGenerate.clear()
            lastGenerate.innerText = "Generated at: ${formatInstant(Clock.System.now())}"

            if (descriptor.dtoNamespace.isEmpty()) {
                toastInfo(hideAfter = 5000) { "Namespace is empty, using ${descriptor.kClassName} as default." }
                descriptor.dtoNamespace = descriptor.kClassName
            }

            classGenerator.descriptor = descriptor
            classGenerator.generators = entryContainer.find<EditorEntry>().mapNotNull { it.generator() }

            commonSource = classGenerator.commonGenerator()
            browserSource = classGenerator.browserFrontendGenerator()
            backendSource = classGenerator.exposedBackendGenerator()

            val result = template
                .replace("@packageName@", descriptor.packageName)
                .replace("// commonSource", commonSource)
                .replace("// browserSource", browserSource)
                .replace("// backendSource", backendSource)

            resultContainer.clear()
            resultContainer += MarkdownView(sourceText = result, context = ZkMarkdownContext(toc = false, hashes = false))
        }
    }
}

class EditorEntry(private val editor: Kodomat.Editor) : ZkElement() {

    val name = ZkTextInput()
    private val type = ZkTextInput(onChange = ::onTypeChange)

    private var schemaParameterContainer = ZkElement()
    private var entryDetails: EntryDetails? = null

    override fun onCreate() {
        super.onCreate()
        + row {

            + div {
                style { alignSelf = "flex-end" }
                + ZkButton(flavour = ZkFlavour.Danger, iconSource = ZkIcons.close, fill = true, buttonSize = 18, tabIndex = -1) {
                    editor -= this@EditorEntry
                } marginRight 10 marginBottom 4
            }

            + div {
                //+ div { + "Name" } css kodomatStyles.editorLabel
                + name css kodomatStyles.largeInput
            } marginRight 10

            + div {
                //+ div { + "Type" } css kodomatStyles.editorLabel
                + type css kodomatStyles.mediumInput
            } marginRight 10

            + schemaParameterContainer
        }

        type.input.addEventListener("keydown", { event: Event ->
            event as KeyboardEvent
            if (event.key == "Tab" && entryDetails == null) {
                guessType()
            }
        })

        this marginBottom 10
    }

    private fun guessType() {
        if (type.value.isEmpty()) return

        val lcv = type.value.toLowerCase()

        val guess = when {
            "boolean".startsWith(lcv) -> "boolean"
            "double".startsWith(lcv) -> "double"
            "enum".startsWith(lcv) -> "enum"
            "int".startsWith(lcv) -> "int"
            "instant".startsWith(lcv) -> "instant"
            "long".startsWith(lcv) -> "long"
            "recordid".startsWith(lcv) -> "recordid"
            "string".startsWith(lcv) -> "string"
            "secret".startsWith(lcv) -> "boolean"
            "uuid".startsWith(lcv) -> "uuid"
            else -> null
        } ?: return

        type.value = guess
        onTypeChange(guess)
    }

    private fun onTypeChange(input: String) {

        val new = when (input.toLowerCase()) {
            "boolean" -> BooleanDetails()
            "double" -> DoubleDetails()
            "enum" -> EnumDetails()
            "instant" -> InstantDetails()
            "int" -> IntDetails()
            "long" -> LongDetails()
            "recordid" -> RecordIdDetails()
            "secret" -> SecretDetails()
            "string" -> StringDetails()
            "uuid" -> UuidDetails()
            else -> null
        }

        when {
            new == null -> {
                schemaParameterContainer -= entryDetails
                entryDetails = null
            }
            entryDetails == null -> {
                schemaParameterContainer += new
                entryDetails = new
            }
            new::class.isInstance(entryDetails !!::class) -> Unit
            else -> {
                schemaParameterContainer -= entryDetails
                entryDetails = new
            }
        }
    }

    fun generator(): PropertyGenerator? {
        if (name.value.isBlank() && type.value.isBlank()) return null
        entryDetails?.let { return it.generator(name.value, editor.descriptor) }
        toastDanger { "Type of ${name.value.escapeHTML()} (${type.value.escapeHTML()}) is wrong!" }
        return null
    }
}
