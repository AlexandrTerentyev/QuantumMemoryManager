package kpfu.magistracy.controller.execution.commands;

import kpfu.terentyev.quantum.api.KazanModel.QuantumMemoryAddress;

public class PhysicalAddressingCommand {

    private CommandTypes mCommandType;

    private Double mCommandParam;

    private QuantumMemoryAddress mQubit_1;
    private QuantumMemoryAddress mQubit_2;
    private QuantumMemoryAddress mQubit_3;

    public CommandTypes getCommandType() {
        return mCommandType;
    }

    public Double getCommandParam() {
        return mCommandParam;
    }

    public QuantumMemoryAddress getQubit_1() {
        return mQubit_1;
    }

    public QuantumMemoryAddress getQubit_2() {
        return mQubit_2;
    }

    public QuantumMemoryAddress getQubit_3() {
        return mQubit_3;
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

        public PhysicalAddressingCommand.Builder setFirstQubit(QuantumMemoryAddress qubitAddress) {
            physicalAddressingCommand.mQubit_1 = qubitAddress;
            return this;
        }

        public PhysicalAddressingCommand.Builder setSecondQubit(QuantumMemoryAddress qubitAddress) {
            physicalAddressingCommand.mQubit_2 = qubitAddress;
            return this;
        }

        public PhysicalAddressingCommand.Builder setThirdQubit(QuantumMemoryAddress qubitAddress) {
            physicalAddressingCommand.mQubit_3 = qubitAddress;
            return this;
        }

        public PhysicalAddressingCommand build() {
            return physicalAddressingCommand;
        }
    }
}
