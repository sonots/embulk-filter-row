package org.embulk.filter.row;

import com.google.common.base.Optional;

import org.embulk.config.Config;
import org.embulk.config.ConfigDefault;
import org.embulk.config.ConfigException;
import org.embulk.config.ConfigSource;
import org.embulk.config.Task;
import org.embulk.config.TaskSource;

import org.embulk.filter.row.condition.ConditionConfig;
import org.embulk.filter.row.where.Parser;

import org.embulk.spi.Exec;
import org.embulk.spi.FilterPlugin;
import org.embulk.spi.Page;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.PageOutput;
import org.embulk.spi.PageReader;
import org.embulk.spi.Schema;
import org.embulk.spi.time.TimestampParser;

import org.slf4j.Logger;

import java.util.List;

public class RowFilterPlugin implements FilterPlugin
{
    private static final Logger logger = Exec.getLogger(RowFilterPlugin.class);
    private Parser parser = null;

    public RowFilterPlugin() {}

    public interface PluginTask extends Task, TimestampParser.Task
    {
        @Config("condition")
        @ConfigDefault("\"AND\"")
        public String getCondition();

        @Config("conditions")
        public List<ConditionConfig> getConditions();

        @Config("where")
        public Optional<String> getWhere();
    }

    @Override
    public void transaction(ConfigSource config, Schema inputSchema,
            FilterPlugin.Control control)
    {
        PluginTask task = config.loadConfig(PluginTask.class);

        configure(task, inputSchema);
        Schema outputSchema = inputSchema;

        control.run(task.dump(), outputSchema);
    }

    void configure(PluginTask task, Schema inputSchema) throws ConfigException
    {
        for (ConditionConfig conditionConfig : task.getConditions()) {
            String columnName = conditionConfig.getColumn();
            inputSchema.lookupColumn(columnName); // throw SchemaConfigException if not found
        }

        String condition = task.getCondition().toLowerCase();
        if (!condition.equals("or") && !condition.equals("and")) {
            throw new ConfigException("condition must be either of \"or\" or \"and\".");
        }

        if (task.getWhere().isPresent()) {
            String where = task.getWhere().get();
        }
    }

    @Override
    public PageOutput open(final TaskSource taskSource, final Schema inputSchema,
            final Schema outputSchema, final PageOutput output)
    {
        final PluginTask task = taskSource.loadTask(PluginTask.class);
        final boolean orCondition = task.getCondition().toLowerCase().equals("or");

        return new PageOutput() {
            private PageReader pageReader = new PageReader(inputSchema);
            private PageBuilder pageBuilder = new PageBuilder(Exec.getBufferAllocator(), outputSchema, output);
            private AbstractColumnVisitor visitor = orCondition ?
                    new ColumnVisitorOrImpl(task, inputSchema, outputSchema, pageReader, pageBuilder) :
                    new ColumnVisitorAndImpl(task, inputSchema, outputSchema, pageReader, pageBuilder);

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
                    if (visitor.visitColumns(inputSchema)) {
                        pageBuilder.addRecord();
                    }
                }
            }
        };
    }
}
