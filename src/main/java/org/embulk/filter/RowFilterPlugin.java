package org.embulk.filter;

import org.embulk.config.Config;
import org.embulk.config.ConfigDefault;
import org.embulk.config.ConfigDiff;
import org.embulk.config.ConfigSource;
import org.embulk.config.Task;
import org.embulk.config.TaskSource;

import java.util.List;
import java.util.HashMap;
import org.embulk.spi.type.Type;
import org.embulk.spi.type.TimestampType;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;

import org.embulk.spi.FilterPlugin;
import org.embulk.spi.Exec;
import org.embulk.spi.Page;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.PageOutput;
import org.embulk.spi.PageReader;
import org.embulk.spi.Schema;
import org.embulk.spi.SchemaConfig;
import org.embulk.spi.Column;
import org.embulk.spi.ColumnVisitor;
import org.embulk.spi.type.BooleanType;
import org.embulk.spi.type.LongType;
import org.embulk.spi.type.DoubleType;
import org.embulk.spi.type.StringType;
import org.embulk.spi.type.TimestampType;
import org.embulk.spi.time.Timestamp;
import org.embulk.filter.row.ConditionConfig;
import org.embulk.filter.row.Condition;
import org.embulk.filter.row.BooleanCondition;
import org.embulk.filter.row.LongCondition;
import org.embulk.filter.row.DoubleCondition;
import org.embulk.filter.row.StringCondition;
import org.embulk.filter.row.TimestampCondition;

public class RowFilterPlugin implements FilterPlugin
{
    public interface PluginTask extends Task
    {
        @Config("conditions")
        public List<ConditionConfig> getConditions();
    }

    @Override
    public void transaction(ConfigSource config, Schema inputSchema,
            FilterPlugin.Control control)
    {
        PluginTask task = config.loadConfig(PluginTask.class);

        Schema outputSchema = inputSchema;

        control.run(task.dump(), outputSchema);
    }

    private final Logger log;

    public RowFilterPlugin()
    {
        log = Exec.getLogger(RowFilterPlugin.class);
    }

    @Override
    public PageOutput open(TaskSource taskSource, Schema inputSchema,
            Schema outputSchema, PageOutput output)
    {
        PluginTask task = taskSource.loadTask(PluginTask.class);

        HashMap<String, Condition> conditionMap = new HashMap<String, Condition>();
        for (ConditionConfig conditionConfig : task.getConditions()) {
            String columnName = conditionConfig.getColumn();
            for (Column column : outputSchema.getColumns()) {
                if (columnName.equals(column.getName())) {
                    Type type = column.getType();
                    String operator = conditionConfig.getOperator();
                    Condition condition = null;
                    if (type instanceof BooleanType) {
                        if (!(conditionConfig.getArgument().get() instanceof Boolean)) {
                            log.warn(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
                            System.exit(1);
                        }
                        boolean argument = ((Boolean)conditionConfig.getArgument().get()).booleanValue();
                        condition = new BooleanCondition(operator, argument);
                    }
                    else if (type instanceof LongType) {
                        if (!(conditionConfig.getArgument().get() instanceof Integer)) {
                            log.warn(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
                            System.exit(1);
                        }
                        long argument = ((Integer)conditionConfig.getArgument().get()).longValue();
                        condition = new LongCondition(operator, argument);
                    }
                    else if (type instanceof DoubleType) {
                        if (!(conditionConfig.getArgument().get() instanceof Number)) {
                            log.warn(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
                            System.exit(1);
                        }
                        double argument = new Double(conditionConfig.getArgument().get().toString()).doubleValue();
                        condition = new DoubleCondition(operator, argument);
                    }
                    else if (type instanceof StringType) {
                        if (!(conditionConfig.getArgument().get() instanceof String)) {
                            log.warn(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
                            System.exit(1);
                        }
                        String argument = (String)conditionConfig.getArgument().get();
                        condition = new StringCondition(operator, argument);
                    }
                    else if (type instanceof TimestampType) {
                        if (!(conditionConfig.getArgument().get() instanceof Timestamp)) {
                            log.warn(String.format("RowFilterPlugin: Type mismatch on column: %s", columnName));
                            System.exit(1);
                        }
                        Timestamp argument = (Timestamp)conditionConfig.getArgument().get();
                        condition = new TimestampCondition(operator, argument);
                    }
                    else {
                        assert(false);
                    }
                    conditionMap.put(columnName, condition);
                    break;
                }
            }
        }

        return new PageOutput() {
            private PageReader pageReader = new PageReader(inputSchema);
            private PageBuilder pageBuilder = new PageBuilder(Exec.getBufferAllocator(), outputSchema, output);
            private boolean shouldAddRecord = true;

            @Override
            public void finish() {
                pageBuilder.finish();
            }

            @Override
            public void close() {
                pageBuilder.close();
            }

            @Override
            public void add(Page page) {
                pageReader.setPage(page);

                ColumnVisitorImpl visitor = new ColumnVisitorImpl(pageBuilder);
                while (pageReader.nextRecord()) {
                    shouldAddRecord = true;
                    inputSchema.visitColumns(visitor);
                    if (shouldAddRecord) pageBuilder.addRecord();
                }
            }

            class ColumnVisitorImpl implements ColumnVisitor {
                private final PageBuilder pageBuilder;

                ColumnVisitorImpl(PageBuilder pageBuilder) {
                    this.pageBuilder = pageBuilder;
                }

                @Override
                public void booleanColumn(Column column) {
                    if (!shouldAddRecord) return;
                    BooleanCondition condition = (BooleanCondition)conditionMap.get(column.getName());
                    if (condition != null) {
                        if (pageReader.isNull(column)) {
                        } else {
                            boolean value = pageReader.getBoolean(column);
                            if (!condition.compare(value)) shouldAddRecord = false;
                        }
                    }
                    if (pageReader.isNull(column)) {
                        pageBuilder.setNull(column);
                    } else {
                        pageBuilder.setBoolean(column, pageReader.getBoolean(column));
                    }
                }

                @Override
                public void longColumn(Column column) {
                    if (!shouldAddRecord) return;
                    LongCondition condition = (LongCondition)conditionMap.get(column.getName());
                    if (condition != null) {
                        if (pageReader.isNull(column)) {
                        } else {
                            long value = pageReader.getLong(column);
                            if (!condition.compare(value)) shouldAddRecord = false;
                        }
                    }
                    if (pageReader.isNull(column)) {
                        pageBuilder.setNull(column);
                    } else {
                        pageBuilder.setLong(column, pageReader.getLong(column));
                    }
                }

                @Override
                public void doubleColumn(Column column) {
                    if (!shouldAddRecord) return;
                    DoubleCondition condition = (DoubleCondition)conditionMap.get(column.getName());
                    if (condition != null) {
                        if (pageReader.isNull(column)) {
                        } else {
                            double value = pageReader.getDouble(column);
                            if (!condition.compare(value)) shouldAddRecord = false;
                        }
                    }
                    if (pageReader.isNull(column)) {
                        pageBuilder.setNull(column);
                    } else {
                        pageBuilder.setDouble(column, pageReader.getDouble(column));
                    }
                }

                @Override
                public void stringColumn(Column column) {
                    if (!shouldAddRecord) return;
                    StringCondition condition = (StringCondition)conditionMap.get(column.getName());
                    if (condition != null) {
                        if (pageReader.isNull(column)) {
                        } else {
                            String value = pageReader.getString(column);
                            if (!condition.compare(value)) shouldAddRecord = false;
                        }
                    }
                    if (pageReader.isNull(column)) {
                        pageBuilder.setNull(column);
                    } else {
                        pageBuilder.setString(column, pageReader.getString(column));
                    }
                }

                @Override
                public void timestampColumn(Column column) {
                    if (!shouldAddRecord) return;
                    TimestampCondition condition = (TimestampCondition)conditionMap.get(column.getName());
                    if (condition != null) {
                        if (pageReader.isNull(column)) {
                        } else {
                            Timestamp value = pageReader.getTimestamp(column);
                            if (!condition.compare(value)) shouldAddRecord = false;
                        }
                    }
                    if (pageReader.isNull(column)) {
                        pageBuilder.setNull(column);
                    } else {
                        pageBuilder.setTimestamp(column, pageReader.getTimestamp(column));
                    }
                }
            }
        };
    }
}
