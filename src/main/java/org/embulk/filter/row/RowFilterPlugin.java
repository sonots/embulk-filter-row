package org.embulk.filter.row;

import org.embulk.config.Config;
import org.embulk.config.ConfigDefault;
import org.embulk.config.ConfigException;
import org.embulk.config.ConfigSource;
import org.embulk.config.Task;
import org.embulk.config.TaskSource;

import org.embulk.filter.row.condition.BooleanCondition;
import org.embulk.filter.row.condition.Condition;
import org.embulk.filter.row.condition.ConditionConfig;
import org.embulk.filter.row.condition.ConditionFactory;
import org.embulk.filter.row.condition.DoubleCondition;
import org.embulk.filter.row.condition.LongCondition;
import org.embulk.filter.row.condition.StringCondition;
import org.embulk.filter.row.condition.TimestampCondition;

import org.embulk.spi.Column;
import org.embulk.spi.ColumnVisitor;
import org.embulk.spi.Exec;
import org.embulk.spi.FilterPlugin;
import org.embulk.spi.Page;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.PageOutput;
import org.embulk.spi.PageReader;
import org.embulk.spi.Schema;
import org.embulk.spi.time.Timestamp;
import org.embulk.spi.time.TimestampParser;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RowFilterPlugin implements FilterPlugin
{
    private static final Logger logger = Exec.getLogger(RowFilterPlugin.class);

    public RowFilterPlugin()
    {
    }

    public interface PluginTask extends Task, TimestampParser.Task
    {
        @Config("condition")
        @ConfigDefault("\"AND\"")
        public String getCondition();

        @Config("conditions")
        public List<ConditionConfig> getConditions();
    }

    @Override
    public void transaction(ConfigSource config, Schema inputSchema,
            FilterPlugin.Control control)
    {
        PluginTask task = config.loadConfig(PluginTask.class);

        for (ConditionConfig conditionConfig : task.getConditions()) {
            String columnName = conditionConfig.getColumn();
            inputSchema.lookupColumn(columnName); // throw SchemaConfigException if not found
        }

        String condition = task.getCondition().toLowerCase();
        if (!condition.equals("or") && !condition.equals("and")) {
            throw new ConfigException("condition must be either of \"or\" or \"and\".");
        }

        Schema outputSchema = inputSchema;
        control.run(task.dump(), outputSchema);
    }

    @Override
    public PageOutput open(final TaskSource taskSource, final Schema inputSchema,
            final Schema outputSchema, final PageOutput output)
    {
        PluginTask task = taskSource.loadTask(PluginTask.class);

        final boolean orCondition = task.getCondition().toLowerCase().equals("or");

        final HashMap<String, List<Condition>> conditionMap = new HashMap<String, List<Condition>>();
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

        return new PageOutput() {
            private PageReader pageReader = new PageReader(inputSchema);
            private PageBuilder pageBuilder = new PageBuilder(Exec.getBufferAllocator(), outputSchema, output);
            private boolean shouldAddRecord;
            private ColumnVisitor visitor = orCondition ? new ColumnVisitorOrImpl(pageBuilder) : new ColumnVisitorAndImpl(pageBuilder);

            @Override
            public void finish()
            {
                pageBuilder.finish();
            }

            @Override
            public void close()
            {
                pageBuilder.close();
            }

            @Override
            public void add(Page page)
            {
                pageReader.setPage(page);

                while (pageReader.nextRecord()) {
                    shouldAddRecord = orCondition ? false : true;
                    inputSchema.visitColumns(visitor);
                    if (shouldAddRecord) {
                        pageBuilder.addRecord();
                    }
                }
            }

            class ColumnVisitorOrImpl implements ColumnVisitor
            {
                private final PageBuilder pageBuilder;

                ColumnVisitorOrImpl(PageBuilder pageBuilder)
                {
                    this.pageBuilder = pageBuilder;
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

            class ColumnVisitorAndImpl implements ColumnVisitor
            {
                private final PageBuilder pageBuilder;

                ColumnVisitorAndImpl(PageBuilder pageBuilder)
                {
                    this.pageBuilder = pageBuilder;
                }

                @Override
                public void booleanColumn(Column column)
                {
                    if (!shouldAddRecord) {
                        return;
                    }
                    List<Condition> conditionList = conditionMap.get(column.getName());
                    for (Condition tempCondition : conditionList) {
                        BooleanCondition condition = (BooleanCondition) tempCondition;
                        if (pageReader.isNull(column)) {
                            if (!condition.compare(null)) {
                                shouldAddRecord = false;
                                break;
                            }
                        }
                        else {
                            boolean subject = pageReader.getBoolean(column);
                            if (!condition.compare(subject)) {
                                shouldAddRecord = false;
                                break;
                            }
                        }
                    }
                    if (pageReader.isNull(column)) {
                        pageBuilder.setNull(column);
                    }
                    else {
                        pageBuilder.setBoolean(column, pageReader.getBoolean(column));
                    }
                }

                @Override
                public void longColumn(Column column)
                {
                    if (!shouldAddRecord) {
                        return;
                    }
                    List<Condition> conditionList = conditionMap.get(column.getName());
                    for (Condition tempCondition : conditionList) {
                        LongCondition condition = (LongCondition) tempCondition;
                        if (pageReader.isNull(column)) {
                            if (!condition.compare(null)) {
                                shouldAddRecord = false;
                                break;
                            }
                        }
                        else {
                            long subject = pageReader.getLong(column);
                            if (!condition.compare(subject)) {
                                shouldAddRecord = false;
                                break;
                            }
                        }
                    }
                    if (pageReader.isNull(column)) {
                        pageBuilder.setNull(column);
                    }
                    else {
                        pageBuilder.setLong(column, pageReader.getLong(column));
                    }
                }

                @Override
                public void doubleColumn(Column column)
                {
                    if (!shouldAddRecord) {
                        return;
                    }
                    List<Condition> conditionList = conditionMap.get(column.getName());
                    for (Condition tempCondition : conditionList) {
                        DoubleCondition condition = (DoubleCondition) tempCondition;
                        if (pageReader.isNull(column)) {
                            if (!condition.compare(null)) {
                                shouldAddRecord = false;
                                break;
                            }
                        }
                        else {
                            double subject = pageReader.getDouble(column);
                            if (!condition.compare(subject)) {
                                shouldAddRecord = false;
                                break;
                            }
                        }
                    }
                    if (pageReader.isNull(column)) {
                        pageBuilder.setNull(column);
                    }
                    else {
                        pageBuilder.setDouble(column, pageReader.getDouble(column));
                    }
                }

                @Override
                public void stringColumn(Column column)
                {
                    if (!shouldAddRecord) {
                        return;
                    }
                    List<Condition> conditionList = conditionMap.get(column.getName());
                    for (Condition tempCondition : conditionList) {
                        StringCondition condition = (StringCondition) tempCondition;
                        if (pageReader.isNull(column)) {
                            if (!condition.compare(null)) {
                                shouldAddRecord = false;
                                break;
                            }
                        }
                        else {
                            String subject = pageReader.getString(column);
                            if (!condition.compare(subject)) {
                                shouldAddRecord = false;
                                break;
                            }
                        }
                    }
                    if (pageReader.isNull(column)) {
                        pageBuilder.setNull(column);
                    }
                    else {
                        pageBuilder.setString(column, pageReader.getString(column));
                    }
                }

                @Override
                public void timestampColumn(Column column)
                {
                    if (!shouldAddRecord) {
                        return;
                    }
                    List<Condition> conditionList = conditionMap.get(column.getName());
                    for (Condition tempCondition : conditionList) {
                        TimestampCondition condition = (TimestampCondition) tempCondition;
                        if (pageReader.isNull(column)) {
                            if (!condition.compare(null)) {
                                shouldAddRecord = false;
                                break;
                            }
                        }
                        else {
                            Timestamp subject = pageReader.getTimestamp(column);
                            if (!condition.compare(subject)) {
                                shouldAddRecord = false;
                                break;
                            }
                        }
                    }
                    if (pageReader.isNull(column)) {
                        pageBuilder.setNull(column);
                    }
                    else {
                        pageBuilder.setTimestamp(column, pageReader.getTimestamp(column));
                    }
                }
            }
        };
    }
}
