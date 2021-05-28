/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.backend

import zakadabar.lib.examples.backend.builtin.SiteBuiltinBackend
import zakadabar.lib.examples.backend.builtin.SiteExampleReferenceBackend
import zakadabar.lib.examples.backend.data.SimpleExampleBl
import zakadabar.stack.backend.BackendModule
import zakadabar.stack.backend.authorize.UnsafeAuthorizer
import zakadabar.stack.backend.custom.ContentBackend
import zakadabar.stack.backend.data.builtin.account.AccountPrivateBackend
import zakadabar.stack.backend.data.builtin.account.AccountPublicBackend
import zakadabar.stack.backend.data.builtin.principal.PrincipalBackend
import zakadabar.stack.backend.data.builtin.resources.LocaleBackend
import zakadabar.stack.backend.data.builtin.resources.SettingBackend
import zakadabar.stack.backend.data.builtin.resources.TranslationBackend
import zakadabar.stack.backend.data.builtin.role.RoleBackend
import zakadabar.stack.backend.data.builtin.rolegrant.RoleGrantBackend
import zakadabar.stack.backend.data.builtin.session.SessionBackend
import zakadabar.stack.backend.server
import zakadabar.stack.util.PublicApi

@PublicApi
object Module : BackendModule {

    override fun onModuleLoad() {
        server += RoleBackend
        server += RoleGrantBackend
        server += PrincipalBackend
        server += SessionBackend
        server += LocaleBackend
        server += TranslationBackend
        server += SettingBackend
        server += AccountPrivateBackend
        server += AccountPublicBackend

        server += ContentBackend()

        server += SiteBuiltinBackend
        server += SiteExampleReferenceBackend

        UnsafeAuthorizer.enabled = true
        server += SimpleExampleBl()
    }

}