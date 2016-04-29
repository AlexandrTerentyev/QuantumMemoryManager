package kpfu.magistracy.controller.execution.results;

import kpfu.magistracy.controller.addresses.GlobalQubitAddress;

public class LowLevelResult {


    private GlobalQubitAddress mGlobalQubitAddress;

    private boolean mMeasureResult;

    public LowLevelResult(GlobalQubitAddress globalQubitAddress, boolean measureResult) {
        this.mGlobalQubitAddress = globalQubitAddress;
        this.mMeasureResult = measureResult;
    }

    public GlobalQubitAddress getGlobalQubitAddress() {
        return mGlobalQubitAddress;
    }

    public boolean isZero() {
        return mMeasureResult;
    }
}
