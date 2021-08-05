# Build and Release

## Prerequisites

The build requires the Android 31 SDK installed.
Path to the SDK has to be set in the `local.properties` file:

```text
sdk.dir=/Users/tiz/Library/Android/sdk
```

## Release Branch

- create a release branch: `release-YYYY.MM.DD`
- copy [Next](/doc/changelog/Next.md) into `archive/YYYY.MM.DD.md`
- add release date and delete first content line from `archive/YYYY.MM.DD.md`
- delete current entries from `Next`
- edit [TOC](/doc/changelog/TOC.md)
    - change current to `archive/YYYY.MM.DD.md`
    - add `YYYY.MM.DD.md` to archive
- change version number in `buildSrc/../Versions.kt`

## Build And Publish

```text
./gradlew clean
./gradlew build
./gradlew publish
```

## Release

- [OSSH](https://s01.oss.sonatype.org/#welcome)
    1. Check version packages and version numbers.
    1. Close.
    1. Release.

## Merge

- merge release branch into `develop`
- merge release branch into `master`
- delete release branch

## Site

```text
./gradlew site:zkBuild
```

Upload to server.

## GitHub

- Create a new release:
   - tag is YYYY.MM.DD
   - changelog is copied from 
- README.md.