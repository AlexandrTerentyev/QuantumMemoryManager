package kpfu.magistracy.controller;

import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressForController;
import kpfu.magistracy.controller.addresses.MemoryStateKeeper;
import kpfu.magistracy.controller.execution.commands.LogicalAddressingCommand;
import kpfu.magistracy.controller.execution.commands.PhysicalAddressingCommand;
import kpfu.magistracy.controller.execution.results.LowLevelResult;
import kpfu.magistracy.controller.memory.EmulatedQuantumMemory;
import kpfu.magistracy.controller.memory.QuantumMemory;
import kpfu.magistracy.service_for_controller.OwnerData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuantumMemoryOperator {

    private static QuantumMemoryOperator mQuantumMemoryOperator;

    private QuantumMemory mQuantumMemory;

    private MemoryStateKeeper mMemoryStateKeeper;

    private Map<LogicalQubitAddress, OwnerData> logicalQubitAddressOwnerDataMap;


    private QuantumMemoryOperator() {
        //todo initialize real memory or emulator
        mQuantumMemory = new EmulatedQuantumMemory();
        mMemoryStateKeeper = new MemoryStateKeeper(mQuantumMemory);
    }

    public static QuantumMemoryOperator getOperator() {
        if (mQuantumMemoryOperator == null) {
            mQuantumMemoryOperator = new QuantumMemoryOperator();
        }
        return mQuantumMemoryOperator;
    }

    public int getCommandsMaxCount(){
        //todo
        return 100;
    }
    public int getQubitsMaxCount(){
        return mMemoryStateKeeper.getMaxQubitCount();
    }
    private void clearMemoryState() {
        mMemoryStateKeeper.clearMemoryState();
    }

    public synchronized Map<OwnerData, Map<LogicalQubitAddress, Boolean>> executeCommands(Map<OwnerData, List<LogicalAddressingCommand>> logicalCommands) {
        logicalQubitAddressOwnerDataMap = new HashMap<LogicalQubitAddress, OwnerData>();

        List<PhysicalAddressingCommand> physicalAddressingCommands = transformTopLevelCommandsToLowLevel(logicalCommands);
        List<LowLevelResult> lowLevelResults = mQuantumMemory.perform(physicalAddressingCommands);

        Map<OwnerData, Map<LogicalQubitAddressForController, Boolean>> finalResults = new HashMap<OwnerData, Map<LogicalQubitAddress, Boolean>>();
        for (LowLevelResult lowLevelResult : lowLevelResults) {
            LogicalQubitAddressForController logicalQubitAddress = mMemoryStateKeeper.getLogicalQubitAddressByPhysical(lowLevelResult.getGlobalQubitAddress());

            OwnerData ownerData = logicalQubitAddressOwnerDataMap.get(logicalQubitAddress);
            if (!finalResults.containsKey(ownerData)) {
                Map<LogicalQubitAddress, Boolean> measureResultMap = new HashMap<LogicalQubitAddress, Boolean>();
                measureResultMap.put(logicalQubitAddress, lowLevelResult.isZero());
                finalResults.put(ownerData, measureResultMap);
            } else {
                finalResults.get(ownerData).put(logicalQubitAddress, lowLevelResult.isZero());
            }
        }

        clearMemoryState();
        return finalResults;
    }

    private List<PhysicalAddressingCommand> transformTopLevelCommandsToLowLevel(Map<OwnerData, List<LogicalAddressingCommand>> logicalCommands) {
        List<PhysicalAddressingCommand> physicalAddressingCommands = new ArrayList<PhysicalAddressingCommand>();
        for (Map.Entry<OwnerData, List<LogicalAddressingCommand>> commandEntry : logicalCommands.entrySet()) {
            for (LogicalAddressingCommand logicalAddressingCommand : commandEntry.getValue()) {
                if (!logicalQubitAddressOwnerDataMap.containsKey(logicalAddressingCommand.getFirstQubit_Part1())) {
                    logicalQubitAddressOwnerDataMap.put(logicalAddressingCommand.getFirstQubit_Part1(), commandEntry.getKey());
                }
                if (!logicalQubitAddressOwnerDataMap.containsKey(logicalAddressingCommand.getFirstQubit_Part2())) {
                    logicalQubitAddressOwnerDataMap.put(logicalAddressingCommand.getFirstQubit_Part2(), commandEntry.getKey());
                }
                if (logicalAddressingCommand.getSecondQubit_Part1() != null && !logicalQubitAddressOwnerDataMap.containsKey(logicalAddressingCommand.getSecondQubit_Part1())) {
                    logicalQubitAddressOwnerDataMap.put(logicalAddressingCommand.getSecondQubit_Part1(), commandEntry.getKey());
                }

                PhysicalAddressingCommand.Builder physicalAddressingCommandBuilder = new PhysicalAddressingCommand.Builder();
                physicalAddressingCommandBuilder
                        .setCommand(logicalAddressingCommand.getCommandType())
                        .setCommandParam(logicalAddressingCommand.getCommandParam())
                        .setFirstQubit_Part1(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getFirstQubit_Part1()))
                        .setFirstQubit_Part2(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getFirstQubit_Part2()));
                if (logicalAddressingCommand.getSecondQubit_Part1() != null) {
                    physicalAddressingCommandBuilder.setSecondQubit_Part1(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getSecondQubit_Part1()));
                    if (logicalAddressingCommand.getSecondQubit_Part2() != null) {
                        physicalAddressingCommandBuilder.setSecondQubit_Part2(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getSecondQubit_Part2()));
                    }
                }
                //todo add init commands
                physicalAddressingCommands.add(physicalAddressingCommandBuilder.build());
            }
            //todo add measure commands
            mMemoryStateKeeper.clearMemoryState();
        }
        return physicalAddressingCommands;
    }

}
