# Next

This page contains the changes included in the next release.

**NOTE** Changes with marker **very low** and such are technically breaking changes. However, they are
not major modifications and in most cases they should not break anything. Notes after the marker
are the places worth to check. If you extended them you may have to apply the change to your own class also.

## Core

**added**

- `ZkTitleBarStyles.localTitleBarText` CSS parameter
- `ZkModalBase.addButtons` property - hides the button row completely
- `ZkModalBase.launch` function - launches the dialog without waiting for close
- `ZkTable` counter option, see "Table With Counter" recipe
- handling of multiple modals **very low** `(ZkModalBase, ZkModalContainer)`

**changed**

- `ZkTitleBarStyles.iconButton` now uses `appTitleBarText` CSS parameter for icon fill, **very low** `(icons of the title bar)`
- `ZkFieldStyles.selectOptionPopup` - z-index is now 2000 to allow use in modals **very low** `form selects`
- `ZkModalBase.run` - does not call `application.modals.hide()` (moved the call to ZkModalContainer)

**fixed**

- removed 2 pixel left padding from sidebar arrow
- title bar text color now uses `appTitleBarText` / `localTitleBarText` **low** `(themes, title bar styles)`

## Site

**fixed**

- read only fields example does not set opt booelan to read only

## Cookbook

**added**

- new recipe: Form In a Modal
- new recipe: Table With Counter