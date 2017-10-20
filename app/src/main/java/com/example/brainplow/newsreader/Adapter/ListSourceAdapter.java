package com.example.brainplow.newsreader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brainplow.newsreader.Common.Common;
import com.example.brainplow.newsreader.Interface.IconBetterIdeaService;
import com.example.brainplow.newsreader.Interface.ItemClickListener;
import com.example.brainplow.newsreader.ListNews;
import com.example.brainplow.newsreader.Model.IconBetterIdea;
import com.example.brainplow.newsreader.Model.Source;
import com.example.brainplow.newsreader.Model.WebSite;
import com.example.brainplow.newsreader.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BrainPlow on 10/19/2017.
 */


class ListSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    ItemClickListener itemClickListener;

    TextView source_title;
    CircleImageView source_image;

    public ListSourceViewHolder(View itemView) {
        super(itemView);

        source_title =(TextView)itemView.findViewById(R.id.source_name);
        source_image = (CircleImageView)itemView.findViewById(R.id.source_image);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder>{


    private IconBetterIdeaService mservice;
    private Context context;
    private WebSite webSite;

    public ListSourceAdapter(Context context, WebSite webSite) {
        this.context = context;
        this.webSite = webSite;
        mservice = Common.getIconService();

    }


    @Override
    public ListSourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.source_layout, parent,false );
        return new ListSourceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListSourceViewHolder holder, int position) {


        StringBuilder iconBetterAPI = new StringBuilder("https://icons.better-idea.org/allicons.json?url=");
        iconBetterAPI.append(webSite.getSources().get(position).getUrl());

        mservice.getIconUrl(iconBetterAPI.toString()).enqueue(new Callback<IconBetterIdea>() {
            @Override
            public void onResponse(Call<IconBetterIdea> call, Response<IconBetterIdea> response) {


                try {


                if (response.body().getIcons().size() > 0 )
                {
                    String abc = response.body().getIcons().get(0).getUrl();

                    Picasso.with(context)
                            .load(response.body().getIcons().get(0).getUrl())
                            .into(holder.source_image);
                }
                }
                catch (Exception e)
                {
                   // Toast.makeText(context,  e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<IconBetterIdea> call, Throwable t) {

            }
        });

        holder.source_title.setText(webSite.getSources().get(position).getName());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

//                try {

                List<Source> sources= webSite.getSources();
              // String web = webSite.getSources().get(position).getSortbysavailable().get(position);
                String abc =webSite.getSources().get(position).getId();
                Intent intent = new Intent(context, ListNews.class);
                intent.putExtra("source", webSite.getSources().get(position).getId());
              //  intent.putExtra("sortBy", webSite.getSources().get(position).getSortbysavailable().get(0));
                context.startActivity(intent);
//            }
//                catch (Exception e)
//            {
//                 Toast.makeText(context,  e.getMessage(), Toast.LENGTH_LONG).show();
//
//            }


            }
        });
    }


    @Override
    public int getItemCount() {
        return webSite.getSources().size();
    }
}
