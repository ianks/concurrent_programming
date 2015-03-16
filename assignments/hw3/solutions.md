% Homework 3
% Ian Ker-Seymer
% March 2015

# Instructions for running

1. `cd` in to directory
2. Run the tests: `make test`

# Problem 1

Below are some tables of the results of running the various concurrent list
implementations found in the `ProblemOne/` folder. Each question slowly reduces
the granularity of the locking in order to maximized the time the threads run
in parallel.

Method   | CoarseList | FineGrainList | LazierList | LazyList |
---------|------------|---------------|------------|----------|
Add      | 6ms        | 59ms          | 14ms       | 16ms     |
Remove   | 27ms       | 20ms          | 31ms       | 29ms     |
Contains | 12ms       | 86ms          | 8ms        | 16ms     |

Table: Number of threads: 8, Array size: 200

Method   | CoarseList | FineGrainList | LazierList | LazyList |
---------|------------|---------------|------------|----------|
Add      | 8ms        | 152ms         | 48ms       | 62ms     |
Remove   | 60ms       | 54ms          | 71ms       | 4ms      |
Contains | 27ms       | 188ms         | 48ms       | 19ms     |

Table: Number of threads: 16, Array size: 200

Method   | CoarseList | FineGrainList | LazierList | LazyList |
---------|------------|---------------|------------|----------|
Add      | 40ms       | 186ms         | 193ms      | 29ms     |
Remove   | 135ms      | 198ms         | 57ms       | 11ms     |
Contains | 92ms       | 253ms         | 46ms       | 68ms     |

Table: Number of threads: 32, Array size: 200

Method   | CoarseList | FineGrainList | LazierList | LazyList |
---------|------------|---------------|------------|----------|
Add      | 119ms      | 6323ms        | 1322ms     | 223ms    |
Remove   | 667ms      | 1020ms        | 3222ms     | 237ms    |
Contains | 2325ms     | 1430ms        | 260ms      | 2532ms   |

Table: Number of threads: 8, Array size: 2000

Method   | CoarseList | FineGrainList | LazierList | LazyList |
---------|------------|---------------|------------|----------|
Add      | 341ms      | 1479ms        | 495ms      | 638ms    |
Remove   | 1175ms     | 3998ms        | 673ms      | 602ms    |
Contains | 3078ms     | 2398ms        | 3151ms     | 466ms    |

Table: Number of threads: 16, List length: 2000

Method   | CoarseList | FineGrainList | LazierList | LazyList |
---------|------------|---------------|------------|----------|
Add      | 2306ms     | 8903ms        | 846ms      | 1850ms   |
Remove   | 14990ms    | 8849ms        | 15075ms    | 1251ms   |
Contains | 7615ms     | 22043ms       | 3202ms     | 3251ms   |

Table: Number of threads: 32, Array size: 2000

## Problem 3

A linearizable object is an object in which a single linearization point can
be found. In the IQueue example, there is not a single point where "the
effects of the method call become visible to other method calls." In order
for something to be considered enqueued, two separate instructions are
required:

```java
tail.compareAndSet(slot, slot+1);
items[slot] = x;
```

Because of this, the List can enter an inconsistent state from the viewpoint
of other methods. This is best exemplified with an example.

Imagine you have N threads which call enqueue. All of the threads manage to
call `tail.compareAndSet(slot, slot+1)`; however, only N-1 threads call
`items[slot] = x`. At this point, one slot is filled with `null` while the
others have some value

+---+---+--------+---+---+
| 1 | 8 | _null_ | 9 | 2 |
+---+---+--------+---+---+

Now, imagine that `dequeue()` is called twice before `items[slot] - x`
changes the `null` value.

+--------+---+---+
| _null_ | 9 | 2 |
+--------+---+---+

If `dequeue()` were called again, it would return an `EmptyException()`,
despite the queue containing values. This is the type of inconsistency that
can happen without linearization guarantees.
