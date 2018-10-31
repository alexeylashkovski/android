package by.transavto.transavto;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

class ClassDB {
	private static final String DB_NAME = "transavtoDB";
	private static final int DB_VERSION = 1;
	private final Context mCtx;

	private DBHelper mDBHelper;
	private SQLiteDatabase mDB;

	public ClassDB(Context ctx) {
		mCtx = ctx;
	}

	// открыть подключение
	public SQLiteDatabase open() {
		mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
		mDB = mDBHelper.getWritableDatabase();
		return mDB;
	}

	// закрыть подключение
	public void close() {
		if (mDBHelper != null)
			mDBHelper.close();
	}

	// получить все данные из таблицы DB_TABLE
	public Cursor getAllData(String db_table) {
		return mDB.query(db_table, null, null, null, null, null, null);
	}

	private class DBHelper extends SQLiteOpenHelper {

		final String TAG = "DB";
		final static String DB_NAME = "transavtoDB";
		final static int DB_VERSION = 1;

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//Log.d(TAG, "--- onCreate database ---");
			// создаем таблицу с полями
			db.execSQL("create table last_locations ("
					+ "id integer primary key autoincrement," + "locId text,"
					+ "flag integer," + "country text," + "region text,"
					+ "city text," + "time integer" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}
}