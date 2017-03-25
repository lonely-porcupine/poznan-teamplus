package cc.lupine.quickbid;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BidlistFragment extends Fragment {
    private final String TAG = AppConfig.TAG;



    private OnFragmentInteractionListener mListener;

    public BidlistFragment() {
        // Required empty public constructor
    }

    public static BidlistFragment newInstance() {
        BidlistFragment fragment = new BidlistFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate BLF");
        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView BLF");
        final View view = inflater.inflate(R.layout.fragment_bidlist, container, false);
        AuctionFetchHelper.fetchBidlist(new AuctionFetchHelper.OnListFetchInterface() {
            @Override
            public void onListFetched(ArrayList<AuctionModel> list) {
                LinearLayout container = (LinearLayout) view.findViewById(R.id.list_container);
                for(AuctionModel auction : list) {
                    final ListAuctionView lav = new ListAuctionView(getActivity());
                    lav.setPhotoURL(auction.getImageMedium());
                    lav.setTitle(auction.getName());
                    lav.setPrice(String.format("%.2f zł", auction.getBidPrice()));
                    if(auction.getWinning())
                        lav.setWinning(getString(R.string.winning));
                    else
                        lav.setWinning(getString(R.string.not_winning));
                    long unixTime = System.currentTimeMillis() / 1000L;
                    long endTime = unixTime + auction.getSecondsLeft();
                    java.util.Date date = new java.util.Date((long)endTime*1000);
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    lav.setTimeLeft(df.format(date));
                    lav.setModel(auction);
                    final RelativeLayout clickable_container = (RelativeLayout) lav.findViewById(R.id.clickable_container);

                    clickable_container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "Clicked");
                            if(!lav.getModel().isSubscribed()) {
                                Log.d(TAG, "Not subscribed");
                                lav.getModel().subscribe(new AuctionModel.OnSubscribeInterface() {
                                    @Override
                                    public void onSubscribed(int resp) {
                                        Log.i(TAG, "Successfully subscribed, dbid " + resp);
                                        lav.ungray();
                                    }

                                    @Override
                                    public void onError() {
                                        Log.e(TAG, "Could not subscribe");
                                    }
                                });
                            } else {
                                Log.d(TAG, "Subscribed");
                                lav.getModel().unsubscribe(new AuctionModel.OnUnsubscribeInterface() {
                                    @Override
                                    public void onUnsubscribed() {
                                        Log.i(TAG, "Successfully unsubscribed");
                                        lav.grayOut();
                                    }

                                    @Override
                                    public void onError() {
                                        Log.e(TAG, "Could not unsubscribe");
                                    }
                                });
                            }
                        }
                    });

                    if(!lav.getModel().isSubscribed())
                        lav.grayOut();
                    else
                        lav.ungray();

                    container.addView(lav);
                }
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach BLF");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach BLF");
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
