package kpfu.magistracy.controller.execution.results;

import kpfu.terentyev.quantum.api.KazanModel.QuantumMemoryAddress;

public class LowLevelResult {

    private QuantumMemoryAddress mQuantumMemoryAddress;

    private boolean mMeasureResult;

    public LowLevelResult(QuantumMemoryAddress quantumMemoryAddress, boolean measureResult) {
        this.mQuantumMemoryAddress = quantumMemoryAddress;
        this.mMeasureResult = measureResult;
    }

    public QuantumMemoryAddress getQuantumMemoryAddress() {
        return mQuantumMemoryAddress;
    }

    public boolean isZero() {
        return mMeasureResult;
    }
}
