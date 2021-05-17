/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import io.ktor.util.*
import kotlinx.browser.window
import kotlinx.coroutines.await
import zakadabar.lib.markdown.frontend.MarkdownView
import zakadabar.stack.data.schema.descriptor.DescriptorDto
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.input.ZkTextInput
import zakadabar.stack.frontend.builtin.toast.dangerToast
import zakadabar.stack.frontend.resources.ZkFlavour
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
        }

    }

    inner class Editor : ZkElement() {

        val descriptor = DescriptorDto("", "", "", emptyList())

        private val packageName = ZkTextInput()
        private val dtoClassName = ZkTextInput()
        private val dtoNamespace = ZkTextInput()

        override fun onCreate() {
            super.onCreate()
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
            } marginBottom 10

            + column {
                + EditorEntry(this@Editor)
                + EditorEntry(this@Editor)

                + ZkButton(flavour = ZkFlavour.Success, iconSource = ZkIcons.add, fill = true, buttonSize = 18) {
                    insertBefore(EditorEntry(this@Editor), findFirst<ZkButton>())
                } marginRight 10 marginBottom 4

                + ZkButton(text = "Generate", flavour = ZkFlavour.Primary) {
                    generate()
                } marginRight 10 marginBottom 4

            }
        }

        private fun generate() {

            descriptor.packageName = packageName.value
            descriptor.kClassName = dtoClassName.value
            descriptor.dtoNamespace = dtoNamespace.value

            classGenerator.descriptor = descriptor
            classGenerator.generators = find<EditorEntry>().mapNotNull { it.generator() }

            val commonSource = classGenerator.dtoGenerator()
            val browserSource = ""
            val backendSource = classGenerator.backendGenerator()

            val result = template
                .replace("@packageName@", descriptor.packageName)
                .replace("commonSource", commonSource)
                .replace("browserSource", browserSource)
                .replace("backendSource", backendSource)

            resultContainer.clear()
            resultContainer += MarkdownView(sourceText = result)
        }
    }
}

class EditorEntry(private val editor: Kodomat.Editor) : ZkElement() {

    private val name = ZkTextInput()
    private val type = ZkTextInput(onChange = ::onTypeChange)

    private var schemaParameterContainer = ZkElement()
    private var schemaParameter: SchemaParameter? = null

    override fun onCreate() {
        super.onCreate()
        + row {

            + div {
                style { alignSelf = "flex-end" }
                + ZkButton(flavour = ZkFlavour.Danger, iconSource = ZkIcons.close, fill = true, buttonSize = 18) {
                    editor -= this@EditorEntry
                } marginRight 10 marginBottom 4
            }

            + div {
                + div { + "Name" } css kodomatStyles.editorLabel
                + name css kodomatStyles.mediumInput
            } marginRight 10

            + div {
                + div { + "Type" } css kodomatStyles.editorLabel
                + type css kodomatStyles.mediumInput
            } marginRight 10

            + schemaParameterContainer
        }

        this marginBottom 10
    }

    private fun onTypeChange(typeName: String) {

        val new = when (typeName.toLowerCase()) {
            "string" -> StringSchemaParameters()
            "boolean" -> BooleanSchemaParameters()
            "recordid" -> RecordIdSchemaParameters()
            else -> null
        }

        when {
            new == null -> {
                schemaParameterContainer -= schemaParameter
                schemaParameter = null
            }
            schemaParameter == null -> {
                schemaParameterContainer += new
                schemaParameter = new
            }
            new::class.isInstance(schemaParameter !!::class) -> Unit
            else -> {
                schemaParameterContainer -= schemaParameter
                schemaParameter = new
            }
        }
    }

    fun generator(): PropertyGenerator? {
        if (name.value.isBlank() && type.value.isBlank()) return null
        schemaParameter?.let { return it.generator(name.value, editor.descriptor) }
        dangerToast { "Type of ${name.value.escapeHTML()} (${type.value.escapeHTML()}) is wrong!" }
        return null
    }
}
