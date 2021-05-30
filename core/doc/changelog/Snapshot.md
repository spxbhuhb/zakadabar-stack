## Common

## Added

- InstanceStore: store and look up instances easily
- StringPair: simple bo that holds a string pair
- TranslationProvider: service interface to be implemented for translators

## Backend

### Added

- global `module` function to bind modules easily
- `main`: loads version info from `zkBuild.properties` resource (if exists)  
- Server: `modules` is now an InstanceStore with `first`, `firstOrNull`
- Server.ModuleDependency: delegate to handle module references
- Server.dependencies: list of module dependencies
- Server.settingsLogger: logger for setting load events
- `-t` or `--test` parameter for Server.main, starts Ktor in non-blocking mode
- ActionBusinessLogicBase: single action BL
- QueryBusinessLogicBase: single query BL  
- EmptyEntityBo: placeholder entity bo to keep type safety
- EmptyPersistenceApi: persistence API for action and query BLs
- EmptyAuthorizer: denies everything, for BLs that are used internally
- EmptyRouter: adds no endpoints, for BLs that are used internally  
- DatabaseSettingsBo: debugSql - when true Exposed log level is set to `DEBUG`
- settings namespace parameter is now optional and automatically set to package name when not passed
- When there is no SettingProvider after module load, the server adds the default SettingBl

### Changed

- `zakadabar.stack.server.yaml` renamed to `stack.server.yaml`
- Server.settingsDirectory: moved from companion to instance
- Server.loadSettings: moved from Server to SettingBl
- SettingBackend: moved to setting package and renamed to SettingBl
- server now have a simple module list instead of multiple lists by module type
- KtorRouter: calls apiCacheControl for queries as well
- Setting: if there is no setting backend, returns with the supplied default value
- Executor constructor is now public instad of internal

### Removed

- `zakadabar.server.description.yaml`: server name and default locale moved into `stack.server.yaml`
- SQL storage for settings, SettingDao, SettingTable
- authorize/rules.kt: ruleBl variable, use `module` function instead
- EntityBusinessLogicBase.logger, auditor has its own logger
- CustomBackend: replaced with BackendModule everywhere
- all account and session related code moved into lib:accounts

### Fixed

- AccountPrivateBackend: add table to Sql.tables instead of direct call to SchemaUtils
- LogAuditor uses "anonymous object" logger for anonymous objects

## Frontend

### Added

- TableBigExample: inline table example with 10.000 rows
- ZkAppRouting: `first` function to find routing targets
- global: `target<T>` function to find routing targets
- sidebar: helpers with `method<T>()` syntax for adding targets
- ZkGreenBlueTheme: a variation of light theme
- ZkApplication.services: store general service instances

### Changed

- sidebar: section style change, close icon shows only on mouse over
- sidebar: min width to 220px
- themes: store chosen theme in localStorage instead of sessionStorage
- application: locale comes from: url, executor, serverDescription, parameter (in this order)  
- application: displays an error message when the locale cannot be determined
- application: looks for TranslationProvider to perform translation, when not installed, skips translation
- application: looks for SessionManager to provide sessions, defaults to EmptySessionManager when no other is installed

### Removed

- settings: the current UI is removed, will be back after synthetic forms
- accounts: move all account related UI to 'lib:accounts'
- locales: move all i18n related UI to 'lib:i18n'
- `downloadTranslations` parameter of `ZkApplication.initLocale`, use service instead
- `sessionManager` parameter of `ZkApplication.initSession`, use service instead

### Fixed

- ZkTable: element.scrollIntoView() caused the page to scroll to the table
- ZkSideBarGroup: clicking close to the highlight border does not open the group
- ZkSideBarGroup: hover text color is now properly set
- ZkElement.clear: children remove did not remove all children properly 

## Lib:Accounts

### Added

- ModuleSettings.initialSoPassword: start password for SO
- db initialization when it is empty

### Changed

- move account, role, login, logout from core into this P&P package
- merge principal and account
- migrate all backend modules to the bl/pa concept

## Lib:Demo

### Added

- new demo and test project
- lib:accounts
- automatically creates `zkBuild.properties` in resources

## Lib:Examples

### Added

- SimpleStandaloneAction
- SimpleStandaloneActionBl
- SimpleStandaloneActionTest: unit (+integration) test
- SimpleStandaloneQuery
- SimpleStandaloneQueryBl
- zakadabar.server.description.yaml - with H2, for unit tests
- zakadabar.stack.server.yaml - for unit tests

## Lib:I18N

### Added

- new i18n plug-and-play module
- working locale and translation frontend and backend

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
