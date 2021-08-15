package com.example.taobaounion.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.taobaounion.R;
import com.example.taobaounion.custom.TextFlowLayout;
import com.example.taobaounion.ui.fragment.HomeFragment;
import com.example.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.test_navigation)
    public RadioGroup navigation;
    @BindView(R.id.text_view)
    public TextFlowLayout mTextFlowLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        initListener();
        List<String> stringList = new ArrayList<>();
        stringList.add("电脑");
        stringList.add("运动滑板鞋");
        stringList.add("小米手机");
        stringList.add("华为平板");
        stringList.add("运动滑板鞋");
        stringList.add("男外衣秋冬");
        stringList.add("机械键盘青轴");
        stringList.add("可乐");
        stringList.add("电脑显示器27寸4k极致高清");
        stringList.add("运动滑板鞋");
        mTextFlowLayout.setTextList(stringList);
        mTextFlowLayout.setOnFlowTextItemClickListener(new TextFlowLayout.OnFlowTextItemClickListener() {
            @Override
            public void onFlowItemClick(String text) {
                LogUtils.d(this,"click text ---> " + text);
            }
        });
    }

    private void initListener() {
        navigation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.test_home:
                        break;
                    case R.id.test_select:
                        break;

                    case R.id.test_red_packet:
                        break;
                    case R.id.test_search:
                        break;
                }
            }
        });
    }


}