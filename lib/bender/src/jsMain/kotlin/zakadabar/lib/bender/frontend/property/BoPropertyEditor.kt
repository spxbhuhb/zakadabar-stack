/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender.frontend.property

import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import zakadabar.lib.bender.PropertyGenerator
import zakadabar.lib.bender.frontend.BoEditor
import zakadabar.lib.bender.frontend.benderStyles
import zakadabar.core.schema.descriptor.*
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.input.ZkTextInput
import zakadabar.core.browser.toast.toastDanger
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIcons
import zakadabar.core.browser.util.marginRight
import zakadabar.core.browser.util.newInstance
import kotlin.reflect.KClass

class BoPropertyEditor(
    private val editor: BoEditor
) : ZkElement() {

    class Type(
        val name: String,
        val constraintEditorClass: KClass<out PropertyConstraintsEditor>,
        val boBropertyClass: KClass<out BoProperty>
    )

    companion object {
        // type guessing goes over this list in the order entries are defined here
        val typeMap = listOf(
            Type("boolean", BooleanPropertyConstraintsEditor::class, BooleanBoProperty::class),
            Type("string", StringPropertyConstraintsEditor::class, StringBoProperty::class),
            Type("double", DoublePropertyConstraintsEditor::class, DoubleBoProperty::class),
            Type("enum", EnumPropertyConstraintsEditor::class, EnumBoProperty::class),
            Type("reference", EntityIdPropertyConstraintsEditor::class, EntityIdBoProperty::class),
            Type("int", IntPropertyConstraintsEditor::class, IntBoProperty::class),
            Type("instant", InstantPropertyConstraintsEditor::class, InstantBoProperty::class),
            Type("localdate", LocalDatePropertyConstraintsEditor::class, LocalDateBoProperty::class),
            Type("localdatetime", LocalDateTimePropertyConstraintsEditor::class, LocalDateTimeBoProperty::class),
            Type("long", LongPropertyConstraintsEditor::class, LongBoProperty::class),
            Type("secret", SecretPropertyConstraintsEditor::class, SecretBoProperty::class),
            Type("uuid", UuidPropertyConstraintsEditor::class, UuidBoProperty::class)
        )
    }

    val name = ZkTextInput()
    private val typeName = ZkTextInput(onChange = ::onTypeChange)

    private var schemaParameterContainer = ZkElement()
    private var constraintsEditor: PropertyConstraintsEditor? = null

    constructor(editor: BoEditor, property: BoProperty) : this(editor) {
        name.value = property.name

        val type = typeMap.firstOrNull { it.boBropertyClass.isInstance(property) }
        if (type == null) {
            toastDanger { "unknown property type: ${property::class.simpleName}" }
            return
        }

        typeName.value = type.name
        constraintsEditor = type.constraintEditorClass.newInstance()
        schemaParameterContainer += constraintsEditor
        constraintsEditor?.update(property)
    }

    override fun onCreate() {
        super.onCreate()
        + row {

            + div {
                style { alignSelf = "flex-end" }
                + ZkButton(flavour = ZkFlavour.Danger, iconSource = ZkIcons.close, fill = true, buttonSize = 18, tabIndex = - 1) {
                    editor.entryContainer -= this@BoPropertyEditor
                } marginRight 10 marginBottom 4
            }

            + div {
                //+ div { + "Name" } css benderStyles.editorLabel
                + name css benderStyles.largeInput
            } marginRight 10

            + div {
                //+ div { + "Type" } css benderStyles.editorLabel
                + typeName css benderStyles.mediumInput
            } marginRight 10

            + schemaParameterContainer
        }

        typeName.input.addEventListener("keydown", { event: Event ->
            event as KeyboardEvent
            if (event.key == "Tab" && constraintsEditor == null) {
                guessType()
            }
        })

        this marginBottom 10
    }

    private fun guessType() {
        if (typeName.value.isEmpty()) return

        val lcv = typeName.value.lowercase()
        val guess = typeMap.firstOrNull { it.name.startsWith(lcv) }?.name ?: return

        typeName.value = guess
        onTypeChange(guess)
    }

    private fun onTypeChange(input: String) {

        val new = typeMap.firstOrNull { it.name == input }?.constraintEditorClass?.newInstance()

        when {
            new == null -> {
                schemaParameterContainer -= constraintsEditor
                constraintsEditor = null
            }
            constraintsEditor == null -> {
                schemaParameterContainer += new
                constraintsEditor = new
            }
            new::class.isInstance(constraintsEditor !!::class) -> Unit
            else -> {
                schemaParameterContainer -= constraintsEditor
                constraintsEditor = new
            }
        }
    }

    fun generator(): PropertyGenerator? {
        if (name.value.isBlank() && typeName.value.isBlank()) return null
        constraintsEditor?.let { return it.generator(name.value, editor.descriptor) }
        toastDanger { "Type of ${name.value.replace("<", "&lt;")} (${typeName.value.replace("<", "&lt;")}) is wrong!" }
        return null
    }
}