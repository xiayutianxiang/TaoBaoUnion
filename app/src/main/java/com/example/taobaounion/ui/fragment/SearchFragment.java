package com.example.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.custom.TextFlowLayout;
import com.example.taobaounion.model.domain.Histories;
import com.example.taobaounion.model.domain.IBaseInfo;
import com.example.taobaounion.model.domain.SearchRecommend;
import com.example.taobaounion.model.domain.SearchResult;
import com.example.taobaounion.presenter.ISearchPresenter;
import com.example.taobaounion.ui.activity.MainActivity;
import com.example.taobaounion.ui.activity.TicketActivity;
import com.example.taobaounion.ui.adapter.LinearItemContentAdapter;
import com.example.taobaounion.utils.KeyBoardUtils;
import com.example.taobaounion.utils.LogUtils;
import com.example.taobaounion.utils.PresenterManager;
import com.example.taobaounion.utils.SizeUtils;
import com.example.taobaounion.utils.TicketUtils;
import com.example.taobaounion.view.ISearchPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchFragment extends BaseFragment implements ISearchPageCallback, TextFlowLayout.OnFlowTextItemClickListener {

    private ISearchPresenter mSearchPresenter = null;
    @BindView(R.id.recommend_view)
    public TextFlowLayout mRecommendSearchWords;
    @BindView(R.id.history_view)
    public TextFlowLayout mHistorySearchWords;
    @BindView(R.id.history_container)
    public View mHistoryContainer;
    @BindView(R.id.recommend_container)
    public View mRecommendContainer;
    @BindView(R.id.search_history_delete)
    public ImageView mHistoryDelete;
    @BindView(R.id.search_result_list)
    public RecyclerView mSearchResultRv;
    @BindView(R.id.search_result_container)
    public TwinklingRefreshLayout mTwinklingRefreshLayout;

    @BindView(R.id.search_btn)
    public TextView mSearchBtn;
    @BindView(R.id.search_clear_btn)
    public ImageView mSearchClearBtn;
    @BindView(R.id.search_input_box)
    public EditText mSearchInputBox;
    private LinearItemContentAdapter mResultAdapter;

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        //获取推荐词
        mSearchPresenter.getRecommendWords();
        //mSearchPresenter.doSearch("毛衣");
        mSearchPresenter.getHistories();
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        mSearchResultRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mResultAdapter = new LinearItemContentAdapter();
        mSearchResultRv.setAdapter(mResultAdapter);

        //设置刷新控件
        mTwinklingRefreshLayout.setEnableLoadmore(true);
        mTwinklingRefreshLayout.setEnableRefresh(false);
        mTwinklingRefreshLayout.setEnableOverScroll(true);

        mSearchResultRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 2);
                outRect.bottom = SizeUtils.dip2px(getContext(), 2);
                outRect.left = SizeUtils.dip2px(getContext(), 2);
                outRect.right = SizeUtils.dip2px(getContext(), 2);
            }
        });
    }

    @Override
    protected void initListener() {
        mHistoryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除历史
                mSearchPresenter.delHistories();
            }
        });

        //加载更多内容
        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mSearchPresenter != null) {
                    mSearchPresenter.loaderMore();
                }
            }
        });

        //跳转到详情领券页面
        mResultAdapter.setOnlistItemClickListener(new LinearItemContentAdapter.OnlistItemClickListener() {
            @Override
            public void onItemClick(IBaseInfo item) {
                TicketUtils.toTicketPage(getContext(), item);
            }
        });

        mSearchInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //LogUtils.d(SearchFragment.this,"actionId ---> " + actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchPresenter != null) {
                    String keyword = v.getText().toString().trim();
                    if (TextUtils.isEmpty(keyword)) {
                        return false;
                    }
                    //判断拿到的内容是否为空
                    //LogUtils.d(this,"input text --- > " + v.getText().toString());
                    //发起搜索
                    toSearch(keyword);
                    //mSearchPresenter.doSearch(keyword);
                }
                return false;
            }
        });

        //监听输入框的内容变化
        mSearchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //变化的时候通知
                LogUtils.d(SearchFragment.this, "input text ----> " + s.toString().trim());
                //如果长度不为0，显示删除按钮

                mSearchClearBtn.setVisibility(hasInput(true) ? View.VISIBLE : View.GONE);
                mSearchBtn.setText(hasInput(false) ? "搜索" : "取消");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //清除输入框的内容
        mSearchClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchInputBox.setText("");
                //回到历史记录界面
                switch2HistoryPage();
            }
        });

        //发起搜索
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果有内容就搜索
                if (hasInput(true)) {
                    //发起搜索
                    if (mSearchPresenter != null) {
                        toSearch(mSearchInputBox.getText().toString().trim());
                        //mSearchPresenter.doSearch(mSearchInputBox.getText().toString().trim());
                        KeyBoardUtils.hide(getContext(), v);
                    }
                } else {
                    //隐藏键盘
                    KeyBoardUtils.hide(getContext(), v);
                }
                //没有内容就取消
            }
        });

        mHistorySearchWords.setOnFlowTextItemClickListener(this);
        mRecommendSearchWords.setOnFlowTextItemClickListener(this);
    }

    /**
     * 切换到历史和推荐界面
     */
    private void switch2HistoryPage() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
        if (mRecommendSearchWords.getContentSize() != 0) {
            mRecommendContainer.setVisibility(View.VISIBLE);
        } else {
            mRecommendContainer.setVisibility(View.GONE);
        }
        //内容隐藏
        mTwinklingRefreshLayout.setVisibility(View.GONE);
    }

    private boolean hasInput(boolean containSpace) {
        if (containSpace) {
            return mSearchInputBox.toString().length() > 0;
        } else {
            return mSearchInputBox.toString().trim().length() > 0;
        }
    }

    @Override
    public void onHistoriesLoaded(Histories histories) {
        LogUtils.d(this, "histories --- >" + histories);
        if (histories == null || histories.getHistories().size() == 0) {
            mHistoryContainer.setVisibility(View.GONE);
        } else {
            mHistoryContainer.setVisibility(View.VISIBLE);
            mHistorySearchWords.setTextList(histories.getHistories());
        }
    }

    @Override
    public void onHistoriesDeleted() {
        //更新历史记录
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        LogUtils.d(SearchFragment.this, "result ----> " + result);
        //隐藏推荐和历史记录
        mHistoryContainer.setVisibility(View.GONE);
        mRecommendContainer.setVisibility(View.GONE);
        //显示搜索结果
        mTwinklingRefreshLayout.setVisibility(View.VISIBLE);
        try {
            mResultAdapter.setData(result.getData()
                    .getTbk_dg_material_optional_response().
                            getResult_list().getMap_data());
        } catch (Exception e) {
            e.printStackTrace();
            setUpState(State.EMPTY);
        }
        //在这里会造成每次搜索后，item的间距会变宽，因为add了多次
/*        mSearchResultRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 2);
                outRect.bottom = SizeUtils.dip2px(getContext(), 2);
                outRect.left = SizeUtils.dip2px(getContext(), 2);
                outRect.right = SizeUtils.dip2px(getContext(), 2);
            }
        });*/
    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        mTwinklingRefreshLayout.finishLoadmore();
        List<SearchResult.DataDTO.TbkDgMaterialOptionalResponseDTO.ResultListDTO.MapDataDTO> moreData = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mResultAdapter.addData(moreData);
    }

    @Override
    public void onMoreLoadedError() {
        mTwinklingRefreshLayout.finishLoadmore();
    }

    @Override
    public void onMoreLoadedEmpty() {
        mTwinklingRefreshLayout.finishLoadmore();
    }

    @Override
    protected void onRetryClick() {
        if (mSearchPresenter != null) {
            mSearchPresenter.reSearch();
        }
    }

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataDTO> recommendWords) {
        LogUtils.d(SearchFragment.this, "recommendWords size ----> " + recommendWords.size());
        List<String> recommendKeywords = new ArrayList<>();
        for (SearchRecommend.DataDTO item : recommendWords) {
            recommendKeywords.add(item.getKeyword());
        }
        if (recommendWords == null || recommendWords.size() == 0) {
            mRecommendSearchWords.setVisibility(View.GONE);
        } else {
            mRecommendSearchWords.setTextList(recommendKeywords);
            mRecommendContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void release() {
        super.release();
        if (mSearchPresenter != null) {
            mSearchPresenter.unregisterViewCallback(this);
        }
    }

    /**
     * 搜索历史和推荐的item点击
     *
     * @param text
     */
    @Override
    public void onFlowItemClick(String text) {
        toSearch(text);
    }

    private void toSearch(String text) {
        if (mSearchPresenter != null) {
            mSearchResultRv.scrollToPosition(0);
            mSearchInputBox.setText(text);
            mSearchPresenter.doSearch(text);
        }
    }
}
