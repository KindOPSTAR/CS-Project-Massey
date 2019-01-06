package com.a12400.mish.mslfandom;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.a12400.mish.mslfandom.database.Gem;
import com.a12400.mish.mslfandom.database.GemDatabase;
import com.a12400.mish.mslfandom.database.Monster;
import com.a12400.mish.mslfandom.database.MonsterDatabase;
import com.a12400.mish.mslfandom.monsterCalc.monsterCalculation;
import com.a12400.mish.mslfandom.monsterCalc.specialSkill;

import java.util.List;

/**
 * the class of calculator fragment
 */
public class CalculatorFragment extends Fragment {

    //set what we need in this page
    private GridView mMonstersGrid;
    private imageMonsterAdapter mTileAdapter;
    private List<Monster> myMonsterCollection;
    private MonsterDatabase monsterDB;
    private int[] picIdMonster;
    private ImageView monster1;
    private ImageView monster2;
    private Monster selectedMonster1;
    private Monster selectedMonster2;

    private ImageView compareButton;
    private Animation myAnim;

    private ImageView[] gemImages =  new ImageView[6];
    private final int[] mInputGemId = {com.a12400.mish.mslfandom.R.id.monster1Gem1,
            com.a12400.mish.mslfandom.R.id.monster1Gem2,
            com.a12400.mish.mslfandom.R.id.monster1Gem3,
            com.a12400.mish.mslfandom.R.id.monster2Gem1,
            com.a12400.mish.mslfandom.R.id.monster2Gem2,
            com.a12400.mish.mslfandom.R.id.monster2Gem3,
    };
    private GridView mGemsGrid;

    private List<Gem> myGemCollection;
    private GemDatabase gemDB;
    private int[] picIdGem;
    private Gem[] selectedGems = new Gem[]{new Gem(),new Gem(),new Gem(),new Gem(),new Gem(),new Gem()};

    monsterCalculation monster1Status;
    monsterCalculation monster2Status;

    private TextView outPutM1Name;
    private TextView outPutM2Name;
    private TextView outPutM1MAX;
    private TextView outPutM2MAX;
    private TextView outPutM1EHP;
    private TextView outPutM2EHP;


    private TextView[] detailTexts =  new TextView[25];
    private final int[] detailTextId = {
            com.a12400.mish.mslfandom.R.id.editTextHp1,
            com.a12400.mish.mslfandom.R.id.editTextAtk1,
            com.a12400.mish.mslfandom.R.id.editTextDef1,
            com.a12400.mish.mslfandom.R.id.editTextRec1,
            com.a12400.mish.mslfandom.R.id.editTextCritDmg1,
            com.a12400.mish.mslfandom.R.id.editTextCritRate1,
            com.a12400.mish.mslfandom.R.id.editTextRes1,
            com.a12400.mish.mslfandom.R.id.editTextHp2,
            com.a12400.mish.mslfandom.R.id.editTextAtk2,
            com.a12400.mish.mslfandom.R.id.editTextDef2,
            com.a12400.mish.mslfandom.R.id.editTextRec2,
            com.a12400.mish.mslfandom.R.id.editTextCritDmg2,
            com.a12400.mish.mslfandom.R.id.editTextCritRate2,
            com.a12400.mish.mslfandom.R.id.editTextRes2,
            com.a12400.mish.mslfandom.R.id.editTextHp3,
            com.a12400.mish.mslfandom.R.id.editTextAtk3,
            com.a12400.mish.mslfandom.R.id.editTextDef3,
            com.a12400.mish.mslfandom.R.id.editTextRec3,
            com.a12400.mish.mslfandom.R.id.textHpFinal,
            com.a12400.mish.mslfandom.R.id.textAtkFinal,
            com.a12400.mish.mslfandom.R.id.textDefFinal,
            com.a12400.mish.mslfandom.R.id.textRecFinal,
            com.a12400.mish.mslfandom.R.id.textCritDmgFinal,
            com.a12400.mish.mslfandom.R.id.textCritRateFinal,
            com.a12400.mish.mslfandom.R.id.textResFinal,

    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(com.a12400.mish.mslfandom.R.layout.calculator_fragment,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        /**
         * this two function is for select monsters
         * once click the selection area (send a sign wishing to add a monster),
         * it will open a pop window with showing all monsters with head image and details from database.
         * as well as, really same things for gem
         */
        monster1.setOnClickListener(new MyMonsterOnClickListener(0));
        monster2.setOnClickListener(new MyMonsterOnClickListener(1));

        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedMonster1!=null &&selectedMonster2!=null) {
                    monster1Status = new monsterCalculation(selectedMonster1, selectedGems[0], selectedGems[1], selectedGems[2]);
                    monster2Status = new monsterCalculation(selectedMonster2, selectedGems[3], selectedGems[4], selectedGems[5]);
                    outPutM1MAX.setText(String.valueOf(monster1Status.calc_max_dmg(specialSkill.None)));
                    outPutM2MAX.setText(String.valueOf(monster2Status.calc_max_dmg(specialSkill.None)));
                    outPutM1EHP.setText(String.valueOf(monster1Status.calc_EHP(0)));
                    outPutM2EHP.setText(String.valueOf(monster2Status.calc_EHP(0)));
                    outPutM1Name.setText(selectedMonster1.mName);
                    outPutM2Name.setText(selectedMonster2.mName);

                    Toast toast2 = Toast.makeText(getContext(), "Long Click on Monster to see the details!",Toast.LENGTH_SHORT);
                    toast2.show();
                }
                else {
                    Toast toast2 = Toast.makeText(getContext(), "Please Fill all the blank!",Toast.LENGTH_SHORT);
                    toast2.show();
                }
            }
        });

        monster1.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if (monster1Status!=null) {

                    Log.i("message", "long click");
                    final View popupView = LayoutInflater.from(getActivity()).inflate(com.a12400.mish.mslfandom.R.layout.pop_up_monster_details, null);

                    for (int i = 0; i < detailTextId.length; i++) {
                        detailTexts[i] = popupView.findViewById(detailTextId[i]);
                    }
                    String[] states1 = {String.valueOf(selectedMonster1.hp),
                            String.valueOf(selectedMonster1.atk),
                            String.valueOf(selectedMonster1.def),
                            String.valueOf(selectedMonster1.rec),
                            String.valueOf(selectedMonster1.critDamage),
                            String.valueOf(selectedMonster1.critRate),
                            String.valueOf(selectedMonster1.resist),
                            monster1Status.get_hp_percentage(),
                            monster1Status.get_atk_percentage(),
                            monster1Status.get_def_percentage(),
                            monster1Status.get_rec_percentage(),
                            monster1Status.get_critdamage_percentage(),
                            monster1Status.get_critrate_percentage(),
                            monster1Status.get_resist_percentage(),
                            monster1Status.get_hp_flat(),
                            monster1Status.get_atk_flat(),
                            monster1Status.get_def_flat(),
                            monster1Status.get_rec_flat(),
                            String.valueOf(monster1Status.get_hp()),
                            String.valueOf(monster1Status.get_atk()),
                            String.valueOf(monster1Status.get_def()),
                            String.valueOf(monster1Status.get_rec()),
                            String.valueOf(monster1Status.get_crit_damage()),
                            String.valueOf(monster1Status.get_crit_rate()),
                            String.valueOf(monster1Status.get_resist())
                    };

                    for (int i = 0; i < detailTextId.length; i++) {
                        detailTexts[i].setText(states1[i]);
                        detailTexts[i].setTextSize(20);
                        detailTexts[i].setTextColor(Color.BLACK);
                        Log.i("message", "Set" + states1[i]);

                    }
                    final PopupWindow popupWindow = new PopupWindow(popupView,
                            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    popupView.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View popupView) {
                            popupWindow.dismiss();
                        }
                    });
                    popupWindow.setAnimationStyle(com.a12400.mish.mslfandom.R.style.Popup_Animation);
                    popupWindow.setFocusable(true);
                    // If you need the PopupWindow to dismiss when when touched outside
                    popupWindow.setBackgroundDrawable(new ColorDrawable());


                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                    if (Build.VERSION.SDK_INT >= 23)
                        popupWindow.setOverlapAnchor(true);
                }
                return true;
            }
        });

        monster2.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if (monster2Status!=null) {

                    Log.i("message", "long click");
                    final View popupView = LayoutInflater.from(getActivity()).inflate(com.a12400.mish.mslfandom.R.layout.pop_up_monster_details, null);

                    for (int i = 0; i < detailTextId.length; i++) {
                        detailTexts[i] = popupView.findViewById(detailTextId[i]);
                    }
                    String[] states2 = {String.valueOf(selectedMonster2.hp),
                            String.valueOf(selectedMonster2.atk),
                            String.valueOf(selectedMonster2.def),
                            String.valueOf(selectedMonster2.rec),
                            String.valueOf(selectedMonster2.critDamage),
                            String.valueOf(selectedMonster2.critRate),
                            String.valueOf(selectedMonster2.resist),
                            monster2Status.get_hp_percentage(),
                            monster2Status.get_atk_percentage(),
                            monster2Status.get_def_percentage(),
                            monster2Status.get_rec_percentage(),
                            monster2Status.get_critdamage_percentage(),
                            monster2Status.get_critrate_percentage(),
                            monster2Status.get_resist_percentage(),
                            monster2Status.get_hp_flat(),
                            monster2Status.get_atk_flat(),
                            monster2Status.get_def_flat(),
                            monster2Status.get_rec_flat(),
                            String.valueOf(monster2Status.get_hp()),
                            String.valueOf(monster2Status.get_atk()),
                            String.valueOf(monster2Status.get_def()),
                            String.valueOf(monster2Status.get_rec()),
                            String.valueOf(monster2Status.get_crit_damage()),
                            String.valueOf(monster2Status.get_crit_rate()),
                            String.valueOf(monster2Status.get_resist())
                    };

                    for (int i = 0; i < detailTextId.length; i++) {
                        detailTexts[i].setText(states2[i]);
                        detailTexts[i].setTextSize(20);
                        detailTexts[i].setTextColor(Color.BLACK);
                        Log.i("message", "Set" + states2[i]);

                    }
                    final PopupWindow popupWindow = new PopupWindow(popupView,
                            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    popupView.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View popupView) {
                            popupWindow.dismiss();
                        }
                    });
                    popupWindow.setFocusable(true);
                    popupWindow.setAnimationStyle(com.a12400.mish.mslfandom.R.style.Popup_Animation);
                    // If you need the PopupWindow to dismiss when when touched outside
                    popupWindow.setBackgroundDrawable(new ColorDrawable());

                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                    if (Build.VERSION.SDK_INT >= 23)
                        popupWindow.setOverlapAnchor(true);
                }
                return true;
            }
        });
    }


    private void init(View view){

        monster1 = view.findViewById(com.a12400.mish.mslfandom.R.id.imageMonster1);
        monster2 = view.findViewById(com.a12400.mish.mslfandom.R.id.imageMonster2);
        monsterDB = MonsterDatabase.getINSTANCE(getActivity());
        myMonsterCollection = monsterDB.monsterDao().getAll();
        picIdMonster = new int[myMonsterCollection.size()];
        mMonstersGrid = (GridView) view.findViewById(com.a12400.mish.mslfandom.R.id.gridViewHome);

        compareButton= view.findViewById(com.a12400.mish.mslfandom.R.id.compareButton);
        outPutM1Name= view.findViewById(com.a12400.mish.mslfandom.R.id.monsterName1);
        outPutM1EHP= view.findViewById(com.a12400.mish.mslfandom.R.id.outputNo1EHPLeader);
        outPutM1MAX= view.findViewById(com.a12400.mish.mslfandom.R.id.outputNo1MaxDmgLeader);
        outPutM2Name= view.findViewById(com.a12400.mish.mslfandom.R.id.monsterName2);
        outPutM2EHP= view.findViewById(com.a12400.mish.mslfandom.R.id.outputNo2EHPLeader);
        outPutM2MAX= view.findViewById(com.a12400.mish.mslfandom.R.id.outputNo2MaxDmgLeader);

        gemDB = GemDatabase.getINSTANCE(getActivity());
        mMonstersGrid = (GridView) view.findViewById(com.a12400.mish.mslfandom.R.id.gridViewHome);
        mGemsGrid = (GridView) view.findViewById(com.a12400.mish.mslfandom.R.id.gridViewHomeGems);
        myGemCollection = gemDB.gemDao().getAll();

        picIdGem = new int[myGemCollection.size()];
        for (int i = 0; i< mInputGemId.length; i++){
            gemImages[i]= view.findViewById(mInputGemId[i]);
            gemImages[i].setOnClickListener(new MyGemOnClickListener(i));
        }

        myAnim = AnimationUtils.loadAnimation(getContext(), com.a12400.mish.mslfandom.R.anim.button_bounce);

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
    }




    class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }


    public class imageMonsterAdapter extends BaseAdapter {
        private GridView mTiles;
        private int[] picId;

        public imageMonsterAdapter(GridView gv,int[] picId) {

            this.mTiles = gv;
            this.picId = picId;
        }


        @Override
        public int getCount() {
            return picId.length;
        }
        // not used
        @Override
        public Object getItem(int i) {
            return null;
        }
        // not used
        @Override
        public long getItemId(int i) {
            return i;
        }

        // populate a view
        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ImageView image;

            if (convertView == null) {
                // if it's not recycled, inflate it from xml
                convertView = getLayoutInflater().inflate(com.a12400.mish.mslfandom.R.layout.image_adapter, null);
              //  Log.i("message","on click my new adapter view");
                // convertview will be a LinearLayout
            }else
                // set size to be square
                convertView.setMinimumHeight(mTiles.getWidth() /  mTiles.getNumColumns());
            // get the imageview in this view
            image = (ImageView) convertView.findViewById(com.a12400.mish.mslfandom.R.id.singleImage);
           // textDisplay = convertView.findViewById(R.id.textViewDisplay);
            //textDisplay.setText(myMonsterCollection.get(i).mName);
            image.setImageResource(picId[i]);
            image.setTag(i);
          //  Log.i("message","new adapter view" + i);
            return convertView;
        }
    }

    public boolean checkIfTwoMonster(){
        if (selectedMonster1==null || selectedMonster2==null){return false;}
        return true;
    }

    public class MyMonsterOnClickListener implements View.OnClickListener {

        int index;

        public MyMonsterOnClickListener(int index)
        {
            this.index = index;
        }

        @Override
        public void onClick(View arg0) {
            {
                Log.i("message","on click");
                final View popupView = LayoutInflater.from(getActivity()).inflate(com.a12400.mish.mslfandom.R.layout.pop_up_gridview, null);

                mMonstersGrid = (GridView) popupView.findViewById(com.a12400.mish.mslfandom.R.id.gridViewPopup);
                //mTiles.setWillNotDraw(false);
                // mTiles.setFocusableInTouchMode(true);
                //  mTiles.setClickable(true);
                // and the adapter for tile data

                for (int i = 0; i < picIdMonster.length; i++) {
                    picIdMonster[i] = myMonsterCollection.get(i).mPicId;
                }
                mTileAdapter = new imageMonsterAdapter(mMonstersGrid,picIdMonster);
                mMonstersGrid.setAdapter(mTileAdapter);

                final PopupWindow popupWindow = new PopupWindow(popupView,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);
                popupWindow.setAnimationStyle(com.a12400.mish.mslfandom.R.style.Popup_Animation);

                // If you need the PopupWindow to dismiss when when touched outside
                popupWindow.setBackgroundDrawable(new ColorDrawable());

                popupWindow.showAtLocation(arg0,  Gravity.CENTER, 0, 0);

                if (Build.VERSION.SDK_INT >= 23)
                    popupWindow.setOverlapAnchor(true);


                mMonstersGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Object listItem = list.getItemAtPosition(position);
                        if (index==0) {
                            monster1.setImageResource(myMonsterCollection.get(position).mPicId);
                            selectedMonster1 = myMonsterCollection.get(position);
                            Log.i("message", selectedMonster1.mName + " " + selectedMonster1.hp);

                        }else if (index==1){
                            monster2.setImageResource(myMonsterCollection.get(position).mPicId);
                            selectedMonster2 = myMonsterCollection.get(position);
                            Log.i("message", selectedMonster2.mName + " " + selectedMonster2.hp);
                        }
                       if( checkIfTwoMonster()){
                            compareButton.startAnimation(myAnim);
                       }
                        popupWindow.dismiss();
                    }
                });
            }
        }


        }

    public class MyGemOnClickListener implements View.OnClickListener {

        int index;

        public MyGemOnClickListener(int index)
        {
            this.index = index;
        }

        @Override
        public void onClick(View arg0) {
            {
               // Log.i("message","on click");
                final View popupView = LayoutInflater.from(getActivity()).inflate(com.a12400.mish.mslfandom.R.layout.pop_up_gridview, null);

                mGemsGrid = (GridView) popupView.findViewById(com.a12400.mish.mslfandom.R.id.gridViewPopup);

                for (int i = 0; i < picIdGem.length; i++) {
                    picIdGem[i] = myGemCollection.get(i).mPicId;
                }
                mTileAdapter = new imageMonsterAdapter(mGemsGrid,picIdGem);
                mGemsGrid.setAdapter(mTileAdapter);

                final PopupWindow popupWindow = new PopupWindow(popupView,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setFocusable(true);

                int[] viewLocation=new int[2];
                arg0.getLocationInWindow(viewLocation);
             //   Log.i("message",viewLocation[0] + " " + viewLocation[1]);

                popupWindow.setAnimationStyle(com.a12400.mish.mslfandom.R.style.Popup_Animation);
                // If you need the PopupWindow to dismiss when when touched outside
                popupWindow.setBackgroundDrawable(new ColorDrawable());

                popupWindow.showAtLocation(arg0, Gravity.NO_GRAVITY, viewLocation[0],viewLocation[1]+200);

                // popupWindow.showAtLocation(arg0,  Gravity.CENTER, 0, 0);

                if (Build.VERSION.SDK_INT >= 23)
                    popupWindow.setOverlapAnchor(true);

                mGemsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Object listItem = list.getItemAtPosition(position);
                        gemImages[index].setImageResource(myGemCollection.get(position).mPicId);
                        selectedGems[index] = myGemCollection.get(position);
                       // Log.i("message", selectedGems[index].atk + " " + selectedGems[index].hp);
                        popupWindow.dismiss();
                    }
                });
            }
        }


    }

}
