package kpfu.magistracy.controller.memory;

import kpfu.terentyev.quantum.api.KazanModel.QuantumMemoryAddress;

import java.util.Collection;

public interface QuantumMemory {

    long getMaxMemoryFrequency();

    long getMinMemoryFrequency();

    long getFrequencyStep();

    int getProcessingUnitsCount();

    long getMaxMemoryTimeCycle();

    long getMemoryTimeStep();

    void initMemory();

    boolean isMemoryAvailable();

    void clearMemoryState(Collection<QuantumMemoryAddress> quantumMemoryAddresses);
}
