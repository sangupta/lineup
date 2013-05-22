LineUp
======

LineUp is a simple in-memory high-throughput message queue service for Java. It can be used as:

1. As a standalone service accessible via REST services
2. Embedded inside an application

Features
--------
1. In-memory and high-throughput
2. Three different queue types
  a) Default: all messages are unique
  b) Reject Duplicates: reject all duplicate messages
  c) Priority: accept all duplicates and merge them to increase their priority
3. Accesible via REST API
4. Amazon SQS API compliant

**NOTE:** The library is currently under development and may not be stable or may not support all features.

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
