/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.resources

import zakadabar.stack.resources.ZkStringStore


val contentStrings = Strings()

@Suppress("unused")
class Strings : ZkStringStore() {
    val content by "Content"
    val contentOverview by "Content Overview"
    val contentCategoryCrud by "Content Categories"
    val contentStatusCrud by "Content Statuses"

    val thumbnail by "Thumbnail"
    val images by "Images"
    val attachments by "Attachments"
}