package kpfu.magistracy.controller;

import kpfu.magistracy.controller.addresses.MemoryStateKeeper;
import kpfu.magistracy.controller.execution.commands.*;
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

    //Map for retrieving OwnerData using LogicalQubitAddressForController (for return results)
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

    /**
     * @return command number count, which controller can process
     */
    public int getCommandsMaxCount() {
        //todo
        return 100;
    }

    /**
     * @return max qubit count, which client can use in a single command pack
     */
    public int getQubitsMaxCount() {
        return mMemoryStateKeeper.getMaxQubitCount();
    }


    public synchronized Map<OwnerData, Map<LogicalQubitAddressForController, Boolean>> executeCommands(Map<OwnerData, List<LogicalAddressingCommand>> logicalCommands) {
        List<PhysicalAddressingCommand> physicalAddressingCommands = transformTopLevelCommandsToLowLevel(logicalCommands);
        List<LowLevelResult> lowLevelResults = mQuantumMemory.perform(physicalAddressingCommands);

        Map<OwnerData, Map<LogicalQubitAddressForController, Boolean>> finalResults = new HashMap<OwnerData, Map<LogicalQubitAddressForController, Boolean>>();
        for (LowLevelResult lowLevelResult : lowLevelResults) {
            LogicalQubitAddressForController logicalQubitAddress = mMemoryStateKeeper.getLogicalQubitAddressByPhysical(lowLevelResult.getGlobalQubitAddress());

            OwnerData ownerData = logicalQubitAddressOwnerDataMap.get(logicalQubitAddress);
            if (!finalResults.containsKey(ownerData)) {
                Map<LogicalQubitAddressForController, Boolean> measureResultMap = new HashMap<LogicalQubitAddressForController, Boolean>();
                measureResultMap.put(logicalQubitAddress, lowLevelResult.isOne());
                finalResults.put(ownerData, measureResultMap);
            } else {
                finalResults.get(ownerData).put(logicalQubitAddress, lowLevelResult.isOne());
            }
        }

        logicalQubitAddressOwnerDataMap.clear();
        mMemoryStateKeeper.clearMemoryData();
        return finalResults;
    }

    /**
     * @param logicalCommands commands, in which we should transform qubit addresses
     * @return list with commands, prepared for memory performance
     */
    private List<PhysicalAddressingCommand> transformTopLevelCommandsToLowLevel(Map<OwnerData, List<LogicalAddressingCommand>> logicalCommands) {
        List<PhysicalAddressingCommand> physicalAddressingCommands = new ArrayList<PhysicalAddressingCommand>();

        List<PhysicalAddressingCommand> tempList = new ArrayList<PhysicalAddressingCommand>();
        for (Map.Entry<OwnerData, List<LogicalAddressingCommand>> commandEntry : logicalCommands.entrySet()) {
            for (LogicalAddressingCommand logicalAddressingCommand : commandEntry.getValue()) {

                //fill map for retrieving owner data later
                if (!logicalQubitAddressOwnerDataMap.containsKey(logicalAddressingCommand.getFirstQubit_Part1())) {
                    logicalQubitAddressOwnerDataMap.put(logicalAddressingCommand.getFirstQubit_Part1(), commandEntry.getKey());
                }
                if (!logicalQubitAddressOwnerDataMap.containsKey(logicalAddressingCommand.getFirstQubit_Part2())) {
                    logicalQubitAddressOwnerDataMap.put(logicalAddressingCommand.getFirstQubit_Part2(), commandEntry.getKey());
                }
                if (!logicalAddressingCommand.isSecondLogicalQubitNull()) {
                    logicalQubitAddressOwnerDataMap.put(logicalAddressingCommand.getSecondQubit_Part1(), commandEntry.getKey());
                    logicalQubitAddressOwnerDataMap.put(logicalAddressingCommand.getSecondQubit_Part2(), commandEntry.getKey());
                }

                //create physical command with first qubit
                PhysicalAddressingCommand.Builder physicalAddressingCommandBuilder = new PhysicalAddressingCommand.Builder();
                physicalAddressingCommandBuilder
                        .setCommand(logicalAddressingCommand.getCommandType())
                        .setCommandParam(logicalAddressingCommand.getCommandParam())
                        .setFirstQubit_Part1(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getFirstQubit_Part1()))
                        .setFirstQubit_Part2(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getFirstQubit_Part2()));

                //if second logical qubit is not null - get address for it's parts and add to command
                if (!logicalAddressingCommand.isSecondLogicalQubitNull()) {
                    physicalAddressingCommandBuilder
                            .setSecondQubit_Part1(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getSecondQubit_Part1()))
                            .setSecondQubit_Part2(mMemoryStateKeeper.getGlobalAddressForQubit(logicalAddressingCommand.getSecondQubit_Part2()));
                }
                PhysicalAddressingCommand physicalAddressingCommand = physicalAddressingCommandBuilder.build();
                //add Init commands for qubits if it is necessary
                if (mMemoryStateKeeper.needInitializeLogicalQubit(physicalAddressingCommand.getFirstQubit_Part1(), physicalAddressingCommand.getFirstQubit_Part2())) {
                    tempList.add(new InitCommand(physicalAddressingCommand.getFirstQubit_Part1(), physicalAddressingCommand.getFirstQubit_Part2()));
                    mMemoryStateKeeper.onQubitInitialized(physicalAddressingCommand.getFirstQubit_Part1());
                    mMemoryStateKeeper.onQubitInitialized(physicalAddressingCommand.getFirstQubit_Part2());
                }
                if (!logicalAddressingCommand.isSecondLogicalQubitNull()) {
                    if (mMemoryStateKeeper.needInitializeLogicalQubit(physicalAddressingCommand.getSecondQubit_Part1(), physicalAddressingCommand.getSecondQubit_Part2())) {
                        tempList.add(new InitCommand(physicalAddressingCommand.getSecondQubit_Part1(), physicalAddressingCommand.getSecondQubit_Part2()));
                        mMemoryStateKeeper.onQubitInitialized(physicalAddressingCommand.getSecondQubit_Part1());
                        mMemoryStateKeeper.onQubitInitialized(physicalAddressingCommand.getSecondQubit_Part2());
                    }
                }
                tempList.add(physicalAddressingCommand);
            }
            physicalAddressingCommands.addAll(tempList);
            //add measure commands for every init command
            for (PhysicalAddressingCommand physicalAddressingCommand : tempList) {
                if (physicalAddressingCommand.getCommandType() == CommandTypes.INIT) {
                    physicalAddressingCommands.add(new MeasureCommand(physicalAddressingCommand.getFirstQubit_Part1()));
                    physicalAddressingCommands.add(new MeasureCommand(physicalAddressingCommand.getFirstQubit_Part2()));

                    if (!physicalAddressingCommand.isSecondLogicalQubitNull()) {
                        physicalAddressingCommands.add(new MeasureCommand(physicalAddressingCommand.getSecondQubit_Part1()));
                        physicalAddressingCommands.add(new MeasureCommand(physicalAddressingCommand.getSecondQubit_Part2()));
                    }
                }
            }
            tempList.clear();
            //clear memory params 'cause we measure all previous qubits
            mMemoryStateKeeper.clearMemoryParams();
        }
        return physicalAddressingCommands;
    }

}
