package john.smith.navigationdrawer3;

import static john.smith.navigationdrawer3.Constants.KEY_BOOLEAN;
import static john.smith.navigationdrawer3.Constants.KEY_LONG;
import static john.smith.navigationdrawer3.Constants.KEY_STRING;
import static john.smith.navigationdrawer3.Constants.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        TextView tvString = view.findViewById(R.id.tvString);
        TextView tvLong = view.findViewById(R.id.tvLong);
        TextView tvBoolean = view.findViewById(R.id.tvBoolean);

        Button button = view.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                if (prefs.contains(KEY_STRING)) {
                    String stringValue = prefs.getString(KEY_STRING, "EMPTY");
                    tvString.setText(stringValue);
                }

                if (prefs.contains((KEY_LONG))) {
                    Long longValue = prefs.getLong(KEY_LONG, -1000);
                    tvLong.setText(String.valueOf(longValue));
                }

                if (prefs.contains(KEY_BOOLEAN)) {
                    Boolean booleanValue = prefs.getBoolean(KEY_BOOLEAN, false);
                    if (booleanValue) {
                        tvBoolean.setText("Checked");
                    } else {
                        tvBoolean.setText("Unchecked");
                    }
                }
            }
        });
        return view;
    }
}