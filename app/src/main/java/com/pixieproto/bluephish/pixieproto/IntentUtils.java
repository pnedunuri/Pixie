package com.pixieproto.bluephish.pixieproto;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nadunoorup on 4/18/17.
 */

public class IntentUtils {
    public static void composeEmail(Activity activity, String subject, String... addresses) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        if (subject != null) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    public static void saveContact(Activity activity, long intentIndex, Map<String, String> data) {
        final Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        if ((intentIndex & Constants.PHONE_NO_INDEX) != 0) {
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, data.get(ContactsContract.Intents.Insert.PHONE));
        } else if ((intentIndex & Constants.EMAIL_INDEX) != 0) {
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, data.get(ContactsContract.Intents.Insert.EMAIL));
        } else if ((intentIndex & Constants.ADDRESS_INDEX) != 0) {
            intent.putExtra(ContactsContract.Intents.Insert.POSTAL, data.get(ContactsContract.Intents.Insert.POSTAL));
        } else if ((intentIndex & Constants.NAME_INDEX) != 0) {
            intent.putExtra(ContactsContract.Intents.Insert.NAME, data.get(ContactsContract.Intents.Insert.NAME));
        } else if ((intentIndex & Constants.URL_INDEX) != 0) {
            intent.putExtra(ContactsContract.Intents.Insert.IM_HANDLE, data.get(ContactsContract.Intents.Insert.IM_HANDLE));
        }
        intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        intent.putExtra("finishActivityOnSaveCompleted", true);
        activity.startActivity(intent);
    }

    // save email to contacts
    public static void saveEmail(Activity activity, String email) {
        Map<String, String> data = new HashMap<String, String>();
        data.put(ContactsContract.Intents.Insert.EMAIL, email);
        saveContact(activity, Constants.EMAIL_INDEX, data);
    }

    // save url to contacts
    public static void saveURL(Activity activity, String email) {
        Map<String, String> data = new HashMap<String, String>();
        data.put(ContactsContract.Intents.Insert.IM_HANDLE, email);
        saveContact(activity, Constants.URL_INDEX, data);
    }

    // save name to contacts
    public static void saveName(Activity activity, String email) {
        Map<String, String> data = new HashMap<String, String>();
        data.put(ContactsContract.Intents.Insert.NAME, email);
        saveContact(activity, Constants.NAME_INDEX, data);
    }

    // save address to contacts
    public static void saveAddress(Activity activity, String address) {
        Map<String, String> data = new HashMap<String, String>();
        data.put(ContactsContract.Intents.Insert.POSTAL, address);
        saveContact(activity, Constants.ADDRESS_INDEX, data);
    }

    // save phone number to contacts
    public static void savePhoneNumber(Activity activity, String digits) {
        Map<String, String> data = new HashMap<String, String>();
        data.put(ContactsContract.Intents.Insert.PHONE, digits);
        saveContact(activity, Constants.PHONE_NO_INDEX, data);
    }

    public static void shareInfo(Activity activity, String sendingInfo, String title) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sendingInfo);
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent, title));
    }

    public static void copyToClipboard(Activity activity, String text) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("extracted text", text);
        clipboard.setPrimaryClip(clip);

        toast(activity, "Copied To Clipboard");
    }

    public static void callNumber(Activity activity, IntentHandlerListener listener, String digits) {
        if (!((ContextList) activity).requestPermissions(listener, Manifest.permission.CALL_PHONE)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + digits));
        activity.startActivity(intent);
    }

    public static void messageToNumber(Activity activity, String digits) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", "" + digits);
        activity.startActivity(smsIntent);
    }

    public static void openWebLink(Activity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http:" + url));
        activity.startActivity(intent);
    }

    public static void saveData(Activity activity, String data) {
        OutputStream outputStream = null;
        try {
            File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), "Pixie");

            // Create the storage directory if it does not exist
            if (!storageDir.exists()) {
                if (!storageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory");
                    return;
                }
            }

            String fileName = "Extracted Data_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            // create file
            File file = new File(storageDir.getPath() + File.separator + fileName + ".txt");
            if (file == null) {
                return;
            }

            outputStream = new FileOutputStream(file);
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();

            toast(activity, "Data saved to fileName" + "!");
        } catch (Exception e) {
            toast(activity, "File not created!");

            e.printStackTrace();
        }
    }

    private static void toast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}