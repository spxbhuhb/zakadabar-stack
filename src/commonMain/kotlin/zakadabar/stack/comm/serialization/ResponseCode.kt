/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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