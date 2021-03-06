# Row filter plugin for Embulk

[![Build Status](https://secure.travis-ci.org/sonots/embulk-filter-row.png?branch=master)](http://travis-ci.org/sonots/embulk-filter-row)

A filter plugin for Embulk to filter out rows

## Configuration

Requirement: version >= 0.3.0

* **where**: Select only rows which match with conditions written in SQL-like syntax. See [SQL-like Syntax](#sql-like-syntax)

## Example

```yaml
filters:
  - type: row
    where: column1 = 'str'
```

```yaml
filters:
  - type: row
    where: |-
      (
        string_column START_WITH 'str' AND
        number_column > 1.0
      )
      OR
      (
        time_column = TIMESTAMP '2016-01-01 +0900' AND
        "true_column" = true
      )
```

See [SQL-like Syntax](#sql-like-syntax) for more details

# SQL-like Syntax

This syntax must be similar with a standard SQL syntax.

```sql
where: column1 = 'str'
```

```sql
where: |-
  (
    string_column START_WITH 'str' AND
    number_column > 1.0
  
  )
  OR
  (
    time_column = TIMESTAMP '2016-01-01 +0900' AND
    "true_column" = true AND
    string_column REGEXP '^reg'
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

NOTE: It became possible to omit `TIMESTAMP` keyword on comparing with `timestamp` identifier (column) from version >= 0.3.3.

`TIMESTAMP ( NumberLiteral | StringLiteral )` such as `TIMESTAMP 1470433087.747123` or `TIMESTAMP '2016-08-06 06:38:07.747123 +0900'` is considered as a timestamp literal

Number is a epoch time since 1970-01-01 UTC with nano time resolution.

String is a timestamp string which matches with one of following format:

* `%Y-%m-%d %H:%M:%S.%N %z`
* `%Y-%m-%d %H:%M:%S.%N`
* `%Y-%m-%d %H:%M:%S %z`
* `%Y-%m-%d %H:%M:%S`
* `%Y-%m-%d %z`
* `%Y-%m-%d`

The time zone for formats without `%z` is UTC, and the time resolution is micro second (caused by limitation of Embulk TimestampParser).

### Json Literal

Not supported yet

### Identifier Literal

Characters matching with a regular expression `[a-zA-Z_][a-zA-z0-9_]*` such as `foobar`, and characters surrounded by `"` such as `"foo-bar"`, `"foo.bar"`, and `"foo\"bar"` are considred as an identifier literal, that is, embulk's column name.

## Operators

### Boolean Operator

* `=`
* `!=`

### Number Operator (Long and Double)

* `=`
* `!=`
* `>`
* `>=`
* `<=`
* `<`

### String Operator

* `=`
* `!=`
* `START_WITH`
* `END_WITH`
* `INCLUDE`
* `REGEXP`

### Timestamp Operator

* `=`
* `!=`
* `>`
* `>=`
* `<=`
* `<`

### Json Operator

Not supported yet

### unary operator

* "xxx IS NULL"
* "xxx IS NOT NULL"
* "NOT xxx"

## Old Configuration

Versions >= 0.3.0 has `where` option to supports SQL-like syntax. I recommend to use it.

Following options are **deprecated**, and **will be removed someday**.

* **condition**: AND or OR (string, default: AND).
* **conditions**: select only rows which matches with conditions.
  * **column**: column name (string, required)
  * **operator** operator (string, optional, default: ==)
    * boolean operator
      * `==`
      * `!=`
    * numeric operator (long, double, Timestamp)
      * `==`
      * `!=`
      * `>`
      * `>=`
      * `<=`
      * `<`
    * string operator
      * `==`
      * `!=`
      * `start_with` (or `startsWith`)
      * `end_with` (or `endsWith`)
      * `include` (or `contains`)
    * unary operator
      * `IS NULL`
      * `IS NOT NULL`
  * **argument**: argument for the operation (string, required for non-unary operators)
  * **not**: not (boolean, optional, default: false)
  * **format**: special option for timestamp column, specify the format of timestamp argument, parsed argument is compared with the column value as Timestamp object (string, default is `%Y-%m-%d %H:%M:%S.%N %z`)
  * **timezone**: special option for timestamp column, specify the timezone of timestamp argument (string, default is `UTC`)

NOTE: column type is automatically retrieved from input data (inputSchema)

## Example (AND)

**Deprecated**

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

**Deprecated**

```yaml
filters:
  - type: row
    condition: OR
    conditions:
      - {column: a, operator: "IS NOT NULL"}
      - {column: b, operator: "IS NOT NULL"}
```

## Example (AND of OR)

**Deprecated**

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

## Comparisions

* [embulk-filter-calcite](https://github.com/muga/embulk-filter-calcite)
  * embulk-filter-calcite is a pretty nice plugin which enables us to write SQL query to filter embulk records, not only `WHERE` but also `SELECT`.
  * Based on [my benchmark (Japanese)](http://qiita.com/sonots/items/a70482d29862de87624d), embulk-filter-row was faster than embulk-filter-calcite.
  * Choose which to use as your demand.

## ToDo

* Support filtering by values of `type: json` with JSONPath
* Support IN operator

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

## Development of SQL-like Syntax

Read the article [Supported SQL-like Syntax with embulk-filter-row using BYACC/J and JFlex](http://blog.livedoor.jp/sonots/archives/48172830.html).

To download BYACC/J and JFlex and run them, you can use:

```
$ script/byaccj.sh
```

or

```
$ ./gradlew byaccj # this runs script/byaccj.sh internally
```

This generates `src/main/java/org/embulk/filter/row/where/{Parser,ParserVal,Yylex}.java`.

The `byaccj` task of gradle is ran before `compileJava` task (which means to be ran before `classpath` or `test` task also) automatically.
