package com.androidsecuritycookbook.chapter9.recipe5;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabase.CursorFactory;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.content.Context;

/**
 * 
 * Remember to include the libraries from https://github.com/sqlcipher/android-database-sqlcipher
 *
 */
public class SQLCipherUtil {
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "my_encrypted_data.db";

	public void initDB(Context context, String password) {
		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_NAME,
				password, null);
		database.execSQL("create table MyTable(a, b)");
	}

	public class SQLCipherHelper extends SQLiteOpenHelper {

		public SQLCipherHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int arg1, int arg2) {

		}

		@Override
		public synchronized SQLiteDatabase getWritableDatabase(String passpharse) {
			return super.getWritableDatabase(passpharse);
		}

	}

}
