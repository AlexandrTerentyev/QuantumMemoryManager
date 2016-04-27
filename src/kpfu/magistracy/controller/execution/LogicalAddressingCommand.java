package kpfu.magistracy.controller.execution;

import kpfu.magistracy.controller.addresses.LogicalQubitAddress;

public class LogicalAddressingCommand {

    private CommandTypes mCommandType;

    private Double mCommandParam;

    private LogicalQubitAddress mQubit_1;
    private LogicalQubitAddress mQubit_2;
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

        public Builder setCommand(CommandTypes commandType) {
            logicalAddressingCommand.mCommandType = commandType;
            return this;
        }

        public Builder setCommandParam(Double commandParam) {
            logicalAddressingCommand.mCommandParam = commandParam;
            return this;
        }

        public Builder setFirstQubit(LogicalQubitAddress qubitAddress) {
            logicalAddressingCommand.mQubit_1 = qubitAddress;
            return this;
        }

        public Builder setSecondQubit(LogicalQubitAddress qubitAddress) {
            logicalAddressingCommand.mQubit_2 = qubitAddress;
            return this;
        }

        public Builder setThirdQubit(LogicalQubitAddress qubitAddress) {
            logicalAddressingCommand.mQubit_3 = qubitAddress;
            return this;
        }

        public LogicalAddressingCommand build() {
            return logicalAddressingCommand;
        }
    }
}


