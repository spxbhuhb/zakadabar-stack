# Build and Release

## 0.

Until the bugfix for markdown vs. Kotlin 1.5 is out you have to compile and
publish into Maven local this repo: https://github.com/ajalt/intellij-markdown.git

Switch to the `ak/kt-15` branch, then `gradle publishToMavenLocal`

## 1.

```shell
gradle publish
```

## 2.

[OSSH](https://s01.oss.sonatype.org/#welcome)

## 3.

Update README.md.