/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.desktop.navigator

import zakadabar.stack.data.entity.EntityRecordDto
import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.desktop.navigator.NavigatorClasses.Companion.navigatorClasses
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.input.Input
import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.launch

abstract class NewEntityItemWithName(
    private val newEntity: NewEntity
) : ZkElement() {

    private var changed = false
    private val input = Input(::onEnter, newEntity::close, ::onChange, placeholder = t("typeName"))
    private val nextIcon = Icons.arrowRight.complex18(::onNext)

    override fun init(): NewEntityItemWithName {
        super.init()

        className = coreClasses.w100Row

        this += input.withClass(coreClasses.grow)
        this += nextIcon.withClass(navigatorClasses.newEntityActionIcon).hide()

        return this
    }

    override fun focus(): NewEntityItemWithName {
        input.focus()
        return this
    }

    private fun onChange(s: String) {
        changed = true
        if (s.isNotBlank()) nextIcon.show() else nextIcon.hide()
    }

    private fun onNext() = next(input.value)

    private fun onEnter(s: String) = next(s)

    fun next(s: String) {

        if (s.isBlank() || ! changed) return

        launch {
            create(newEntity.parentDto, s)
        }

        if (! newEntity.repeat) {
            newEntity.close()
        } else {
            changed = false
        }
    }

    abstract suspend fun create(parentDto: EntityRecordDto?, name: String)
}
