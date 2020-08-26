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

package zakadabar.stack.frontend.builtin.desktop

import zakadabar.stack.frontend.FrontendContext.t
import zakadabar.stack.frontend.builtin.desktop.DesktopClasses.Companion.desktopClasses
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.extend.ViewContract
import zakadabar.stack.util.PublicApi
import zakadabar.stack.util.UUID

@PublicApi
class DesktopFooter : ComplexElement() {

    companion object : ViewContract() {

        override val uuid = UUID("fc03ac6c-260c-4fec-997a-7ce26bd14c85")

        override val target = Desktop.footer

        override fun newInstance() = DesktopFooter()

    }

    override fun init(): ComplexElement {
        super.init()

        className = desktopClasses.footer
        innerHTML += """<div class="${desktopClasses.copyright}">${t("copyright")}</div>"""

        return this
    }

}