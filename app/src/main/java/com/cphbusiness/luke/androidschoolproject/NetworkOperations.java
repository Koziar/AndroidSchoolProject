package com.cphbusiness.luke.androidschoolproject;

import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import java.util.ArrayList;

/**
 * Created by Mato on 23.05.16.
 */

class NetworkOperations extends AsyncTask<String,ArrayList<String>, ArrayList<String>> {

    SendActivity sa = new SendActivity();
    LoginActivity la = new LoginActivity();

    private DropboxAPI<AndroidAuthSession> API=la.dropboxAPI;
    private ArrayList<String> dirNames = new ArrayList<>();
    private DropboxAPI.Entry direct = null;


    @Override
    protected ArrayList<String> doInBackground(String... params) {

    String pathString = params[0];


        try {
            direct = API.metadata(pathString, 1000, null, true, null);
        } catch (DropboxException e) {
            System.out.println("Error " + e.getMessage());
        }

        for (DropboxAPI.Entry ent: direct.contents){
            if(ent.isDir){
                dirNames.add(ent.fileName());
            }
        }

        return dirNames;
    }

    @Override
    protected void onPostExecute(ArrayList<String> whatever) {
        System.out.println("went through" +  whatever.toString());
        sa.setDirectories(whatever);

    }



}

