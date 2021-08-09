# Introduction: Site

The `site` sub-project contains the source code of the website [https://zakadabar.io](https://zakadabar.io).

Almost all content of the site comes from the `doc` directory of the main project.

Structure of `doc` is quite self-explanatory.

Points of interest:

- `/doc/guides/TOC.md` contains table of contents for the guides
- [Cookbook](/doc/guides/site/Cookbook.md)
- [SiteMarkdownContext](/site/src/jsMain/kotlin/zakadabar.site.frontend/SiteMarkdownContext.kt)

## Future Plans

I would like to make the site into a general documentation server which can be used 
for other projects with some customization.

Before that I would like to add these features:

- general search (probably with Sol8r)
- API docs (get from maven central)
- cookbook: work from the git repo and pull whenever it changes
- multiple versions (now it shows only the latest)
- better rendering for different screen sizes
    - toc and sidebar width
    - toc should be accessible on mobile
- better positioning for hashtag navigation

Other features that would be nice to have:

- cookbook should fetch the source codes from git and display them in-line
- ability to register and comment pages
- show last modification time of pages and the list of contributors
