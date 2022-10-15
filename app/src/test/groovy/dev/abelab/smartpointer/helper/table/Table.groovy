package dev.abelab.smartpointer.helper.table

import groovy.transform.ToString
import groovy.transform.TupleConstructor

@ToString
@TupleConstructor
class Table {
    List<Column> columns
    List<Row> rows

    List<Map<String, Object>> toMapList() {
        rows.collect { [columns, it.values].transpose().collectEntries { [it[0].name, it[1]] } }
    }
}