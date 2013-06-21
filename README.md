LineUp
======

LineUp is a simple in-memory high-throughput message queue service for Java. It can be used as:

* As a standalone service accessible via REST services
* Embedded inside an application

Features
--------

* In-memory and high-throughput
* Three different queue types
 * Default: all messages are allowed, including duplicates
 * Reject Duplicates: reject all duplicate messages
 * Priority: accept all duplicates and merge them to increase their priority
* Accesible via REST API
* Amazon SQS API compliant

**NOTE:** The library is currently under development and may not be stable or may not support all features.

Builds
------

**0.1.0**

* Initial release
* Allows access of queues via REST or in embedded mode

Dependencies
------------

The `lineup` project is dependent on the following projects:

* `jerry` for utility classes and methods
* `jersey` for accessing over REST
* `grizzly` for providing a container when run from command line
* `xstream` for serialization and deserialization of objects when accessing over REST

Versioning
----------

For transparency and insight into our release cycle, and for striving to maintain backward compatibility, 
`lineup` will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.

License
-------
	
Copyright (c) 2013, Sandeep Gupta

The project uses various other libraries that are subject to their
own license terms. See the distribution libraries or the project
documentation for more details.

The entire source is licensed under the Apache License, Version 2.0 
(the "License"); you may not use this work except in compliance with
the LICENSE. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
