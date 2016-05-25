package com.cphbusiness.luke.androidschoolproject;

import android.os.AsyncTask;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import java.util.ArrayList;

/**
 * Created by Mato on 23.05.16.
 */

private class NetworkOperations extends AsyncTask<String,Void, ArrayList<String>> {


    private DropboxAPI<AndroidAuthSession> dropboxAPI;
    private static final String APP_KEY = "68okzghak0jlx8e";
    private static final String APP_SECRET = "yel5utqc01prwak";
    private static final String ACCESSTOKEN = "VQp1oWrVDsAAAAAAAAAAB8hcszBhmPmO77vkQ2yq_t_PBVtBfwAPT56RnSYgrwQg";
    private DropboxAPI.UploadRequest request;

    private ArrayList<String> dirNames;
    //private ArrayList<DropboxAPI.Entry> files;
    private DropboxAPI.Entry direct = null;
    private int i = 0;

    SendActivity sa = new SendActivity();


    protected ArrayList<String> doInBackground(String name) {

        try {
            direct = dropboxAPI.metadata(name, 1000,null, true, null);
        } catch (DropboxException e) {
            System.out.println("Error " + e.getMessage());
        }

        for (DropboxAPI.Entry ent: direct.contents){
            if(ent.isDir){
                dirNames.add(ent.fileName());
            }
        }return dirNames;

    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        sa.setDirectories(result);
    }



}

