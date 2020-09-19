/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend

import zakadabar.stack.Stack
import zakadabar.stack.frontend.builtin.i18n
import zakadabar.stack.frontend.extend.FrontendModule
import zakadabar.stack.util.PublicApi

@PublicApi
object Module : FrontendModule() {

    override val uuid = Stack.uuid

    override fun init() {

        FrontendContext += uuid to i18n

//        FrontendContext += Desktop
//        FrontendContext += EntityNavigator
//
//        EntityRecordDto.comm = EntityCache
//
//        FrontendContext += DtoFrontend(
//            Stack.uuid,
//            FolderDto.Companion,
//            iconSource = Icons.folder,
//            newView = NewFolder
//        )
//
//        FrontendContext += DtoFrontend(
//            Stack.uuid,
//            SystemDto.Companion,
//            comm = null,
//            iconSource = Icons.settings
//        )
//
//        FrontendContext += DtoFrontend(
//            Stack.uuid,
//            CommonAccountDto.Companion,
//            comm = null,
//            iconSource = Icons.account_box
//        )

    }

}
