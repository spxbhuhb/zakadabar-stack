# Library: Lucene

A very simple module to use Lucene as a search engine. 

As of know, the module allows search for the public and indexing for the
security-officer.

Also, the module only indexes ".md" files.

## Setup

To use this module in your application:

1. add the gradle dependency,
2. add the module to your server configuration, for details see [Modules](../../common/Modules.md)
3. add the configuration file to the `etc` directory

**gradle**

```kotlin
implementation("hu.simplexion.zakadabar:lucene:$contentVersion")
```

**backend**

```kotlin
zakadabar.lib.lucene.install()
```

**configuration file**

Copy [lib.lucene.yaml](/lib/lucene/template/test/lib.lucene.yaml) into the `etc`
directory of your backend. 

Corresponding settings BO: [LuceneSettings](/lib/lucene/src/commonMain/kotlin/zakadabar/lib/lucene/data/LuceneSettings.kt)

## Use

The module provides two functions through its API:

- [LuceneQuery](/lib/lucene/src/commonMain/kotlin/zakadabar/lib/lucene/data/LuceneQuery.kt) to perform queries
- [UpdateIndex](/lib/lucene/src/commonMain/kotlin/zakadabar/lib/lucene/data/UpdateIndex.kt) to perform an index update

## Database
Lucene builds an index that is stored in files. Path to the index is
stored in the settings. Default is `var/index`.