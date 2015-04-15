# Report

## Trials

| BarrierArray |
|--------------|
| 0.083781     |
| 0.084117     |
| 0.084388     |
| 0.084447     |
| 0.084594     |
| 0.084697     |
| 0.086087     |
| 0.088752     |
| 0.090773     |
| 0.093241     |
| 0.093892     |
| 0.096322     |
| 0.103533     |
| 0.103704     |
| 0.106448     |
| 0.106899     |
| 0.109710     |
| 0.113847     |

Table: Avg = 0.0944s

| BarrierCounter |
|----------------|
| 0.092458       |
| 0.107523       |
| 0.109609       |
| 0.111809       |
| 0.115008       |
| 0.115184       |
| 0.118276       |
| 0.118333       |
| 0.119280       |
| 0.119649       |
| 0.126556       |
| 0.129875       |
| 0.130622       |
| 0.130840       |
| 0.134857       |
| 0.137904       |
| 0.144906       |
| 0.147741       |

Table: Avg = 0.1228s

## Explanation

From the results of the experiment, BarrierArray performs significantly better
than BarrierCounter by a margin of over 20%. The reason this happens is because
in the Counter implementation, there is a single shared resource that must be
locked in order to ensure safe/correct behavior. The benefit of this method is
that it requires constant memory usage; only one variable is needed to store
the barrier.

The BarrierArray does not require any locks, since writes to a slot can only
happen from a single writer. This allows writes to be done in parallel; in
contrast to the sequential bottleneck experienced in the BarrierCounter. The
trade-off is that the array requires linear memory, at least one boolean
register per thread. However, in real life, this is rarely an issue.
