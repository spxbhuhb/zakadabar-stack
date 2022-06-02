# Next

This page contains the changes included in the next release.

**NOTE** Changes with marker **very low** and such are technically breaking changes. However, they are
not major modifications and in most cases they should not break anything. Notes after the marker
are the places worth to check. If you extended them you may have to apply the change to your own class also.

## Structure

- CliKt dependency to 3.4.2
- Kaml dependency to 0.44.0
- Logback dependency to 1.2.11

## Core

**added**

- `ZkSideBar.arrowClose` when true, clicking on the text does not close sidebar groups
- `ZkSideBar.arrowSize` changes the size of the sidebar open/close icon size

**changed**

- `LogAuditor` now uses `server.settings.logReads` to check if reads should be logged or not, default is to log
- `ServerSettingsBo.logReads` to log only data modification and do not log list, read, query operations
- `ZkSideBar.arrowOpen` effects only group open, for close use `ZkSideBar.arrowClose` **very low**
- `ZkButton.onClick` now focuses on itself before `event.preventDefault` **very low**

**fixed**

- select dropdown now closes when clicking on a button

## Documentation

**fixed**

- obsoleted information about `Server.logReads`

## Lib:Cookbook

**added**

- SideBar Arrow Options recipe