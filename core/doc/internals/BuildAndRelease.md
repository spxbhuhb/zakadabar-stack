# Build and Release

## Markdown

Until the bugfix for markdown vs. Kotlin 1.5 is out you have to compile and
publish into Maven local this repo: https://github.com/ajalt/intellij-markdown.git

Switch to the `ak/kt-15` branch, then `gradle publishToMavenLocal`

## Release Branch

- create a release branch: `release-YYYY.MM.DD`
- rename [Snapshot](/doc/changelog/Snapshot.md)` to `YYYY.MM.DD.md`, move to archive
- edit [TOC](/doc/changelog/TOC.md)
    - move current to archive
    - add 'YYYY.MM.DD.md' as current
    - change title in the markdown file from Snapshot to YYYY.MM.DD
- change version numbers in `build.gradle.kts` of all published projects

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