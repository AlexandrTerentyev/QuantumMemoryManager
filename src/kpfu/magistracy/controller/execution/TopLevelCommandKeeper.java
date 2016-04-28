package kpfu.magistracy.controller.execution;

import kpfu.magistracy.controller.addresses.LogicalQubitAddress;
import kpfu.magistracy.controller.execution.commands.GeneralCommand;
import kpfu.magistracy.controller.execution.commands.LogicalAddressingCommand;

import java.util.List;

public class TopLevelCommandKeeper extends GeneralCommand {

    private List<LogicalAddressingCommand> mLogicalAddressingCommands;

    public List<LogicalAddressingCommand> getLogicalAddressingCommands() {
        return mLogicalAddressingCommands;
    }

    public boolean containsQubit(LogicalQubitAddress logicalQubitAddress) {
        for (LogicalAddressingCommand logicalAddressingCommand : mLogicalAddressingCommands) {
            if (logicalAddressingCommand.getQubit_1().equals(logicalQubitAddress)) {
                return true;
            }
            if (logicalAddressingCommand.getQubit_2().equals(logicalQubitAddress)) {
                return true;
            }
            if (logicalAddressingCommand.getQubit_3() != null && logicalAddressingCommand.getQubit_3().equals(logicalQubitAddress)) {
                return true;
            }
        }
        return false;
    }
}
