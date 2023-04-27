package com.kalmac.filibrary;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;



import java.util.ArrayList;

public class CustomAdapter  extends ArrayAdapter<Film>{

   // public static String IMAGE_URL = "https://image.tmdb.org/t/p/w500";


    public CustomAdapter(Context context, ArrayList<Film> films){
        super(context, 0, films);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){

        Film film = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_film, parent, false);
        }

        ImageButton filmPoster = (ImageButton) convertView.findViewById(R.id.filmPoster);
        TextView filmT = (TextView) convertView.findViewById(R.id.filmT);
        TextView filmReleaseDate = (TextView) convertView.findViewById(R.id.filmReleaseDate);

//        filmT.setText(film.getTitle());
//        filmReleaseDate.setText(film.getFilmReleaseDate());

        return convertView;
    }
}
