# Build and Release

## Prerequisites

### Android SDK

For a full-build you need an Android SDK installed and the path to the sdk
set in Gradle properties or as an environment variable.

The build does work without the SDK, but the modules that depend on it are
skipped.

Properties:

```text
android.sdk.path=/Users/tiz/Library/Android/sdk/platforms/android-30
```

Environment variable (Unix, Mac OS):

```text
export ANDROID_SDK_PATH=/Users/tiz/Library/Android/sdk/platforms/android-30
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