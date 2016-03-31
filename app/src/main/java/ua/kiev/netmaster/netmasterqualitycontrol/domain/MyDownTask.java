package ua.kiev.netmaster.netmasterqualitycontrol.domain;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

import ua.kiev.netmaster.netmasterqualitycontrol.R;
import ua.kiev.netmaster.netmasterqualitycontrol.loger.L;


/**
 * Created by ПК on 06.08.2015.
 */
public class MyDownTask extends AsyncTask<Void, Void, String>{

    private Gson gson;
    private Activity activity;
    private String login, result, inputLine, id, gsonString, query, password, type, address, latitude, longitude, officeName, title, description, officeId, officeGson,
            networkName, networkId, owners, networkgson, employee, task, taskId, emlpId, urlStr = "http://195.18.29.171:8082/";
    private final String LOGIN="login", PASSWORD="password", GSONSTRING="gsonString", TITLE="title", DESCRIPTION="description",
            URLTAIL ="urlTail", EMPLOYEE="employee", TASK="task", EMPLID="emlpId", TASKID="taskId", NETWORKID="networkId",
            NETWORKGSON="networkgson", OWNERS="owners", TYPE="type", ADDRESS="address", OFFCIEID="officeId", OFFICEGSON="officeGson", LATITUDE="latitude",
            LONGITUDE="longitude", NETWORKNAME="networkname", OFFICENAME="officeName", ID="id";

    private URL url;
    private HttpURLConnection con;
    private Uri.Builder builder;
    private OutputStream os;
    private BufferedWriter writer;
    private BufferedReader in;
    private StringBuilder responses;


    public MyDownTask(Map<String,String> params, Activity activity) {
        this.activity = activity;
        //this.params = params;
        login = params.get(LOGIN);
        password = params.get(PASSWORD);
        gsonString = params.get(GSONSTRING);
        title = params.get(TITLE);
        description = params.get(DESCRIPTION);
        employee = params.get(EMPLOYEE);
        task = params.get(TASK);
        taskId = params.get(TASKID);
        emlpId = params.get(EMPLID);
        networkName = params.get(NETWORKNAME);
        owners = params.get(OWNERS);
        networkId = params.get(NETWORKID);
        networkgson = params.get(NETWORKGSON);

        type = params.get(TYPE);
        address = params.get(ADDRESS);
        officeId = params.get(OFFCIEID);
        officeGson = params.get(OFFICEGSON);
        officeName= params.get(OFFICENAME);
        latitude = params.get(LATITUDE);
        longitude = params.get(LONGITUDE);
        id = params.get(ID);
        if(params.get(URLTAIL)==null)    choseUrlTail();
        else urlStr+=params.get(URLTAIL);
    }

    protected void onPreExecute() {
        L.l("onPreExecute()", this);
    }

    @Override
    protected String doInBackground(Void... params) {
        L.l("MyDownTask. doInBackground");
        return commonConnect();
    }

    @Override
    protected void onPostExecute(String result) {
        L.l("onPostExecute() result = " + result); //// TODO: 15-Mar-16 log to service
        if(activity!=null && result!=null && result.equals(activity.getString(R.string.server_unreachable))) errorDialog(result);
        if(activity!=null && result!=null && result.equals("Not authenticated"))L.t("Not authenticated!", activity);
    }

    private void choseUrlTail(){
        if(login!=null) urlStr+="authAndroid";
        if(title!=null) urlStr+="task/addTask";                 //// TODO: 23-Mar-16  
        if(employee!=null)urlStr+="employee/updateEmpl";
        if(task!=null) urlStr+="task/updateTask";
        if(taskId!=null) urlStr+="task/deleteTask";
        if(emlpId!=null) urlStr+="employee/deleteEmpl";
        if(networkName!=null) urlStr+="network/create";
        if(networkId!=null) urlStr+="network/deleteEmpl";
        if(networkgson!=null) urlStr+="network/update";
        if(type!=null) urlStr+="task/addTask";                   // TODO: 23-Mar-16  
        if(officeName!=null) urlStr+="office/create";
    }

    private void appendQueryParameters(){
        if(login!=null) builder.appendQueryParameter(LOGIN, login);
        if(password!=null)builder.appendQueryParameter(PASSWORD, password);
        if(gsonString!=null)builder.appendQueryParameter(GSONSTRING,gsonString);
        if(title!=null)builder.appendQueryParameter(TITLE,title);
        if(description!=null) builder.appendQueryParameter(DESCRIPTION, description);
        if(employee!=null) builder.appendQueryParameter(EMPLOYEE, employee);
        if(task!=null) builder.appendQueryParameter(TASK, task);
        if(taskId!=null) builder.appendQueryParameter(TASKID, taskId);
        if(emlpId!=null) builder.appendQueryParameter(EMPLID, emlpId);
        if(networkName!=null) builder.appendQueryParameter(NETWORKNAME,networkName);
        if(owners!=null) builder.appendQueryParameter(OWNERS, owners);
        if(networkId!=null) builder.appendQueryParameter(NETWORKID, networkId);
        if(networkgson!=null)builder.appendQueryParameter(NETWORKGSON, networkgson);
        if(type!=null) builder.appendQueryParameter(TYPE,type);
        if(address!=null) builder.appendQueryParameter(ADDRESS, address);
        if(officeGson!=null) builder.appendQueryParameter(OFFICEGSON, officeGson);
        if(officeId!=null) builder.appendQueryParameter(OFFCIEID, officeId);
        if(longitude!=null) builder.appendQueryParameter(LONGITUDE, longitude);
        if(latitude!=null) builder.appendQueryParameter(LATITUDE, latitude);
        if(officeName!=null) builder.appendQueryParameter(OFFICENAME, officeName);
        if(id!=null) builder.appendQueryParameter(ID, id);
    }

    private String commonConnect() {
        L.l("MyDownTask. commonConnect()");
        CookieHandler.getDefault();
        responses = new StringBuilder();
        try {
            L.l("urlStr= " + urlStr);
            url = new URL(urlStr);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(2000);
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            builder = new Uri.Builder();
            appendQueryParameters();
            query = builder.build().getEncodedQuery();
            L.l("commonConnect(). String query = " + query);
            os = con.getOutputStream();
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            if(query!=null)writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            con.connect();
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                responses.append(inputLine);
            }
            result = prepareToParseToGson(responses.toString());
        }catch (SocketTimeoutException ste){
            L.l("catch (SocketTimeoutException ste)");
            ste.printStackTrace();
            result = "Server Unreachable";
        }catch (Exception e){
            L.l("catch (Exception e)");
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        //L.l("MyDownTask. NEW COMMON CONNECT! responses.toString() = " + responses.toString());
        //L.l("MyDownTask. NEW COMMON CONNECT! result = " + result);
        return result;
    }

    private String prepareToParseToGson(String input) {
        int startChar = input.indexOf('[');
        int endChar = input.indexOf(']');
        if(startChar>0&startChar<5) {   // TODO: 25.12.2015  5???
            String res = input.substring(startChar, endChar + 1);
            L.l("MyDownTask. after prepareToParseToGson: " + res);
            return res;
        }else
            return input;
    }

    private void errorDialog(String description) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(description+"(");
        alertDialog.setMessage("Please, try to reconnect later.");
        //alertDialog.setIcon(R.drawable.ic_warning);
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    public String getResalt() {
        return result;
    }

    public void setResalt(String resalt) {
        this.result = resalt;
    }

}

