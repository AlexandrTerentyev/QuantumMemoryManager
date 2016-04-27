package kpfu.magistracy.controller.addresses;

import kpfu.magistracy.controller.memory.QuantumMemory;
import kpfu.terentyev.quantum.api.KazanModel.QuantumMemoryAddress;

import java.util.ArrayList;
import java.util.List;

public class MemoryStateKeeper {

    private List<QuantumMemoryAddress> mMemoryAddresses;

    private int mMaxQubitCount;

    public MemoryStateKeeper(QuantumMemory quantumMemory){
        int mMaxQubitCount = (int) Math.min(
                quantumMemory.getMaxMemoryTimeCycle() / quantumMemory.getMemoryTimeStep(),
                (quantumMemory.getMaxMemoryFrequency() - quantumMemory.getMinMemoryFrequency())/quantumMemory.getFrequencyStep()
        );
        mMemoryAddresses = new ArrayList<QuantumMemoryAddress>();
    }

    public void clearMemoryState(){
        mMemoryAddresses = new ArrayList<QuantumMemoryAddress>();
    }

    public List<QuantumMemoryAddress> getMemoryAddresses() {
        return mMemoryAddresses;
    }
}
