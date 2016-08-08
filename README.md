# Row filter plugin for Embulk

[![Build Status](https://secure.travis-ci.org/sonots/embulk-filter-row.png?branch=master)](http://travis-ci.org/sonots/embulk-filter-row)

A filter plugin for Embulk to filter out rows

## Configuration

* **condition**: AND or OR (string, default: AND).
* **conditions**: select only rows which matches with conditions.
  * **column**: column name (string, required)
  * **operator** operator (string, optional, default: ==)
    * boolean operator
      * ==
      * !=
    * numeric operator (long, double, Timestamp)
      * ==
      * !=
      * >
      * >=
      * <=
      * <
    * string operator
      * ==
      * !=
      * start_with (or startsWith)
      * end_with (or endsWith)
      * include (or contains)
    * unary operator
      * "IS NULL"
      * "IS NOT NULL"
  * **argument**: argument for the operation (string, required for non-unary operators)
  * **not**: not (boolean, optional, default: false)
  * **format**: special option for timestamp column, specify the format of timestamp argument, parsed argument is compared with the column value as Timestamp object (string, default is `%Y-%m-%d %H:%M:%S.%N %z`)
  * **timezone**: special option for timestamp column, specify the timezone of timestamp argument (string, default is `UTC`)
* **where** (experimental): Write conditions with SQL-like syntax. See [SQL-like Syntax](#sql-like-syntax)

NOTE: column type is automatically retrieved from input data (inputSchema)

## Example (AND)

```yaml
filters:
  - type: row
    condition: AND
    conditions:
      - {column: foo,  operator: "IS NOT NULL"}
      - {column: id,   operator: ">=", argument: 10}
      - {column: id,   operator: "<",  argument: 20}
      - {column: name, opeartor: "include", argument: foo, not: true}
      - {column: time, operator: "==", argument: "2015-07-13", format: "%Y-%m-%d"}
```

## Example (OR)

```yaml
filters:
  - type: row
    condition: OR
    conditions:
      - {column: a, operator: "IS NOT NULL"}
      - {column: b, operator: "IS NOT NULL"}
```

## Example (AND of OR)

You can express a condition such as `(A OR B) AND (C OR D)` by combining multiple filters like

```yaml
filters:
  - type: row
    condition: OR
    conditions:
      - {column: a, operator: "IS NOT NULL"}
      - {column: b, operator: "IS NOT NULL"}
  - type: row
    condition: OR
    conditions:
      - {column: c, operator: "IS NOT NULL"}
      - {column: d, operator: "IS NOT NULL"}
```

This is equivalent with `((A OR B) AND (C OR D))`.

## Example (WHERE) (Experimental)

Versions >= 0.3.0 suppors SQL-like syntax like

```yaml
filters:
  - type: row
    where: |-
      (
        string START_WITH 'str' AND
        number > 1.0
      )
      OR
      (
        time = TIMESTAMP '2016-01-01 +0900' AND
        "true" = true
      )
```

See [SQL-like Syntax](#sql-like-syntax) for more details

# SQL-like Syntax

Versions >= 0.3.0 suppors SQL-like syntax as:

```
    where: |-
      (
        string START_WITH 'str' AND
        number > 1.0
      )
      OR
      (
        time = TIMESTAMP '2016-01-01 +0900' AND
        "true" = true
      )
```

## Literals

### Boolean Literal

`true` or `TRUE` or `false` or `FALSE` are considered as a boolean literal

### Number Literal

Characters matching with a regular expression `-?[0-9]+(\.[0-9]+)?` is considered as a number literal

### String Literal

Characters surrounded by `'` such as `'foo'` is considered as a string literal

### Timestamp Literal

`TIMESTAMP ( NumberLiteral | StringLiteral )` such as `TIMESTAMP 1470433087.747123` or `TIMESTAMP '2016-08-06 06:38:07.747123 +0900'` is considered as a timestamp literal

Number is a epoch time since 1970-01-01 UTC with nano time resolution.

String is a timestamp string which matches with one of following format:

* `%Y-%m-%d %H:%M:%S.%N %z`
* `%Y-%m-%d %H:%M:%S.%N`
* `%Y-%m-%d %H:%M:%S %z`
* `%Y-%m-%d %H:%M:%S`
* `%Y-%m-%d %z`
* `%Y-%m-%d`

The default time zone is UTC, and the time resolution is micro second (caused by limitation of Embulk TimestampParser).

### Json Literal

Not supported yet

### Identifier Literal

Characters matching with a regular expression `[a-zA-Z_][a-zA-z0-9_]*` such as `foobar`, and characters surrounded by `"` such as `"foo\"bar"` are considred as an identifier literal, that is, embulk's column name.

## Operators

### Boolean Operator

* ==
* !=

### Number Operator (Long and Double)

* ==
* !=
* >
* >=
* <=
* <

### String Operator

* ==
* !=
* START_WITH
* END_WITH
* INCLUDE

### Timestamp Operator

* ==
* !=
* >
* >=
* <=
* <

### Json Operator

Not supported yet

### unary operator

* "xxx IS NULL"
* "xxx IS NOT NULL"
* "NOT xxx"

## ToDo

* Support filtering by values of `type: json` with JSONPath

## ChangeLog

[CHANGELOG.md](./CHANGELOG.md)

## Development

Run example:

```
$ ./gradlew classpath
$ embulk preview -I lib example/example.yml
```

Run test:

```
$ ./gradlew test
```

Run checkstyle:

```
$ ./gradlew check
```

Release gem:

```
$ ./gradlew gemPush
```
