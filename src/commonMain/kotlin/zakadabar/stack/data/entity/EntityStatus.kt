/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.entity

enum class EntityStatus {
    /**
     * The entity will be referenced by another in the close future. If that does
     * not happen it may be removed. Used for multi-part uploads where the files
     * are uploaded before the parent entity is created.
     */
    Pending,

    /**
     * Marks entities which should not be shown in the entity tree by default because
     * they are used for internal system functions and the user usually has nothing
     * to do with them. For example: ACL lists and role grants should not be shown
     * in the entity tree by default.
     */
    System,

    Preparation,
    Active,
    Hidden,
    Archived,
    MarkedForDelete
}