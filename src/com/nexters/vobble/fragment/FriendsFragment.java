package com.nexters.vobble.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nexters.vobble.R;

public class FriendsFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_friends, null);

        TextView tvNotice = (TextView) view.findViewById(R.id.tv_notice);
        String notice = "페이스북과 연동된<br><b>내 친구들의 보블 듣기 서비스!</b><br>곧 찾아옵니다.";
        tvNotice.setText(Html.fromHtml(notice), TextView.BufferType.SPANNABLE);
        return view;
    }
}
