# Snapshot

## General

* application now remembers theme selection
  * user account may have theme
  * if not, window.localStorage is used
* consistent page styles
  * general pages are scrollable by default
  * general pages may use `ZkPageStyles.fixed` to switch off default scrolling
  * CRUD tables use fixed page (as the table provides scrolling by itself)
  * CRUD forms use scrollable page

## New Properties

* `ZkApplication.themes` - stores the registered themes (set in jsMain/main.kt)
* `ZkTheme.name` - name of the theme
* `AccountPublicDto.theme` - name of the theme this account uses
* `AccountPrivateDto.theme` - name of the theme this account uses
* `AccountPrivateDao.theme` - name of the theme this account uses
* `AccountPrivateTable.theme` - name of the theme this account uses
* `ZkPageStyles.fixed` - new style for non-scrolling page content
* `ZkPageStyles.scrollable` - new style for scrolling page content (this is the default)
* `ZkPageStyles.content` - new style for content with margin

## Changed Properties

* `ZkPage.title` - ZkPageTitle instead of text
* `ZkArgPage.title` - ZkPageTitle instead of text
* `ZkLayoutTheme.paddingStep` renamed to `ZkLayoutTheme.spacingStep`
* `ZkLayoutTheme.marginStep` renamed to `ZkLayoutTheme.spacingStep`

## Removed Properties

* `ZkLayoutStyles` - all non-used
* `ZkArgPage.cssClasses` - replaced with a single-string constructor parameter
* `ZkPage.cssClasses` - replaced with a single-string constructor parameter

## New Behaviour

* `ZkApplication.initTheme` - selects theme: 1) account setting 2) local storage 3) default light
* `main.kt` - should register themes, see site for example

## Changed Behaviour

* `ZkApplication.theme.set` - saves the selected theme to into window.localStorage under key 'zk-theme-name'
* `ZkDefaultLayout.onCreate` - containers now uses css from ZkLayoutStyles
* `ZkCrudTarget` - uses ZkPageStyles.content around forms, uses ZkPageStyles.fixed for tables
* `ZkPage.init` - title is set to the class name translated when not passed to constructor
* `ZkArgPage.init` - title is set to the class name translated when not passed to constructor

## Bugfixes

* `ZkTitleBarStyles.titleContainer` - removed padding
* `ZkDoubleColumn.render` - now displays value

---

## Before the changelog structure change

* Common title bar concept. Added to pages, form and table.
* Refactor of theme and styles, themes demo.
* New landing page for site.
* Zakadabar Design colors to ZkColors.

### ZkApplication

* property `title`, stores the data for top application title bar
* property `onTitleChange`, callback, executed when `title` changes
* property `styleSheets`, stores attached CSS style sheets
* calls `onThemeChange` of all attached style sheeds when the theme changes

### ZkCssStyleSheet

* Remove generic type.
* Add `merge` method.
* Add `onThemeChange` callback.

### ZkDefaultLayout

* Implements the app-handle/sidebar/title-bar/content layout from the demo.
* ZkPageTitle class to store application title bar data.
* ZkAppHandle class to display application name and menu close button.
* ZkAppTitleBar class to display the application title bar.

### ZkArgPage

* Use `ZkApplication.title`.
* Refresh title bar during onResume.

### ZkArgPage

* Use `ZkApplication.title`.
* Refresh title bar during onResume.

### ZkForm

* Use `ZkApplication.title`.
* Refresh title bar during onResume.
* `onConfigure` method to replace `init`

### ZkTable

* Use `ZkApplication.title`.
* Refresh title bar during onResume.
* Actions moved into the `actions` package:
  * ZkAddRowAction
  * ZkExportCsvAction
  * ZkSearchAction

### ZkElement

* New convenience functions:
  * `gridRow` to set CSS grid row placement,
  * `gridColumn` to set CSS grid column placement,
  * `display` to set CSS display property.

### ZkButton

* replace `className=` with `classList +=`.

### ZkIconButton

* `open` class.
* Replace `className=` with `classList +=`.
* Function `onMouseClick`, calls `onClick` by default.

## Deprecated

* `ZkSideBarTitle` - replaced by ZkAppHandle
* `ZkSideBar.title` - replaced by appHandle in ZkDefaultLayout

## Removed

* `ZkForm.titleBar`
* `ZkTableTitleBar`