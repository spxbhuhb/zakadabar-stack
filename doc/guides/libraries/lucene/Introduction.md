# Library: Lucene

A very simple module to use Lucene as a search engine. 

As of know, the module allows search for the public and indexing for the
security-officer.

Also, the module only indexes ".md" files.

## Setup

To use this module in your application:

1. add the gradle dependency,
2. if you build a shadow jar, add `mergeServiceFiles`
3. add the module to your server configuration, for details see [Modules](../../common/Modules.md)
4. add the configuration file to the `etc` directory

**gradle**

```kotlin
implementation("hu.simplexion.zakadabar:lucene:$contentVersion")
```

For building shadow jars the `mergeServiceFiles` is necessary.

```kotlin
tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    mergeServiceFiles()
}
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

## Troubleshooting

If you see this exception, you haven't added `mergeServiceFiles` to `build.gradle.kts` (see above).

```text
2022-04-06 10:23:33.338 [DefaultDispatcher-worker-5    ] ERROR LuceneBl - lucene index failed
java.lang.IllegalArgumentException: An SPI class of type org.apache.lucene.codecs.PostingsFormat with name 'Lucene90' does not exist.  You need to add the corresponding JAR file supporting this SPI to your classpath.  The current classpath supports the following names: [IDVersion]
        at org.apache.lucene.util.NamedSPILoader.lookup(NamedSPILoader.java:113)
        at org.apache.lucene.codecs.PostingsFormat.forName(PostingsFormat.java:111)
        at org.apache.lucene.codecs.perfield.PerFieldPostingsFormat$FieldsReader.<init>(PerFieldPostingsFormat.java:324)
        at org.apache.lucene.codecs.perfield.PerFieldPostingsFormat.fieldsProducer(PerFieldPostingsFormat.java:391)
        ...
```
