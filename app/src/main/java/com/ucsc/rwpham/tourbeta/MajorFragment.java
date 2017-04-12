package com.ucsc.rwpham.tourbeta;


import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.dd.CircularProgressButton;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import java.util.ArrayList;


public class MajorFragment extends SlideFragment {


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
        View root = inflater.inflate(R.layout.checkbox, container, false);

        setupListeners(root);

        return root;
    }

    public void setupListeners(View root) {

        final CircularProgressButton graduateButton = (CircularProgressButton) root.findViewById(R.id.graduateButton);
        graduateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (graduateButton.getProgress() == 0) {
                    simulateSuccessProgress(graduateButton);
                    majorsToDisplay.add("Graduate Division");
                } else {
                    graduateButton.setProgress(0);
                    majorsToDisplay.remove("Graduate Division");

                }
            }
        });

        final CircularProgressButton computerscienceButton = (CircularProgressButton) root.findViewById(R.id.computerscienceButton);
        computerscienceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (computerscienceButton.getProgress() == 0) {
                    simulateSuccessProgress(computerscienceButton);
                    majorsToDisplay.add("Computer Science");
                } else {
                    computerscienceButton.setProgress(0);
                    majorsToDisplay.remove("Computer Science");

                }
            }
        });


        final CircularProgressButton csgamedesignButton = (CircularProgressButton) root.findViewById(R.id.gamedesignButton);
        csgamedesignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (csgamedesignButton.getProgress() == 0) {
                    simulateSuccessProgress(csgamedesignButton);
                    majorsToDisplay.add("CS: Game Design");
                } else {
                    csgamedesignButton.setProgress(0);
                    majorsToDisplay.remove("CS: Game Design");
                }
            }
        });

        final CircularProgressButton computerengineeringButton = (CircularProgressButton) root.findViewById(R.id.computerengineeringButton);
        computerengineeringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (computerengineeringButton.getProgress() == 0) {
                    simulateSuccessProgress(computerengineeringButton);
                    majorsToDisplay.add("Computer Engineering");

                } else {
                    computerengineeringButton.setProgress(0);
                    majorsToDisplay.remove("Computer Engineering");

                }
            }
        });


        final CircularProgressButton electricalEngineeringButton = (CircularProgressButton) root.findViewById(R.id.electricalengineeringButton);
        electricalEngineeringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (electricalEngineeringButton.getProgress() == 0) {
                    simulateSuccessProgress(electricalEngineeringButton);
                    majorsToDisplay.add("Electrical Engineering");

                } else {
                    electricalEngineeringButton.setProgress(0);
                    majorsToDisplay.remove("Electrical Engineering");

                }
            }
        });

        final CircularProgressButton timButton = (CircularProgressButton) root.findViewById(R.id.timButton);
        timButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timButton.getProgress() == 0) {
                    simulateSuccessProgress(timButton);
                    majorsToDisplay.add("Technology Information Management");
                } else {
                    timButton.setProgress(0);
                    majorsToDisplay.remove("Technology Information Management");

                }
            }
        });


        final CircularProgressButton bioengineeringButton = (CircularProgressButton) root.findViewById(R.id.bioengineeringButton);
        bioengineeringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bioengineeringButton.getProgress() == 0) {
                    simulateSuccessProgress(bioengineeringButton);
                    majorsToDisplay.add("Bioengineering");
                } else {
                    bioengineeringButton.setProgress(0);
                    majorsToDisplay.remove("Bioengineering");
                }
            }
        });

    }

    private void simulateSuccessProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
            }
        });
        widthAnimation.start();
    }

    private void simulateErrorProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 99);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
                if (value == 99) {
                    button.setProgress(-1);
                }
            }
        });
        widthAnimation.start();
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

/*


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

 */