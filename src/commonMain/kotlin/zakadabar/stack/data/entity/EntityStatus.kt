/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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