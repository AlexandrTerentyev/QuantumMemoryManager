package kpfu.magistracy.controller.execution.results;

import kpfu.magistracy.controller.addresses.LogicalQubitAddress;

import java.util.HashMap;
import java.util.Map;

public class GeneralResult {

    private long mUserId;

    private int mUserCommandNumber;

    private Map<LogicalQubitAddress, Boolean> mMeasureResults;

    public GeneralResult(long userId, int userCommandNumber) {
        mUserId = userId;
        mUserCommandNumber = userCommandNumber;
        mMeasureResults = new HashMap<LogicalQubitAddress, Boolean>();
    }

    public void addMeasureResult(LogicalQubitAddress logicalQubitAddress, Boolean result) {
        mMeasureResults.put(logicalQubitAddress, result);
    }

    public long getUserId() {
        return mUserId;
    }

    public int getUserCommandNumber() {
        return mUserCommandNumber;
    }
}
