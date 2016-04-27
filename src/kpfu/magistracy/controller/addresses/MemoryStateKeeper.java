package kpfu.magistracy.controller.addresses;

import kpfu.magistracy.controller.memory.QuantumMemory;
import kpfu.terentyev.quantum.api.KazanModel.QuantumMemoryAddress;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryStateKeeper {

    private Map<LogicalQubitAddress, QuantumMemoryAddress> mMemoryAddresses;

    private QuantumMemory mQuantumMemory;

    private int mMaxQubitCount;

    private Long lastUsedFrequency;

    private Long lastUsedTime;

    public MemoryStateKeeper(QuantumMemory quantumMemory){
        mQuantumMemory = quantumMemory;
        mMaxQubitCount = (int) Math.min(
                quantumMemory.getMaxMemoryTimeCycle() / quantumMemory.getMemoryTimeStep(),
                (quantumMemory.getMaxMemoryFrequency() - quantumMemory.getMinMemoryFrequency())/quantumMemory.getFrequencyStep()
        );
        mMemoryAddresses = new HashMap<LogicalQubitAddress, QuantumMemoryAddress>();
    }

    public QuantumMemoryAddress createAddressForQubit(Long qubitLogicalAddress) {
        //// TODO: 27.04.2016 create address, using lastUsedFrequency and lastUsedTime
        return null;
    }

    public void clearMemoryState(){
        mMemoryAddresses = new HashMap<LogicalQubitAddress, QuantumMemoryAddress>();
    }

    public Collection<QuantumMemoryAddress> getMemoryAddresses() {
        return mMemoryAddresses.values();
    }


}
