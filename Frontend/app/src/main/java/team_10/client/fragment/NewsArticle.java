package team_10.client.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import team_10.client.R;
import team_10.client.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsArticle.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsArticle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsArticle extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "url";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    WebView webView;
    private String urlName;

    private OnFragmentInteractionListener mListener;

    public NewsArticle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment NewsArticle.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsArticle newInstance(String param1) {
        NewsArticle fragment = new NewsArticle();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_article , container ,false);
        //MainActivity.hideBottomNavigationView();
        //view.findViewById(R.id.navigation).setVisibility(View.GONE);
        webView = (WebView) view.findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(mParam1);
        urlName = mParam1;
        webView.setWebViewClient(new MyWebviewClient());
        webView.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == MotionEvent.ACTION_UP) && webView.canGoBack())
                {
                    webView.goBack();
                    return true;
                }
                if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == MotionEvent.ACTION_UP) && !webView.canGoBack() && getFragmentManager().getBackStackEntryCount() > 0)
                {
                    //MainActivity.showBottomNavigationView();
                    getFragmentManager().popBackStack();
                }
                return false;
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        MainActivity.hideBottomNavigationView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        MainActivity.showBottomNavigationView();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class MyWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (Uri.parse(url).getHost().equals(Uri.parse(urlName).getHost()))
            {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }
}
