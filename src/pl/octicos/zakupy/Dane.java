package pl.octicos.zakupy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Environment;
import android.widget.Toast;


public class Dane implements Serializable {
	private static final long serialVersionUID = 1L;
	public List<Produkt> listaProduktow;
	public List<Uzytkownik> listaUzytkownikow;
	public File folder;
	public Uzytkownik uzytkownik;
	public Uzytkownik uzytkownikPolaczony;

	public Dane(String email, String pass) {

		this.listaProduktow = new ArrayList<Produkt>();
		this.listaUzytkownikow = new ArrayList<Uzytkownik>();
		File f = Environment.getExternalStorageDirectory();
		folder = new File(Environment.getExternalStorageDirectory() + File.separator + "Zakupy");
		if (!folder.exists()) {
			folder.mkdir();
		}
		uzytkownik = new Uzytkownik(email, pass, false);

	}

	public static void setClassFile(Dane c) {
		ObjectOutputStream pl = null;
		try {

			File f = Environment.getExternalStorageDirectory();
			pl = new ObjectOutputStream(new FileOutputStream(f.getAbsolutePath() + "/Zakupy/conf.cfg"));
			System.out.println(f.getAbsolutePath() + "/Zakupy/conf.cfg");
			pl.writeObject(c);
			pl.flush();
			pl.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static Dane getConfFile() {
		File f = Environment.getExternalStorageDirectory();
		File file2 = new File(f.getAbsolutePath() + "/Zakupy/conf.cfg");
		Dane c = null;

		if (file2.exists()) {
			ObjectInputStream pl2 = null;
			try {
				pl2 = new ObjectInputStream(new FileInputStream(f.getAbsolutePath() + "/Zakupy/conf.cfg"));
				c = (Dane) pl2.readObject();
				pl2.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}

		if (c != null) {
			for (Uzytkownik el : c.listaUzytkownikow) {
				el.setPolaczony(false);
			}
		}
		return c;
	}

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	public static boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	public static boolean isValidPassword(final String password) {

		if (password.length() < 3) {
			return false;
		}
		Pattern pattern;
		Matcher matcher;
		final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{4,}$";
		pattern = Pattern.compile(PASSWORD_PATTERN);
		matcher = pattern.matcher(password);

		return matcher.matches();

	}

}
