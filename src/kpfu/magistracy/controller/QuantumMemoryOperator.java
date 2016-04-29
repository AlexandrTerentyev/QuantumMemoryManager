package kpfu.magistracy.controller;

import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressForController;
import kpfu.magistracy.controller.addresses.MemoryStateKeeper;
import kpfu.magistracy.controller.execution.TopLevelCommandKeeper;
import kpfu.magistracy.controller.execution.commands.LogicalAddressingCommand;
import kpfu.magistracy.controller.execution.commands.PhysicalAddressingCommand;
import kpfu.magistracy.controller.execution.results.GeneralResult;
import kpfu.magistracy.controller.execution.results.LowLevelResult;
import kpfu.magistracy.controller.memory.EmulatedQuantumMemory;
import kpfu.magistracy.controller.memory.QuantumMemory;
import kpfu.magistracy.service_for_controller.OwnerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuantumMemoryOperator {

    private static QuantumMemoryOperator mQuantumMemoryOperator;

    private QuantumMemory mQuantumMemory;

    private MemoryStateKeeper mMemoryStateKeeper;

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

    public synchronized List<GeneralResult> executeCommands(Map<OwnerData,List<LogicalAddressingCommand>> logicalCommands) {
        List<PhysicalAddressingCommand> physicalAddressingCommands = transformTopLevelCommandsToLowLevel(logicalCommands.values());

        List<LowLevelResult> lowLevelResults = mQuantumMemory.perform(physicalAddressingCommands);
        List<GeneralResult> finalResults = new ArrayList<GeneralResult>();

        for (LowLevelResult lowLevelResult : lowLevelResults) {
            LogicalQubitAddressForController logicalQubitAddressForController = mMemoryStateKeeper.getLogicalQubitAddressByPhysical(lowLevelResult.getQuantumMemoryAddress());
            addFinalResult(topLevelCommandCommandKeepers, finalResults, lowLevelResult, logicalQubitAddressForController);
        }
        clearMemoryState();
        return finalResults;
    }

    private void addFinalResult(List<TopLevelCommandKeeper> topLevelCommandCommandKeepers, List<GeneralResult> finalResults, LowLevelResult lowLevelResult, LogicalQubitAddressForController logicalQubitAddressForController) {
        for (TopLevelCommandKeeper topLevelCommandKeeper : topLevelCommandCommandKeepers) {
            if (topLevelCommandKeeper.containsQubit(logicalQubitAddressForController)) {

                for (GeneralResult generalResult : finalResults) {
                    if (generalResult.getUserId() == topLevelCommandKeeper.getUserId() && generalResult.getUserCommandNumber() == topLevelCommandKeeper.getUserCommandNumber()) {
                        generalResult.addMeasureResult(logicalQubitAddressForController, lowLevelResult.isZero());
                        break;
                    }
                }

                GeneralResult generalResult = new GeneralResult(topLevelCommandKeeper.getUserId(), topLevelCommandKeeper.getUserCommandNumber());
                generalResult.addMeasureResult(logicalQubitAddressForController, lowLevelResult.isZero());
                finalResults.add(generalResult);

            }
        }
    }

    private List<PhysicalAddressingCommand> transformTopLevelCommandsToLowLevel(List<LogicalAddressingCommand> logicalAddressingCommands) {
        List<PhysicalAddressingCommand> physicalAddressingCommands = new ArrayList<PhysicalAddressingCommand>();

            for (LogicalAddressingCommand logicalAddressingCommand : logicalAddressingCommands) {
                PhysicalAddressingCommand.Builder physicalAddressingCommandBuilder = new PhysicalAddressingCommand.Builder();
                physicalAddressingCommandBuilder
                        .setCommand(logicalAddressingCommand.getCommandType())
                        .setCommandParam(logicalAddressingCommand.getCommandParam())
                        .setFirstQubit(mMemoryStateKeeper.createAddressForQubit(logicalAddressingCommand.getQubit_1()))
                        .setSecondQubit(mMemoryStateKeeper.createAddressForQubit(logicalAddressingCommand.getQubit_2()));
                if (logicalAddressingCommand.getQubit_3() != null) {
                    physicalAddressingCommandBuilder.setThirdQubit(mMemoryStateKeeper.createAddressForQubit(logicalAddressingCommand.getQubit_3()));
                }
                physicalAddressingCommands.add(physicalAddressingCommandBuilder.build());
        }
        return physicalAddressingCommands;
    }
}
