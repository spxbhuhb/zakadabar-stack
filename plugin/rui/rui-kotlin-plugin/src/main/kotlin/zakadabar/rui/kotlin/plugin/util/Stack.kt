/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.rui.kotlin.plugin.util

/**
 * Stack as type alias of Mutable List
 * Credit: https://stackoverflow.com/a/61724278/3796844
 */
typealias Stack<T> = MutableList<T>

/**
 * Pushes item to [Stack]
 * @param item Item to be pushed
 */
fun <T> Stack<T>.push(item: T) = add(item)

/**
 * Pops (removes and return) last item from [Stack]
 * @return item Last item
 */
fun <T> Stack<T>.pop(): T = removeAt(lastIndex)

/**
 * Peeks (return) last item from [Stack]
 * @return item Last item if [Stack] is not empty
 * @throws IndexOutOfBoundsException when the stack is empty
 */
fun <T> Stack<T>.peek(): T = this[lastIndex]