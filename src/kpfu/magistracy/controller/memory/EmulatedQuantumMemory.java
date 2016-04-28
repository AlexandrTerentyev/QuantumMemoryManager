package kpfu.magistracy.controller.memory;

import kpfu.magistracy.controller.execution.commands.PhysicalAddressingCommand;
import kpfu.magistracy.controller.execution.results.LowLevelResult;
import kpfu.terentyev.quantum.api.KazanModel.Emulator;

import java.util.List;

public class EmulatedQuantumMemory implements QuantumMemory {

    private Emulator mEmulator;

    public EmulatedQuantumMemory() {
        mEmulator = new Emulator(getMaxMemoryFrequency(), getMinMemoryFrequency(), getMaxMemoryTimeCycle(), getProcessingUnitsCount());
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
            mEmulator = new Emulator(getMaxMemoryFrequency(), getMinMemoryFrequency(), getMaxMemoryTimeCycle(), getProcessingUnitsCount());
        } else {
            throw new IllegalStateException("Memory already initialized");
        }
    }

    @Override
    public boolean isMemoryAvailable() {
        return mEmulator != null;
    }

    @Override
    public List<LowLevelResult> perform(List<PhysicalAddressingCommand> physicalAddressingCommands) {
        //// TODO: 28.04.2016
        return null;
    }

}
