package mincheol.memberapplication.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabasehHelper extends SQLiteOpenHelper {

    // 데이터베이스 초기화 변수
    private static final String DBNAME = "android.db";
    private static final int DBVERSION = 1;

    // 생성자 - 클래스 호출시 자동으로 sqlite 디비 파일 생성(한번만!)
    public DatabasehHelper(@Nullable Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    // 디비 파일 생성시 자동으로 실행하는 메서드
    // 주로 테이블 생성시 사용
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL = "create table member (" +
                "mno int primary key autoincrement,+ " +
                "userid verchar(18) unique," +
                "passwd verchar(18) not null," +
                "name verchar(18) not null," +
                "email text(18) not null," +
                "regdate datetime default current_timestamp)";
        db.execSQL(SQL);
    }

    // 테이블 재생성시 사용
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists member");
        onCreate(db);
    }

}
