package org.embulk.filter.row;

import org.embulk.filter.row.RowFilterPlugin.PluginTask;
import org.embulk.filter.row.condition.BooleanCondition;
import org.embulk.filter.row.condition.Condition;
import org.embulk.filter.row.condition.DoubleCondition;
import org.embulk.filter.row.condition.LongCondition;
import org.embulk.filter.row.condition.StringCondition;
import org.embulk.filter.row.condition.TimestampCondition;

import org.embulk.spi.Column;
import org.embulk.spi.Exec;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.PageReader;
import org.embulk.spi.Schema;
import org.embulk.spi.time.Timestamp;

import org.slf4j.Logger;

import java.util.List;

class ColumnVisitorOrImpl extends AbstractColumnVisitor
{
    private static final Logger logger = Exec.getLogger(RowFilterPlugin.class);
    private boolean shouldAddRecord;

    ColumnVisitorOrImpl(PluginTask task, Schema inputSchema, Schema outputSchema, PageReader pageReader, PageBuilder pageBuilder)
    {
        super(task, inputSchema, outputSchema, pageReader, pageBuilder);
    }

    public boolean visitColumns(Schema schema)
    {
        //Visitor objects are created for each thread :)
        //System.out.println(String.format("thread_id:%d object_id:%d", Thread.currentThread().getId(), this.hashCode()));
        shouldAddRecord = false;
        for (Column column : schema.getColumns()) {
            column.visit(this);
        }
        return shouldAddRecord;
    }

    @Override
    public void booleanColumn(Column column)
    {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column);
        }
        else {
            pageBuilder.setBoolean(column, pageReader.getBoolean(column));
        }
        if (shouldAddRecord) {
            return;
        }
        List<Condition> conditionList = conditionMap.get(column.getName());
        for (Condition tempCondition : conditionList) {
            BooleanCondition condition = (BooleanCondition) tempCondition;
            if (pageReader.isNull(column)) {
                if (condition.compare(null)) {
                    shouldAddRecord = true;
                    break;
                }
            }
            else {
                boolean subject = pageReader.getBoolean(column);
                if (condition.compare(subject)) {
                    shouldAddRecord = true;
                    break;
                }
            }
        }
    }

    @Override
    public void longColumn(Column column)
    {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column);
        }
        else {
            pageBuilder.setLong(column, pageReader.getLong(column));
        }
        if (shouldAddRecord) {
            return;
        }
        List<Condition> conditionList = conditionMap.get(column.getName());
        for (Condition tempCondition : conditionList) {
            LongCondition condition = (LongCondition) tempCondition;
            if (pageReader.isNull(column)) {
                if (condition.compare(null)) {
                    shouldAddRecord = true;
                    break;
                }
            }
            else {
                long subject = pageReader.getLong(column);
                if (condition.compare(subject)) {
                    shouldAddRecord = true;
                    break;
                }
            }
        }
    }

    @Override
    public void doubleColumn(Column column)
    {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column);
        }
        else {
            pageBuilder.setDouble(column, pageReader.getDouble(column));
        }
        if (shouldAddRecord) {
            return;
        }
        List<Condition> conditionList = conditionMap.get(column.getName());
        for (Condition tempCondition : conditionList) {
            DoubleCondition condition = (DoubleCondition) tempCondition;
            if (pageReader.isNull(column)) {
                if (condition.compare(null)) {
                    shouldAddRecord = true;
                    break;
                }
            }
            else {
                double subject = pageReader.getDouble(column);
                if (condition.compare(subject)) {
                    shouldAddRecord = true;
                    break;
                }
            }
        }
    }

    @Override
    public void stringColumn(Column column)
    {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column);
        }
        else {
            pageBuilder.setString(column, pageReader.getString(column));
        }
        if (shouldAddRecord) {
            return;
        }
        List<Condition> conditionList = conditionMap.get(column.getName());
        for (Condition tempCondition : conditionList) {
            StringCondition condition = (StringCondition) tempCondition;
            if (pageReader.isNull(column)) {
                if (condition.compare(null)) {
                    shouldAddRecord = true;
                    break;
                }
            }
            else {
                String subject = pageReader.getString(column);
                if (condition.compare(subject)) {
                    shouldAddRecord = true;
                    break;
                }
            }
        }
    }

    @Override
    public void timestampColumn(Column column)
    {
        if (pageReader.isNull(column)) {
            pageBuilder.setNull(column);
        }
        else {
            pageBuilder.setTimestamp(column, pageReader.getTimestamp(column));
        }
        if (shouldAddRecord) {
            return;
        }
        List<Condition> conditionList = conditionMap.get(column.getName());
        for (Condition tempCondition : conditionList) {
            TimestampCondition condition = (TimestampCondition) tempCondition;
            if (pageReader.isNull(column)) {
                if (condition.compare(null)) {
                    shouldAddRecord = true;
                    break;
                }
            }
            else {
                Timestamp subject = pageReader.getTimestamp(column);
                if (condition.compare(subject)) {
                    shouldAddRecord = true;
                    break;
                }
            }
        }
    }
}
