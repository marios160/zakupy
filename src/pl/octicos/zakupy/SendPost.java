package pl.octicos.zakupy;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.widget.Toast;




class SendPost extends AsyncTask<String, Void, String> {

	public static SendPost _AsyncSendPost;
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
