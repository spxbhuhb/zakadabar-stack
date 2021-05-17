# Roadmap

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="Plans and Reality">
This roadmap is something we would like to follow but there is <b>no guarantee at all</b>
that we'll be able to do so.
</div>

## 2021 May

* public release
  * cookie policy
  * paid services
  
### Core

* responsive components:
    * table
    * form
    * tab container
* sidebar
    * sections
* popup to the user when the server is unavailable
    * short gap: retry
    * long gap: refresh the page and/or ask the user what to do

### Documentation

* Get Started - make it a code lab
* write more about
    * schema and validation
    * server startup  
    * form
    * table
    * pages
    * theme borders and box-shadows
* migrate "Examples" of site into "Documentation"

## 2021 June

### Core

* Add option to use Redis for session handling

## Unscheduled

* synthetic forms (halfway done)
* settings browser UI (halfway, depends on syntethic forms)
* indexing
  * index static content
  * optionally index data record
  * search action for application title bar
  * use Lucene
* unit test concept, unit tests for comm
* use common frontend comm code base on Ktor Client
* compile and add css classes on-demand (just an idea, but it is possible to manage)
* themes and styles
  * make theme.onResume style changes non-cumulative (save style variables?)
  * make style sheet switches carry the original class name
* sidebar
  * add keyboard navigation
  * add search function that filters sidebar topics
  * remember state during session
* markdown:
  * space between parts (margins are wrong here and there)
  * add dark / light support for pictures
  * add an icon after titles which let the user add / see comments
  * add an icon before titles to get a permalink
  * improve positioning when opening with a `#`
  * table of contents on the left
    * fix position feedback (from content scroll to TOC)
    * use `a` for toc entries with `#`
    * add action to hide/show on desktop
    * move to title bar on mobile as an action, drop down like the sidebar


