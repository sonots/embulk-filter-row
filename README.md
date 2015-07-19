# Row filter plugin for Embulk

[![Build Status](https://secure.travis-ci.org/sonots/embulk-filter-row.png?branch=master)](http://travis-ci.org/sonots/embulk-filter-row)

A filter plugin for Embulk to filter out rows

## Configuration

* **conditions**: select only rows which matches with conditions. (support only **AND** conditions)
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
  * **format**: special option for timestamp column, specify the format of timestamp argument (string, default is `%Y-%m-%d %H:%M:%S.%N %z`)
  * **timezone**: special option for timestamp column, specify the timezone of timestamp argument (string, default is `UTC`)

NOTE: column type is automatically retrieved from input data (inputSchema)

## Example

```yaml
filters:
  - type: row
    conditions:
      - {column: foo,  operator: "IS NOT NULL"}
      - {column: id,   operator: ">=", argument: 10}
      - {column: id,   operator: "<",  argument: 20}
      - {column: name, opeartor: "include", argument: foo, not: true}
      - {column: time, operator: "==", argument: "2015-07-13", format: "%Y-%m-%d"}
```

NOTE: column type is automatically retrieved from input data (inputSchema)

## ToDo

* Support OR condition
  * It should be better to think using Query engine like [Apache Drill](https://drill.apache.org/) or [Presto](https://prestodb.io/)
  * With them, it is possible to send a query to local files, even to S3 files.

## ChangeLog

[CHANGELOG.md](./CHANGELOG.md)

## Development

Run example:

```
$ ./gradlew classpath
$ embulk run -I lib example.yml
```

Run test:

```
$ ./gradlew test
```

Release gem:

```
$ ./gradlew gemPush
```
