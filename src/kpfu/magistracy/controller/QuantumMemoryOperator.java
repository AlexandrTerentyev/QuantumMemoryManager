package kpfu.magistracy.controller;

import kpfu.magistracy.controller.addresses.LogicalQubitAddress;
import kpfu.magistracy.controller.addresses.MemoryStateKeeper;
import kpfu.magistracy.controller.execution.TopLevelCommandKeeper;
import kpfu.magistracy.controller.execution.commands.LogicalAddressingCommand;
import kpfu.magistracy.controller.execution.commands.PhysicalAddressingCommand;
import kpfu.magistracy.controller.execution.results.GeneralResult;
import kpfu.magistracy.controller.execution.results.LowLevelResult;
import kpfu.magistracy.controller.memory.EmulatedQuantumMemory;
import kpfu.magistracy.controller.memory.QuantumMemory;

import java.util.ArrayList;
import java.util.List;

public class QuantumMemoryOperator {

    private static QuantumMemoryOperator mQuantumMemoryOperator;

    private QuantumMemory mQuantumMemory;

    private MemoryStateKeeper mMemoryStateKeeper;

    private QuantumMemoryOperator() {
        //todo initialize real memory or emulator
        mQuantumMemory = new EmulatedQuantumMemory();
        mMemoryStateKeeper = new MemoryStateKeeper(mQuantumMemory);
    }

    public QuantumMemoryOperator getOperator() {
        if (mQuantumMemoryOperator == null) {
            mQuantumMemoryOperator = new QuantumMemoryOperator();
        }
        return mQuantumMemoryOperator;
    }

    private void clearMemoryState() {
        mMemoryStateKeeper.clearMemoryState();
    }

    public synchronized List<GeneralResult> executeCommands(List<TopLevelCommandKeeper> topLevelCommandCommandKeepers) {
        List<PhysicalAddressingCommand> physicalAddressingCommands = transformTopLevelCommandsToLowLevel(topLevelCommandCommandKeepers);

        List<LowLevelResult> lowLevelResults = mQuantumMemory.perform(physicalAddressingCommands);
        List<GeneralResult> finalResults = new ArrayList<GeneralResult>();

        for (LowLevelResult lowLevelResult : lowLevelResults) {
            LogicalQubitAddress logicalQubitAddress = mMemoryStateKeeper.getLogicalQubitAddressByPhysical(lowLevelResult.getQuantumMemoryAddress());
            addFinalResult(topLevelCommandCommandKeepers, finalResults, lowLevelResult, logicalQubitAddress);
        }
        clearMemoryState();
        return finalResults;
    }

    private void addFinalResult(List<TopLevelCommandKeeper> topLevelCommandCommandKeepers, List<GeneralResult> finalResults, LowLevelResult lowLevelResult, LogicalQubitAddress logicalQubitAddress) {
        for (TopLevelCommandKeeper topLevelCommandKeeper : topLevelCommandCommandKeepers) {
            if (topLevelCommandKeeper.containsQubit(logicalQubitAddress)) {

                for (GeneralResult generalResult : finalResults) {
                    if (generalResult.getUserId() == topLevelCommandKeeper.getUserId() && generalResult.getUserCommandNumber() == topLevelCommandKeeper.getUserCommandNumber()) {
                        generalResult.addMeasureResult(logicalQubitAddress, lowLevelResult.isZero());
                        break;
                    }
                }

                GeneralResult generalResult = new GeneralResult(topLevelCommandKeeper.getUserId(), topLevelCommandKeeper.getUserCommandNumber());
                generalResult.addMeasureResult(logicalQubitAddress, lowLevelResult.isZero());
                finalResults.add(generalResult);

            }
        }
    }

    private List<PhysicalAddressingCommand> transformTopLevelCommandsToLowLevel(List<TopLevelCommandKeeper> topLevelCommandCommandKeepers) {
        List<PhysicalAddressingCommand> physicalAddressingCommands = new ArrayList<PhysicalAddressingCommand>();

        for (TopLevelCommandKeeper topLevelCommandKeeper : topLevelCommandCommandKeepers) {
            for (LogicalAddressingCommand logicalAddressingCommand : topLevelCommandKeeper.getLogicalAddressingCommands()) {
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
        }
        return physicalAddressingCommands;
    }
}
