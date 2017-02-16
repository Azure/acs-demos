This is a simulation of a low priority batch workload that is to be
run in spare capacity in the cluster. The scale health request will
indicate that it should be scaled down if any container, other than
itself, in the cluster is in a `waiting` state. If there is no
container in a waiting state then it will indicate a desire to scale
up.

This will result in the batch workload scaling up until there is no
capacity left in the cluster.'
