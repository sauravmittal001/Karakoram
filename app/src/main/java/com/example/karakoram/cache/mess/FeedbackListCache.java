package com.example.karakoram.cache.mess;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.karakoram.resource.Anonymity;
import com.example.karakoram.resource.Event;
import com.example.karakoram.resource.Meal;
import com.example.karakoram.resource.MessFeedback;

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


public class FeedbackListCache {

    Context CONTEXT;
    String FEEDBACK_FILE_NAME;
    String KEY_FILE_NAME;
    boolean getMine;

    String TIMESTAMP = "timestamp";
    String USER_ID = "userId";
    String USER_NAME = "userName";
    String DESCRIPTION = "description";
    String RATING = "rating";
    String MEAL = "meal";
    String ANONYMITY = "anonymity";


    private FeedbackListCache() {
    }

    public FeedbackListCache(Context context, boolean getMine) {
        this.CONTEXT = context;
        this.getMine = getMine;

        if(getMine){
            FEEDBACK_FILE_NAME = "MyUstFeedback.txt";
            KEY_FILE_NAME = "MyUstKey.txt";
        }
        else{
            FEEDBACK_FILE_NAME = "UstFeedback.txt";
            KEY_FILE_NAME = "UstKey.txt";
        }
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

    public ArrayList<MessFeedback> getFeedbackArray() {
        ArrayList<MessFeedback> feedbacks = new ArrayList<>();

        FileInputStream fis = null;
        try {
            fis = CONTEXT.getApplicationContext().openFileInput(FEEDBACK_FILE_NAME);
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
                    MessFeedback feedbackObject = new MessFeedback();
                    feedbackObject.setTimestamp(new Date(Date.parse((String) value.get(TIMESTAMP))));
                    feedbackObject.setUserId((String) value.get(USER_ID));
                    feedbackObject.setUserName((String) value.get(USER_NAME));
                    feedbackObject.setDescription((String) value.get(DESCRIPTION));
                    feedbackObject.setRating(Integer.parseInt((String) value.get(RATING)));
                    feedbackObject.setMeal(Meal.valueOf((String) value.get(MEAL)));
                    feedbackObject.setAnonymity(Anonymity.valueOf((String) value.get(ANONYMITY)));
                    feedbacks.add(feedbackObject);
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
        return feedbacks;
    }

    public void setFeedbackArray(ArrayList<MessFeedback> feedbacks) throws IOException, JSONException {
        clearCacheFile(CONTEXT.getApplicationContext().getFilesDir() + FEEDBACK_FILE_NAME);
//        printFileContent(EVENT_FILE_NAME);

        int i = 0;
        JSONObject JSON = new JSONObject();
        for (MessFeedback feedbackObject : feedbacks) {
            JSONObject valueJSON = new JSONObject();
            valueJSON.put(TIMESTAMP, feedbackObject.getTimestamp());
            valueJSON.put(USER_ID, (String) feedbackObject.getUserId());
            valueJSON.put(USER_NAME, (String) feedbackObject.getUserName());
            valueJSON.put(DESCRIPTION, (String) feedbackObject.getDescription());
            valueJSON.put(RATING, String.valueOf(feedbackObject.getRating()));
            valueJSON.put(MEAL, feedbackObject.getMeal().name());
            valueJSON.put(ANONYMITY, feedbackObject.getAnonymity().name());
            JSON.put(String.valueOf(i++), valueJSON);
        }

        FileOutputStream fos = null;
        try {
            fos = CONTEXT.openFileOutput(FEEDBACK_FILE_NAME, MODE_PRIVATE);
            fos.write(JSON.toString().getBytes());
            File file = CONTEXT.getFilesDir();
//            Toast.makeText(CONTEXT, "Saved to " + file + "/" + FEEDBACK_FILE_NAME, Toast.LENGTH_SHORT).show();
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
