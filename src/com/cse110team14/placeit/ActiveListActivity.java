package com.cse110team14.placeit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.cse110team14.placeit.model.PlaceIt;
import com.cse110team14.placeit.model.SimplePlaceIt;
import com.cse110team14.placeit.model.CPlaceIts;
import com.cse110team14.placeit.server_side.UpdatePlaceItsOnServer;
import com.cse110team14.placeit.util.CustomComparator;

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
	private Iterator<SimplePlaceIt> piIterator;
	private List<SimplePlaceIt> sorted;

	List<HashMap<String, String>> activeListMap;

	private SimplePlaceIt clicked;
	private int id;

	/**
	 * Called when the activity is first created. This method create the list
	 * view, and show the list view items and bind it to the click listener.
	 * 
	 * @param savedInstanceState
	 *            - If the activity is being re-initialized after previously
	 *            being shut down then this Bundle contains the data it most
	 *            recently supplied in onSaveInstanceState(Bundle).
	 * 
	 * @return void
	 */
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		sorted = new ArrayList<SimplePlaceIt>();

		final List<PlaceIt> active = MainActivity.getActiveList();
		final List<CPlaceIts> c_active = MainActivity.cActiveList;
		for (Iterator<PlaceIt> i = active.iterator(); i.hasNext();)
			sorted.add(i.next());
		for(Iterator<CPlaceIts> i = c_active.iterator(); i.hasNext();)
			sorted.add(i.next());
		Collections.sort(sorted, new CustomComparator());

		setContentView(R.layout.activity_activelist);

		ListView listView = (ListView) findViewById(R.id.ActiveListView);

		refresh();

		SimpleAdapter adapter = new SimpleAdapter(this, activeListMap,
				R.layout.list_item, new String[] { "ItemTitle", "ItemText",
						"ItemDateToRemind", "ItemPostTime" }, new int[] {
						R.id.ItemTitle, R.id.ItemText, R.id.ItemDateToRemind,
						R.id.ItemPostTime });
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long id) {

				ActiveListActivity.this.id = (int) id;
				clicked = sorted.get((int) id);
				//Regular placeit
				if(clicked.getRorC() == 1){
					clicked = (PlaceIt)sorted.get((int)id);
					Dialog detailsDialog = createDetailsDialog(clicked);
					detailsDialog.show();
				}
				//category placeit
				else if(clicked.getRorC() == 2)
				{
					clicked = (CPlaceIts)sorted.get((int)id);
					Dialog detailsDialog = createDetailsDialog(clicked);
					detailsDialog.show();
				}
			}
		});
	}

	/**
	 * This method create the dialog showing the details of a place-it, and the
	 * related buttons for repost and other options.
	 * 
	 * @param none
	 * 
	 * @return the dialog showing actions and details of a place-it
	 */
	public Dialog createDetailsDialog(SimplePlaceIt p1) {
		if(p1.getRorC() == 1){
			final PlaceIt clicked = (PlaceIt) p1;
			Dialog dia = new AlertDialog.Builder(ActiveListActivity.this)
				.setTitle("Title: " + clicked.getTitle())
				.setItems(
						new String[] {
								"Description: " + clicked.getDescription(),
								"Date to be Reminded: "
										+ clicked.getDateReminded(),
								"Post Date and time: " + clicked.getDate(),
								"Location: (" + clicked.getLocation().latitude
										+ ", "
										+ clicked.getLocation().longitude + ")" },
						null)
				.setPositiveButton("Move To Pulled-Down",
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MainActivity.pullDown.add(clicked);
								sorted.remove(clicked);
								MainActivity.activeList.remove(clicked);
								//Set the list type to be 2 to indicate pulled down
								clicked.setListType("2");
								UpdatePlaceItsOnServer.postPlaceIts(clicked);
								Toast.makeText(
										ActiveListActivity.this,
										"Item \""
												+ clicked.getTitle()
												+ "\" is now moved to Pulled-Down list",
										Toast.LENGTH_LONG).show();
								finish();
								startActivity(getIntent());
							}
						}).setNeutralButton("OK", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(
								ActiveListActivity.this,
								"Reminding item \"" + clicked.getTitle()
										+ "\" completed.", Toast.LENGTH_LONG)
								.show();
					}
				}).setNegativeButton("Discard", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						sorted.remove(clicked);
						MainActivity.activeList.remove(clicked);
						clicked.setListType("3");
						UpdatePlaceItsOnServer.postPlaceIts(clicked);
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
		//If a C-PlaceIts is clicked
		else{
			final CPlaceIts clicked = (CPlaceIts) p1;
			Dialog dia = new AlertDialog.Builder(ActiveListActivity.this)
				.setTitle("Title: " + clicked.getTitle())
				.setItems(
						new String[] {
								"Description: " + clicked.getDescription(),
								"Date to be Reminded: "
										+ clicked.getDateReminded(),
								"Post Date and time: " + clicked.getDate(),
								"Categories: " + clicked.getCategoriesToString() },
						null)
				.setPositiveButton("Move To Pulled-Down",
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MainActivity.cPullDownList.add(clicked);
								sorted.remove(clicked);
								MainActivity.cActiveList.remove(clicked);
								//Set the list type to be 2 to indicate pulled down
								clicked.setListType("2");
								UpdatePlaceItsOnServer.postCPlaceIts(clicked);
								Toast.makeText(
										ActiveListActivity.this,
										"Item \""
												+ clicked.getTitle()
												+ "\" is now moved to Pulled-Down list",
										Toast.LENGTH_LONG).show();
								finish();
								startActivity(getIntent());
							}
						}).setNeutralButton("OK", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(
								ActiveListActivity.this,
								"Reminding item \"" + clicked.getTitle()
										+ "\" completed.", Toast.LENGTH_LONG)
								.show();
					}
				}).setNegativeButton("Discard", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						sorted.remove(clicked);
						MainActivity.cActiveList.remove(clicked);
						clicked.setListType("3");
						UpdatePlaceItsOnServer.postCPlaceIts(clicked);
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
	}

	/**
	 * re-put list item onto the active list
	 * 
	 * @param none
	 * 
	 * @return void
	 */
	public void refresh() {
		activeListMap = new ArrayList<HashMap<String, String>>();
		for (SimplePlaceIt curr : sorted) {
			HashMap<String, String> map = new HashMap<String, String>();

			map.put("ItemTitle", "Title: " + curr.getTitle());
			map.put("ItemText", "Description: " + curr.getDescription());
			map.put("ItemDateToRemind",
					"Date and time to Remind: " + curr.getDateReminded());
			map.put("ItemPostTime", "Post Time: " + curr.getDate());

			activeListMap.add(map);
		}
	}

	/**
	 * Initialize the contents of the Activity's standard options menu
	 * 
	 * @param The
	 *            options menu in which you place your items.
	 * 
	 * @return Initialize the contents of the Activity's standard options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
