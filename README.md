# Row filter plugin for Embulk

A filter plugin for Embulk to filter out rows

## Configuration

* **conditions**: select only rows which matches with conditions. (support only AND conditions)
  * **column**: column name (string, required)
  * **operator** operator (string, optional, default: ==)
    * boolean operator
      * ==
      * !=
    * numeric operator
      * ==
      * !=
      * >
      * >=
      * <=
      * <
    * string operator
      * ==
      * !=
      * start_with
      * end_with
      * include
    * unary operator
      * "IS NULL"
      * "IS NOT NULL"
  * **argument**: argument for the operation (string, required for non-unary operators)
  * **not**: not (boolean, optional, default: false)

## Example

```yaml
filters:
  - type: row
    conditions:
      - {column: id,   operator: >,  argument: 10}
      - {column: name, opeartor: ==, argument: foo, not: true}
```

## ToDo

* Support OR condition
  * It should be better to think using Query engine like [Apache Drill](https://drill.apache.org/) or [Presto](https://prestodb.io/)

## Development

Run example:

```
$ ./gradlew classpath
$ embulk run -I lib example.yml
```

Release gem:

```
$ ./gradlew gemPush
```
