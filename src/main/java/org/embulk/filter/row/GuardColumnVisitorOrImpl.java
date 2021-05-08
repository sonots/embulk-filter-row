package org.embulk.filter.row;

import org.embulk.filter.row.RowFilterPlugin.PluginTask;
import org.embulk.filter.row.condition.BooleanCondition;
import org.embulk.filter.row.condition.Condition;
import org.embulk.filter.row.condition.DoubleCondition;
import org.embulk.filter.row.condition.LongCondition;
import org.embulk.filter.row.condition.StringCondition;
import org.embulk.filter.row.condition.TimestampCondition;

import org.embulk.spi.Column;
import org.embulk.spi.ColumnVisitor;
import org.embulk.spi.Exec;
import org.embulk.spi.PageReader;
import org.embulk.spi.Schema;
import org.embulk.spi.time.Timestamp;

import org.slf4j.Logger;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

class GuardColumnVisitorOrImpl
        extends AbstractGuardColumnVisitor
        implements ColumnVisitor
{
    private static final Logger logger = Exec.getLogger(RowFilterPlugin.class);
    private boolean shouldAddRecord;
    private HashMap<String, List<Condition>> conditionMap;

    GuardColumnVisitorOrImpl(PluginTask task, Schema inputSchema, Schema outputSchema, PageReader pageReader)
    {
        super(task, inputSchema, outputSchema, pageReader);
        this.conditionMap = buildConditionMap(task, outputSchema);
    }

    public boolean visitColumns(Schema inputSchema)
    {
        //Visitor objects are created for each thread :)
        //System.out.println(String.format("thread_id:%d object_id:%d", Thread.currentThread().getId(), this.hashCode()));
        shouldAddRecord = false;
        for (Column column : inputSchema.getColumns()) {
            column.visit(this);
        }
        return shouldAddRecord;
    }

    @Override
    public void booleanColumn(Column column)
    {
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
                Instant subject = pageReader.getTimestampInstant(column);
                if (condition.compare(subject)) {
                    shouldAddRecord = true;
                    break;
                }
            }
        }
    }

    @Override
    public void jsonColumn(Column column)
    {
    }
}
