/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.jwt

import com.auth0.jwt.interfaces.DecodedJWT

fun DecodedJWT.getString(name: String) : String? = getClaim(name).asString()

inline fun <reified T> DecodedJWT.getList(name: String) : List<T>? = getClaim(name).asList(T::class.java)
