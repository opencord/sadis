# Subscriber / Access Device Information Service (SADIS)

When deploying at a customer there is often a need to access per-subscriber /
access device information. This information is authoritatively stored by the
customer and made available to the residential CORD or VOLTHA deployment. The
purpose of this service is to define an optional service that provides a bridge
to the customer's infrastructure to query / cache subsciber/access information
and make it available to other services / applications within the ONOS/CORD
infrastructure.

### Overview

The below diagram is a high level block diagram for the Access Information
Service that runs as an ONOS application and will be used within the CORD/vOLTHA
context by other ONOS applications such as the DHCP Relay, iGMP, and AAA. The
basic concept is that the service can run in two modes: local mode - in local
mode the per subscriber / access device information can be established via
ONOS's network configuration capability. In this mode all queries are answered
based on the information from the network configuration remote mode - in the
remove mode the service is configured with a URL and this URL is used to query
an external source for subscriber / access device information using a URI
structure that is defined as part of this service. Information retrieved from
the external source is cached locally.

![test](overview.png)

### Pull Through Cache

The cache used for the remote mode is considered a pull through cache in which
all queries are answered from the cache and if there is a cache miss the remote
API is used and the cache is populated.

To prevent the cache from growing indefinitely a policy, initially time/use
based, will be leveraged to kick / purge items from the cache. Additionally, the
cache can be influenced via manually operations of the the CLI/API exposed as
part of ONOS. The time limits for cache entry purging should be configurable.

### Service Configuration

The service is configurable via both the the network configuration as well as
via ONOS configuration. This configuration consists of service-wide properties
such as operation mode, remote URL, cache purge options, etc.

### Clustered Behavior

The in memory cache is not a clustered, distributed data structure, such that
each instance of ONOS in a cluster might have a different set of objects in its
cache. The thought behind this is that each instance in a cluster will be a
master for a different set of devices and thus needs different information.
