package com.udacity.thefedex87.takemyorder.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.thefedex87.takemyorder.R;
import com.udacity.thefedex87.takemyorder.model.GooglePlaceDetailModel.RestaurantReviewModel;
import com.udacity.thefedex87.takemyorder.utils.UserInterfaceUtils;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by federico.creti on 11/06/2018.
 */

public class RestaurantReviewsAdapter extends RecyclerView.Adapter<RestaurantReviewsAdapter.RestaurantReviewsViewHolder> {
    private List<RestaurantReviewModel> reviews;
    private Context context;

    public RestaurantReviewsAdapter(Context context, @NonNull List<RestaurantReviewModel> reviews){
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public RestaurantReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.review, parent, false);


        return new RestaurantReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantReviewsViewHolder holder, int position) {
        holder.reviewAuthor.setText(reviews.get(position).getAuthorName());
        holder.reviewText.setText("\"" + reviews.get(position).getReview() + "\"");
        UserInterfaceUtils.SetAverageDots(reviews.get(position).getRating(),
                holder.average1, holder.average2, holder.average3, holder.average4, holder.average5, context);

        //Setup review date
        Date reviewDate = new Date(reviews.get(position).getTime() * 1000);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dateWithoutTime = formatter.parse(formatter.format(reviewDate));
            holder.reviewDate.setText(new SimpleDateFormat("MM/dd/yyyy").format(dateWithoutTime));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.reviewDate.setText("--/--/----");
        }

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class RestaurantReviewsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.review_author)
        public TextView reviewAuthor;

        @BindView(R.id.review_date)
        TextView reviewDate;

        @BindView(R.id.review_text)
        public TextView reviewText;

        @BindView(R.id.average_1)
        public ImageView average1;

        @BindView(R.id.average_2)
        public ImageView average2;

        @BindView(R.id.average_3)
        public ImageView average3;

        @BindView(R.id.average_4)
        public ImageView average4;

        @BindView(R.id.average_5)
        public ImageView average5;

        public RestaurantReviewsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
