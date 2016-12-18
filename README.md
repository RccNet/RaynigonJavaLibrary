# RaynigonJavaLibrary
Build Status: [![Build Status](https://travis-ci.org/raynigon/RaynigonJavaLibrary.svg?branch=master)](https://travis-ci.org/raynigon/RaynigonJavaLibrary)
CI Best Practice: [![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/522/badge)](https://bestpractices.coreinfrastructure.org/projects/522)


[JavaDoc Link](http://rccnet.de/javadoc/rayjavalib/index.html)

[Download Binary](http://rccnet.de/index.php/downloads/download/4-java/2-ray-java-lib)

##Release
Released Lib Version: **0.1.3**

Compatibly to Java: **1.8.0**

##Release (Java 1.7)
Released Lib Version: **0.0.2**

Compatibly to Java: **1.7.0**

##Maven

```
<dependency>
	<groupId>com.raynigon</groupId>
	<artifactId>RayCommons</artifactId>
	<version>0.1.2</version>
</dependency>
```

```
<repositories>
	<repository>
		<releases>
			<enabled>true</enabled>
			<updatePolicy>always</updatePolicy>
		</releases>
		<id>Raynigon.com</id>
		<name>Raynigon.com</name>
		<url>http://www.maven.raynigon.com/</url>
		<layout>default</layout>
	</repository>
</repositories>
```

##Examples
###Event Handling
###IOUtils
###Database Connectors
###Rest Service
##Versions
### Version 0.1.1
Release Date: 20.01.2016

Features:
- Powerfull Encrypting Engine
- Database Connector Collection
- Event API, for custom Events accross the whole Application
- Hashing Collection
- IOUtils for Stream conversion, e.g. stream -> stream, string -> stream...
- JSON Mapper, creates Objects from JSON and also JSON from Objects
- Byte Conversion

### Version 0.1.2
Release Date: 12.05.2016

Features:
- Byte Conversion, fast implementation
- ZIP Compression

### Version 0.1.3
Planned ReleaseData: 18.12.2016

Planned Features:
- [x] JSON Refactoring
- [x] JSON Custom Field Converters

### Version 0.1.4
Planned Release Date: December 2017

Planned Features:
- [ ] Extends Event Handling with customs Threads (Costumizeable with the EventHandler and a Lambda)
- [ ] Math min, max with arrays e.g. RayMath.min(int a, int b, int... values)
- [ ] implement parts of the C++ algorithm library in Java
 - [x] std::find_if
 - [ ] std::sort
 - [ ] std::copy_if
 - [ ] std::fill
 - [x] std::remove
 - [ ] Math min, max with arrays

### Version 0.1.5
