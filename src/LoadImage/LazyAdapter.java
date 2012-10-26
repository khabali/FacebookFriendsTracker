package LoadImage;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.khabali.fft.GridFriends;
import com.khabali.fft.R;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    @Override
	public int getCount() {
        return data.size();
    }

    @Override
	public Object getItem(int position) {
        return position;
    }

    @Override
	public long getItemId(int position) {
        return position;
    }
    
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.items, null);
        ImageView image=(ImageView)vi.findViewById(R.id.picture); // thumb image
        TextView name = (TextView)vi.findViewById(R.id.name);
        
        HashMap<String, String> liste = new HashMap<String, String>();
        liste  = data.get(position);
        
         name.setText(liste.get(GridFriends.TAG_NAME));
         imageLoader.DisplayImage(liste.get(GridFriends.TAG_PICTURE), image);
        
        
        return vi;
    }
}