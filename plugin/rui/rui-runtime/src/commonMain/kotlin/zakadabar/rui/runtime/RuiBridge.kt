/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime

/**
 * Fragments that handle the underlying UI implementation such as DOM, implement
 * this interface to make a bridge between the Rui fragment and the UI element.
 *
 * @param BT Type of the underlying receiver this bridge handles.
 *           `org.w3c.dom.Node` for example.
 *
 * @property  receiver  The element of the underlying UI that is connected to
 *                      the Rui fragment by this bridge.
 */
interface RuiBridge<BT> {

    val receiver: BT

    fun add(child: RuiBridge<BT>)

    fun replace(oldChild: RuiBridge<BT>, newChild: RuiBridge<BT>)

    fun remove(child: RuiBridge<BT>)

}