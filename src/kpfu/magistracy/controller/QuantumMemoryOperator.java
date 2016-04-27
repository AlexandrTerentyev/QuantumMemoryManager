package kpfu.magistracy.controller;

import kpfu.magistracy.controller.memory.EmulatedQuantumMemory;
import kpfu.magistracy.controller.addresses.MemoryStateKeeper;
import kpfu.magistracy.controller.memory.QuantumMemory;

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
}
