package com.nec.staffApp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;


/**
 * Created by Javeeth Asraf on 11/4/2017.
 */

public class DBHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "staff.db";
    private static final int DATABASE_VERSION = 1;
    private static final String ID="ID";
    private static final String NAME="NAME";
    private static final String DEPT="DEPT";
    private static final String PHNO="PHNO";
    private static final String QFN="QFN";
    private static final String DSN="DSN";
    private static final String MAIL="MAIL";
    private static final String TABLE="staff";

    private Context mContext;
    private SQLiteDatabase mDB;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;
        setForcedUpgrade();
    }
    public Staff getStaff(String id)
    {
        SQLiteDatabase db=getReadableDatabase();
        String[] columns={DBHelper.ID,DBHelper.NAME,DBHelper.DEPT,DBHelper.PHNO,DBHelper.QFN,DBHelper.DSN,DBHelper.MAIL};
        Cursor cursor = db.query(DBHelper.TABLE, columns, "ID=?", new String[] { id }, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

            Staff questions = new Staff();
            questions.id = cursor.getString(cursor.getColumnIndex(DBHelper.ID));
            questions.name = cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
            questions.dept = cursor.getString(cursor.getColumnIndex(DBHelper.DEPT));
            questions.dsn = cursor.getString(cursor.getColumnIndex(DBHelper.DSN));
            questions.qfn = cursor.getString(cursor.getColumnIndex(DBHelper.QFN));
            questions.phno = cursor.getString(cursor.getColumnIndex(DBHelper.PHNO));
            questions.mail = cursor.getString(cursor.getColumnIndex(DBHelper.MAIL));

        return questions;


    }
    public ArrayList<Staff> getUsers(){
        SQLiteDatabase db=getWritableDatabase();
        String[] columns={DBHelper.ID,DBHelper.NAME,DBHelper.DEPT,DBHelper.PHNO,DBHelper.QFN,DBHelper.DSN,DBHelper.MAIL};
//        String[] selectionArgs={categoryId+"",subjectId+"",yearId+""};
        Cursor cursor=db.query(DBHelper.TABLE, columns, null, null, null, null, null);
//        Cursor cursor=db.query(MyDatabase.TABLE_NAME, columns, null,null, null, null, null);
        ArrayList<Staff> questionsArrayList=new ArrayList<>();

        while(cursor.moveToNext()){
            Staff questions=new Staff();
            questions.id=cursor.getString(cursor.getColumnIndex(DBHelper.ID));
            questions.name=cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
            questions.dept=cursor.getString(cursor.getColumnIndex(DBHelper.DEPT));
            questions.dsn=cursor.getString(cursor.getColumnIndex(DBHelper.DSN));
            questions.qfn=cursor.getString(cursor.getColumnIndex(DBHelper.QFN));
            questions.phno=cursor.getString(cursor.getColumnIndex(DBHelper.PHNO));
            questions.mail=cursor.getString(cursor.getColumnIndex(DBHelper.MAIL));
            questionsArrayList.add(questions);
        }
        return questionsArrayList;
    }


   /* public Cursor getCursor() {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder ();

        queryBuilder.setTables("staff");

        String[] columns={DBHelper.ID,DBHelper.NAME,DBHelper.DEPT,DBHelper.PHNO,DBHelper.QFN,DBHelper.DSN,DBHelper.MAIL};

        Cursor cursor=queryBuilder.query(DBHelper.TABLE, columns, null, null, null, null, null);
        return cursor;
    }

    public String getName (Cursor c){
        return(c.getString(1));


    }*/
}
