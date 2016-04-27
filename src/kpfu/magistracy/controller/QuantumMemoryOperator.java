package kpfu.magistracy.controller;

import kpfu.magistracy.controller.addresses.MemoryStateKeeper;
import kpfu.magistracy.controller.execution.GeneralCommand;
import kpfu.magistracy.controller.execution.GeneralResult;
import kpfu.magistracy.controller.memory.EmulatedQuantumMemory;
import kpfu.magistracy.controller.memory.QuantumMemory;

import java.util.List;

public class QuantumMemoryOperator {

    private static QuantumMemory mQuantumMemory;

    private static MemoryStateKeeper mMemoryStateKeeper;

    public static void initialize(){
        //todo initialize real memory or emulator
        mQuantumMemory = new EmulatedQuantumMemory();
        mMemoryStateKeeper = new MemoryStateKeeper(mQuantumMemory);
    }

    public static void clearMemoryState(){
        mQuantumMemory.clearMemoryState(mMemoryStateKeeper.getMemoryAddresses());
        mMemoryStateKeeper.clearMemoryState();
    }

    public synchronized List<GeneralResult> executeCommands(List<GeneralCommand> generalCommands) {
        //// TODO: 27.04.2016 execute commands
        return null;
    }
}
