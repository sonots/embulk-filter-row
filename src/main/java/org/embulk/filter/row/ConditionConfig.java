package org.embulk.filter.row;

import org.embulk.config.Config;
import org.embulk.config.ConfigDefault;
import org.embulk.config.Task;
import com.google.common.base.Optional;

public interface ConditionConfig extends Task
{
    @Config("column")
    public String getColumn();

    @Config("operator")
    @ConfigDefault("==")
    public Optional<String> getOperator();

    @Config("argument")
    @ConfigDefault("null")
    public Optional<Object> getArgument();

    @Config("not")
    @ConfigDefault("false")
    public Optional<Boolean> getNot();
}
