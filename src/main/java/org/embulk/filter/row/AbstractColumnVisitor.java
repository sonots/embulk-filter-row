package org.embulk.filter.row;

import org.embulk.filter.row.RowFilterPlugin.PluginTask;
import org.embulk.filter.row.condition.Condition;

import org.embulk.filter.row.condition.ConditionConfig;
import org.embulk.filter.row.condition.ConditionFactory;
import org.embulk.spi.Column;
import org.embulk.spi.ColumnVisitor;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.PageReader;
import org.embulk.spi.Schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

abstract class AbstractColumnVisitor
        implements ColumnVisitor
{
    final PluginTask task;
    final Schema inputSchema;
    final Schema outputSchema;
    final PageReader pageReader;
    final PageBuilder pageBuilder;
    HashMap<String, List<Condition>> conditionMap;

    AbstractColumnVisitor(PluginTask task, Schema inputSchema, Schema outputSchema, PageReader pageReader, PageBuilder pageBuilder)
    {
        this.task = task;
        this.inputSchema = inputSchema;
        this.outputSchema = outputSchema;
        this.pageReader = pageReader;
        this.pageBuilder = pageBuilder;

        this.conditionMap = buildConditionMap(task, outputSchema);
    }

    static HashMap<String, List<Condition>> buildConditionMap(PluginTask task, Schema outputSchema)
    {
        HashMap<String, List<Condition>> conditionMap = new HashMap<>();
        for (Column column : outputSchema.getColumns()) {
            String columnName = column.getName();
            conditionMap.put(columnName, new ArrayList<Condition>());
        }

        for (ConditionConfig conditionConfig : task.getConditions()) {
            String columnName = conditionConfig.getColumn();
            for (Column column : outputSchema.getColumns()) {
                if (columnName.equals(column.getName())) {
                    ConditionFactory factory = new ConditionFactory(task.getJRuby(), column, conditionConfig);
                    Condition condition = factory.createCondition();
                    conditionMap.get(columnName).add(condition);
                    break;
                }
            }
        }
        return conditionMap;
    }

    abstract public boolean visitColumns(Schema schema);
}
