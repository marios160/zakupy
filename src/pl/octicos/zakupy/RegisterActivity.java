package pl.octicos.zakupy;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	SendPost _AsyncSendPost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		Button rejestracja = (Button) findViewById(R.id.zarejestruj);
		rejestracja.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditText email = (EditText) findViewById(R.id.email);
				EditText haslo = (EditText) findViewById(R.id.haslo);
				String emailStr = email.getText().toString();
				String hasloStr = haslo.getText().toString();
				if (!Dane.validate(emailStr)) {
					Toast.makeText(getBaseContext(), "Niepoprawny adres e-mail", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(!Dane.isValidPassword(hasloStr)){
					Toast.makeText(getBaseContext(), "Niepoprawne haslo", Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				String odpowiedz = "";
				_AsyncSendPost = new SendPost();
				try {
					odpowiedz = _AsyncSendPost
							.execute("http://www.zakupypollub.cba.pl/register.php", "email=" + emailStr+ "&haslo=" + hasloStr).get();
				} catch (InterruptedException | ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {

					if (odpowiedz.contains("Rejestracja nieudana")) {
						Toast.makeText(getBaseContext(), "Rejestracja nieudana", Toast.LENGTH_SHORT).show();
					} else if (odpowiedz.contains("Uzytkownik juz istnieje")) {
						Toast.makeText(getBaseContext(), "Bledne dane logowania",
								Toast.LENGTH_SHORT).show();
					} else if (odpowiedz.contains("Zarejestrowano") || odpowiedz.contains("Zalogowano")) {
						if(odpowiedz.contains("Zarejestrowano")){
						Toast.makeText(getBaseContext(), "Rejestracja udana!", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getBaseContext(), "Logowanie udane!", Toast.LENGTH_SHORT).show();
						}
						MainActivity.dane = new Dane(emailStr,hasloStr);
						Dane.setClassFile(MainActivity.dane);
						Intent intent = new Intent(getBaseContext(), MainActivity.class);
						startActivity(intent);
					} else {
						Toast.makeText(getBaseContext(), "Blad!" + odpowiedz, Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
	}

	class SendPost extends AsyncTask<String, Void, String> {

		// public String odpowiedz;
		protected String doInBackground(String... urls) {
			URL obj;
			String responseStr = "";
			try {

				obj = new URL(urls[0]);

				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setReadTimeout(5000);

				con.setRequestMethod("POST");
				con.setRequestProperty("User-Agent", "Zakupy");
				con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
				con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				String urlParameters = urls[1];
				// Send post request
				con.setDoOutput(true);

				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				// OutputStream wr = con.getOutputStream();
				wr.write(urlParameters.getBytes("UTF-8"));
				// wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				responseStr = response.toString();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return responseStr;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
