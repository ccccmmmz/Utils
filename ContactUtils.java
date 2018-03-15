package com.example.ligen.aidl_demo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.RequiresPermission;
import android.util.Log;

/**
 * Created by ligen$user
 */

public class ContactUtils {

    @RequiresPermission(allOf = {Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS})
    public static void getInfos(Context context) {
        ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
        Cursor contactCur = contentResolver.
                query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (contactCur == null) {
            return;
        }
        while (contactCur.moveToNext()) {

            String contactId = contactCur.getString(contactCur.getColumnIndex(ContactsContract.Contacts._ID));
            //联系人姓名
            String name = contactCur.getString(contactCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //头像
            getPhotoCommon(contentResolver,contactId);
            //备注
            getData1(contentResolver,contactId,ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE);
            //电话号码
            getContactPhonenumber(contentResolver,contactId);
        }

        contactCur.close();

    }

    /**
     * 联系人电话
     */
    public static String getContactPhonenumber(ContentResolver contentResolver, String contactId) {
        String phoneNumber = null;
        Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + " = " + contactId,
                null, null);
        while (phones.moveToNext()) {
             phoneNumber = phones.getString(phones.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        phones.close();
        return phoneNumber;
    }

    /**
     * @param personId raw_contact_id这里用的这个 对应personId获取必须也用raw_contact_id(21+)这里用的这个
     *                 通过raw_contact_id 获取头像
     */
    @TargetApi(21)
    private static Bitmap getContactPhotoAbove21(Context c, String personId,
                                          int defaultIco) {
        byte[] data = new byte[0];
        Uri u = Uri.parse("content://com.android.contacts/data");
        String where = "raw_contact_id = " + personId
                + " AND mimetype ='vnd.android.cursor.item/photo'";
        Cursor cursor = c.getContentResolver()
                .query(u, null, where, null, null);
        if (cursor.moveToFirst()) {
            data = cursor.getBlob(cursor.getColumnIndex("data15"));
        }
        cursor.close();
        if (data == null || data.length == 0) {
            return BitmapFactory.decodeResource(c.getResources(), defaultIco);
        } else
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }


    /**
     * 通过_id获取头像
     */
    public static Bitmap getPhotoCommon(final ContentResolver contentResolver, String contactId) {
        Bitmap photo = null;
        Cursor dataCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                new String[]{"data15"},
                ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                        + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'",
                new String[]{String.valueOf(contactId)}, null);
        if (dataCursor != null) {
            if (dataCursor.getCount() > 0) {
                dataCursor.moveToFirst();
                byte[] bytes = dataCursor.getBlob(dataCursor.getColumnIndex("data15"));
                if (bytes != null) {
                    photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Log.e("ligen", "getPhoto: 有头像" + contactId);
                }
            }
            dataCursor.close();
        }
        return photo;
    }


    /**
     * 没有特例写的东西都用这个方法获取
     * 根据MIMETYPE类型, 返回对应联系人的data1字段的数据
     *
     * @param mimeType
     *  ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,//联系人名称
        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,//联系人电话(可能包含多个)
        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,//邮箱(多个)
        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE,//头像试过不能用 用
        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,//公司
        ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE,
        ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE,
        ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE,//备注
        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,//地址
        ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
        ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE,//网站
        ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
        ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE,
        ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE
     */
    private static String getData1(final ContentResolver contentResolver, String contactId, final String mimeType) {
        StringBuilder stringBuilder = new StringBuilder();

        Cursor dataCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data.DATA1},
                ContactsContract.Data.CONTACT_ID + "=?" + " AND "
                        + ContactsContract.Data.MIMETYPE + "='" + mimeType + "'",
                new String[]{String.valueOf(contactId)}, null);
        if (dataCursor != null && dataCursor.getCount() > 0) {
            if (dataCursor.moveToFirst()) {
                do {
                    stringBuilder.append(dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1)));
                    //                    stringBuilder.append("_");//多个值,之间的分隔符.可以自定义;
                } while (dataCursor.moveToNext());
            }
            dataCursor.close();
        }

        return stringBuilder.toString();
    }
}
