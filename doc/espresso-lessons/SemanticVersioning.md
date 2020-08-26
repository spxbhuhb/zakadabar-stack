# Why This Silly "Continuous Versioning" Stuff

Long time ago I've been a big-big fan of semantic versioning.

It contains information, so logically structured, what a great idea.

Nowadays I simply don't look at version numbers, they are like labels.
There is a dependency, there is a webpage, cut and paste.

Oh, they released a new version? Okay, cut and paste that version number.

Actually, there is only one piece of real information - for me - in the
semantic version number: the major version.

That is actually important because it means that time shown that the
original design decisions made become outdated and there is a need to
completely rewire that software.

I know that it is a more complex, but that's the basic idea. If the 
original design can handle the additions there is no need to break
things, right? So, you can just go with 1.234.56 as version number.

So, only the first number is important, and when that changes I expect
a big piece of work to upgrade to the next major version.

All that said, what will I do if we realize that some major changes needed: 

zakadabar-stack-v2 ... Hah! You did not see that coming, did you? :D

Anyway, there are a few projects which go on with a similar approach:

* OpenBSD releases each spring and autumn, incrementing their minor by 1
* IDEA spash screen shows: "2020.2" and there is a "2020.2.1".
* Oracle Java ... 15 is coming

It is no secret that I love OpenBSD. I started to use it 20 years ago, and it
helped me realize that this whole versioning thing is quite simple actually.

If you are disciplined and willing to work, that is.





