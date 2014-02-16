package com.cse110team14.placeit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ActiveListActivity<activeListView> extends Activity {
	private Iterator<PlaceIt> piIterator;
	private List<PlaceIt> sorted;

	List<HashMap<String, String>> activeList;

	private PlaceIt clicked;
	private int id;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		sorted = new ArrayList<PlaceIt>();
		// piIterator = MainActivity.PlaceIts.iterator();

		final List<PlaceIt> active = MainActivity.getActiveList();
		for (Iterator<PlaceIt> i = active.iterator(); i.hasNext();)
			sorted.add(i.next());

		Collections.sort(sorted, new CustomComparator());

		setContentView(R.layout.activity_activelist);

		ListView listView = (ListView) findViewById(R.id.ActiveListView);

		refresh();

		SimpleAdapter adapter = new SimpleAdapter(this, activeList,
				R.layout.list_item, new String[] { "ItemTitle", "ItemText",
						"ItemDateToRemind","ItemPostTime" }, new int[] { R.id.ItemTitle,
						R.id.ItemText, R.id.ItemDateToRemind, R.id.ItemPostTime });
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long id) {
				// ListView listView = (ListView) arg0;

				ActiveListActivity.this.id = (int) id;
				clicked = sorted.get((int) id);
/*
				Dialog dialog = new AlertDialog.Builder(ActiveListActivity.this)
						.setTitle("Title: " + clicked.getTitle())
						.setItems(
								new String[] {
										"Description: "
												+ clicked.getDescription(),
										"Date to be Reminded: "
												+ clicked.getDateReminded(),
										"Post Date and time: "
												+ clicked.getDate(),
										"Location: ("
												+ clicked.getLocation().latitude
												+ ", "
												+ clicked.getLocation().longitude
												+ ")" }, null)
						.setPositiveButton("Move To Pulled-Down",
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										// List<PlaceIt> pulled =
										// MainActivity.getActiveList();
										MainActivity.pullDown.add(clicked);
										sorted.remove(clicked);
										MainActivity.PlaceIts.remove(clicked);

										Toast.makeText(
												ActiveListActivity.this,
												"Item \""
														+ clicked.getTitle()
														+ "\" is now moved to Pulled-Down list",
												Toast.LENGTH_LONG).show();
										finish();
										startActivity(getIntent());
									}
								})
						.setNeutralButton("OK", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Toast.makeText(
										ActiveListActivity.this,
										"Reminding item \""
												+ clicked.getTitle()
												+ "\" completed.",
										Toast.LENGTH_LONG).show();
							}
						}).setNegativeButton("Discard", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								sorted.remove(clicked);
								MainActivity.PlaceIts.remove(clicked);

								Toast.makeText(
										ActiveListActivity.this,
										"Item \"" + clicked.getTitle()
												+ "\" is now discarded.",
										Toast.LENGTH_LONG).show();
								finish();
								startActivity(getIntent());
							}
						}).create();

				dialog.show();*/

				// Toast.makeText(
				// ActiveListActivity.this,"ID：" + id + ":" + clicked.getTitle()
				// + "item："
				// + listView.getItemAtPosition(arg2).toString(),
				// Toast.LENGTH_LONG).show();

				Dialog detailsDialog = createDetailsDialog();
				detailsDialog.show();
			}
		});
	}

	public Dialog createDetailsDialog(){
		Dialog dia = new AlertDialog.Builder(ActiveListActivity.this)
		.setTitle("Title: " + clicked.getTitle())
		.setItems(
				new String[] {
						"Description: "
								+ clicked.getDescription(),
						"Date to be Reminded: "
								+ clicked.getDateReminded(),
						"Post Date and time: "
								+ clicked.getDate(),
						"Location: ("
								+ clicked.getLocation().latitude
								+ ", "
								+ clicked.getLocation().longitude
								+ ")" }, null)
		.setPositiveButton("Move To Pulled-Down",
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {

						// List<PlaceIt> pulled =
						// MainActivity.getActiveList();
						MainActivity.pullDown.add(clicked);
						sorted.remove(clicked);
						MainActivity.PlaceIts.remove(clicked);

						Toast.makeText(
								ActiveListActivity.this,
								"Item \""
										+ clicked.getTitle()
										+ "\" is now moved to Pulled-Down list",
								Toast.LENGTH_LONG).show();
						finish();
						startActivity(getIntent());
					}
				})
		.setNeutralButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				Toast.makeText(
						ActiveListActivity.this,
						"Reminding item \""
								+ clicked.getTitle()
								+ "\" completed.",
						Toast.LENGTH_LONG).show();
			}
		}).setNegativeButton("Discard", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {

				sorted.remove(clicked);
				MainActivity.PlaceIts.remove(clicked);

				Toast.makeText(
						ActiveListActivity.this,
						"Item \"" + clicked.getTitle()
								+ "\" is now discarded.",
						Toast.LENGTH_LONG).show();
				finish();
				startActivity(getIntent());
			}
		}).create();
		return dia;
	}
	
	public void refresh() {
		activeList = new ArrayList<HashMap<String, String>>();
		for (PlaceIt curr : sorted) {
			HashMap<String, String> map = new HashMap<String, String>();
			// curr = piIterator.next();
			map.put("ItemTitle", "Title: " + curr.getTitle());
			map.put("ItemText", "Description: " + curr.getDescription());
			map.put("ItemDateToRemind",
					"Date and time to Remind: " + curr.getDateReminded());
			map.put("ItemPostTime", "Post Time: " + curr.getDate());
			activeList.add(map);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
