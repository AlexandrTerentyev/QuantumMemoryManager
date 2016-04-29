package kpfu.magistracy.controller.execution;

import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressForController;
import kpfu.magistracy.controller.execution.commands.GeneralCommand;
import kpfu.magistracy.controller.execution.commands.LogicalAddressingCommand;

import java.util.List;

public class TopLevelCommandKeeper extends GeneralCommand {

    private List<LogicalAddressingCommand> mLogicalAddressingCommands;

    public List<LogicalAddressingCommand> getLogicalAddressingCommands() {
        return mLogicalAddressingCommands;
    }

    public boolean containsQubit(LogicalQubitAddressForController logicalQubitAddressForController) {
        for (LogicalAddressingCommand logicalAddressingCommand : mLogicalAddressingCommands) {
            if (logicalAddressingCommand.getQubit_1().equals(logicalQubitAddressForController)) {
                return true;
            }
            if (logicalAddressingCommand.getQubit_2().equals(logicalQubitAddressForController)) {
                return true;
            }
            if (logicalAddressingCommand.getQubit_3() != null && logicalAddressingCommand.getQubit_3().equals(logicalQubitAddressForController)) {
                return true;
            }
        }
        return false;
    }
}
