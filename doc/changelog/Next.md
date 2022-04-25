# Next

This page contains the changes included in the next release.

**NOTE** Changes with marker **very low** and such are technically breaking changes. However, they are
not major modifications and in most cases they should not break anything. Notes after the marker
are the places worth to check. If you extended them you may have to apply the change to your own class also.

## Core

**changed**

- `ZkTable` 
  - now supports variable row heights, see the Variable Table Row Height recipe
    - this shouldn't be a breaking change, table should work as before when `fixedRowHeight` is true (the default)
  - now partially (sorting doesn't work) supports row multi-level rows
    - this shouldn't be a breaking change, table should work as before when `multiLevel` is false (the default)
  - replace interception observer based virtualization with scroll event based one **middle** (might have bugs)
  - add `renderData` which is derived from `filteredData`, see comments for details **low**

## Cookbook

**added**

- Variable Table Row Height recipe
- Multi Level Table recipe