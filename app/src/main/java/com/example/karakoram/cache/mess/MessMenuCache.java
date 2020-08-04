package com.example.karakoram.cache.mess;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.karakoram.resource.Event;
import com.example.karakoram.resource.Menu;

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
public class MessMenuCache {

    Context CONTEXT;
    String MENU_FILE_NAME = "Menu.txt";
    String DAY_FILE_NAME = "Day.txt";

    String BREAKFAST = "breakFast";
    String LUNCH = "lunch";
    String DINNER = "dinner";
    String DAY = "day";

    private MessMenuCache() {
    }

    public MessMenuCache(Context context) {
        this.CONTEXT = context;
    }

    public ArrayList<String> getDayArray() {
        ArrayList<String> days = new ArrayList<>();

        FileInputStream fis = null;
        try {
            fis = CONTEXT.getApplicationContext().openFileInput(DAY_FILE_NAME);
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
                    days.add((String) keyIterator.next());
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
        return days;
    }

    public void setDayArray(ArrayList<String> days) throws IOException, JSONException {
        clearCacheFile(CONTEXT.getApplicationContext().getFilesDir() + DAY_FILE_NAME);
//        printFileContent(KEY_FILE_NAME);

        JSONObject JSON = new JSONObject();
        for (String day : days)
            JSON.put(day, day);

        FileOutputStream fos = null;
        try {
            fos = CONTEXT.openFileOutput(DAY_FILE_NAME, MODE_PRIVATE);
            fos.write(JSON.toString().getBytes());
            File file = CONTEXT.getFilesDir();
            Toast.makeText(CONTEXT, "Saved to " + file + "/" + DAY_FILE_NAME, Toast.LENGTH_SHORT).show();
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

    public ArrayList<Menu> getMenuArray() {
        ArrayList<Menu> allDayMenu = new ArrayList<>();

        FileInputStream fis = null;
        try {
            fis = CONTEXT.getApplicationContext().openFileInput(MENU_FILE_NAME);
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
                    JSONObject Obj = (JSONObject) ValueJSON.get(key);
                    Menu menu = new Menu();
                    menu.setBreakFast((String) Obj.get(BREAKFAST));
                    menu.setLunch((String) Obj.get(LUNCH));
                    menu.setDinner((String) Obj.get(DINNER));
                    menu.setDay((String) Obj.get(DAY));
                    allDayMenu.add(menu);
                }
            }
        }
        catch (IOException | JSONException e) {
            Log.i("DEBUG", String.valueOf(e));
            e.printStackTrace();
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return allDayMenu;
    }

    public void setMenuArray(ArrayList<Menu> allDayMenu) throws IOException, JSONException {
        clearCacheFile(CONTEXT.getApplicationContext().getFilesDir() + MENU_FILE_NAME);
//        printFileContent(EVENT_FILE_NAME);

        int i = 0;
        JSONObject JSON = new JSONObject();
        for (Menu menu : allDayMenu) {
            JSONObject menuJSON = new JSONObject();
            menuJSON.put(BREAKFAST, menu.getBreakFast());
            menuJSON.put(LUNCH, menu.getLunch());
            menuJSON.put(DINNER, menu.getDinner());
            menuJSON.put(DAY, menu.getDay());
            JSON.put(String.valueOf(i++), menuJSON);
        }

        FileOutputStream fos = null;
        try {
            fos = CONTEXT.openFileOutput(MENU_FILE_NAME, MODE_PRIVATE);
            fos.write(JSON.toString().getBytes());
            File file = CONTEXT.getFilesDir();
            Toast.makeText(CONTEXT, "Saved to " + file + "/" + MENU_FILE_NAME, Toast.LENGTH_SHORT).show();
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
