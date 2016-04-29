package kpfu.magistracy.service_for_controller;


public class OwnerData {

    protected String userId;
    //time, when user's commands list was transformed
    protected long timeStamp;

    public OwnerData(String userId, long timeStamp) {
        this.userId = userId;
        this.timeStamp = timeStamp;
    }
    public String getUserId() {
        return userId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OwnerData)) return false;

        OwnerData that = (OwnerData) o;

        return getUserId() == that.getUserId() && getTimeStamp() == that.getTimeStamp();

    }

}
