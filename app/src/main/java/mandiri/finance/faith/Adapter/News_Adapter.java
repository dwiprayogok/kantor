package mandiri.finance.faith.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mandiri.finance.faith.Model.News;
import mandiri.finance.faith.R;

public class News_Adapter extends RecyclerView.Adapter<News_Adapter.MyViewHolder> {
    private List<News> listberita;
    private ArrayList<News> beritabaru;
    private Activity activity;
    private ArrayList<News> items = null;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView header, content, footer;

        public MyViewHolder(View view) {
            super(view);
            header = (TextView) view.findViewById(R.id.header);
            content = (TextView) view.findViewById(R.id.contentberita);
            footer = (TextView) view.findViewById(R.id.footer);
        }
    }


//    public News_Adapter (Activity a, List<News> NewsList){
//        this.listberita = NewsList;
//        activity = a;
//        this.items = NewsList;
//    }

    public News_Adapter(Activity a, ArrayList<News> NewsList) {
        this.beritabaru = NewsList;
        activity = a;
        this.items = NewsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_berita, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //News task = beritabaru.get(position);

        holder.header.setText(beritabaru.get(position).getHeader());
        holder.content.setText(beritabaru.get(position).getContent());
        holder.footer.setText(beritabaru.get(position).getFooter());

        holder.content.setVisibility(View.GONE);


        Log.d("cek", "onBindViewHolder: "
                + "ini header" + "\n" + beritabaru.get(position).getHeader()
                + "ini contennya" + "\n" + beritabaru.get(position).getContent()
                + "ini footernyar " + "\n" + beritabaru.get(position).getFooter());

    }

    @Override
    public int getItemCount() {
        return beritabaru.size();
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    public ArrayList<News> getItems() {
        return items;
    }
}
