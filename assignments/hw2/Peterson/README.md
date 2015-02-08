# Peterson Tree

_Author: Ian Ker-Seymer_

## What is it

A Peterson Tree is a mechanism for achieving mutual exclusion for n-threads
(where n is a power of two).

## How does it work

First you create a binary tree of [Peterson
Locks](http://en.wikipedia.org/wiki/Peterson%27s_algorithm). Peterson locks can
achieve mutual exclusion for 2 threads per lock. By creating a binary tree of
these locks, we create for 'levels' for which a limited number of threads can
enter. Precisely, each level reduces the number of threads that can enter by
half.

Since we are only implementing these for n threads (where n is a power of 2,
remember!), we can safely assume we have a full binary tree.

![Full binary
tree](http://web.cecs.pdx.edu/~sheard/course/Cs163/Graphics/FullBinary.jpg)

When a thread obtains a lock, it attempts to grab all of the locks starting
from its leaf Peterson lock, all the way to the root of the tree. Only one
thread will be fortunate enough to obtain the root lock, thus all other threads
will be locked out of the critical section.

Mind you, this is mostly an academic exercise to show a method in which mutual
exclusion can be achieved in software. It will be deathly inefficient as we are
spin locking. Think of it as an interesting mental exercise.

## How to run

1. `make`
1. `java TestPetersonTree`
