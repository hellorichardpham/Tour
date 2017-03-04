package com.example.julu.tourbeta;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.zdvdev.checkview.CheckView;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.example.julu.tourbeta.R.id.root;

public class MajorFragment extends SlideFragment {

    private EditText fakeUsername;

    private EditText fakePassword;

    private Button fakeLogin;

    private boolean loggedIn = false;

    private ArrayList<String> majorsToDisplay = new ArrayList<>();



    public MajorFragment() {
        // Required empty public constructor
    }

    public static MajorFragment newInstance() {
        return new MajorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        setupListeners(root);

        return root;
    }

    public void setupListeners(View root) {

        final CheckView checkView2 = (CheckView) root.findViewById(R.id.graduateDivision);
        checkView2.check();
        checkView2.setTag("Graduate Division");
        checkView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(checkView2.getTag());
                if(majorsToDisplay.contains(checkView2.getTag())) {
                    System.out.println(checkView2.getTag() + " is contained so I will remove it now");
                    majorsToDisplay.remove(checkView2.getTag());
                } else {
                    System.out.println(checkView2.getTag() + " is not here so i will add it now");
                    majorsToDisplay.add(checkView2.getTag().toString());
                }
            }
        });

        /*    private String[] majorArray = {"Graduate Division", "Computer Science", "CS: Game Design",
        "Computer Engineering", "Electrical Engineering",
                "Technology Information Management", "Bioengineering"};
    */
        final CheckView checkView3 = (CheckView) root.findViewById(R.id.computerScience);
        checkView3.check();
        checkView3.setTag("Computer Science");
        checkView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(checkView3.getTag());
                if(majorsToDisplay.contains(checkView3.getTag())) {
                    System.out.println(checkView3.getTag() + " is contained so I will remove it now");
                    majorsToDisplay.remove(checkView3.getTag());
                } else {
                    System.out.println(checkView3.getTag() + " is not here so i will add it now");
                    majorsToDisplay.add(checkView3.getTag().toString());
                }
            }
        });

        final CheckView checkView4 = (CheckView) root.findViewById(R.id.computerScienceGameDesign);
        checkView4.check();
        checkView4.setTag("CS: Game Design");
        checkView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(checkView4.getTag());
                if(majorsToDisplay.contains(checkView4.getTag())) {
                    System.out.println(checkView4.getTag() + " is contained so I will remove it now");
                    majorsToDisplay.remove(checkView4.getTag());
                } else {
                    System.out.println(checkView4.getTag() + " is not here so i will add it now");
                    majorsToDisplay.add(checkView4.getTag().toString());
                }
            }
        });

        final CheckView checkView5 = (CheckView) root.findViewById(R.id.computerEngineering);
        checkView5.check();
        checkView5.setTag("Computer Engineering");
        checkView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(checkView5.getTag());
                if(majorsToDisplay.contains(checkView5.getTag())) {
                    System.out.println(checkView5.getTag() + " is contained so I will remove it now");
                    majorsToDisplay.remove(checkView5.getTag());
                } else {
                    System.out.println(checkView5.getTag() + " is not here so i will add it now");
                    majorsToDisplay.add(checkView5.getTag().toString());
                }
            }
        });

        final CheckView checkView6 = (CheckView) root.findViewById(R.id.electricalEngineering);
        checkView6.check();
        checkView6.setTag("Electrical Engineering");
        checkView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(checkView6.getTag());
                if(majorsToDisplay.contains(checkView6.getTag())) {
                    System.out.println(checkView6.getTag() + " is contained so I will remove it now");
                    majorsToDisplay.remove(checkView6.getTag());
                } else {
                    System.out.println(checkView6.getTag() + " is not here so i will add it now");
                    majorsToDisplay.add(checkView6.getTag().toString());
                }
            }
        });

        final CheckView checkView7 = (CheckView) root.findViewById(R.id.TechnologyInformationManagement);
        checkView7.check();
        checkView7.setTag("Technology Information Management");
        checkView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(checkView7.getTag());
                if(majorsToDisplay.contains(checkView7.getTag())) {
                    System.out.println(checkView7.getTag() + " is contained so I will remove it now");
                    majorsToDisplay.remove(checkView7.getTag());
                } else {
                    System.out.println(checkView7.getTag() + " is not here so i will add it now");
                    majorsToDisplay.add(checkView7.getTag().toString());
                }
            }
        });

        final CheckView checkView8 = (CheckView) root.findViewById(R.id.Bioengineering);
        checkView8.check();
        checkView8.setTag("Bioengineering");
        checkView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(checkView8.getTag());
                if(majorsToDisplay.contains(checkView8.getTag())) {
                    System.out.println(checkView8.getTag() + " is contained so I will remove it now");
                    majorsToDisplay.remove(checkView8.getTag());
                } else {
                    System.out.println(checkView8.getTag() + " is not here so i will add it now");
                    majorsToDisplay.add(checkView8.getTag().toString());
                }
            }
        });

    }

    @Override
    public boolean canGoForward() {
        return (majorsToDisplay.size() != 0);
    }


    @Override
    public void onDestroy() {
        Intent intent = new Intent(this.getContext(), MapsActivity.class);
        intent.putStringArrayListExtra("majorsToDisplay", majorsToDisplay);
        startActivity(intent);

        super.onDestroy();

    }

}
