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

* Change identifier syntax from `[a-zA-Z$][a-zA-z0-9\.\-_]*` to `[a-zA-Z_][a-zA-z0-9_]*`
  * Allow starting _. Disallow staring $. Diallow -. Disallow \.

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
