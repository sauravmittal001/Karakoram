package com.example.karakoram.cache;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.karakoram.resource.Event;

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

//Singleton class
public class HomeCache {

    Context CONTEXT;
    String EVENT_FILE_NAME = "Event.txt";
    String KEY_FILE_NAME = "Key.txt";

    String DATE_TIME = "dateTime";
    String DESCRIPTION = "description";
    String TITLE = "title";
    String IMAGE_ATTACHED = "isImageAttached";
    String TIME_STAMP = "timeStamp";
    String USER_ID = "userId";

    private HomeCache() {
    }

    public HomeCache(Context context) {
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
//            Toast.makeText(CONTEXT, "Saved to " + file + "/" + KEY_FILE_NAME, Toast.LENGTH_SHORT).show();
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

    public ArrayList<Event> getValueArray() {
        ArrayList<Event> values = new ArrayList<>();

        FileInputStream fis = null;
        try {
            fis = CONTEXT.getApplicationContext().openFileInput(EVENT_FILE_NAME);
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
                    Event event = new Event();
                    event.setDateTime(new Date(Date.parse((String) value.get(DATE_TIME))));
                    event.setDescription((String) value.get(DESCRIPTION));
                    event.setTitle((String) value.get(TITLE));
                    event.setImageAttached(Boolean.parseBoolean((String)value.get(IMAGE_ATTACHED)));
                    event.setTimeStamp(new Date(Date.parse((String)value.get(DATE_TIME))));
                    event.setUserId((String)value.get(USER_ID));
                    values.add(event);
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
        return values;
    }

    public void setValueArray(ArrayList<Event> values) throws IOException, JSONException {
        clearCacheFile(CONTEXT.getApplicationContext().getFilesDir() + EVENT_FILE_NAME);
//        printFileContent(EVENT_FILE_NAME);

        int i = 0;
        JSONObject JSON = new JSONObject();
        for (Event event : values) {
            JSONObject valueJSON = new JSONObject();
            valueJSON.put(DESCRIPTION, event.getDescription());
            valueJSON.put(DATE_TIME, (event.getDateTime()));
            valueJSON.put(TITLE, event.getTitle());
            valueJSON.put(IMAGE_ATTACHED,String.valueOf(event.isImageAttached()));
            valueJSON.put(TIME_STAMP,event.getTimeStamp());
            valueJSON.put(USER_ID,event.getUserId());
            JSON.put(String.valueOf(i++), valueJSON);
        }

        FileOutputStream fos = null;
        try {
            fos = CONTEXT.openFileOutput(EVENT_FILE_NAME, MODE_PRIVATE);
            fos.write(JSON.toString().getBytes());
            File file = CONTEXT.getFilesDir();
//            Toast.makeText(CONTEXT, "Saved to " + file + "/" + EVENT_FILE_NAME, Toast.LENGTH_SHORT).show();
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
