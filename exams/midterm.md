# Notes

## Ch 3: Linearizability

A guarantee about single operations on single objects. It provides a real-time
(i.e., wall-clock) guarantee on the behavior of a set of single operations
(often reads and writes) on a single object (e.g., distributed register or data
item)

- What if I told you that my definition of correctness was that there was no
  data races?
  - You can have data races but yet things can still be correct. i.e. all of
    the algorithms which didn't use locks.

- Queiescient consistency:
  - execution is equivalent to some to some execution of seq calls
  - composable

- Sequential consistency:

  - method calls should appear to take effect in program order
  - not composable

## Ch. 4: Registers

How far can we get without hardware support?

### Boolean

- Safe register SRSW
  - not necesarily atomic; for overlapping method calls the reader can return
    anything because the underlying components are not safe
  - can return any value in the registers allowed range of values

- Regular registers
  - MRSW where writes do not happen atomically
  - reads may 'flicker' between old and new value

- Atomic registers

- Atomic snapshots

## Ch 5

What you _cannot_ do without hardware support. To achieve some things, you do
need hardware support for CAS.

- No queue from registers
- No consensus from simple registers

- Assume by contradiction you have queue by registers...
  - If you have queue, then you have consensus. Red ball; black ball.

- CAS gives you consensus.
  - One thread succeeds in writing its value, that is used as consensus value.

## Ch 7

- TAS lock and TTAS lock
    - both are deadlock free and provide mutual exclusion
    - TAS forces delays all threads (even those not on the lock), and forces
    - them to discard their cached copies of the lock. They must fetch a new
      value from the bus everytime.
- Anderson lock: allows to spin on different values (memory locations)
    - have to allocate in advance
    - have to know maximmum number of threads in advance
- CLH lock

## Ch 8: Monitors

Use them to implement partial queue, RW lock, semaphore

## Ch 10: Queues

- Unbounded total queue
- Bounded partial queue
