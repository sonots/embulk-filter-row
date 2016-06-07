package org.embulk.filter.row.condition;

import com.google.common.base.Optional;
import org.embulk.config.Config;
import org.embulk.config.ConfigDefault;
import org.embulk.config.Task;

public interface ConditionConfig extends Task
{
    @Config("column")
    public String getColumn();

    @Config("operator")
    @ConfigDefault("\"==\"")
    public Optional<String> getOperator();

    @Config("argument")
    @ConfigDefault("null")
    public Optional<Object> getArgument();

    @Config("not")
    @ConfigDefault("false")
    public Optional<Boolean> getNot();

    @Config("format")
    @ConfigDefault("\"%Y-%m-%d %H:%M:%S.%N %z\"")
    public Optional<String> getFormat();

    @Config("timezone")
    @ConfigDefault("\"UTC\"")
    public Optional<String> getTimezone();
}
