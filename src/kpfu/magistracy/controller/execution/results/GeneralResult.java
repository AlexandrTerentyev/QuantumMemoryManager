package kpfu.magistracy.controller.execution.results;

import kpfu.magistracy.service_for_controller.addresses.LogicalQubitAddressForController;

import java.util.HashMap;
import java.util.Map;

public class GeneralResult {

    private long mUserId;

    private int mUserCommandNumber;

    private Map<LogicalQubitAddressForController, Boolean> mMeasureResults;

    public GeneralResult(long userId, int userCommandNumber) {
        mUserId = userId;
        mUserCommandNumber = userCommandNumber;
        mMeasureResults = new HashMap<LogicalQubitAddressForController, Boolean>();
    }

    public void addMeasureResult(LogicalQubitAddressForController logicalQubitAddressForController, Boolean result) {
        mMeasureResults.put(logicalQubitAddressForController, result);
    }

    public long getUserId() {
        return mUserId;
    }

    public int getUserCommandNumber() {
        return mUserCommandNumber;
    }
}
