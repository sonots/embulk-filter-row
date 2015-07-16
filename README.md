# Row filter plugin for Embulk

A filter plugin for Embulk to filter out rows

## Configuration

* **conditions**: select only rows which matches with conditions. (support only AND conditions)
  * **column**: column name (string, required)
  * **operator** operator (string, required)
    * boolean operator
      * ==
      * !=
    * numeric operator
      * >
      * >=
      * ==
      * !=
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
  * **argument**: argument for the operation (string, required for binary operators)
  * **not**: not operation (boolean, default: false)
  * **value**: synonym to `operator: ==` and `argument: #{value}`

## Example

```yaml
filters:
  - type: row
    conditions:
      - {column: id,   operator: >,  argument: 10}
      - {column: name, operator: ==, argument: foo, not: true}
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
