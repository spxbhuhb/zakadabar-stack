## Backend

### Added

- global `module` function to get other modules easily
- Server: `first`, `firstOrNull` functions to find modules
- Server.ModuleDependency: delegate to handle module references
- Server.ModuleDependency: delegate to handle module references
- Server.dependencies: list of module dependencies
- `-t` or `--test` parameter for Server.main, starts Ktor in non-blocking mode
- ActionBusinessLogicBase: single action BL
- QueryBusinessLogicBase: single query BL  
- EmptyEntityBo: placeholder entity bo to keep type safety
- EmptyPersistenceApi: persistence API for action and query BLs

### Changed

- server now have a simple module list instead of multiple lists by module type
- KtorRouter: calls apiCacheControl for queries as well
- Setting: if there is no setting backend, returns with the supplied default value

### Removed

- authorize/rules.kt: ruleBl variable, use `module` function instead
- EntityBusinessLogicBase.logger, auditor has its own logger
- CustomBackend: replaced with BackendModule everywhere

### Fixed

- AccountPrivateBackend: add table to Sql.tables instead of direct call to SchemaUtils
- LogAuditor uses "anonymous object" logger for anonymous objects

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

## Lib:Examples

### Added

- SimpleStandaloneAction
- SimpleStandaloneActionBl
- SimpleStandaloneActionTest: unit (+integration) test
- SimpleStandaloneQuery
- SimpleStandaloneQueryBl
- zakadabar.server.description.yaml - with H2, for unit tests
- zakadabar.stack.server.yaml - for unit tests

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