/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.runtime.testing

/**
 * A partial DOM [Node](https://developer.mozilla.org/en-US/docs/Web/API/Node)
 * implementation for testing
 *
 * If you want to add something to this class, check Node on MDN first if there
 * is any method/property covers the functionality. If so, use that syntax/semantics.
 */
class TestNode {

    var parentNode: TestNode? = null

    val childNodes = mutableListOf<TestNode>()

    val firstChild: TestNode?
        get() = childNodes.firstOrNull()

    val lastChild: TestNode?
        get() = childNodes.lastOrNull()

    val nextSibling: TestNode?
        get() = parentNode?.let {
            val idx = it.childNodes.indexOf(this)
            check(idx != - 1) { "node is not child of it's own parent" }
            if (idx == it.childNodes.size - 1) null else it.childNodes[idx + 1]
        }

    val previousSibling: TestNode?
        get() = parentNode?.let {
            val idx = it.childNodes.indexOf(this)
            check(idx != - 1) { "node is not child of it's own parent" }
            if (idx == 0) null else it.childNodes[idx-1]
        }

    fun appendChild(aChild : TestNode) {
        aChild.parentNode?.removeChild(aChild)
        aChild.parentNode = this
        childNodes += aChild
    }

    fun insertBefore(newNode : TestNode, referenceNode : TestNode?) {
        if (referenceNode == null) appendChild(newNode)
        newNode.parentNode?.removeChild(newNode)
        val idx = childNodes.indexOf(referenceNode)
        require(idx != -1) { "reference node is not a child of this node"}
        childNodes.add(idx, newNode)
    }

    fun replaceChild(newChild : TestNode, oldChild : TestNode) {
        val idx = childNodes.indexOf(oldChild)
        require(idx != -1) { "reference node is not a child of this node"}
        newChild.parentNode = this
        oldChild.parentNode = null
        childNodes[idx] = newChild
    }

    fun removeChild(child : TestNode) {
        if (!childNodes.remove(child))  throw NoSuchElementException()
        child.parentNode = null
    }

}