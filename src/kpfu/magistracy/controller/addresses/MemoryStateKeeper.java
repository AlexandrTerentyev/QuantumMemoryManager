package kpfu.magistracy.controller.addresses;

import kpfu.magistracy.controller.memory.QuantumMemory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryStateKeeper {

    private Map<GlobalQubitAddress, LogicalQubitAddress> mMemoryAddresses;

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

        mMemoryAddresses = new HashMap<GlobalQubitAddress, LogicalQubitAddress>();
        mFrequencyToUse = quantumMemory.getMinMemoryFrequency();
        mTimeToUse = 0L;
    }

    public GlobalQubitAddress getGlobalAddressForQubit(LogicalQubitAddress qubitLogicalAddress) {
        //// TODO: 27.04.2016 create address, using frequencyToUse and timeToUse (or return old)

        if (mFrequencyToUse > mMaxFrequencyToUse || mTimeToUse > mMaxTimeToUse)
            throw new IllegalStateException("Wrong attempt to add another one qubit while memory is completely full");


        return null;
    }

    public void clearMemoryState() {
        mMemoryAddresses = new HashMap<GlobalQubitAddress, LogicalQubitAddress>();
    }

    public Collection<GlobalQubitAddress> getMemoryAddresses() {
        return mMemoryAddresses.keySet();
    }

    public LogicalQubitAddress getLogicalQubitAddressByPhysical(GlobalQubitAddress globalQubitAddress) {
        return mMemoryAddresses.get(globalQubitAddress);
    }


}
