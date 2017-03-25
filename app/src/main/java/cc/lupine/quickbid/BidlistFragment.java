package cc.lupine.quickbid;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        View view = inflater.inflate(R.layout.fragment_bidlist, container, false);
        AuctionFetchHelper.fetchBidlist(new AuctionFetchHelper.OnListFetchInterface() {
            @Override
            public void onListFetched(ArrayList<AuctionModel> list) {
                ArrayList<AuctionModel> auctions = list;
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
