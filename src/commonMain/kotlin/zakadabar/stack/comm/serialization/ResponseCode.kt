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

package zakadabar.stack.comm.serialization

enum class ResponseCode {
    /**
     * All is well.
     */
    OK,

    /**
     * Requested resource does not exists (or is hidden).
     */
    NOT_FOUND,

    /**
     * Internal server error, should notify IT / customer service.
     */
    INTERNAL_SERVER_ERROR,

    /**
     *  The message type is not known (for this type of session).
     */
    UNKNOWN_MESSAGE_TYPE,

    /**
     *  An open session message is expected but got something else.
     */
    OPEN_EXPECTED,

    /**
     *  A non-open session message on a closed session.
     */
    SESSION_IS_CLOSED,

    /**
     * The requested function is defined but not yet implemented.
     */
    NOT_IMPLEMENTED,

    /**
     * The authentication during session open has been failed.
     */
    AUTHENTICATION_FAILED,

    /**
     * The position request failed because there was no parent to position on.
     */
    NO_PARENT,

    /**
     * A session tries to close a snapshot without having a lock on it.
     */
    MISSING_LOCK,

    /**
     * Fetching content and the request revision number is higher than the current entity revision number.
     */
    NOT_AVAILABLE_YET,

    /**
     * Fetch content contains a data size larger than the allowed.
     */
    REQUEST_SIZE_LIMIT

}