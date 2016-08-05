package org.embulk.filter.row;

import org.embulk.filter.row.RowFilterPlugin.PluginTask;

import org.embulk.filter.row.where.ParserExp;
import org.embulk.spi.Exec;
import org.embulk.spi.PageReader;
import org.embulk.spi.Schema;

import org.slf4j.Logger;

class GuardColumnVisitorWhereImpl
        extends AbstractGuardColumnVisitor
{
    private static final Logger logger = Exec.getLogger(RowFilterPlugin.class);
    ParserExp parserExp;

    GuardColumnVisitorWhereImpl(PluginTask task, Schema inputSchema, Schema outputSchema, PageReader pageReader, ParserExp parserExp)
    {
        super(task, inputSchema, outputSchema, pageReader);
        this.parserExp = parserExp;
    }

    public boolean visitColumns(Schema inputSchema)
    {
        return parserExp.eval(pageReader);
    }
}
