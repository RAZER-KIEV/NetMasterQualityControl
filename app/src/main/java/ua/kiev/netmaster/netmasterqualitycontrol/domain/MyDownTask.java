package ua.kiev.netmaster.netmasterqualitycontrol.domain;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import ua.kiev.netmaster.netmasterqualitycontrol.activities.LoginActivity;


/**
 * Created by ПК on 06.08.2015.
 */
public class MyDownTask extends AsyncTask<Void, Void, String>{

    private Gson gson;
    private Context context;
    private String login, result, inputLine, gsonString, query, password, title, description, employee, task, taskId, emlpId, urlStr = "http://176.37.239.136:8082/";
    private final String LOGIN="login", PASSWORD="password", GSONSTRING="gsonString", TITLE="title", DESCRIPTION="description", URLSTR="urlTail", EMPLOYEE="employee", TASK="task", EMPLID="emlpId", TASKID="taskId";
    private Map<String,String> params;
    private URL url;
    private HttpURLConnection con;
    private Uri.Builder builder;
    private OutputStream os;
    private BufferedWriter writer;
    private BufferedReader in;
    private StringBuilder responses;
    public static boolean isReachable;

    public MyDownTask(String urlTeil, String login, String password, Context context) {
        urlStr += urlTeil;
        this.login = login;
        this.password = password;
        this.context = context;
        gson = new Gson();

    }

    public MyDownTask(Map<String,String> params, Context context) {
        this.context = context;
        this.params = params;
        login = params.get(LOGIN);
        password = params.get(PASSWORD);
        gsonString = params.get(GSONSTRING);
        title = params.get(TITLE);
        description = params.get(DESCRIPTION);
        employee = params.get(EMPLOYEE);
        task = params.get(TASK);
        taskId = params.get(TASKID);
        emlpId = params.get(EMPLID);
        if(params.get(URLSTR)==null)    choseUrlTail();
         else urlStr+=params.get(URLSTR);
    }

    public MyDownTask(String urlStrTail, String gsonString, Context context) {
        this.urlStr+= urlStrTail;
        this.gsonString = gsonString;
        this.context = context;
        gson = new Gson();
    }

    public MyDownTask(String urlStrTail, Context context) {
        this.urlStr+= urlStrTail;
        gson = new Gson();
        this.context = context;
    }



    protected void onPreExecute() {}

    @Override
    protected String doInBackground(Void... params) {
        Log.d(LoginActivity.LOG, "MyDownTask. doInBackground");
        if(this.params!=null) return commonConnect();
            else
        return connect();
    }

    protected void onPostExecute(String result) {
       // if(!isReachable) Toast.makeText(context, "No Internet connection! Try later.", Toast.LENGTH_LONG).show(); // TODO: 15.12.2015
    }

    private void choseUrlTail(){
        if(login!=null) urlStr+= "authAndroid";
        if(title!=null) urlStr+="task/addTask";
        if(employee!=null)urlStr+="employee/updateEmpl";
        if(task!=null) urlStr+="task/updateTask";
        if(taskId!=null) urlStr+="task/deleteTask";
        if(emlpId!=null) urlStr+="employee/deleteEmpl";
    }

    public String connect() {
        Log.d(LoginActivity.LOG, "MyDownTask. connect");
        if(params!=null) {  return commonConnect();}
        CookieHandler.getDefault();
        responses = new StringBuilder();
        //con = null;

        try {
            Log.d(LoginActivity.LOG, "urlStr= "+urlStr);
            url = new URL(urlStr);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(2000);
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            builder = new Uri.Builder();
            if((login != null && password != null)|(gsonString!=null)) {
                if (login != null && password != null) {
                    Log.d(LoginActivity.LOG, "connect(). login != null && password != null");
                    builder.appendQueryParameter("login", login)
                            .appendQueryParameter("password", password);
                } else if (gsonString != null) {
                    Log.d(LoginActivity.LOG, "connect(). gsonString != null");
                    builder.appendQueryParameter("task", gsonString);
                }
                query = builder.build().getEncodedQuery();
                Log.d(LoginActivity.LOG, "connect(). String query = "+query);
                os = con.getOutputStream();
                writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
            }
            con.connect();

            in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            while ((inputLine = in.readLine()) != null) {
                responses.append(inputLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        Log.d(LoginActivity.LOG, "MyDownTask. responses.toString() = " + responses.toString());
        result = prepareToParseToGson(responses.toString());
        return result;
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
    }

    private String commonConnect() {
        Log.d(LoginActivity.LOG, "MyDownTask. commonConnect()");
        CookieHandler.getDefault();
        responses = new StringBuilder();
        try {
            Log.d(LoginActivity.LOG, "urlStr= " + urlStr);
            url = new URL(urlStr);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(2000);
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            builder = new Uri.Builder();
            appendQueryParameters();
            query = builder.build().getEncodedQuery();
            Log.d(LoginActivity.LOG, "connect(). String query = "+query);
            os = con.getOutputStream();
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            con.connect();
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                responses.append(inputLine);
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
        if (con != null) {
            con.disconnect();
        }
    }
    Log.d(LoginActivity.LOG, "MyDownTask. NEW COMMON CONNECT! responses.toString() = " + responses.toString());
    result = prepareToParseToGson(responses.toString());
    return result;
    }



    private String prepareToParseToGson(String input) {
        int startChar = input.indexOf('[');
        int endChar = input.indexOf(']');

        if(startChar>0&startChar<5) {//// TODO: 25.12.2015  5??? 
            String res = input.substring(startChar, endChar + 1);
            Log.d(LoginActivity.LOG, "MyDownTask. after prepareToParseToGson: " + res);
            return res;
        }else
            return input;
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
    /* private void isReachable() throws IOException {
        Log.d(LoginActivity.LOG, "MyDownTask. isReachable()");
        String ipAddress = "agromonitor.mechatroniclab.com"; /// todo ???? check string!!!
        InetAddress inet = InetAddress.getByName(ipAddress);

        if(inet.isReachable(3000)){
            Log.d(LoginActivity.LOG, ipAddress + " = Host is reachable");
            isReachable = true;
        }else {
            Log.d(LoginActivity.LOG, ipAddress + " = Host is NOT reachable");
            isReachable = false;
        }
    }*/
}

