package ua.kiev.netmaster.netmasterqualitycontrol.enums;

/**
 * Created by RAZER on 2/19/2016.
 */
public enum EmplPossition {
    TECHNICIAN(1), ADMIN(2), SUPERADMIN(3);

    private int priority;
    EmplPossition(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
