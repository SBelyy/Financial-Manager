package xyz.sbely.financemanager.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import xyz.sbely.financemanager.R;

public class CategoryLayout extends LinearLayout {

    private TextView text;
    private ImageView image;
    private Context context;
    private int idImage;

    public CategoryLayout(final Context context, String name, int idImage) {
        super(context);

        this.context = context;
        this.idImage = idImage;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View category = layoutInflater.inflate(R.layout.category_fragment, null);

        text = category.findViewById(R.id.textCategory);
        text.setText(name);

        image = category.findViewById(R.id.imageCategory);
        image.setImageResource(idImage);

        setPadding(20, 0, 20, 0);
        addView(category);
    }

    public String getName(){
        return text.getText().toString();
    }

    public int getIdImage(){ return idImage; }

    public void visibleOn(){
        image.setColorFilter(getResources().getColor(R.color.colorPrimary, context.getTheme()));
    }

    public void visibleOff(){
        image.setColorFilter(getResources().getColor(R.color.colorBackground, context.getTheme()));
    }

}
