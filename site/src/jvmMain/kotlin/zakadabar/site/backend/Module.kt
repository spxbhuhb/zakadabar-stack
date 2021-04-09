/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend

import zakadabar.demo.backend.lib.BuiltinBackend
import zakadabar.demo.backend.lib.ExampleReferenceBackend
import zakadabar.site.backend.data.content.ContentBackend
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.data.builtin.account.AccountPrivateBackend
import zakadabar.stack.backend.data.builtin.account.AccountPublicBackend
import zakadabar.stack.backend.data.builtin.principal.PrincipalBackend
import zakadabar.stack.backend.data.builtin.resources.LocaleStringBackend
import zakadabar.stack.backend.data.builtin.resources.SettingStringBackend
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
        Server += LocaleStringBackend
        Server += SettingStringBackend
        Server += AccountPrivateBackend
        Server += AccountPublicBackend

        Server += ContentBackend

        Server += BuiltinBackend
        Server += ExampleReferenceBackend
    }

}