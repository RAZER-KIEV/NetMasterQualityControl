package ua.kiev.netmaster.netmasterqualitycontrol.enums;

/**
 * Created by RAZER on 2/26/2016.
 */
public enum TaskType {
    USER_CONNECTING("User Connecting"),
    REPAIR("Repair"),
    BOX_INSTALL("Box installation"),
    CABLE_INSTALL("Cable installation"),
    OTHER("Other");

    private String friendlyName;
    TaskType(String s) {
        friendlyName = s;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
