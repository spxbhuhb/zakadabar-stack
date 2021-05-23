# Versioning

Zakadabar stack uses the date of the release as version number: YYYY.MM.DD,
where M and D do not contain the leading zeros:

- 2021.5.1
- 2021.12.29

In special cases when an urgent fix is needed, or we provide long-term support
for a specific version, an additional number may be added: YYYY.MM.DD.X

## Why Not Semantic

Shortly put: it is too much trouble and in most cases it is basically useless.

**OpenBSD**

I started to use OpenBSD when they've been somewhere at `3.6`, if I recall
correctly. That was in 2004. Now they are at `6.8`, incremented the version
number by `0.1` every half year without fail.

**Java**

I've never heard of Java 2.0.0. Now the increment the major version number
in every half year. To be honest, I've lost track what specific Java versions
change and why. I use mostly Kotlin, JVM just runs my Kotlin programs.

**Kotlin**

What does `1` doing here?

1.0 - 2016 Feb
1.2 - 2017 Nov
1.3 - 2018 Oct
1.4 - 2020 Aug
1.5 - 2021 May

### Conclusion

Many major software packages changed their public versioning to something that
is related to time. Of the three part of the semantic version usually just one
contains important information, the other two is mostly noise.
