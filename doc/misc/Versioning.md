# Versioning

Zakadabar stack uses the date of the release as version number: YYYY.MM.DD,
where M and D do not contain the leading zeros:

- 2021.5.1
- 2021.12.29

In special cases when an urgent fix is needed, or we provide long-term support
for a specific version, an additional number may be added: YYYY.MM.DD.X

## Deprecations

Deprecations may be introduced at any time. Depending on the weight of the
change we will set and publish the EOL of each deprecation.

## Breaking Changes

We may introduce breaking changes only in the first release of a month. All
other releases will maintain compatibility (hotfixes may be an exception if
really needed).