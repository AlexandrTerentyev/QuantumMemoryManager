package kpfu.magistracy.controller.memory;

import kpfu.terentyev.quantum.api.KazanModel.QuantumMemoryAddress;

import java.util.List;

public interface QuantumMemory {

    public long getMaxMemoryFrequency();

    public long getMinMemoryFrequency();

    public long getFrequencyStep();

    public int getProcessingUnitsCount();

    public long getMaxMemoryTimeCycle();

    public long getMemoryTimeStep();

    public void initMemory();

    public boolean isMemoryAvailable();

    public void clearMemoryState(List<QuantumMemoryAddress> quantumMemoryAddresses);
}
