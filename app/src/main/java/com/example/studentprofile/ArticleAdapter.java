package com.example.studentprofile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<ArticleItem> listItems;
    private Context context;

    public ArticleAdapter(List<ArticleItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArticleItem listItem = listItems.get(position);

        holder.articleId.setText(listItem.getId());
        holder.title.setText(listItem.getTitle());
        holder.content.setText(listItem.getContent());
        holder.description.setText(listItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView articleId;
        public TextView title;
        public TextView content;
        public TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            articleId = (TextView) itemView.findViewById(R.id.article_id);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
