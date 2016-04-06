package ua.kiev.netmaster.netmasterqualitycontrol.sequrity;

import android.location.Location;

import ua.kiev.netmaster.netmasterqualitycontrol.activities.MyApplication;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Employee;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Network;
import ua.kiev.netmaster.netmasterqualitycontrol.domain.Task;
import ua.kiev.netmaster.netmasterqualitycontrol.enums.EmplPossition;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;

/**
 * Created by RAZER on 2/9/2016.
 */
public class MySecurity {
    public static final String errorMessage="You haven't permissions";
    //public static MyApplication myApplication;
    private static boolean IamSuperadmin, IamAdmin, HeisAdmin, HeisTechnician, areWeInOneNetwork , changeMyself, iAmMajor, amIOneOfExec, isItDoneAcception;

    public static boolean hasPermissionsToModify(Object o, MyApplication myApp){

        if(o instanceof Task){
            if (((Task)o).getNetworkId().equals(myApp.getMe().getNetworkId())  &  (myApp.getMe().getPosition().getPriority()>1)){
                return true;
            }else return false;
        }else if(o instanceof Employee){
            Employee e = (Employee)o;
            changeMyself = myApp.getMe().equals(e);
            if(changeMyself) return true;
            areWeInOneNetwork = e.getNetworkId()!=null && e.getNetworkId().equals(myApp.getMe().getNetworkId());
            iAmMajor = myApp.getMe().getPosition().getPriority()>e.getPosition().getPriority();
            L.l("MySecurity: iAmMajor = " +iAmMajor + " areWeInOneNetwork = "+ areWeInOneNetwork);
            if(areWeInOneNetwork & iAmMajor) return true;
            return false;
        }else if(o instanceof Network){
            Network n = (Network)o;
            if(myApp.getMe().getNetworkId()!= null && myApp.getMe().getNetworkId().equals(n.getNetworkId()) && myApp.getMe().getPosition().equals(EmplPossition.SUPERADMIN)){
                return true;
            }else return false;
        }
        return false;
    }

    public static boolean hasPermissionsToAccept(Task task, MyApplication myApp){
        Employee me = myApp.getMe();
        areWeInOneNetwork = task.getNetworkId().equals(myApp.getMe().getNetworkId());
        if(areWeInOneNetwork){
            isItDoneAcception =task.getLatitude()!=null && task.getAccepted()!=null && task.getDone()==null;
            if(isItDoneAcception){
                for(Long id : task.getExecuterIds()){
                    if (me.getId()== id) amIOneOfExec = true;
                    break;
                }
                if(amIOneOfExec){
                    float[] distance = new float[1];
                    Location.distanceBetween(me.getLastLat(), me.getLastLong(), task.getLatitude(), task.getLongitude(), distance);
                        if (distance[0] >= 300.0f) {
                            return false;
                    }
                }else return false;
            }
            return true;
        }else return false;
    }

    public static boolean hasPermissionsToCreate(MyApplication myApp){
        if(myApp.getMe().getPosition().equals(EmplPossition.ADMIN) || myApp.getMe().getPosition().equals(EmplPossition.SUPERADMIN)){
            return true;
        }return false;
    }
}