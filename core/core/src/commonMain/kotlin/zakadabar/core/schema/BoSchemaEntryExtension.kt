/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.schema

/**
 * Schema entry extensions may be used to attach additional data to a BO
 * schema entry. For example the settings module defines an extension
 * that tells the system which environment variable may contain the
 * given setting.
 */
abstract class BoSchemaEntryExtension<T>