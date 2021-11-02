# Next

This page contains the changes included in the next release.

## Core

**added**

- `ZkTable` fields:
    - `contentContainer` element for content to get scroll position
    - event handler for `scroll` event on `contentContainer`
    - `contentScrollTop` current vertical scroll of the `contentContainer`
    - `contentScrollLeft` current horizontal scroll of `contentContainer`
- `ZkTable` functions:
    - `rebuild` removes all cached row renders, scrolls the `contentContainer` into position, renders the table
    - `getRowData`
    - `setRowData`
- Direction of Navigation:
    - `ZkNavState` fields: `new`, `forward`, `backward`
    - session storage data: `zk-nav-last-shown`, `zk-nav-counter`
    - `ZkApplication`: `onPopState`, `init` rework
    - see: [Routing](/doc/guides/browser/structure/Routing.md)
    - see: [Direction of Navigation](/doc/cookbook/browser/navigation/direction/recipe.md)
- `TextOverflow` CSS constants

**changed**

- `ZkTableRow.data` is now writable

**fixed**

- singleton (non-factory) ZkArgPage now updates args properly

## Cookbook

**added**

- Direction of Navigation recipe
- Preserve Table State recipe
- cookbook table now remembers search