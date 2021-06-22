# Roadmap

This roadmap is something we would like to follow. Please note that we cannot guarantee that well able to do so.

## 2021 June

### Core

* upgrade to Ktor 1.6.0
* minor improvements
* date and time editors for the frontend
* form
     * option to add help tooltip
  
### Lib: Content

* implementation of a SEO-ready, multi-language content backend and admin frontend

### Documentation

* write more about
    * form
    * table
    * pages
  
## Unscheduled

### Core

* scheduler: schedule and run backend tasks in a multi-node environment
* responsive components:
  * table
  * form (already works, but that's just happy coincidence)
  * tab container
* form
  * provide feedback on invalid fields from the schema
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

### Site

* search function
* clean up site routing and page structure, now it is a bit confused (/en/GetHelp for example)

### Uncategorized

* synthetic forms (halfway done)
* settings browser UI (depends on syntethic forms)
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

