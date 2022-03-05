package pl.octicos.zakupy;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import pl.octicos.zakupy.R.id;
import static pl.octicos.zakupy.SendPost.*;

public class ListaZakupow extends Activity {

	private ListaZakupowAdapter listaZakupowAdapter;
	private ListView listaZakupow;
	private int produktIndex;
	Context context;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_zakupow);
		try {
			if (MainActivity.dane.uzytkownikPolaczony != null){
				findViewById(R.id.wyczyscListeButton).setEnabled(false);
			} else {
				findViewById(R.id.wyczyscListeButton).setEnabled(true);
			}
			refresh();
			pobierz();
			aktualizujListe();
			context = this;
			registerForContextMenu(listaZakupow);
			listaZakupow.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					TextView nazwa = (TextView) view.findViewById(R.id.produktText);
					TextView ilosc = (TextView) view.findViewById(R.id.iloscText);
					Produkt produkt = (Produkt) parent.getItemAtPosition(position);
					pobierz();
					if (produkt.isKupiony()) {
						nazwa.setTextColor(Color.BLACK);
						ilosc.setTextColor(Color.BLACK);
						MainActivity.dane.listaProduktow.remove(produkt);
						produkt.setKupiony(false);
						MainActivity.dane.listaProduktow.add(0, produkt);
						Toast.makeText(context, "Wycofano produkt " + produkt.getNazwa() + " z listy zakupow",
								Toast.LENGTH_SHORT).show();
						

					} else {
						nazwa.setTextColor(Color.GRAY);
						ilosc.setTextColor(Color.GRAY);
						MainActivity.dane.listaProduktow.remove(produkt);
						produkt.setKupiony(true);
						MainActivity.dane.listaProduktow.add(MainActivity.dane.listaProduktow.size(), produkt);
						Toast.makeText(context, "Produkt " + produkt.getNazwa() + " został kupiony", Toast.LENGTH_SHORT)
								.show();
						
					}
					
					aktualizujListe();
					update(MainActivity.dane.listaProduktow);
				}

			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pobierz() {
		try {
			if(MainActivity.dane.uzytkownikPolaczony == null){
				String odpowiedz = "";
				_AsyncSendPost = new SendPost();
				try {
					odpowiedz = _AsyncSendPost
							.execute("http://www.zakupypollub.cba.pl/getProductsList.php", "email=" + MainActivity.dane.uzytkownik.getEmail())
							.get();
				} catch (InterruptedException | ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				odpowiedz = odpowiedz.substring(odpowiedz.indexOf("*STARTLISTY*") + 12,
						odpowiedz.indexOf("*KONIECLISTY*"));
				
				MainActivity.dane.listaProduktow = new ArrayList<Produkt>();
				String[] odp = odpowiedz.split(";");
				for (String el2 : odp) {
					String[] prod = el2.split(" - ");					
					MainActivity.dane.listaProduktow.add(new Produkt(prod[0], prod[1], (prod[2].equals("1") ? true : false)));
				}

			} else {
			String odpowiedz = "";
			for (Uzytkownik el : MainActivity.dane.listaUzytkownikow) {
				if (el.isPolaczony()) {
					odpowiedz = "";
					_AsyncSendPost = new SendPost();
					try {
						odpowiedz = _AsyncSendPost
								.execute("http://www.zakupypollub.cba.pl/getProductsList.php", "email=" + el.getEmail())
								.get();
					} catch (InterruptedException | ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					odpowiedz = odpowiedz.substring(odpowiedz.indexOf("*STARTLISTY*") + 12,
							odpowiedz.indexOf("*KONIECLISTY*"));
					
					MainActivity.dane.listaProduktow = new ArrayList<Produkt>();
					String[] odp = odpowiedz.split(";");
					for (String el2 : odp) {
						String[] prod = el2.split(" - ");					
						MainActivity.dane.listaProduktow.add(new Produkt(prod[0], prod[1], (prod[2].equals("1") ? true : false)));
					}

				}
			}
			}
			Dane.setClassFile(MainActivity.dane);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void aktualizujListe() {

		try {

			List<Produkt> lista = new ArrayList<Produkt>(MainActivity.dane.listaProduktow);
			Collections.reverse(lista);
			listaZakupow = (ListView) findViewById(R.id.listaZakupowList);
			listaZakupowAdapter = new ListaZakupowAdapter(this, R.layout.produkt, MainActivity.dane.listaProduktow);
			listaZakupow.setAdapter(listaZakupowAdapter);
			Dane.setClassFile(MainActivity.dane);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	@Override
	protected void onResume() {
		// aktualizujListe();
		super.onResume();
	}

	public void dodaj(View view) {
		try {
			pobierz();
			EditText nazwa = (EditText) findViewById(id.podajProduktField);
			EditText ilosc = (EditText) findViewById(id.iloscField);
			if(nazwa.getText().toString().isEmpty())
				return;
			Produkt produkt = new Produkt(nazwa.getText().toString(), ilosc.getText().toString(), false);
			MainActivity.dane.listaProduktow.add(produkt);
			nazwa.setText(null);
			ilosc.setText(null);
			Toast.makeText(this, "Dodano produkt " + produkt.getNazwa() + " - " + produkt.getIlosc(),
					Toast.LENGTH_SHORT).show();
			List<Produkt> lista = new ArrayList<Produkt>(MainActivity.dane.listaProduktow);
			Collections.reverse(lista);
			listaZakupow = (ListView) findViewById(R.id.listaZakupowList);
			listaZakupowAdapter = new ListaZakupowAdapter(this, R.layout.produkt, MainActivity.dane.listaProduktow);
			listaZakupow.setAdapter(listaZakupowAdapter);
			Dane.setClassFile(MainActivity.dane);
			update(lista);

			aktualizujListe();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_zakupow, menu);
		return true;
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		
		if (v.getId() == R.id.listaZakupowList) {
			ListView lv = (ListView) v;
			AdapterView.AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) menuInfo;
			produktIndex = MainActivity.dane.listaProduktow.indexOf((Produkt) lv.getItemAtPosition(acmi.position));

		}
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.menu_kontekstowe_produkt, menu);

	}

	public boolean onContextItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {

			case R.id.edytujProdukt: {
				edytujProdukt(item.getItemId());
			}
				break;
			case R.id.usunProdukt: {
				usunProdukt(item.getItemId());
			}
				break;
			default:

		}
		return true;
	}

	public void edytujProdukt(int id) {

		Intent intent = new Intent(this, EdytujProdukt.class);
		intent.putExtra("index", produktIndex);
		startActivity(intent);
	}

	void usunProdukt(int id) {

		Produkt produkt = MainActivity.dane.listaProduktow.get(produktIndex);
		Toast.makeText(this, "Usunieto produkt " + produkt.getNazwa() + " - " + produkt.getIlosc(), Toast.LENGTH_SHORT)
				.show();
		MainActivity.dane.listaProduktow.remove(produktIndex);
		aktualizujListe();
		update(MainActivity.dane.listaProduktow);
	}

	public void wyczyscListe(View view) {
		MainActivity.dane.listaProduktow = new ArrayList<>();
		listaZakupow = (ListView) findViewById(R.id.listaZakupowList);
		listaZakupowAdapter = new ListaZakupowAdapter(this, R.layout.produkt, MainActivity.dane.listaProduktow);
		listaZakupow.setAdapter(listaZakupowAdapter);
		Dane.setClassFile(MainActivity.dane);
		aktualizujListe();
		update(MainActivity.dane.listaProduktow);
	}

	public void odswiezListe(View view) {
		refresh();
		pobierz();
		aktualizujListe();
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

	public void update(List<Produkt> lista) {
		String listaZakupow = "";
		for (Produkt produkt : lista) {
			listaZakupow += produkt.getNazwaIlosc() + ";";
			
		}
		String odpowiedz = "";
		_AsyncSendPost = new SendPost();
		try {
			if(MainActivity.dane.uzytkownikPolaczony != null){
			odpowiedz = _AsyncSendPost.execute("http://www.zakupypollub.cba.pl/update.php",
					"email=" + MainActivity.dane.uzytkownikPolaczony.getEmail() + "&products=" + listaZakupow).get();
			} else {
				odpowiedz = _AsyncSendPost.execute("http://www.zakupypollub.cba.pl/update.php",
						"email=" + MainActivity.dane.uzytkownik.getEmail() + "&products=" + listaZakupow).get();
			}
		} catch (InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
					pobierz();
					MainActivity.dane.uzytkownik.setCzyTworzyListe(true);
					break;
				}
			}
		} else {
			for (Uzytkownik el : MainActivity.dane.listaUzytkownikow) {
				if (el.isPolaczony()) {
					el.setPolaczony(false);
					MainActivity.dane.uzytkownik.setCzyTworzyListe(true);
				}
			}
		}
		Dane.setClassFile(MainActivity.dane);

	}
}
