# Roadmap

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="Plans and Reality">
This roadmap is something we would like to follow but there is <b>no guarantee at all</b>
that we'll be able to do so.
</div>

## 2021 May

### Core

* dark mode
    * table
    * tab container
* synthetic forms (halfway done)
* settings (halfway, depends on syntethic forms)
    * change settings from browser UI
* liquibase integration
    * database initialization procedure
* responsive components:
    * table
    * form
    * tab container
* indexing
    * index static content
    * optionally index data record
    * search action for application title bar
    * use Lucene
* sidebar
    * sections
    * add `item` helper with a ZkTarget parameter to build `a` tag - DONE
* popup to the user when the server is unavailable
    * short gap: retry
    * long gap: refresh the page and/or ask the user what to do
* optimize for SEO (https://developers.google.com/search/docs/advanced/guidelines/get-started)

### Lib

* markdown:
    * handle internal links internally, without page reload - DONE
    * table of contents on the left
        * fix position feedback - ACCEPTABLE
        * add scrolling - DONE
* integration with Lucene

### Documentation

* Get Started - make it a code lab
* write more about
    * form
    * table
    * pages
    * frontend routing concept - DONE
    * theme borders and box-shadows
* finish the marina demo
* migrate "Examples" of site into "Documentation"

## 2021 June

### Core

* Add option to use Redis for session handling

### Lib

* markdown:
    * add support for # links
    * space between parts (margins are wrong here and there)
    * better integration with built-in components - write note content in markdown
    * add dark / light support for markdown pictures
    * add an icon after titles which let the user add / see comments
    * table of contents on the left
        * fix position feedback
        * use "a" for toc entries
        * add action to hide/show on desktop
        * move to title bar on mobile as an action, drop down like the sidebar
        * make it smaller when possible

## Unscheduled

* unit test concept, unit tests for comm
* use common frontend comm code base on Ktor Client
* move ContentBackend from site into the stack, document content backends
* compile and add css classes on-demand (just an idea, but it is possible to manage)
* show error message when user hits unknown route on UI - DONE, back is broken tho
* themes and styles
  * make theme.onResume style changes non-cumulative (save style variables?)
  * make style sheet switches carry the original class name
* sidebar
  * add keyboard navigation
  * add search function that filters sidebar topics
  * use "a" for entries when possible (part of SEO optimization) - DONE
  * remember state during session


