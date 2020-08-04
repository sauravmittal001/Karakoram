package com.example.karakoram.cache.bill;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.karakoram.resource.Category;
import com.example.karakoram.resource.HostelBill;

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
public class HostelBillCache {

    Context CONTEXT;
    String HOSTEL_BILL_FILE_NAME;
    String KEY_FILE_NAME;

    String CATEGORY = "category";
    String DESCRIPTION = "description";
    String AMOUNT = "amount";
    String USER_ID = "userId";
    String TIMESTAMP = "timeStamp";

    private HostelBillCache() {
    }

    public HostelBillCache(Context context, Category category) {
        this.CONTEXT = context;
        HOSTEL_BILL_FILE_NAME = category.name() + "HostelBill.txt";
        KEY_FILE_NAME = category.name() + "Key.txt";
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

    public ArrayList<HostelBill> getHostelBillArray() {
        ArrayList<HostelBill> hostelBills = new ArrayList<>();

        FileInputStream fis = null;
        try {
            fis = CONTEXT.getApplicationContext().openFileInput(HOSTEL_BILL_FILE_NAME);
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
                    HostelBill bill = new HostelBill();
                    bill.setCategory(Category.valueOf((String) Obj.get(CATEGORY)));
                    bill.setDescription((String) Obj.get(DESCRIPTION));
                    bill.setAmount(Double.parseDouble((String) Obj.get(AMOUNT)));
                    bill.setUserId((String) Obj.get(USER_ID));
                    bill.setTimeStamp(new Date(Date.parse((String) Obj.get(TIMESTAMP))));
                    hostelBills.add(bill);
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
        return hostelBills;
    }

    public void setHostelBillArray(ArrayList<HostelBill> hostelBills) throws IOException, JSONException {
        clearCacheFile(CONTEXT.getApplicationContext().getFilesDir() + HOSTEL_BILL_FILE_NAME);
//        printFileContent(EVENT_FILE_NAME);

        int i = 0;
        JSONObject JSON = new JSONObject();
        for (HostelBill billObj : hostelBills) {
            JSONObject billJSON = new JSONObject();
            billJSON.put(CATEGORY, (String) billObj.getCategory().name());
            billJSON.put(DESCRIPTION, billObj.getDescription());
            billJSON.put(AMOUNT, String.valueOf(billObj.getAmount()));
            billJSON.put(USER_ID, billObj.getUserId());
            billJSON.put(TIMESTAMP, billObj.getTimeStamp());
            JSON.put(String.valueOf(i++), billJSON);
        }

        FileOutputStream fos = null;
        try {
            fos = CONTEXT.openFileOutput(HOSTEL_BILL_FILE_NAME, MODE_PRIVATE);
            fos.write(JSON.toString().getBytes());
            File file = CONTEXT.getFilesDir();
            Toast.makeText(CONTEXT, "Saved to " + file + "/" + HOSTEL_BILL_FILE_NAME, Toast.LENGTH_SHORT).show();
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
