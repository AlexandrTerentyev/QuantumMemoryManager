package com.company;

import kpfu.magistracy.controller.QuantumMemoryOperator;
import kpfu.magistracy.controller.execution.commands.CommandTypes;
import kpfu.magistracy.controller.execution.commands.LogicalAddressingCommand;
import kpfu.magistracy.service_for_controller.OwnerData;
import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressForController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        OwnerData ownerData1 = new OwnerData("1", System.currentTimeMillis());
        OwnerData ownerData2 = new OwnerData("2", System.currentTimeMillis());

        LogicalQubitAddressForController logicalQubitAddressForController1 = new LogicalQubitAddressForController(1, 0);
        LogicalQubitAddressForController logicalQubitAddressForController2 = new LogicalQubitAddressForController(1, 1);
        LogicalAddressingCommand.Builder builder = new LogicalAddressingCommand.Builder();
        builder.setCommand(CommandTypes.PHASE)
                .setCommandParam(0.9)
                .setFirstQubit_Part1(logicalQubitAddressForController1)
                .setFirstQubit_Part2(logicalQubitAddressForController2);

        List<LogicalAddressingCommand> logicalAddressingCommandList = new ArrayList<LogicalAddressingCommand>();
        logicalAddressingCommandList.add(builder.build());

        Map<OwnerData, List<LogicalAddressingCommand>> mapWithCommands = new HashMap<OwnerData, List<LogicalAddressingCommand>>();
        mapWithCommands.put(ownerData1, logicalAddressingCommandList);

        QuantumMemoryOperator quantumMemoryOperator = QuantumMemoryOperator.getOperator();
        Map<OwnerData, Map<LogicalQubitAddressForController, Boolean>> mapWithResults = quantumMemoryOperator.executeCommands(mapWithCommands);

        for (Map.Entry<OwnerData, Map<LogicalQubitAddressForController, Boolean>> mapEntry : mapWithResults.entrySet()) {
            for (Map.Entry<LogicalQubitAddressForController, Boolean> innerEntry : mapEntry.getValue().entrySet()) {
                System.out.println("OwnerData = " + mapEntry.getKey() + ", LogicalAddress = " + innerEntry.getKey() + ", Value = " + innerEntry.getValue());
            }
        }
    }
}
