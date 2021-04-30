/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend

import zakadabar.demo.lib.backend.builtin.BuiltinBackend
import zakadabar.demo.lib.backend.builtin.ExampleReferenceBackend
import zakadabar.site.backend.data.content.ContentBackend
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.data.builtin.account.AccountPrivateBackend
import zakadabar.stack.backend.data.builtin.account.AccountPublicBackend
import zakadabar.stack.backend.data.builtin.principal.PrincipalBackend
import zakadabar.stack.backend.data.builtin.resources.LocaleBackend
import zakadabar.stack.backend.data.builtin.resources.SettingBackend
import zakadabar.stack.backend.data.builtin.resources.TranslationBackend
import zakadabar.stack.backend.data.builtin.role.RoleBackend
import zakadabar.stack.backend.data.builtin.rolegrant.RoleGrantBackend
import zakadabar.stack.backend.data.builtin.session.SessionBackend
import zakadabar.stack.util.PublicApi

@PublicApi
object Module : BackendModule {

    override fun onModuleLoad() {
        Server += RoleBackend
        Server += RoleGrantBackend
        Server += PrincipalBackend
        Server += SessionBackend
        Server += LocaleBackend
        Server += TranslationBackend
        Server += SettingBackend
        Server += AccountPrivateBackend
        Server += AccountPublicBackend

        Server += ContentBackend

        Server += BuiltinBackend
        Server += ExampleReferenceBackend
    }

}