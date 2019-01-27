package info.androidhive.nec;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Javeeth Asraf on 11/4/2017.
 */

public class DBHelper1 extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "staff.db";
    public static final String DBLOCATION="/data/data/info.androidhive.nec/databases/";
    private static final int DATABASE_VERSION = 1;
    private static final String ID="ID";
    private static final String NAME="NAME";
    private static final String DEPT="DEPT";
    private static final String PHNO="PHNO";
    private static final String QFN="QFN";
    private static final String DSN="DSN";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private static final String MAIL="MAIL";
    private static final String TABLE="staff";

    private Context mContext;
    private SQLiteDatabase mDB;

    public DBHelper1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       this.mContext=context;

    }

    public void openDatabase(){
        String dbPath=mContext.getDatabasePath(DATABASE_NAME).getPath();
        if(mDB !=null && mDB.isOpen()){
            return;
        }
        mDB=SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
    }


    public void closeDatabase(){
        if(mDB!=null){
            mDB.close();
        }
    }


    public Staff login(String mno)
    {
        Staff questions=null;
        openDatabase();
        String[] columns={DBHelper1.ID, DBHelper1.NAME, DBHelper1.DEPT, DBHelper1.PHNO, DBHelper1.QFN, DBHelper1.DSN, DBHelper1.MAIL};
        Cursor cursor =mDB.query(DBHelper1.TABLE, columns, "ID=?", new String[] { mno }, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {


            questions=new Staff();
            questions.id = cursor.getString(cursor.getColumnIndex(DBHelper1.ID));
            questions.name = cursor.getString(cursor.getColumnIndex(DBHelper1.NAME));
            questions.dept = cursor.getString(cursor.getColumnIndex(DBHelper1.DEPT));
            questions.dsn = cursor.getString(cursor.getColumnIndex(DBHelper1.DSN));
            questions.qfn = cursor.getString(cursor.getColumnIndex(DBHelper1.QFN));
            questions.phno = cursor.getString(cursor.getColumnIndex(DBHelper1.PHNO));
            questions.mail = cursor.getString(cursor.getColumnIndex(DBHelper1.MAIL));
            cursor.close();
        }

        closeDatabase();
        return questions;


    }







    public Staff getStaff(String id)
    {
        openDatabase();
        String[] columns={DBHelper1.ID, DBHelper1.NAME, DBHelper1.DEPT, DBHelper1.PHNO, DBHelper1.QFN, DBHelper1.DSN, DBHelper1.MAIL};
        Cursor cursor =mDB.query(DBHelper1.TABLE, columns, "ID=?", new String[] { id }, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

            Staff questions = new Staff();
            questions.id = cursor.getString(cursor.getColumnIndex(DBHelper1.ID));
            questions.name = cursor.getString(cursor.getColumnIndex(DBHelper1.NAME));
            questions.dept = cursor.getString(cursor.getColumnIndex(DBHelper1.DEPT));
            questions.dsn = cursor.getString(cursor.getColumnIndex(DBHelper1.DSN));
            questions.qfn = cursor.getString(cursor.getColumnIndex(DBHelper1.QFN));
            questions.phno = cursor.getString(cursor.getColumnIndex(DBHelper1.PHNO));
            questions.mail = cursor.getString(cursor.getColumnIndex(DBHelper1.MAIL));
            cursor.close();
            closeDatabase();
        return questions;


    }
    public ArrayList<Staff> getUsers(String dept){
        openDatabase();
        String[] columns={DBHelper1.ID, DBHelper1.NAME, DBHelper1.DEPT, DBHelper1.PHNO, DBHelper1.QFN, DBHelper1.DSN, DBHelper1.MAIL};
//        String[] selectionArgs={categoryId+"",subjectId+"",yearId+""};
        Cursor cursor =mDB.query(DBHelper1.TABLE, columns, "DEPT=?", new String[] { dept }, null, null, null);
//        Cursor cursor=db.query(MyDatabase.TABLE_NAME, columns, null,null, null, null, null);
        ArrayList<Staff> questionsArrayList=new ArrayList<>();

        while(cursor.moveToNext()){
            Staff questions=new Staff();
            questions.id=cursor.getString(cursor.getColumnIndex(DBHelper1.ID));
            questions.name=cursor.getString(cursor.getColumnIndex(DBHelper1.NAME));
            questions.dept=cursor.getString(cursor.getColumnIndex(DBHelper1.DEPT));
            questions.dsn=cursor.getString(cursor.getColumnIndex(DBHelper1.DSN));
            questions.qfn=cursor.getString(cursor.getColumnIndex(DBHelper1.QFN));
            questions.phno=cursor.getString(cursor.getColumnIndex(DBHelper1.PHNO));
            questions.mail=cursor.getString(cursor.getColumnIndex(DBHelper1.MAIL));
            questionsArrayList.add(questions);
        }
        cursor.close();
        closeDatabase();
        return questionsArrayList;
    }
}
