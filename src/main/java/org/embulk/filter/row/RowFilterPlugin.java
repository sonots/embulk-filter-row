package org.embulk.filter.row;

import java.util.Optional;

import org.embulk.util.config.Config;
import org.embulk.util.config.ConfigDefault;
import org.embulk.config.ConfigException;
import org.embulk.config.ConfigSource;
import org.embulk.util.config.ConfigMapper;
import org.embulk.util.config.ConfigMapperFactory;
import org.embulk.util.config.Task;
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
import org.embulk.util.config.TaskMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RowFilterPlugin implements FilterPlugin
{
    private static final Logger logger = LoggerFactory.getLogger(RowFilterPlugin.class);
    private static final ConfigMapperFactory CONFIG_MAPPER_FACTORY = ConfigMapperFactory
            .builder()
            .addDefaultModules()
            .build();
    private static final ConfigMapper CONFIG_MAPPER = CONFIG_MAPPER_FACTORY.createConfigMapper();
    public RowFilterPlugin() {}

    public interface PluginTask extends Task
    {
        @Config("condition")
        @ConfigDefault("\"AND\"")
        @Deprecated
        public String getCondition();

        @Config("conditions")
        @ConfigDefault("null")
        @Deprecated
        public Optional<List<ConditionConfig>> getConditions();

        @Config("where")
        @ConfigDefault("null")
        public Optional<String> getWhere();

        // From org.embulk.spi.time.TimestampParser.Task.
        @Config("default_timezone")
        @ConfigDefault("\"UTC\"")
        String getDefaultTimeZoneId();

        // From org.embulk.spi.time.TimestampParser.Task.
        @Config("default_timestamp_format")
        @ConfigDefault("\"%Y-%m-%d %H:%M:%S.%N %z\"")
        String getDefaultTimestampFormat();

        // From org.embulk.spi.time.TimestampParser.Task.
        @Config("default_date")
        @ConfigDefault("\"1970-01-01\"")
        String getDefaultDate();
    }

    public interface TimestampColumnOption extends Task
    {
        // From org.embulk.spi.time.TimestampParser.TimestampColumnOption.
        @Config("timezone")
        @ConfigDefault("null")
        public Optional<String> getTimeZoneId();

        // From org.embulk.spi.time.TimestampParser.TimestampColumnOption.
        @Config("format")
        @ConfigDefault("null")
        public Optional<String> getFormat();

        // From org.embulk.spi.time.TimestampParser.TimestampColumnOption.
        @Config("date")
        @ConfigDefault("null")
        public Optional<String> getDate();
    }

    @Override
    public void transaction(ConfigSource config, Schema inputSchema,
            FilterPlugin.Control control)
    {
        final PluginTask task = CONFIG_MAPPER.map(config, PluginTask.class);

        configure(task, inputSchema);
        Schema outputSchema = inputSchema;

        control.run(task.dump(), outputSchema);
    }

    void configure(PluginTask task, Schema inputSchema) throws ConfigException
    {
        if (task.getConditions().isPresent()) {
            logger.warn("embulk-filter-row: \"conditions\" is deprecated, use \"where\" instead.");
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
            // objects must be created in open since plugin instances in transaction and open are different
            parser.parse(where); // throws ConfigException if something wrong
        }
        else {
            throw new ConfigException("Either of `conditions` or `where` must be set.");
        }
    }

    @Override
    public PageOutput open(final TaskSource taskSource, final Schema inputSchema,
            final Schema outputSchema, final PageOutput output)
    {
        final TaskMapper taskMapper = CONFIG_MAPPER_FACTORY.createTaskMapper();
        final PluginTask task = taskMapper.map(taskSource, PluginTask.class);
        final boolean orCondition = task.getCondition().toLowerCase().equals("or");
        final PageReader pageReader = new PageReader(inputSchema);
        final PageBuilder pageBuilder = new PageBuilder(Exec.getBufferAllocator(), outputSchema, output);

        final AbstractGuardColumnVisitor guradVisitor;
        if (task.getWhere().isPresent()) {
            ParserExp parserExp = new Parser(inputSchema).parse(task.getWhere().get());
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
