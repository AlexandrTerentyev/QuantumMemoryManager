package kpfu.magistracy.controller.execution.commands;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import kpfu.magistracy.controller.addresses.LogicalQubitAddress;

public class LogicalAddressingCommand {
    @NotNull
    private CommandTypes mCommandType;

    @NotNull
    private Double mCommandParam;

    @NotNull
    private LogicalQubitAddress mQubit_1;
    @NotNull
    private LogicalQubitAddress mQubit_2;
    @Nullable
    private LogicalQubitAddress mQubit_3;

    public CommandTypes getCommandType() {
        return mCommandType;
    }

    public Double getCommandParam() {
        return mCommandParam;
    }

    public LogicalQubitAddress getQubit_1() {
        return mQubit_1;
    }

    public LogicalQubitAddress getQubit_2() {
        return mQubit_2;
    }

    public LogicalQubitAddress getQubit_3() {
        return mQubit_3;
    }

    public static class Builder {

        private LogicalAddressingCommand logicalAddressingCommand;

        public Builder() {
            logicalAddressingCommand = new LogicalAddressingCommand();
        }

        public Builder setCommand(@NotNull CommandTypes commandType) {
            logicalAddressingCommand.mCommandType = commandType;
            return this;
        }

        public Builder setCommandParam(@NotNull Double commandParam) {
            logicalAddressingCommand.mCommandParam = commandParam;
            return this;
        }

        public Builder setFirstQubit(@NotNull LogicalQubitAddress qubitAddress) {
            logicalAddressingCommand.mQubit_1 = qubitAddress;
            return this;
        }

        public Builder setSecondQubit(@NotNull LogicalQubitAddress qubitAddress) {
            logicalAddressingCommand.mQubit_2 = qubitAddress;
            return this;
        }

        public Builder setThirdQubit(@NotNull LogicalQubitAddress qubitAddress) {
            logicalAddressingCommand.mQubit_3 = qubitAddress;
            return this;
        }

        public LogicalAddressingCommand build() {
            return logicalAddressingCommand;
        }
    }
}


