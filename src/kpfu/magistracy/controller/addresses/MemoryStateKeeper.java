package kpfu.magistracy.controller.addresses;

import kpfu.magistracy.controller.memory.QuantumMemory;
import kpfu.terentyev.quantum.api.KazanModel.QuantumMemoryAddress;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryStateKeeper {

    private Map<QuantumMemoryAddress, LogicalQubitAddress> mMemoryAddresses;

    private QuantumMemory mQuantumMemory;

    private int mMaxQubitCount;

    private long mMaxFrequencyToUse;

    private long mMaxTimeToUse;

    private Long mFrequencyToUse;

    private Long mTimeToUse;

    public MemoryStateKeeper(QuantumMemory quantumMemory) {
        mQuantumMemory = quantumMemory;
        mMaxFrequencyToUse = quantumMemory.getMaxMemoryFrequency();
        mMaxTimeToUse = quantumMemory.getMaxMemoryTimeCycle();

        mMaxQubitCount = (int) Math.min(
                quantumMemory.getMaxMemoryTimeCycle() / quantumMemory.getMemoryTimeStep(),
                (quantumMemory.getMaxMemoryFrequency() - quantumMemory.getMinMemoryFrequency()) / quantumMemory.getFrequencyStep()
        );

        mMemoryAddresses = new HashMap<QuantumMemoryAddress, LogicalQubitAddress>();
        mFrequencyToUse = quantumMemory.getMinMemoryFrequency();
        mTimeToUse = 0L;
    }

    public QuantumMemoryAddress createAddressForQubit(LogicalQubitAddress qubitLogicalAddress) {
        //// TODO: 27.04.2016 create address, using frequencyToUse and timeToUse

        if (mFrequencyToUse > mMaxFrequencyToUse || mTimeToUse > mMaxTimeToUse)
            throw new IllegalStateException("Wrong attempt to add another one qubit while memory is completely full");


        return null;
    }

    public void clearMemoryState() {
        mMemoryAddresses = new HashMap<QuantumMemoryAddress, LogicalQubitAddress>();
    }

    public Collection<QuantumMemoryAddress> getMemoryAddresses() {
        return mMemoryAddresses.keySet();
    }

    public LogicalQubitAddress getLogicalQubitAddressByPhysical(QuantumMemoryAddress quantumMemoryAddress) {
        return mMemoryAddresses.get(quantumMemoryAddress);
    }
}
