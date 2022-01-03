/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.other

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.other.ZkGeneratedProfilePicture

class ExampleGeneratedProfilePicture: ZkElement() {

    override fun onCreate() {
        super.onCreate()

        + ZkGeneratedProfilePicture("FirstName LastName")
    }
}