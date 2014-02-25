package com.nexters.vobble.fragment;


public abstract class BaseMainFragment extends BaseFragment {

    public interface OnFragmentListener {
        public void onRemovedVobble();
    }

    abstract public void load();
}
