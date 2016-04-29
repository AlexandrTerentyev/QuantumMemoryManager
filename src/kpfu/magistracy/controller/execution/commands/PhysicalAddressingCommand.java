package kpfu.magistracy.controller.execution.commands;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import kpfu.magistracy.controller.addresses.GlobalQubitAddress;

public class PhysicalAddressingCommand {
    @NotNull
    private CommandTypes mCommandType;

    @NotNull
    private Double mCommandParam;

    @NotNull
    private GlobalQubitAddress mFirstQubit_Part1;
    @NotNull
    private GlobalQubitAddress mFirstQubit_Part2;
    @Nullable
    private GlobalQubitAddress mSecondQubit_Part1;
    @Nullable
    private GlobalQubitAddress mSecondQubit_Part2;

    public CommandTypes getCommandType() {
        return mCommandType;
    }

    public Double getCommandParam() {
        return mCommandParam;
    }

    public GlobalQubitAddress getFirstQubit_Part1() {
        return mFirstQubit_Part1;
    }

    public GlobalQubitAddress getFirstQubit_Part2() {
        return mFirstQubit_Part2;
    }

    public GlobalQubitAddress getSecondQubit_Part1() {
        return mSecondQubit_Part1;
    }

    public GlobalQubitAddress getSecondQubit_Part2() {
        return mSecondQubit_Part2;
    }

    public static class Builder {

        private PhysicalAddressingCommand physicalAddressingCommand;

        public Builder() {
            physicalAddressingCommand = new PhysicalAddressingCommand();
        }

        public PhysicalAddressingCommand.Builder setCommand(CommandTypes commandType) {
            physicalAddressingCommand.mCommandType = commandType;
            return this;
        }

        public PhysicalAddressingCommand.Builder setCommandParam(Double commandParam) {
            physicalAddressingCommand.mCommandParam = commandParam;
            return this;
        }

        public PhysicalAddressingCommand.Builder setFirstQubit_Part1(GlobalQubitAddress qubitAddress) {
            physicalAddressingCommand.mFirstQubit_Part1 = qubitAddress;
            return this;
        }

        public PhysicalAddressingCommand.Builder setFirstQubit_Part2(GlobalQubitAddress qubitAddress) {
            physicalAddressingCommand.mFirstQubit_Part2 = qubitAddress;
            return this;
        }

        public PhysicalAddressingCommand.Builder setSecondQubit_Part1(GlobalQubitAddress qubitAddress) {
            physicalAddressingCommand.mSecondQubit_Part1 = qubitAddress;
            return this;
        }

        public PhysicalAddressingCommand.Builder setSecondQubit_Part2(GlobalQubitAddress qubitAddress) {
            physicalAddressingCommand.mSecondQubit_Part2 = qubitAddress;
            return this;
        }

        public PhysicalAddressingCommand build() {
            return physicalAddressingCommand;
        }
    }
}
