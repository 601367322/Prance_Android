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

@SuppressLint("ValidFragment")
public class VoteHostFragement extends Fragment implements OnClickListener {
	private Button voteAdd;
	private ListView voteLv;
	private VoteAdapter voteAdapter;
	private RtSdk rtSdk;
	private Button btnClose;

	public VoteHostFragement(RtSdk rtSdk) {
		this.rtSdk = rtSdk;
		voteAdapter = new VoteAdapter(getActivity());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void notifyData(List<VoteGroup> voteList) {
		if (null != voteAdapter) {
			voteAdapter.notifyData(voteList);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.vote_host_layout, null);
		btnClose = (Button) view.findViewById(R.id.vote_host_close_btn);
		btnClose.setOnClickListener(this);
		voteAdd = (Button) view.findViewById(R.id.vote_add_btn);
		voteAdd.setOnClickListener(this);
		voteLv = (ListView) view.findViewById(R.id.vote_list_lv);
		voteLv.setAdapter(voteAdapter);
		voteLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				VoteGroup voteGroup = (VoteGroup) voteAdapter.getItem(position);
				FragmentTransaction fragmentTransaction = getFragmentManager()
						.beginTransaction();
				fragmentTransaction.replace(R.id.vote_holder_container,
						new VoteQueryFragement(voteGroup));
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		});
		return view;
	}

	private void voteAdd() {
		enterQuestionFragement(new VoteGroup(), false);
	}

	private void enterQuestionFragement(VoteGroup voteGroup, boolean bEdit) {
		VoteQuestionFragement questionFragement = new VoteQuestionFragement(
				rtSdk);
		questionFragement.setVoteGroup(voteGroup, bEdit);
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.replace(R.id.vote_holder_container,
				questionFragement);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vote_add_btn:
			voteAdd();
			break;
		case R.id.vote_host_close_btn:
			close();
			break;
		}
	}

	private void close() {
		((MutiVideoActivity) getActivity()).closeVote();
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
					R.layout.vote_list_item_layout, null);
		}

		@Override
		protected AbstractViewHolder createViewHolder(View view) {
			return new VoteViewHolder(view);
		}

		class VoteViewHolder extends AbstractViewHolder {
			private TextView voteNameTv;
			private TextView voteStatusTv;
			private Button btnVoteQuery;
			private Button btnVoteEdit;
			private Button btnVoteDelete;
			private Button btnVotePublish;
			private Button btnVoteResult;
			private Button btnVoteDeadline;

			public VoteViewHolder(View view) {
				voteNameTv = (TextView) view.findViewById(R.id.vote_name_tv);
				voteStatusTv = (TextView) view
						.findViewById(R.id.vote_status_tv);
				btnVoteQuery = (Button) view.findViewById(R.id.vote_query_btn);
				btnVoteEdit = (Button) view.findViewById(R.id.vote_edit_btn);
				btnVoteDelete = (Button) view
						.findViewById(R.id.vote_delete_btn);
				btnVotePublish = (Button) view
						.findViewById(R.id.vote_publish_btn);
				btnVoteResult = (Button) view
						.findViewById(R.id.vote_result_btn);
				btnVoteDeadline = (Button) view
						.findViewById(R.id.vote_deadline_btn);
			}

			@Override
			public void init(int positon) {
				final VoteGroup voteGroup = (VoteGroup) getItem(positon);
				if (voteGroup.isM_bPublishResult() || voteGroup.isM_bDeadline()) {
					btnVoteEdit.setVisibility(View.GONE);
					btnVoteDelete.setVisibility(View.GONE);
					btnVotePublish.setVisibility(View.GONE);
					btnVoteResult.setVisibility(View.GONE);
					btnVoteDeadline.setVisibility(View.GONE);
					if (voteGroup.isM_bPublishResult()) {
						voteStatusTv.setText(R.string.vote_have_result);
					} else {
						voteStatusTv.setText(R.string.vote_have_deadline);
					}
				} else if (voteGroup.isM_bPublish()) {
					btnVoteEdit.setVisibility(View.GONE);
					btnVoteDelete.setVisibility(View.GONE);
					btnVotePublish.setVisibility(View.GONE);
					btnVoteResult.setVisibility(View.VISIBLE);
					btnVoteDeadline.setVisibility(View.VISIBLE);
					voteStatusTv.setText(R.string.vote_have_publish);
				} else {
					btnVoteEdit.setVisibility(View.VISIBLE);
					btnVoteDelete.setVisibility(View.VISIBLE);
					btnVotePublish.setVisibility(View.VISIBLE);
					btnVoteResult.setVisibility(View.GONE);
					btnVoteDeadline.setVisibility(View.GONE);
					voteStatusTv.setText(R.string.vote_not_publish);
				}
				voteNameTv.setText(voteGroup.getM_strText());
				btnVoteQuery.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						FragmentTransaction fragmentTransaction = getFragmentManager()
								.beginTransaction();
						fragmentTransaction.replace(R.id.vote_holder_container,
								new VoteQueryFragement(voteGroup));
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
					}
				});
				btnVotePublish.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						rtSdk.votePublish(voteGroup.getM_strId(),false, null);
					}
				});
				btnVoteResult.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						rtSdk.votePublishResult(voteGroup.getM_strId(), null);
					}
				});
				btnVoteDeadline.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						rtSdk.voteDeadline(voteGroup.getM_strId(), null);
					}
				});
				btnVoteEdit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						enterQuestionFragement(voteGroup, true);
					}
				});
				btnVoteDelete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						rtSdk.voteDel(voteGroup.getM_strId(), null);
					}
				});
			}

		}

	}
}
