package kpfu.magistracy.controller.memory;

import kpfu.terentyev.quantum.api.KazanModel.Emulator;
import kpfu.terentyev.quantum.api.KazanModel.QuantumMemoryAddress;

import java.util.List;

public class EmulatedQuantumMemory implements QuantumMemory {

    private Emulator mEmulator;

    public EmulatedQuantumMemory(){
        mEmulator = new Emulator(getMaxMemoryFrequency(),getMinMemoryFrequency(),getMaxMemoryTimeCycle(),getProcessingUnitsCount());
    }

    @Override
    public long getMaxMemoryFrequency() {
        return 200;
    }

    @Override
    public long getMinMemoryFrequency() {
        return 50;
    }

    @Override
    public long getFrequencyStep() {
        return 1;
    }

    @Override
    public int getProcessingUnitsCount() {
        return 2;
    }

    @Override
    public long getMaxMemoryTimeCycle() {
        return 50;
    }

    @Override
    public long getMemoryTimeStep() {
        return 0;
    }

    @Override
    public void initMemory() {
        if (mEmulator == null) {
            mEmulator = new Emulator(getMaxMemoryFrequency(),getMinMemoryFrequency(),getMaxMemoryTimeCycle(),getProcessingUnitsCount());
        } else {
            throw new IllegalStateException("Memory already initialized");
        }
    }

    @Override
    public boolean isMemoryAvailable() {
        return mEmulator != null;
    }

    @Override
    public void clearMemoryState(List<QuantumMemoryAddress> quantumMemoryAddresses) {
        for (QuantumMemoryAddress quantumMemoryAddress : quantumMemoryAddresses){
            mEmulator.measure(quantumMemoryAddress);
        }
    }

}
