package cc.lupine.quickbid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ListAuctionView extends CardView {
    private LayoutInflater mInflater;

    public ImageView photoView;
    public TextView titleView;
    public TextView priceView;
    public TextView timeLeftView;
    public TextView winningView;

    private AuctionModel model;

    public ListAuctionView(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public ListAuctionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public ListAuctionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public void init() {
        View v = mInflater.inflate(R.layout.list_auction_view, this, true);
        this.photoView = (ImageView) v.findViewById(R.id.img_photo);
        this.titleView = (TextView) v.findViewById(R.id.lbl_name);
        this.priceView = (TextView) v.findViewById(R.id.lbl_price);
        this.timeLeftView = (TextView) v.findViewById(R.id.lbl_timeleft);
        this.winningView = (TextView) v.findViewById(R.id.lbl_winning);
    }

    public void setPhotoURL(String url) {
        Picasso.with(getContext()).load(url).into(photoView);
    }

    public void setTitle(String title) {
        this.titleView.setText(title);
        this.titleView.setSelected(true);
    }

    public void setPrice(String price) {
        this.priceView.setText(price);
    }

    public void setTimeLeft(String time) {
        this.timeLeftView.setText(time);
    }

    public void setWinning(String win) {
        this.winningView.setText(win);
    }

    public AuctionModel getModel() {
        return model;
    }

    public void setModel(AuctionModel model) {
        this.model = model;
    }

    public void grayOut() {
        this.titleView.setTextColor(getResources().getColor(android.support.v7.appcompat.R.color.primary_text_disabled_material_light));
        this.priceView.setTextColor(getResources().getColor(android.support.v7.appcompat.R.color.primary_text_disabled_material_light));
        this.timeLeftView.setTextColor(getResources().getColor(android.support.v7.appcompat.R.color.primary_text_disabled_material_light));
        this.winningView.setTextColor(getResources().getColor(android.support.v7.appcompat.R.color.primary_text_disabled_material_light));
    }

    public void ungray() {
        this.titleView.setTextColor(getResources().getColor(android.support.v7.appcompat.R.color.primary_text_default_material_light));
        this.priceView.setTextColor(getResources().getColor(android.support.v7.appcompat.R.color.primary_text_default_material_light));
        this.timeLeftView.setTextColor(getResources().getColor(android.support.v7.appcompat.R.color.primary_text_default_material_light));
        this.winningView.setTextColor(getResources().getColor(android.support.v7.appcompat.R.color.primary_text_default_material_light));
    }
}
