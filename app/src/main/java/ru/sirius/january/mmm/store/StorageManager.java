package ru.sirius.january.mmm.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

public final class StorageManager {

    private static final String SHARED_PREFERENCES_LOCATION = "ru.sirius.sharedPreferencesLocation";
    private SharedPreferences sharedPreferences;
    private String CACHE_DIR;

    private static volatile StorageManager instance;

    private StorageManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_LOCATION, Context.MODE_PRIVATE);
        CACHE_DIR = context.getCacheDir().getAbsolutePath();
        myCache = new HashMap<String, Bitmap>();
    }

    public static StorageManager getInstance(Context context) {
        if (instance == null) {
            synchronized (StorageManager.class) {
                if (instance == null) {
                    instance = new StorageManager(context);
                }
            }
        }
        return instance;
    }

    private static final String INVALID_KEY = "Error invalid key";

    public int getInteger(String KEY) {
        synchronized (sharedPreferences) {
            if (!sharedPreferences.contains(KEY)) {
                Log.e("GetInteger", INVALID_KEY);
                return 0;
            }
            return sharedPreferences.getInt(KEY, 0);
        }
    }

    public boolean getBoolean(String KEY) {
        synchronized (sharedPreferences) {
            if (!sharedPreferences.contains(KEY)) {
                Log.e("GetBoolean", INVALID_KEY);
                return false;
            }
            return sharedPreferences.getBoolean(KEY, false);
        }
    }


    public long getLong(String KEY) {
        synchronized (sharedPreferences) {
            if (!sharedPreferences.contains(KEY)) {
                Log.e("GetLong", INVALID_KEY);
                return 0;
            }
            return sharedPreferences.getLong(KEY, 0);
        }
    }

    public float getFloat(String KEY) {
        synchronized (sharedPreferences) {
            if (!sharedPreferences.contains(KEY)) {
                Log.e("GetFloat", INVALID_KEY);
                return 0f;
            }
            return sharedPreferences.getFloat(KEY, 0f);
        }
    }

    public String getString(String KEY) {
        synchronized (sharedPreferences) {
            if (!sharedPreferences.contains(KEY)) {
                Log.e("GetString", INVALID_KEY);
                return null;
            }
            return sharedPreferences.getString(KEY, null);
        }
    }

    public void putString(String KEY, String val) {
        synchronized (sharedPreferences) {
            sharedPreferences.edit().putString(KEY, val).commit();
        }
    }

    public void putInteger(String KEY, int val) {
        synchronized (sharedPreferences) {
            sharedPreferences.edit().putInt(KEY, val).commit();
        }
    }
    public void putLong(String KEY, long val) {
        synchronized (sharedPreferences) {
            sharedPreferences.edit().putLong(KEY, val).commit();
        }
    }
    public void putFloat(String KEY, float val) {
        synchronized (sharedPreferences) {
            sharedPreferences.edit().putFloat(KEY, val).commit();
        }
    }
    public void putBoolean(String KEY, boolean val) {
        synchronized (sharedPreferences) {
            sharedPreferences.edit().putBoolean(KEY, val).commit();
        }
    }

    private HashMap<String, Bitmap> myCache;

    public void cacheBitmap(String fileName, Bitmap bitmap) {
        synchronized (myCache) {
            if (isBitmapCached(fileName))
                return;
            try {
                myCache.put(fileName, bitmap);
                FileOutputStream outFile = new FileOutputStream(new File(CACHE_DIR + "/" + fileName));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isBitmapCached(String fileName) {
        synchronized (myCache) {
            if (myCache.containsKey(fileName))
                return true;
            return (new File(CACHE_DIR + "/" + fileName).exists());
        }
    }

    public Bitmap getCachedBitmap(String fileName) {
        synchronized (myCache) {
            if (myCache.containsKey(fileName))
                return myCache.get(fileName);
            Bitmap bitmap = BitmapFactory.decodeFile(CACHE_DIR + "/" + fileName);
            myCache.put(fileName, bitmap);
            return bitmap;
        }
    }

}