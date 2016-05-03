package kpfu.magistracy.service_for_controller.addresses;


import com.google.gson.annotations.SerializedName;
import kpfu.magistracy.service_for_controller.OwnerData;

public class LogicalQubitAddressFromClient {

    //локальный порядковый номер кубита в программе клиента
    @SerializedName("local_id")
    int mLogicalQubitAddress;
    //data not from a client
    OwnerData mOwnerData;

    public LogicalQubitAddressFromClient(int logicalQubitAddress) {
        mLogicalQubitAddress = logicalQubitAddress;
    }

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
