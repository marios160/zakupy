package pl.octicos.zakupy;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EdytujProdukt extends Activity {

	private Produkt produkt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edytuj_produkt);
		EditText nazwa = (EditText) findViewById(R.id.edytujNazweProduktu);
		EditText ilosc = (EditText) findViewById(R.id.edytujIloscProduktu);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    produkt = MainActivity.dane.listaProduktow.get(extras.getInt("index"));
		}
		nazwa.setText(produkt.getNazwa());
		ilosc.setText(produkt.getIlosc());

	}
	
	public void zapisz(View view) {
		EditText nazwa = (EditText) findViewById(R.id.edytujNazweProduktu);
		EditText ilosc = (EditText) findViewById(R.id.edytujIloscProduktu);
		produkt.setNazwa(nazwa.getText().toString());
		produkt.setIlosc(ilosc.getText().toString());
		onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edytuj_produkt, menu);
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
}
