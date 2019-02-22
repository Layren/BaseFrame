package com.layren.basedebug;

import com.base.baseClass.BaseFragment;

/**
 * Created by GaoTing on 2019/2/21.
 * <p>
 * Explain:
 */
public class MainFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected int getStatusBarView() {
        return R.id.ima_fragment;
    }

    @Override
    protected void initView() {

    }
}
