package com.a12400.mish.mslfandom;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.a12400.mish.mslfandom.database.Gem;
import com.a12400.mish.mslfandom.database.GemDatabase;
import com.a12400.mish.mslfandom.database.Monster;
import com.a12400.mish.mslfandom.database.MonsterDatabase;

import java.util.List;

/**
 * home page fragment
 */
public class HomePageFragment extends Fragment {
    /**
     * things will be used in this page
     */
    private GridView mMonstersGrid; //grid view
    private GridView mGemsGrid;
    private imageMonsterAdapter mTileAdapter; // adapter
    private imageMonsterAdapter mTileAdapterGem;
    private  List<Monster> myMonsterCollection; //list of monster
    private  List<Gem> myGemCollection; //list of gem
    private MonsterDatabase monsterDB;//monster database
    private GemDatabase gemDB;//gem database
    private Button deleteAll;//empty button
    private int[] picIdMonster;//monster image id
    private int[] picIdGem;//gem image id


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(com.a12400.mish.mslfandom.R.layout.home_page_fragment,null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monsterDB.monsterDao().deleteAll();
                gemDB.gemDao().deleteAll();
            Fragment frg = new HomePageFragment();
            if (frg!=null){
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(com.a12400.mish.mslfandom.R.id.main_screen, frg);
                ft.commit();
            }
            }
        });

    }

    /**
     *
     * @param view
     * assignment
     */
    private void init(View view){

        mMonstersGrid = (GridView) view.findViewById(com.a12400.mish.mslfandom.R.id.gridViewHome);
        mGemsGrid = (GridView) view.findViewById(com.a12400.mish.mslfandom.R.id.gridViewHomeGems);

        monsterDB = MonsterDatabase.getINSTANCE(getActivity());
        gemDB = GemDatabase.getINSTANCE(getActivity());

        myMonsterCollection = monsterDB.monsterDao().getAll();
        myGemCollection = gemDB.gemDao().getAll();
        Log.i("message"," Home" + myGemCollection.size());

        picIdMonster = new int[myMonsterCollection.size()];
        picIdGem = new int[myGemCollection.size()];

        deleteAll = view.findViewById(com.a12400.mish.mslfandom.R.id.deleteAll);

        for (int i = 0; i < picIdMonster.length; i++) {
            picIdMonster[i] = myMonsterCollection.get(i).mPicId;
        }
        mTileAdapter = new imageMonsterAdapter(mMonstersGrid,picIdMonster,myMonsterCollection,null);
        mMonstersGrid.setAdapter(mTileAdapter);

        for (int i = 0; i < picIdGem.length; i++) {
            picIdGem[i] = myGemCollection.get(i).mPicId;
        }
        mTileAdapterGem = new imageMonsterAdapter(mGemsGrid,picIdGem,null,myGemCollection);
        mGemsGrid.setAdapter(mTileAdapterGem);


    }

    public class imageMonsterAdapter extends BaseAdapter {
        private GridView mTiles;
        private int[] picId;
        private List<Monster> monsterDataSource;
        private List<Gem> gemDataSource;

        public imageMonsterAdapter(GridView gv, int[] picId, List<Monster> data , List<Gem>data2 ) {

            this.mTiles = gv;
            this.picId = picId;
            this.monsterDataSource = data;
            this.gemDataSource = data2;
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
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ImageView image;

            if (convertView == null) {
                // if it's not recycled, inflate it from xml
                convertView = getLayoutInflater().inflate(com.a12400.mish.mslfandom.R.layout.image_adapter, null);
               // Log.i("message","on click my new adapter view");
                // convertview will be a LinearLayout
            }else
                // set size to be square
                convertView.setMinimumHeight(mTiles.getWidth() /  mTiles.getNumColumns());
            // get the imageview in this view
            image = (ImageView) convertView.findViewById(com.a12400.mish.mslfandom.R.id.singleImage);
            image.setImageResource(picId[i]);
            image.setTag(i);
            if (monsterDataSource !=null) {
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final View popupView = LayoutInflater.from(getActivity()).inflate(com.a12400.mish.mslfandom.R.layout.pop_up_home_monster_details, null);

                        ImageView homeImage = popupView.findViewById(com.a12400.mish.mslfandom.R.id.homeHead);
                        TextView homeName = popupView.findViewById(com.a12400.mish.mslfandom.R.id.homeName);
                        TextView homeHp = popupView.findViewById(com.a12400.mish.mslfandom.R.id.homeHp1);
                        TextView homeAtk = popupView.findViewById(com.a12400.mish.mslfandom.R.id.homeAtk1);
                        TextView homeDef = popupView.findViewById(com.a12400.mish.mslfandom.R.id.homeDef1);
                        TextView homeRec = popupView.findViewById(com.a12400.mish.mslfandom.R.id.homeRec1);
                        TextView homeCritDamage = popupView.findViewById(com.a12400.mish.mslfandom.R.id.homeCritDmg1);
                        TextView homeCritRate = popupView.findViewById(com.a12400.mish.mslfandom.R.id.homeCritRate1);
                        TextView homeResist = popupView.findViewById(com.a12400.mish.mslfandom.R.id.homeRes1);

                        Monster current = monsterDataSource.get(i);

                        homeImage.setImageResource(current.mPicId);
                        homeName.setText(current.mName);
                        homeHp.setText(String.valueOf(current.hp));
                        homeAtk.setText(String.valueOf(current.atk));
                        homeDef.setText(String.valueOf(current.def));
                        homeRec.setText(String.valueOf(current.rec));
                        homeCritDamage.setText(String.valueOf(current.critDamage));
                        homeCritRate.setText(String.valueOf(current.critRate));
                        homeResist.setText(String.valueOf(current.resist));


                        final PopupWindow popupWindow = new PopupWindow(popupView,
                                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        popupWindow.setFocusable(true);

                        popupWindow.setAnimationStyle(com.a12400.mish.mslfandom.R.style.Popup_Animation);

                        // If you need the PopupWindow to dismiss when when touched outside
                        popupWindow.setBackgroundDrawable(new ColorDrawable());
                        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


                        if (Build.VERSION.SDK_INT >= 23)
                            popupWindow.setOverlapAnchor(true);


                    }
                });
            }else if (gemDataSource !=null) {
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final View popupView = LayoutInflater.from(getActivity()).inflate(com.a12400.mish.mslfandom.R.layout.pop_up_home_gem_details, null);
                        ImageView homeImage = popupView.findViewById(com.a12400.mish.mslfandom.R.id.homeGemHead);
                        TextView homeMain = popupView.findViewById(com.a12400.mish.mslfandom.R.id.gemMain);
                        TextView homeSubs = popupView.findViewById(com.a12400.mish.mslfandom.R.id.gemSub);

                        Gem current = gemDataSource.get(i);
                        homeImage.setImageResource(current.mPicId);
                      //  Log.i("message", current.main);
                     //   Log.i("message", current.subs);
                        homeMain.setText(current.main);
                        homeSubs.setText(current.subs);

                        final PopupWindow popupWindow = new PopupWindow(popupView,
                                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        popupWindow.setFocusable(true);
                        popupWindow.setAnimationStyle(com.a12400.mish.mslfandom.R.style.Popup_Animation);


                        // If you need the PopupWindow to dismiss when when touched outside
                        popupWindow.setBackgroundDrawable(new ColorDrawable());
                        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


                        if (Build.VERSION.SDK_INT >= 23)
                            popupWindow.setOverlapAnchor(true);

                    }
                });
            }
          //  Log.i("message","new adapter view" + i);
            return convertView;
        }
    }
}
