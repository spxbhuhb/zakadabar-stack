/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

package zakadabar.stack.frontend.builtin.note

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.resources.ZkFlavour

fun primaryNote(title: String, text: String) = ZkNote(ZkFlavour.Primary, title, text)
fun secondaryNote(title: String, text: String) = ZkNote(ZkFlavour.Secondary, title, text)
fun successNote(title: String, text: String) = ZkNote(ZkFlavour.Success, title, text)
fun warningNote(title: String, text: String) = ZkNote(ZkFlavour.Warning, title, text)
fun dangerNote(title: String, text: String) = ZkNote(ZkFlavour.Danger, title, text)
fun infoNote(title: String, text: String) = ZkNote(ZkFlavour.Info, title, text)

fun primaryNote(title: String, content: ZkElement) = ZkNote(ZkFlavour.Secondary, title, content)
fun secondaryNote(title: String, content: ZkElement) = ZkNote(ZkFlavour.Secondary, title, content)
fun successNote(title: String, content: ZkElement) = ZkNote(ZkFlavour.Success, title, content)
fun warningNote(title: String, content: ZkElement) = ZkNote(ZkFlavour.Warning, title, content)
fun dangerNote(title: String, content: ZkElement) = ZkNote(ZkFlavour.Danger, title, content)
fun infoNote(title: String, content: ZkElement) = ZkNote(ZkFlavour.Info, title, content)
