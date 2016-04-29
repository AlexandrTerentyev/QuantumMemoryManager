package kpfu.magistracy.controller;

import kpfu.magistracy.controller.addresses.MemoryStateKeeper;
import kpfu.magistracy.controller.execution.commands.InitCommand;
import kpfu.magistracy.controller.execution.commands.LogicalAddressingCommand;
import kpfu.magistracy.controller.execution.commands.MeasureCommand;
import kpfu.magistracy.controller.execution.commands.PhysicalAddressingCommand;
import kpfu.magistracy.controller.execution.results.LowLevelResult;
import kpfu.magistracy.controller.memory.EmulatedQuantumMemory;
import kpfu.magistracy.controller.memory.QuantumMemory;
import kpfu.magistracy.service_for_controller.OwnerData;
import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressForController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuantumMemoryOperator {

    private static QuantumMemoryOperator mQuantumMemoryOperator;

    private QuantumMemory mQuantumMemory;

    private MemoryStateKeeper mMemoryStateKeeper;

    private Map<LogicalQubitAddressForController, OwnerData> logicalQubitAddressOwnerDataMap;


    private QuantumMemoryOperator() {
        //todo initialize real memory or emulator
        mQuantumMemory = new EmulatedQuantumMemory();
        mMemoryStateKeeper = new MemoryStateKeeper(mQuantumMemory);
        logicalQubitAddressOwnerDataMap = new HashMap<LogicalQubitAddressForController, OwnerData>();

    }

    public static QuantumMemoryOperator getOperator() {
        if (mQuantumMemoryOperator == null) {
            mQuantumMemoryOperator = new QuantumMemoryOperator();
        }
        return mQuantumMemoryOperator;
    }

    public int getCommandsMaxCount() {
        //todo
        return 100;
    }

    public int getQubitsMaxCount() {
        return mMemoryStateKeeper.getMaxQubitCount();
    }

    private void clearMemoryState() {
        mMemoryStateKeeper.clearMemoryState();
    }

    public synchronized Map<OwnerData, Map<LogicalQubitAddressForController, Boolean>> executeCommands(Map<OwnerData, List<LogicalAddressingCommand>> logicalCommands) {
        logicalQubitAddressOwnerDataMap.clear();

        List<PhysicalAddressingCommand> physicalAddressingCommands = transformTopLevelCommandsToLowLevel(logicalCommands);
        List<LowLevelResult> lowLevelResults = mQuantumMemory.perform(physicalAddressingCommands);

        Map<OwnerData, Map<LogicalQubitAddressForController, Boolean>> finalResults = new HashMap<OwnerData, Map<LogicalQubitAddressForController, Boolean>>();
        for (LowLevelResult lowLevelResult : lowLevelResults) {
            LogicalQubitAddressForController logicalQubitAddress = mMemoryStateKeeper.getLogicalQubitAddressByPhysical(lowLevelResult.getGlobalQubitAddress());

            OwnerData ownerData = logicalQubitAddressOwnerDataMap.get(logicalQubitAddress);
            if (!finalResults.containsKey(ownerData)) {
                Map<LogicalQubitAddressForController, Boolean> measureResultMap = new HashMap<LogicalQubitAddressForController, Boolean>();
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
        //todo write comments for method body

        List<PhysicalAddressingCommand> physicalAddressingCommands = new ArrayList<PhysicalAddressingCommand>();

        List<PhysicalAddressingCommand> tempList = new ArrayList<PhysicalAddressingCommand>();
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

                if (!logicalAddressingCommand.isSecondLogicalQubitNull()) {
                    physicalAddressingCommandBuilder
                            .setSecondQubit_Part1(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getSecondQubit_Part1()))
                            .setSecondQubit_Part2(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getSecondQubit_Part2()));
                }
                PhysicalAddressingCommand physicalAddressingCommand = physicalAddressingCommandBuilder.build();
                if (mMemoryStateKeeper.needInitializeLogicalQubit(physicalAddressingCommand.getFirstQubit_Part1(), physicalAddressingCommand.getFirstQubit_Part2())) {
                    tempList.add(new InitCommand(physicalAddressingCommand.getFirstQubit_Part1(), physicalAddressingCommand.getFirstQubit_Part2()));
                }
                if (!logicalAddressingCommand.isSecondLogicalQubitNull()) {
                    if (mMemoryStateKeeper.needInitializeLogicalQubit(physicalAddressingCommand.getSecondQubit_Part1(), physicalAddressingCommand.getSecondQubit_Part2())) {
                        tempList.add(new InitCommand(physicalAddressingCommand.getSecondQubit_Part1(), physicalAddressingCommand.getSecondQubit_Part2()));
                    }
                }
                tempList.add(physicalAddressingCommand);
            }
            physicalAddressingCommands.addAll(tempList);
            for (PhysicalAddressingCommand physicalAddressingCommand : tempList) {
                physicalAddressingCommands.add(new MeasureCommand(physicalAddressingCommand.getFirstQubit_Part1()));
                physicalAddressingCommands.add(new MeasureCommand(physicalAddressingCommand.getFirstQubit_Part2()));

                if (!physicalAddressingCommand.isSecondLogicalQubitNull()) {
                    physicalAddressingCommands.add(new MeasureCommand(physicalAddressingCommand.getSecondQubit_Part1()));
                    physicalAddressingCommands.add(new MeasureCommand(physicalAddressingCommand.getSecondQubit_Part2()));
                }
            }
            mMemoryStateKeeper.clearMemoryState();
        }
        return physicalAddressingCommands;
    }

}
