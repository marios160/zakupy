package pl.octicos.zakupy;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import static pl.octicos.zakupy.SendPost.*;



public class MainActivity extends Activity {

	static final private int CUSTOM_ALERT_DIALOG = 4;
	public static Dane dane = null;
	@Override

	protected void onCreate(Bundle savedInstanceState) {
		// showDialog(CUSTOM_ALERT_DIALOG);
		try {

			dane = Dane.getConfFile();
			if (dane == null) {
				rejestracja();
			}
			Dane.setClassFile(dane);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String odpowiedz = "";
		_AsyncSendPost = new SendPost();
		try {
			odpowiedz = _AsyncSendPost.execute("http://www.zakupypollub.cba.pl/refresh.php",
					"email=" + MainActivity.dane.uzytkownik.getEmail()).get();
		} catch (InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(odpowiedz.isEmpty()){
			Toast.makeText(this, "Brak połączenia z internetem!", Toast.LENGTH_SHORT)
			.show();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	// @Override
	// protected Dialog onCreateDialog(int id) {
	// // TODO Auto-generated method stub
	// return createCustomAlertDialog();
	// }

	// private Dialog createCustomAlertDialog() {
	// AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
	// View layout = getCustomDialogLayout();
	// dialogBuilder.setView(layout);
	// dialogBuilder.setTitle("Ta aplikacja działa wedle zasady OPOU (one phone
	// one user)");
	// Button button = (Button) findViewById(R.id.dialogOK);
	//
	// button.setOnClickListener(new OnClickListener() {
	//
	//
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// // TODO Auto-generated method stub
	//
	// }
	// };
	//
	//
	// return dialogBuilder.create();
	// }

	private View getCustomDialogLayout() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(R.layout.opou_dialog, (ViewGroup) findViewById(R.id.layout_root));
	}

	public void dialogOK(View view) {

	}

	public void rejestracja() {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

	public void listaZakupow(View view) {
		Intent intent = new Intent(this, ListaZakupow.class);
		startActivity(intent);
	}

	public void twoiUzytkownicy(View view) {
		Intent intent = new Intent(this, TwoiUzytkownicy.class);
		startActivity(intent);
	}

	public void paragony(View view) {
		Intent intent = new Intent(this, Paragony.class);
		startActivity(intent);
	}

	public void zakoncz(View view) {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
