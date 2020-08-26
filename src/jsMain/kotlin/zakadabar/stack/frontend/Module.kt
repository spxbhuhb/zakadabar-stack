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

package zakadabar.stack.frontend

import zakadabar.stack.Stack
import zakadabar.stack.data.FolderDto
import zakadabar.stack.data.SystemDto
import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.data.security.CommonAccountDto
import zakadabar.stack.frontend.builtin.desktop.Desktop
import zakadabar.stack.frontend.builtin.folder.NewFolder
import zakadabar.stack.frontend.builtin.i18n
import zakadabar.stack.frontend.builtin.icon.Icons
import zakadabar.stack.frontend.builtin.navigator.EntityNavigator
import zakadabar.stack.frontend.comm.util.EntityCache
import zakadabar.stack.frontend.extend.FrontendEntitySupport
import zakadabar.stack.frontend.extend.FrontendModule

object Module : FrontendModule() {

    override val uuid = Stack.uuid

    override fun init() {

        FrontendContext += uuid to i18n

        FrontendContext += Desktop
        FrontendContext += EntityNavigator

        EntityDto.comm = EntityCache

        FrontendContext += FrontendEntitySupport(
            Stack.uuid,
            FolderDto.Companion,
            iconSource = Icons.folder,
            newView = NewFolder
        )

        FrontendContext += FrontendEntitySupport(
            Stack.uuid,
            SystemDto.Companion,
            comm = null,
            iconSource = Icons.settings
        )

        FrontendContext += FrontendEntitySupport(
            Stack.uuid,
            CommonAccountDto.Companion,
            comm = null,
            iconSource = Icons.account_box
        )

    }

}
