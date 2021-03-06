package com.buct.museumguide.ui.FragmentForMain.Search;

import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.buct.museumguide.R;
import com.buct.museumguide.bean.Exhibition;
import com.buct.museumguide.ui.FragmentForMain.Comment.CommentAdapter;
import com.buct.museumguide.ui.FragmentForMain.Comment.PerComment;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.List;

public class ExhibitionAdapter extends RecyclerView.Adapter<ExhibitionAdapter.ViewHolder> {

    private List<Exhibition> mExhibitionList;
    private ExhibitionAdapter.OnItemClickListener onItemClickListener;
    private View mHeaderView;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView content;
        TextView start_time;
        TextView end_time;
        TextView time;
        TextView tag;
        ImageView image;
        CardView cardView;

        public  ViewHolder (View view){
            super(view);
            name=view.findViewById(R.id.search_exhibition_name);
            content=view.findViewById(R.id.search_exhibition_content);
            start_time=view.findViewById(R.id.search_exhibition_start_time);
            end_time=view.findViewById(R.id.search_exhibition_end_time);
            time=view.findViewById(R.id.search_exhibition_time);
            tag=view.findViewById(R.id.search_exhibition_tag);
            image=view.findViewById(R.id.search_exhibition_iamge);
            cardView=view.findViewById(R.id.cardViewExhibitions);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    // ② 定义一个设置点击监听器的方法
    public void setOnItemClickListener(ExhibitionAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ExhibitionAdapter(List<Exhibition> exhibitionList){
        mExhibitionList=exhibitionList;
    }

    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.search_exhibition_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exhibition exhibition=mExhibitionList.get(position);

        holder.cardView.setOnClickListener(v -> {
            if(onItemClickListener != null) {
                int pos1 = getRealPosition(holder);
                onItemClickListener.onItemClick(holder.cardView, pos1);
            }
        });

        holder.name.setText(exhibition.getName());
        TextPaint tp=holder.name.getPaint();tp.setFakeBoldText(true);
        if(exhibition.getStart_time().equals("null")) holder.start_time.setText("结束时间:  暂无");
        else holder.start_time.setText("结束时间:  "+exhibition.getStart_time());
        if(exhibition.getEnd_time().equals("null")) holder.end_time.setText("结束时间:  暂无");
        else holder.end_time.setText("结束时间:  "+exhibition.getEnd_time());
//        holder.time.setText("时间： "+exhibition.getTime());
//        holder.tag.setText("标签： "+exhibition.getTag());
//        holder.content.setText("内容： "+exhibition.getContent());
//        Uri uri = Uri.fromFile(new File(exhibition.getImgUrl()));
//        holder.image.setImageURI(uri);
//        Glide.with(holder.itemView)
//                .load(exhibition.getImgUrl())
//                .into(holder.image);
        String imageUrl="";

        try{
            JSONArray imageArray=exhibition.getImage_list();
            Log.d("geturl",imageArray.toString());
            String imageShow;
            imageShow=imageArray.getString(0);

            Log.d("geturl","imageShow");
            imageUrl="http://192.144.239.176:8080/"+imageShow.toString();
        }
        catch (JSONException e){
            e.printStackTrace();
            Log.d("geturl",e.toString());
        }
        Log.d("geturl",imageUrl);
        if(!imageUrl.equals("")){
            Glide.with(holder.itemView)
                    .load(imageUrl)
                    .into(holder.image);
        }

    }

    @Override
    public int getItemCount() {

        return mExhibitionList.size();

    }
}


