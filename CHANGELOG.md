# 0.6.1 (2022-10-24)

Fixes:

* Avoid NullPointerException (thanks to Tetsuro Sano)

# 0.6.0 (2021-06-10)

Changes:

* Get ready for v0.11 and v1.0 (thanks to hiroyuki-sato)

# 0.5.0 (2017-08-23)

Changes

* Follow new TimestampParser API of embulk >= 0.8.29.
  * Note that embulk-filter-row >= 0.5.0 requires embulk >= 0.8.29.

# 0.4.0 (2017-05-18)

Enhancements

* Use joni library for REGEXP operator to improve performance
  * Note that This may introduce trival incompatibility changes

# 0.3.3 (2016-08-09)

Enhancements

* Support REGEXP operator
* Allow to omit TIMESTAMP keyword

# 0.3.2 (2016-08-08)

Changes

* `conditions` option is now deprecated. Show deprecation warning messages.

Fxies:

* Fix NullPointerException for `where` option

# 0.3.1 (2016-08-08)

Changes:

* Change identifier syntax from `[a-zA-Z$][a-zA-z0-9\.\-_]*` to `[a-zA-Z_][a-zA-z0-9_]*` to allow starting _, disallow staring $, disallow -, disallow . (dot).

# 0.3.0 (2016-08-06)

Enhancements:

* Support SQL-like Syntax

# 0.2.2 (2016-08-05)

Enhancements:

* Support `type: json` (not supporting filtering with json yet, though)

# 0.2.1 (2016-06-27)

Enhancements:

* Add a workaround to treat `0.x` argument as a double which is treated as a String by sanekyaml (thanks to @toyama0919)

# 0.2.0 (2015-12-05)

Enhancements:

* Support OR condition

# 0.1.4

Fixes:

* raise SchemaConfigException if column name is not found

# 0.1.3

Fixes:

* Remove debug print

# 0.1.2

Enhancements:

* Support Java 1.7

# 0.1.1

Enhancements:

* Support `startsWith`, `endsWith`, `contains` string operators

# 0.1.0

first version
