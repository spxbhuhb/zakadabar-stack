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
package zakadabar.stack.frontend.builtin

import zakadabar.stack.frontend.util.Dictionary

val i18n: Dictionary = mutableMapOf(
    "hu" to mutableMapOf(
        "details" to "részletek",
        "loading" to "betöltés",
        "commError" to "kommunikációs hiba:",
        "authError" to "jogosultság hiány",
        "generalError" to "hiba",
        "drop.files.here" to "ide dobhatsz fájlokat feltöltéshez",
        "copyright" to "Copyright © 2020, Simplexion, Magyarország. Minden jog fenntartva.",

        "create" to "Létrehozás",
        "content" to "Tartalom",
        "navigation" to "Navigáció",
        "preview" to "Előnézet",
        "emptyFolder" to "Ez a mappa üres.",
        "load.error" to "kommunikációs hiba to  ",
        "typeToSelect" to "írj ide a választáshoz",
        "typeName" to "írd ide a nevet",

        "modifiedBy" to "Utoljára módosította: ",
        "modifiedAt" to "Utolsó módosítás: ",

        "3a8627/system" to "Rendszer",
        "3a8627/folder" to "Mappa"
    ),
    "en" to mutableMapOf(
        "details" to "details",
        "loading" to "loading",
        "commError" to "kommunikációs hiba to  ",
        "authError" to "jogosultság hiány",
        "generalError" to "hiba",
        "drop.files.here" to "drop files here to upload",
        "copyright" to "Copyright © 2020, Simplexion, Hungary. All rights reserved.",

        "create" to "Create",
        "content" to "Content",
        "navigation" to "Navigation",
        "preview" to "Preview",
        "emptyFolder" to "This folder is empty.",
        "load.error" to "communication error to  ",
        "click.on.something" to "click on the left for preview",
        "typeToSelect" to "type here to select",
        "typeName" to "type the name here",

        "modifiedBy" to "Modified by: ",
        "modifiedAt" to "Last modification: ",

        "3a8627/system" to "System",
        "3a8627/folder" to "Folder"
    )
)