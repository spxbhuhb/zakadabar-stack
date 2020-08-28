/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.extend

import zakadabar.stack.frontend.elements.ComplexElement

/**
 * Use [ScopedViewContract] when the given view may be used more than once on
 * the same frontend AND it is linked to a local data scope.
 *
 * For example: to make it is possible to have two document editors open at the
 * same time in the same window the components used by the editor have to know
 * which editor they belong to. So, they use a scope which in this case is the
 * editor instance itself.
 */
abstract class ScopedViewContract : ViewContract() {

    /**
     * Create a view in the given scope.
     */
    abstract fun newInstance(scope: Any?): Any

    /**
     * Create a new instance as a [ComplexElement] in the given scope.
     */
    fun newElement(scope: Any?) = newInstance(scope) as ComplexElement

    /**
     * By default scoped views do not to support un-scoped use, so
     * [newInstance] without a scope throws NotImplementedError.
     */
    override fun newInstance(): Any {
        throw NotImplementedError()
    }

}