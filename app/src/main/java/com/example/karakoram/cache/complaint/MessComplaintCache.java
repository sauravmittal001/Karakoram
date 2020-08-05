package com.example.karakoram.cache.complaint;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.ComplaintArea;
import com.example.karakoram.resource.MaintComplaint;
import com.example.karakoram.resource.MessComplaint;
import com.example.karakoram.resource.Status;
import com.example.karakoram.resource.Wing;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static android.content.Context.MODE_PRIVATE;

public class MessComplaintCache {

    Context CONTEXT;
    String COMPLAINT_FILE_NAME = "MessComplaintCache.txt";
    String KEY_FILE_NAME = "AllComplaintKey.txt";


    String USER_ID = "userId";
    String ENTRY_NUMBER = "entryNumber";
    String USER_NAME = "userName";
    String TIMESTAMP = "timestamp";
    String STATUS = "status";
    String CATEGORY = "category";
    String DESCRIPTION = "description";
    String IS_IMAGE_ATTACHED = "isImageAttached";
    String COMPLAINT_AREA = "complaintArea";

    private MessComplaintCache() {
    }

    public MessComplaintCache(Context context) {
        this.CONTEXT = context;
    }

    public ArrayList<String> getKeyArray() {
        ArrayList<String> keys = new ArrayList<>();

        FileInputStream fis = null;
        try {
            fis = CONTEXT.getApplicationContext().openFileInput(KEY_FILE_NAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String json_string;
            while ((json_string = br.readLine()) != null)
                sb.append(json_string);
            json_string = sb.toString();
            if (!json_string.equals("")) {
                JSONObject EventJSON = new JSONObject(json_string);
                Iterator<?> keyIterator = EventJSON.keys();
                while (keyIterator.hasNext())
                    keys.add((String) keyIterator.next());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return keys;
    }

    public void setKeyArray(ArrayList<String> keys) throws IOException, JSONException {
        clearCacheFile(CONTEXT.getApplicationContext().getFilesDir() + KEY_FILE_NAME);
//        printFileContent(KEY_FILE_NAME);

        JSONObject JSON = new JSONObject();
        for (String key : keys)
            JSON.put(key, key);

        FileOutputStream fos = null;
        try {
            fos = CONTEXT.openFileOutput(KEY_FILE_NAME, MODE_PRIVATE);
            fos.write(JSON.toString().getBytes());
            File file = CONTEXT.getFilesDir();
            Toast.makeText(CONTEXT, "Saved to " + file + "/" + KEY_FILE_NAME, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public ArrayList<MessComplaint> getComplaintArray() {
        ArrayList<MessComplaint> complaints = new ArrayList<>();

        FileInputStream fis = null;
        try {
            fis = CONTEXT.getApplicationContext().openFileInput(COMPLAINT_FILE_NAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String json_string;
            while ((json_string = br.readLine()) != null)
                sb.append(json_string);
            json_string = sb.toString();

            if (!json_string.equals("")) {
                JSONObject ValueJSON = new JSONObject(json_string); //key: index number (no use), value: Event.java object in json format
                Iterator<?> keyIterator = ValueJSON.keys();

                while (keyIterator.hasNext()) {
                    String key = (String) keyIterator.next();
                    JSONObject value = (JSONObject) ValueJSON.get(key);
                    MessComplaint complaint = new MessComplaint();
                    complaint.setUserId((String) value.get(USER_ID));
                    complaint.setEntryNumber((String) value.get(ENTRY_NUMBER));
                    complaint.setUserName((String) value.get(USER_NAME));
                    complaint.setTimestamp(new Date(Date.parse((String) value.get(TIMESTAMP))));
                    complaint.setStatus(Status.valueOf((String) value.get(STATUS)));
                    complaint.setCategory(Category.valueOf((String) value.get(CATEGORY)));
                    complaint.setDescription((String) value.get(DESCRIPTION));
                    complaint.setImageAttached((Boolean) value.get(IS_IMAGE_ATTACHED));
                    complaint.setComplaintArea(ComplaintArea.valueOf((String) value.get(COMPLAINT_AREA)));
                    complaints.add(complaint);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return complaints;
    }

    public void setValueArray(ArrayList<MessComplaint> complaints) throws IOException, JSONException {
        clearCacheFile(CONTEXT.getApplicationContext().getFilesDir() + COMPLAINT_FILE_NAME);
//        printFileContent(EVENT_FILE_NAME);

        int i = 0;
        JSONObject JSON = new JSONObject();
        for (MessComplaint complaint : complaints) {
            JSONObject valueJSON = new JSONObject();
            valueJSON.put(USER_ID, (String) complaint.getDescription());
            valueJSON.put(ENTRY_NUMBER, (String) complaint.getEntryNumber());
            valueJSON.put(USER_NAME, (String) complaint.getUserName());
            valueJSON.put(TIMESTAMP, String.valueOf(complaint.getTimestamp()));
            valueJSON.put(STATUS, String.valueOf(complaint.getStatus()));
            valueJSON.put(CATEGORY, String.valueOf(complaint.getCategory()));
            valueJSON.put(DESCRIPTION, (String) complaint.getDescription());
            valueJSON.put(IS_IMAGE_ATTACHED, String.valueOf(complaint.isImageAttached()));
            valueJSON.put(COMPLAINT_AREA, String.valueOf(complaint.getComplaintArea()));

            JSON.put(String.valueOf(i++), valueJSON);
        }

        FileOutputStream fos = null;
        try {
            fos = CONTEXT.openFileOutput(COMPLAINT_FILE_NAME, MODE_PRIVATE);
            fos.write(JSON.toString().getBytes());
            File file = CONTEXT.getFilesDir();
            Toast.makeText(CONTEXT, "Saved to " + file + "/" + COMPLAINT_FILE_NAME, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void printFileContent(String fileName) throws IOException {
        FileInputStream fis = CONTEXT.getApplicationContext().openFileInput(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        StringBuilder sb = new StringBuilder();
        String json_string;
        while ((json_string = br.readLine()) != null)
            sb.append(json_string);
        json_string = sb.toString();

        Log.i("HomeCacheLog", json_string);

    }

    private void clearCacheFile(String FILE_PATH) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(FILE_PATH);
        writer.print("");
        writer.close();
    }

}
