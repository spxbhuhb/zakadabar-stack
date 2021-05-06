# Roadmap

## 2021 May

### Core

1. upgrade to latest kotlinx, Ktor, Exposed
1. synthetic forms
1. settings
   1. change settings from browser UI
1. liquibase integration
1. database initialization procedure
1. responsive components:
   1. table
   1. form
   1. tab container
1. toast styles
1. note styles
1. indexing
   * index static content
   * optionally index data record
   * search action for application title bar
   * use Lucene

### Lib

1. markdown:
   1. images
   1. tables
   1. general style revamp
   1. table of contents on the left
      1. fix position feedback
      1. use "a" for toc entries
   1. add ZkElements from markdown
1. integration with Lucene

### Documentation

1. site should serve `*.md` from static
1. convert demo to demo-marina
1. move demo-lib to site

## 2021 June

1. Add option to use Redis for session handling

## Unscheduled

* unit test concept, unit tests for comm
* use common frontend comm code base on Ktor Client
* move ContentBackend from site into the stack, document content backends

### ZkSideBar

* add keyboard navigation
* add search function that filters sidebar topics

## History

### 2021 April

1. load from configuration file
1. update from SQL
