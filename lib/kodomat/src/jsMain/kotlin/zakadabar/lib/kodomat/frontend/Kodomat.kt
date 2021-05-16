/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.kodomat.frontend

import io.ktor.util.*
import zakadabar.stack.data.schema.descriptor.DescriptorDto
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.input.ZkTextInput
import zakadabar.stack.frontend.builtin.layout.tabcontainer.ZkTabContainer
import zakadabar.stack.frontend.builtin.toast.dangerToast
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.marginBottom

class Kodomat : ZkTabContainer() {

    private val commonCode = ZkElement()
    private val browserCode = ZkElement()
    private val backendCode = ZkElement()

    override fun onCreate() {
        super.onCreate()

        height = "100%"

        + tab(Editor())
        + tab(commonCode, title = "Common")
        + tab(browserCode, title = "Browser")
        + tab(backendCode, title = "Backend")
    }

    inner class Editor : ZkElement() {

        val descriptor = DescriptorDto("", "", "", emptyList())

        private val packageName = ZkTextInput()
        private val dtoClassName = ZkTextInput()
        private val dtoNamespace = ZkTextInput()

        override fun onCreate() {
            super.onCreate()
            + grid {
                gridTemplateColumns = "repeat(6, max-content)"
                gridGap = 20
                + "package"
                + packageName
                + "class"
                + dtoClassName
                + "namespace"
                + dtoNamespace
            } marginBottom 20

            + column {
                + EditorEntry(this@Editor)
                + EditorEntry(this@Editor)

                + ZkButton(iconSource = ZkIcons.addBox, fill = false, border = false) {
                    insertBefore(EditorEntry(this@Editor), findFirst<ZkButton>())
                }
            }
        }

        override fun onPause() {

            descriptor.packageName = packageName.value
            descriptor.kClassName = dtoClassName.value
            descriptor.dtoNamespace = dtoNamespace.value

            val generators = find<EditorEntry>().mapNotNull { it.generator() }

            commonCode.innerText = dtoGenerator(descriptor, generators)
        }
    }
}

class EditorEntry(private val editor: Kodomat.Editor) : ZkElement() {

    private val name = ZkTextInput()
    private val type = ZkTextInput(onChange = ::onTypeChange)

    private var schemaParameter: SchemaParameter? = null

    override fun onCreate() {
        super.onCreate()
        + grid {
            gridTemplateColumns = "repeat(6, max-content)"
            gridGap = 20

            + ZkButton(iconSource = ZkIcons.minimize, fill = false, border = false) {
                editor -= this@EditorEntry
            }

            + name css kodomatStyles.mediumInput
            + type css kodomatStyles.mediumInput
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
                this -= schemaParameter
                schemaParameter = null
            }
            schemaParameter == null -> {
                this += new
                schemaParameter = new
            }
            new::class.isInstance(schemaParameter !!::class) -> Unit
            else -> {
                this -= schemaParameter
                schemaParameter = new
            }
        }
    }

    fun generator(): PropertyGenerator? {
        schemaParameter?.let { return it.generator(name.value, editor.descriptor) }
        dangerToast { "Type of ${name.value.escapeHTML()} (${type.value.escapeHTML()}) is wrong!" }
        return null
    }
}
