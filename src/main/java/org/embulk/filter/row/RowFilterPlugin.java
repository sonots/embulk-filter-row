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
import org.embulk.filter.row.where.ParserExp;

import org.embulk.filter.row.where.ParserLiteral;
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
    private ParserExp parserExp = null;

    public RowFilterPlugin() {}

    public interface PluginTask extends Task, TimestampParser.Task
    {
        @Config("condition")
        @ConfigDefault("\"AND\"")
        public String getCondition();

        @Config("conditions")
        @ConfigDefault("null")
        public Optional<List<ConditionConfig>> getConditions();

        @Config("where")
        @ConfigDefault("null")
        public Optional<String> getWhere();
    }

    @Override
    public void transaction(ConfigSource config, Schema inputSchema,
            FilterPlugin.Control control)
    {
        PluginTask task = config.loadConfig(PluginTask.class);
        ParserLiteral.setJRuby(task.getJRuby());

        configure(task, inputSchema);
        Schema outputSchema = inputSchema;

        control.run(task.dump(), outputSchema);
    }

    void configure(PluginTask task, Schema inputSchema) throws ConfigException
    {
        if (task.getConditions().isPresent()) {
            for (ConditionConfig conditionConfig : task.getConditions().get()) {
                String columnName = conditionConfig.getColumn();
                inputSchema.lookupColumn(columnName); // throw SchemaConfigException if not found
            }

            String condition = task.getCondition().toLowerCase();
            if (!condition.equals("or") && !condition.equals("and")) {
                throw new ConfigException("condition must be either of \"or\" or \"and\".");
            }
        }
        else if (task.getWhere().isPresent()) {
            String where = task.getWhere().get();
            Parser parser = new Parser(inputSchema);
            parserExp = parser.parse(where); // throw ConfigException if something wrong
        }
        else {
            throw new ConfigException("Either of `conditions` or `where` must be set.");
        }
    }

    @Override
    public PageOutput open(final TaskSource taskSource, final Schema inputSchema,
            final Schema outputSchema, final PageOutput output)
    {
        final PluginTask task = taskSource.loadTask(PluginTask.class);
        final boolean orCondition = task.getCondition().toLowerCase().equals("or");
        final PageReader pageReader = new PageReader(inputSchema);
        final PageBuilder pageBuilder = new PageBuilder(Exec.getBufferAllocator(), outputSchema, output);

        final AbstractGuardColumnVisitor guradVisitor;
        if (task.getWhere().isPresent()) {
            guradVisitor = new GuardColumnVisitorWhereImpl(task, inputSchema, outputSchema, pageReader, parserExp);
        }
        else if (orCondition) {
            guradVisitor = new GuardColumnVisitorOrImpl(task, inputSchema, outputSchema, pageReader);
        }
        else {
            guradVisitor = new GuardColumnVisitorAndImpl(task, inputSchema, outputSchema, pageReader);
        }

        final BuilderColumnVisitorImpl builderVisitor;
        builderVisitor = new BuilderColumnVisitorImpl(task, inputSchema, outputSchema, pageReader, pageBuilder);

        return new PageOutput() {
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
                    if (guradVisitor.visitColumns(inputSchema)) {
                        // output.add(page); did not work, double release() error occurred. We need to copy from reader to builder...
                        outputSchema.visitColumns(builderVisitor);
                        pageBuilder.addRecord();
                    }
                }
            }
        };
    }
}
