package kpfu.magistracy.service_for_controller.addresses;


import kpfu.magistracy.service_for_controller.OwnerData;

public class LogicalQubitAddressFromClient {

    OwnerData mOwnerData;
    //локальный порядковый номер кубита в программе клиента
    int mLogicalQubitAddress;

    public int getLogicalQubitAddress() {
        return mLogicalQubitAddress;
    }

    public OwnerData getOwnerData() {
        return mOwnerData;
    }

    public void setOwnerData(OwnerData ownerData) {
        mOwnerData = ownerData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogicalQubitAddressFromClient)) return false;

        LogicalQubitAddressFromClient that = (LogicalQubitAddressFromClient) o;
        return getLogicalQubitAddress() == that.getLogicalQubitAddress() && getOwnerData().equals(that.getOwnerData());

    }
}
