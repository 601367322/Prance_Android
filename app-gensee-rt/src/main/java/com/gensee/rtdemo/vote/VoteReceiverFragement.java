package com.gensee.rtdemo.vote;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gensee.room.RtSdk;
import com.gensee.demo.R;
import com.gensee.rtdemo.MutiVideoActivity;
import com.gensee.rtdemo.adapter.AbstractAdapter;
import com.gensee.vote.VoteGroup;

/*
 * 接收投票显示，可重定向到提交页跟查询页
 */
@SuppressLint("ValidFragment")
public class VoteReceiverFragement extends Fragment implements OnClickListener {
	private ListView voteLv;
	private VoteAdapter voteAdapter;
	private RtSdk rtSdk;
	private Button btnClose;

	public void notifyData(List<VoteGroup> voteList) {
		if (null != voteAdapter) {
			voteAdapter.notifyData(voteList);
		}
	}

	public VoteReceiverFragement(RtSdk rtSdk) {
		this.rtSdk = rtSdk;
		voteAdapter = new VoteAdapter(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.vote_receiver_layout, null);
		btnClose = (Button) view.findViewById(R.id.vote_receiver_close_btn);
		btnClose.setOnClickListener(this);
		voteLv = (ListView) view.findViewById(R.id.vote_list_lv);
		voteLv.setAdapter(voteAdapter);
		voteLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				VoteGroup voteGroup = (VoteGroup) voteAdapter.getItem(position);
				FragmentTransaction fragmentTransaction = getFragmentManager()
						.beginTransaction();
				if (voteGroup.isM_bPublishResult() || voteGroup.isM_bDeadline()
						|| voteGroup.isVoteSubmmit()) {
					fragmentTransaction.replace(R.id.vote_holder_container,
							new VoteQueryFragement(voteGroup));
				} else {
					fragmentTransaction.replace(R.id.vote_holder_container,
							new VoteCommitFragement(rtSdk, voteGroup));
				}
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		});
		return view;
	}

	@Override
	public void onResume() {
		voteAdapter.notifyDataSetChanged();
		super.onResume();
	}

	private class VoteAdapter extends AbstractAdapter<VoteGroup> {

		public VoteAdapter(Context context) {
			super(context);
		}

		@Override
		protected View createView() {
			return LayoutInflater.from(getActivity()).inflate(
					R.layout.vote_list_receive_item_layout, null);
		}

		@Override
		protected AbstractViewHolder createViewHolder(View view) {
			return new VoteViewHolder(view);
		}

		class VoteViewHolder extends AbstractViewHolder {
			private TextView voteNameTv;
			private TextView voteStatusTv;

			public VoteViewHolder(View view) {
				voteNameTv = (TextView) view.findViewById(R.id.vote_name_tv);
				voteStatusTv = (TextView) view
						.findViewById(R.id.vote_status_tv);
			}

			@Override
			public void init(int positon) {
				final VoteGroup voteGroup = (VoteGroup) getItem(positon);
				if (voteGroup.isM_bPublishResult() || voteGroup.isM_bDeadline()) {
					if (voteGroup.isM_bPublishResult()) {
						voteStatusTv.setText(R.string.vote_have_result);
					} else {
						voteStatusTv.setText(R.string.vote_have_deadline);
					}
				} else if (voteGroup.isM_bPublish()) {
					voteStatusTv.setText(R.string.vote_have_publish);
				} else {
					voteStatusTv.setText(R.string.vote_not_publish);
				}
				voteNameTv.setText(voteGroup.getM_strText());
			}

		}

	}

	private void close() {
		((MutiVideoActivity) getActivity()).closeVote();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vote_receiver_close_btn:
			close();
			break;
		}
	}
}
