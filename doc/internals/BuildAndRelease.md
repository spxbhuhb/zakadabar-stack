# Build and Release

## Prerequisites

The build requires the Android 31 SDK installed.
Path to the SDK has to be set in the `local.properties` file:

```text
sdk.dir=/Users/tiz/Library/Android/sdk
```

## Release Branch

- create a release branch: `release-YYYY.MM.DD`
- rename [Next](/doc/changelog/Next.md)` to `YYYY.MM.DD.md`, move to archive
- edit [TOC](/doc/changelog/TOC.md)
    - move current to archive
    - add 'YYYY.MM.DD.md' as current
    - change title in the markdown file from Snapshot to YYYY.MM.DD
- change version numbers in root `build.gradle.kts`

## Build and Publish

```shell
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

```shell
./gradlew site:zkBuild
```

Change version number in DefaultLayout.
Upload to server.

## GitHub

- Create a new release:
   - tag is YYYY.MM.DD
   - changelog is copied from 
- README.md.