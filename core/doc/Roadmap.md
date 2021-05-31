# Roadmap

This roadmap is something we would like to follow. Please note that we cannot guarantee that well able to do so.

## 2021 June

* public release on the 4th
    * discussion topic
    * Kotlin Marketplace
    * https://github.com/AAkira/Kotlin-Multiplatform-Libraries

### Core

* responsive components:
    * table
    * form
    * tab container
* form
    * option to add help tooltip
* popup to the user when the server is unavailable
    * short gap: retry
    * long gap: refresh the page and/or ask the user what to do

### Lib

* `lib:redis` - option to use Redis for session handling

* `lib:search`
    * index static content
    * optionally index data
    * search action for application title bar
    * use Lucene or Solr

### Documentation

* write more about
    * form
    * table
    * pages
    * theme borders and box-shadows

### Site

* search function
* clean up site routing and page structure, now it is a bit confused (/en/GetHelp for example)

## Unscheduled

* synthetic forms (halfway done)
* settings browser UI (halfway, depends on syntethic forms)
* compile and add css classes on-demand (just an idea, but it is possible to manage)
* themes and styles
    * make style sheet switches carry the original class name
* sidebar
    * add keyboard navigation
    * add search function that filters sidebar topics
    * remember state during session
* markdown:
    * add dark / light support for pictures
    * add an icon after titles which let the user add / see comments
    * add an icon before titles to get a permalink
    * improve positioning when opening with a `#`
    * table of contents on the left
        * fix position feedback (from content scroll to TOC)
        * use `a` for toc entries with `#`
        * add action to hide/show on desktop
        * move to title bar on mobile as an action, drop down like the sidebar

## Think about

i18n: restrict translation query before login

