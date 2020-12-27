package com.androidsecuritycookbook.chapter5.recipe4;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * Data accessor for the RSS items table
 * 
 */
public class RssItemDAO {

	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;

	static String COL_TITLE = "title";

	static String TABLE_NAME = "RSS_ITEMS";

	static String INSERT_SQL = "insert into  " + TABLE_NAME
			+ " (content, link, title) values (?,?,?)";

	public RssItemDAO(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(INSERT_SQL);
	}

	/**
	 * Saves a RssItem to the database
	 * 
	 * @param item
	 * @return rowId of the inserted row, or -1 if failed.
	 */
	public long save(RssItem item) {
		insertStatement.bindString(1, item.getContent());
		insertStatement.bindString(2, item.getLink());
		insertStatement.bindString(3, item.getTitle());
		return insertStatement.executeInsert();
	}

	/**
	 * Gets the RSS items with matching title
	 * 
	 * @param title
	 *            uses wildcards and matches anyway in the title
	 * @return list of matching rssItems
	 */
	public List<RssItem> fetchRssItemsByTitle(String title) {

		Cursor cursor = db.query(TABLE_NAME, null, COL_TITLE + "LIKE ?",
				new String[] { "%" + title + "%" }, null, null, null);

		// process cursor into list
		List<RssItem> rssItems = new ArrayList<RssItemDAO.RssItem>(
				cursor.getCount());
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			// maps cursor columns of RssItem properties
			RssItem item = cursorToRssItem(cursor);
			rssItems.add(item);
			cursor.moveToNext();
		}
		return rssItems;
	}

	/**
	 * maps cursor columns of RssItem properties
	 * 
	 * @param cursor
	 * @return
	 */
	private RssItem cursorToRssItem(Cursor cursor) {
		// not implemented for this demo
		return null;
	}

	/**
	 * Data object to represent a RSS story
	 * 
	 * @author scottab
	 * 
	 */
	public class RssItem {
		private String content;
		private String link;
		private String title;

		public RssItem(String content, String link, String title) {
			super();
			this.content = content;
			this.link = link;
			this.title = title;
		}

		public String getContent() {
			return content;
		}

		public String getLink() {
			return link;
		}

		public String getTitle() {
			return title;
		}

	}
}
