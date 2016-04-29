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
    private LogicalQubitAddress mFirstQubit_Part1;
    @NotNull
    private LogicalQubitAddress mFirstQubit_Part2;
    @Nullable
    private LogicalQubitAddress mSecondQubit_Part1;
    @Nullable
    private LogicalQubitAddress mSecondQubit_Part2;

    public CommandTypes getCommandType() {
        return mCommandType;
    }

    public Double getCommandParam() {
        return mCommandParam;
    }

    public LogicalQubitAddress getFirstQubit_Part1() {
        return mFirstQubit_Part1;
    }

    public LogicalQubitAddress getFirstQubit_Part2() {
        return mFirstQubit_Part2;
    }

    public LogicalQubitAddress getSecondQubit_Part1() {
        return mSecondQubit_Part1;
    }

    public LogicalQubitAddress getSecondQubit_Part2() {
        return mSecondQubit_Part2;
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

        public Builder setFirstQubit_Part1(@NotNull LogicalQubitAddress qubitAddress) {
            logicalAddressingCommand.mFirstQubit_Part1 = qubitAddress;
            return this;
        }

        public Builder setFirstQubit_Part2(@NotNull LogicalQubitAddress qubitAddress) {
            logicalAddressingCommand.mFirstQubit_Part2 = qubitAddress;
            return this;
        }

        public Builder setSecondQubit_Part1(@NotNull LogicalQubitAddress qubitAddress) {
            logicalAddressingCommand.mSecondQubit_Part1 = qubitAddress;
            return this;
        }

        public Builder setSecondQubit_Part2(@NotNull LogicalQubitAddress qubitAddress) {
            logicalAddressingCommand.mSecondQubit_Part2 = qubitAddress;
            return this;
        }

        public LogicalAddressingCommand build() {
            return logicalAddressingCommand;
        }
    }
}


