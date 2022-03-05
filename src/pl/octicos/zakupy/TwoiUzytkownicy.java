package pl.octicos.zakupy;

import java.io.BufferedReader;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.textservice.SpellCheckerSession.SpellCheckerSessionListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import pl.octicos.zakupy.R.id;
import static pl.octicos.zakupy.SendPost.*;

public class TwoiUzytkownicy extends Activity {

	private ListView listaUzytkownikow;
	private ListaUzytkownikowAdapter adapter;
	private int uzytkownikIndex;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twoi_uzytkownicy);
		try {

			refresh();
			
			context = this;
			aktualizujListe();
			registerForContextMenu(listaUzytkownikow);
			

			listaUzytkownikow.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					TextView email = (TextView) view.findViewById(R.id.uzytkownikText);
					Uzytkownik uzytkownik = (Uzytkownik) parent.getItemAtPosition(position);
					if (uzytkownik.isPolaczony()) {
						disconnect(uzytkownik);
						MainActivity.dane.uzytkownikPolaczony = null;
						email.setTextColor(Color.BLACK);
						uzytkownik.setPolaczony(false);
						Toast.makeText(context, "Rozlaczono z uzytkownikiem " + uzytkownik.getEmail(),
								Toast.LENGTH_SHORT).show();
						getProductsList(MainActivity.dane.uzytkownik);
					} else {

						if(MainActivity.dane.uzytkownikPolaczony != null){
							Toast.makeText(context, "Jesteś już połączony z innym użytkownikiem!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						connect(uzytkownik);
						email.setTextColor(Color.GREEN);
						uzytkownik.setPolaczony(true);
						MainActivity.dane.uzytkownikPolaczony = uzytkownik;
						MainActivity.dane.uzytkownik.setCzyTworzyListe(true);
						Toast.makeText(context, "Polaczono z uzytkownikiem " + uzytkownik.getEmail(),
								Toast.LENGTH_SHORT).show();
						getProductsList(uzytkownik);

					}
					

				}
			});
			Dane.setClassFile(MainActivity.dane);
			aktualizujListe();
			registerForContextMenu(listaUzytkownikow);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	@Override
	protected void onResume() {
		// aktualizujListe();
		super.onResume();
	}

	public void aktualizujListe() {
		adapter = new ListaUzytkownikowAdapter(this, R.layout.uzytkownicy, MainActivity.dane.listaUzytkownikow);
		listaUzytkownikow = (ListView) findViewById(R.id.listaTwoiUzytkownicy);
		listaUzytkownikow.setAdapter(adapter);
		Dane.setClassFile(MainActivity.dane);

	}

	public void dodaj(View view) {
		EditText uzytkownik = (EditText) findViewById(R.id.uzytkownicyField);
		String email = uzytkownik.getText().toString();
		uzytkownik.setText(null);
		String odpowiedz = "";
		_AsyncSendPost = new SendPost();
		try {
			odpowiedz = _AsyncSendPost
					.execute("http://www.zakupypollub.cba.pl/ifUserExist.php", "email=" + email)
					.get();
		} catch (InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(odpowiedz.contains("!false?")){
			Toast.makeText(this, "Użytkownik " + email + " nie istnieje!", Toast.LENGTH_SHORT).show();
			return;
		}
		System.out.println(email);
		// TODO zrobic uwierzytelnienie
		if(!email.isEmpty()){
			MainActivity.dane.listaUzytkownikow.add(new Uzytkownik(email, false));
			Toast.makeText(this, "Dodano użytkownika " + email, Toast.LENGTH_SHORT).show();
			aktualizujListe();
			Dane.setClassFile(MainActivity.dane);
		}

	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.listaTwoiUzytkownicy) {
			ListView lv = (ListView) v;
			AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
			uzytkownikIndex = MainActivity.dane.listaUzytkownikow
					.indexOf((Uzytkownik) lv.getItemAtPosition(acmi.position));

		}
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.menu_kontekstowe_uzytkownik, menu);

	}

	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.usunUzytkowika: {
			usunUzytkownika(item.getItemId());
		}
			break;
		default:

		}
		return true;
	}

	public void usunUzytkownika(int id) {

		Uzytkownik uzytkownik = MainActivity.dane.listaUzytkownikow.get(uzytkownikIndex);
		Toast.makeText(this, "Usunieto uzytkownika " + uzytkownik.getEmail(), Toast.LENGTH_SHORT).show();
		MainActivity.dane.listaUzytkownikow.remove(uzytkownik);
		aktualizujListe();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.twoi_uzytkownicy, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void getProductsList(Uzytkownik uzytkownik) {
		String odpowiedz = "";
		_AsyncSendPost = new SendPost();
		try {
			odpowiedz = _AsyncSendPost
					.execute("http://www.zakupypollub.cba.pl/getProductsList.php", "email=" + uzytkownik.getEmail())
					.get();
		} catch (InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		odpowiedz = odpowiedz.substring(odpowiedz.indexOf("*STARTLISTY*") + 12, odpowiedz.indexOf("*KONIECLISTY*"));
		MainActivity.dane.listaProduktow = new ArrayList<Produkt>();
		String[] odp = odpowiedz.split(";");
		if(odp.length > 1){
		for (String el : odp) {
			String[] prod = el.split(" - ");
			MainActivity.dane.listaProduktow.add(new Produkt(prod[0], prod[1], (prod[2].equals("1") ? true : false)));				
			

		}
		}
		Dane.setClassFile(MainActivity.dane);
	}

	public void connect(Uzytkownik uzytkownik) {
		String odpowiedz = "";
		_AsyncSendPost = new SendPost();
		try {
			odpowiedz = _AsyncSendPost
					.execute("http://www.zakupypollub.cba.pl/connect.php",
							"email=" + uzytkownik.getEmail() + "&emailMoj=" + MainActivity.dane.uzytkownik.getEmail())
					.get();
		} catch (InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MainActivity.dane.uzytkownik.setCzyTworzyListe(true);
		Dane.setClassFile(MainActivity.dane);
	}

	public void disconnect(Uzytkownik uzytkownik) {
		String odpowiedz = "";
		_AsyncSendPost = new SendPost();
		try {
			odpowiedz = _AsyncSendPost
					.execute("http://www.zakupypollub.cba.pl/disconnect.php",
							"email=" + uzytkownik.getEmail() + "&emailMoj=" + MainActivity.dane.uzytkownik.getEmail())
					.get();
		} catch (InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		uzytkownik.setPolaczony(false);
		MainActivity.dane.uzytkownik.setCzyTworzyListe(true);
		Dane.setClassFile(MainActivity.dane);
	}

	public void refresh() {
		String odpowiedz = "";
		_AsyncSendPost = new SendPost();
		try {
			odpowiedz = _AsyncSendPost.execute("http://www.zakupypollub.cba.pl/refresh.php",
					"email=" + MainActivity.dane.uzytkownik.getEmail()).get();
		} catch (InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (odpowiedz.contains("polacz1")) {
			odpowiedz = odpowiedz.substring(odpowiedz.indexOf("polacz1") + 7, odpowiedz.indexOf("polacz2"));
			for (Uzytkownik el : MainActivity.dane.listaUzytkownikow) {
				if (el.getEmail().matches(odpowiedz)) {
					el.setPolaczony(true);
					MainActivity.dane.uzytkownik.setCzyTworzyListe(true);
					MainActivity.dane.uzytkownikPolaczony = el;
					break;
				}
			}
		} else {
			for (Uzytkownik el : MainActivity.dane.listaUzytkownikow) {
				if (el.isPolaczony()) {
					el.setPolaczony(false);
				}
			}
			MainActivity.dane.uzytkownik.setCzyTworzyListe(true);
		}
		Dane.setClassFile(MainActivity.dane);
	}
	
}
