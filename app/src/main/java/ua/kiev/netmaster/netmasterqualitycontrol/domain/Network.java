package ua.kiev.netmaster.netmasterqualitycontrol.domain;

import java.util.Arrays;

/**
 * Created by RAZER on 2/6/2016.
 */
public class Network {
        private Long networkId;
        private String name;
        private Long[] owners;
        private Long[] employees;
        private Long[] tasks;
        private Long[] friendlyNetworks;
        private String contacts;

        public Network() {}

    public Network(Long networkId, String name, Long[] owners, Long[] employees, Long[] tasks, Long[] frindlyNetworks, String contacts) {
        this.networkId = networkId;
        this.name = name;
        this.owners = owners;
        this.employees = employees;
        this.tasks = tasks;
        this.friendlyNetworks = frindlyNetworks;
        this.contacts = contacts;
    }

    public Long getNetworkId() {
            return networkId;
        }

        public void setNetworkId(Long networkId) {
            this.networkId = networkId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long[] getOwners() {
            return owners;
        }

        public void setOwners(Long[] owners) {
            this.owners = owners;
        }

        public Long[] getEmployees() {
            return employees;
        }

        public void setEmployees(Long[] employees) {
            this.employees = employees;
        }

        public Long[] getTasks() {
            return tasks;
        }

        public void setTasks(Long[] tasks) {
            this.tasks = tasks;
        }

        public Long[] getFriendlyNetworks() {
            return friendlyNetworks;
        }

        public void setFriendlyNetworks(Long[] friendlyNetworks) {
            this.friendlyNetworks = friendlyNetworks;
        }

        public String getContacts() {
            return contacts;
        }

        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        @Override
        public String toString() {
            return "Network{" +
                    "networkId=" + networkId +
                    ", name='" + name + '\'' +
                    ", owners=" + Arrays.toString(owners) +
                    ", employees=" + Arrays.toString(employees) +
                    ", tasks=" + Arrays.toString(tasks) +
                    ", friendlyNetworks=" + Arrays.toString(friendlyNetworks) +
                    ", contacts='" + contacts + '\'' +
                    '}';
        }
}
