## Backend

### Added

- global `module` function to get other modules easily
- Server: `first`, `firstOrNull` functions to find modules
- Server.ModuleDependency: delegate to handle module references
- Server.dependencies: list of module dependencies

### Changed

- server now have a simple module list instead of multiple lists by module type
- KtorRouter: calls apiCacheControl for queries as well

### Removed

- authorize/rules.kt: ruleBl variable, use `module` function instead

## Frontend

### Added

- TableBigExample: inline table example with 10.000 rows

### Changed

- sidebar: section style change, close icon shows only on mouse over
- sidebar: min width to 220px
- themes: store chosen theme in localStorage instead of sessionStorage

### Fixed

- ZkTable: element.scrollIntoView() caused the page to scroll to the table
- ZkSideBarGroup: clicking close to the highlight border does not open the group
- ZkSideBarGroup: hover text color is now properly set

## Lib:Markdown

### Changed

- markdown style tuning

## Site

### Added

- ProjectStatus page

### Changed

- content of Welcome
- move SiteMarkdownContext out of pages package
- show "ALPHA" next to the version  

## Documentation

### Changed

- backend/Modules
