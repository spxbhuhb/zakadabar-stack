/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.navigator

import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.desktop.navigator.NavigatorClasses.Companion.navigatorClasses
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.input.Input
import zakadabar.stack.frontend.data.DtoFrontend
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.ZkElement

/**
 * Entity creation from the global navigator. Displays list of entity types that
 * may be created as child of the given parent.
 *
 * Pay attention to root when the parent is null.
 */
class NewEntity(
    private val navigator: EntityNavigator,
    val parentDto: EntityRecordDto?
) : ZkElement() {

    private val header = NewEntityHeader(this)

    private val inputAndActions = ZkElement()
    private val nextIcon = Icons.arrowRight.complex18(::next)
    private val input = Input(::onEnter, ::close, ::onChange, placeholder = t("typeToSelect"))

    private val typeList = ZkElement()

    internal var selected: DtoFrontend<*>? = null

    var repeat = false

    override fun init(): ZkElement {
        super.init()

        className = navigatorClasses.newEntity

        this += header

        this += inputAndActions.withClass(coreClasses.w100Row)

        inputAndActions += input.withClass(coreClasses.grow)
        inputAndActions += nextIcon.withClass(navigatorClasses.newEntityActionIcon)

        nextIcon.hide()

        this += typeList.withClass(coreClasses.w100) // TODO some scrolling and height limit would be nice

        FrontendContext.dtoFrontends.forEach {
//            if (it.value.hasCreate) {
            typeList += NewEntityItem(this, it.value)
            //}
        }

        input.value = ""

        return this
    }

    override fun focus() = input.focus()

    fun next() {
        if (selected == null) return

        this -= inputAndActions
        this -= typeList

        val entityBuilder = selected?.createView()
        this += entityBuilder
        entityBuilder?.focus()
    }

    fun close() {
        navigator -= this
    }

    private fun onEnter(@Suppress("UNUSED_PARAMETER") s: String) = next()

    private fun onChange(s: String) {
        val trimmed = s.trim().toLowerCase()
        var count = 0

        var lastShown: DtoFrontend<*>? = null

        typeList.childElements.forEach {
            if (it is NewEntityItem) {
                if (trimmed.isEmpty() || it.support.displayName.toLowerCase().contains(trimmed)) {
                    it.show()
                    count ++
                    lastShown = it.support
                } else {
                    it.hide()
                }
            }
        }

        selected = if (trimmed.isNotEmpty() && count == 1) {
            nextIcon.show()
            lastShown
        } else {
            nextIcon.hide()
            null
        }
    }

}