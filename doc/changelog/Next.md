# Next

This page contains the changes included in the next release.

**NOTE** Changes with marker **very low** and such are technically breaking changes. However, they are
not major modifications and in most cases they should not break anything. Notes after the marker
are the places worth to check. If you extended them you may have to apply the change to your own class also.

## Core

**added**

- style specification interfaces, extended by style classes
- option to use the native, browser supplied date editor for LocalDate fields #107

**changed**

- `ZkCssStyleSheet` now implements `CssStyleSpec`
- `zk*Styles` now implement the appropriate style interface **low**
- `ZkAttachmentsField` and `ZkImagesField` now allows click on the area to select files #103

**fixed**

- empty multi-level table bugfix #104
- `ZkAttachmentsField` and `ZkImagesField` now refuses multi-upload when it would go over maximum allowed entries

