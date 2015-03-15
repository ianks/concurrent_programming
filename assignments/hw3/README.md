# Homework 3

_Author: Ian Ker-Seymer_

## Instructions for running

1. `cd` in to directory

2. Compile the code
  - `make`

3. Run the tests

## Filter lock vs Peterson tree

| Threads       | Peterson Tree | Filter Lock                        |
| ------------- |:-------------:|:----------------------------------:|
| 8             | 55 ms         | 155 ms                             |
| 64            | 9780 ms       | Unknown (Too long for my computer) |

## Questions

1. na

2. na

3. A linearizable object is an object in which a single linearization point can
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

   |   |   |        |   |   |
   |:-:|:-:|:------:|:-:|:-:|
   | 1 | 8 | _null_ | 9 | 2 |

   Now, imagine that `dequeue()` is called twice before `items[slot] = x`
   changes the `null` value.

   |        |   |   |
   |:------:|:-:|:-:|
   | _null_ | 9 | 2 |

   If `dequeue()` were called again, it would return an `EmptyException()`,
   despite the queue containing values. This is the type of inconsistency that
   can happen without linearization guarantees.
