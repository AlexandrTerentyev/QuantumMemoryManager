package kpfu.magistracy.controller.execution.commands;

import kpfu.magistracy.controller.addresses.GlobalQubitAddress;

public class PhysicalAddressingCommand extends GeneralCommand<GlobalQubitAddress> {

    public static class Builder {

        private PhysicalAddressingCommand physicalAddressingCommand;

        public Builder() {
            physicalAddressingCommand = new PhysicalAddressingCommand();
        }

        public PhysicalAddressingCommand.Builder setCommand(CommandTypes commandType) {
            physicalAddressingCommand.setCommandType(commandType);
            return this;
        }

        public PhysicalAddressingCommand.Builder setCommandParam(Double commandParam) {
            physicalAddressingCommand.setCommandParam(commandParam);
            return this;
        }

        public PhysicalAddressingCommand.Builder setFirstQubit_Part1(GlobalQubitAddress qubitAddress) {
            physicalAddressingCommand.setFirstQubit_Part1(qubitAddress);
            return this;
        }

        public PhysicalAddressingCommand.Builder setFirstQubit_Part2(GlobalQubitAddress qubitAddress) {
            physicalAddressingCommand.setFirstQubit_Part2(qubitAddress);
            return this;
        }

        public PhysicalAddressingCommand.Builder setSecondQubit_Part1(GlobalQubitAddress qubitAddress) {
            physicalAddressingCommand.setSecondQubit_Part1(qubitAddress);
            return this;
        }

        public PhysicalAddressingCommand.Builder setSecondQubit_Part2(GlobalQubitAddress qubitAddress) {
            physicalAddressingCommand.setSecondQubit_Part2(qubitAddress);
            return this;
        }

        public PhysicalAddressingCommand build() {
            return physicalAddressingCommand;
        }
    }
}
