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


public class ListaZakupowAdapter extends ArrayAdapter<Produkt> {

	Context context;
	int produktLayout;
	List<Produkt> listaZakupow;

	public ListaZakupowAdapter(Context context, int produktLayout, List<Produkt> listaZakupow) {

		super(context, produktLayout, listaZakupow);
		this.context = context;
		this.produktLayout = produktLayout;
		this.listaZakupow = listaZakupow;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.produkt, parent, false);
		}
		Produkt produkt = listaZakupow.get(position);
		TextView nazwa = (TextView) convertView.findViewById(R.id.produktText); 
		TextView ilosc = (TextView) convertView.findViewById(R.id.iloscText);
		nazwa.setText(produkt.getNazwa());
		ilosc.setText(produkt.getIlosc());
		if (produkt.isKupiony()) {
			nazwa.setTextColor(Color.GRAY);
			ilosc.setTextColor(Color.GRAY);

		} else {
			nazwa.setTextColor(Color.BLACK);
			ilosc.setTextColor(Color.BLACK);

		}

		return convertView;
	}

}
