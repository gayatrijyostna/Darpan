package com.app.ecommerce.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.app.ecommerce.activities.LoginRegisterActivity;
import java.io.File;
import java.util.HashMap;


@SuppressWarnings("ALL")
public class SessionManager {

    public static final String KEY_user_id = "user_id";
    public static final String KEY_name = "name";
    public static final String KEY_mobile = "mobile";
    public static final String KEY_password = "password";
    public static final String KEY_email = "email";
    public static final String KEY_landline = "landline";
    public static final String KEY_address = "address";
    public static final String KEY_pincode = "pincode";
    public static final String KEY_city = "city";
    public static final String KEY_executive_id = "executive_id";
    public static final String KEY_otp_code = "otp_code";
    public static final String KEY_mobile_verified = "mobile_verified";
    public static final String KEY_fcm_registration_id = "fcm_registration_id";
    public static final String KEY_status = "status";
    public static final String KEY_created_at = "created_at";
    public static final String KEY_updated_at = "updated_at";
    private static final String PREF_NAME = "UserPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;


    public SessionManager(Context context) 
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static void deleteCache(Context context)
    {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir)
    {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } 
        else 
            if (dir != null && dir.isFile()) 
            {
            return dir.delete();
        } 
            else
                {
            return false;
        }
    }



    public void checkLogin()
    {

        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, LoginRegisterActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

     //Get stored session data
    public HashMap<String, String> getUserDetails()
    {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_user_id,  pref.getString(KEY_user_id, null));
        user.put(KEY_name,  pref.getString(KEY_name, null));
        user.put(KEY_mobile, pref.getString(KEY_mobile, null));
        user.put(KEY_password,  pref.getString(KEY_password, null));
        user.put(KEY_email,  pref.getString(KEY_email, null));
        user.put(KEY_landline,  pref.getString(KEY_landline, null));
        user.put(KEY_address,  pref.getString(KEY_address, null));
        user.put(KEY_pincode,  pref.getString(KEY_pincode, null));
        user.put(KEY_city,  pref.getString(KEY_city, null));
        user.put(KEY_executive_id,  pref.getString(KEY_executive_id, null));
        user.put(KEY_otp_code,  pref.getString(KEY_otp_code, null));
        user.put(KEY_mobile_verified,  pref.getString(KEY_mobile_verified, null));
        user.put(KEY_fcm_registration_id, pref.getString(KEY_fcm_registration_id, null));
        user.put(KEY_status,  pref.getString(KEY_status, null));
        user.put(KEY_created_at,  pref.getString(KEY_created_at, null));
        user.put(KEY_updated_at,  pref.getString(KEY_updated_at, null));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        deleteCache(_context);
        Intent i = new Intent(_context, LoginRegisterActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn()
    {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void createLoginSession(
                                   String user_id,
                                   String name,
                                   String mobile,
                                   String password,
                                   String email,
                                   String landline,
                                   String address,
                                   String pincode,
                                   String city,
                                   String executive_id,
                                   String otp_code,
                                   String mobile_verified,
                                   String fcm_registration_id,
                                   String status,
                                   String created_at,
                                   String updated_at)
    {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_user_id, user_id);
        editor.putString(KEY_name, name);
        editor.putString(KEY_mobile, mobile);
        editor.putString(KEY_password, password);
        editor.putString(KEY_email, email);
        editor.putString(KEY_landline, landline);
        editor.putString(KEY_address, address);
        editor.putString(KEY_pincode, pincode);
        editor.putString(KEY_city, city);
        editor.putString(KEY_executive_id, executive_id);
        editor.putString(KEY_otp_code, otp_code);
        editor.putString(KEY_mobile_verified, mobile_verified);
        editor.putString(KEY_fcm_registration_id, fcm_registration_id);
        editor.putString(KEY_status, status);
        editor.putString(KEY_created_at, created_at);
        editor.putString(KEY_updated_at, updated_at);
        editor.commit();
    }
}
