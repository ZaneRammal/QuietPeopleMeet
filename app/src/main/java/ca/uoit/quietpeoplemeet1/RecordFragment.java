package ca.uoit.quietpeoplemeet1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 *  Record Fragment will contain functionality related to recording audio.
 */
public class RecordFragment extends Fragment {

    SoundReader soundReader;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecordFragment newInstance(String param1, String param2) {
        RecordFragment fragment = new RecordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Instantiating the SoundReader here so it can be started and stopped from anywhere
        * in the fragment
        * */

        soundReader = new SoundReader();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Start Recording Button
    public void onButtonClick1(View v) {

        try {
            soundReader.start();
            Toast.makeText(getActivity(), "Recording Started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    // Stop Recording Button
    public void onButtonClick2(View v) {

        try {
            soundReader.stop();
            Toast.makeText(getActivity(), "Recording Stopped", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* Show Results Button
    *
    *  Displays amplitude
    * */
    public void onButtonClick3(View v) {

        String ampMessage = Double.toString(soundReader.getAmplitude());

        Toast.makeText(getActivity(), ampMessage, Toast.LENGTH_SHORT).show();

        /* Manipulating views in a fragment requires you to call
        * "getView().findViewById()" instead of calling findViewByID
        * directly like with activities
        *
        *  This cannot be used in OnCreate or OnCreateView
        * */
        TextView tv = (TextView)getView().findViewById(R.id.textAmp);
        tv.setText(ampMessage);


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onRecordPressed(View view) {

        Toast.makeText(getActivity(), "Test", Toast.LENGTH_LONG).show();


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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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


}
