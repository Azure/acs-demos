This container is an experiment in generic microscaling.

How it works
============

A list of containers to monitor is provided.

Periodically the microscaling container will ask each of these
containers if they need to scale. The response to this enquiry is a
value betwwen -100 and +100. 0 indicates no need to scale while +100
is an urgent need to scale up (inidcates the container is
significantly overworked) and -100 indicates the container is
significantly underworked and should be scaled down.

This microscaling container does not care how this value is
calcualted, all it cares about is the value.

The values returned are used to make a decision about whether to scale
up/down or remain unchanged.
