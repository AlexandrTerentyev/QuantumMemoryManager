package kpfu.magistracy.controller.addresses;

public class LogicalQubitAddress {
    //todo for Andrey
    private long systemCurrentTime;

    private int memoryPart;

    public long getSystemCurrentTime() {
        return systemCurrentTime;
    }

    public int getMemoryPart() {
        return memoryPart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogicalQubitAddress)) return false;

        LogicalQubitAddress that = (LogicalQubitAddress) o;

        return getSystemCurrentTime() == that.getSystemCurrentTime() && getMemoryPart() == that.getMemoryPart();

    }

}
