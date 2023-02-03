/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.jwt

import com.auth0.jwt.interfaces.DecodedJWT

val DecodedJWT.upn : String? get() = getString("upn")

val DecodedJWT.name : String? get() = getString("name")

val DecodedJWT.cn : String? get() = getString("commonname")

val DecodedJWT.email : String? get() = getString("email")

val DecodedJWT.roles : Set<String>? get() = getArray("roles")?.toSet()

private fun DecodedJWT.getString(name: String) : String? = getClaim(name).asString()

private fun DecodedJWT.getArray(name: String) : Array<String>? = getClaim(name).asArray(String::class.java)
