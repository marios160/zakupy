package pl.octicos.zakupy;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListaUzytkownikowAdapter extends ArrayAdapter<Uzytkownik> {

	Context context;
	int uzytkownikLayout;
	List<Uzytkownik> listaUzytkownikow;
	
	public ListaUzytkownikowAdapter(Context context, int uzytkownikLayout, List<Uzytkownik> listaUzytkownikow) {
		super(context, uzytkownikLayout, listaUzytkownikow);
		
		this.context = context;
		this.uzytkownikLayout = uzytkownikLayout;
		this.listaUzytkownikow = listaUzytkownikow;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.uzytkownicy, parent, false);
		}
		Uzytkownik uzytkownik = listaUzytkownikow.get(position);
		TextView email = (TextView) convertView.findViewById(R.id.uzytkownikText); 
		email.setText(uzytkownik.getEmail());
		if (uzytkownik.isPolaczony()) {
			email.setTextColor(Color.GREEN);
		} else {
			email.setTextColor(Color.BLACK);
		}

		return convertView;
	}

}
