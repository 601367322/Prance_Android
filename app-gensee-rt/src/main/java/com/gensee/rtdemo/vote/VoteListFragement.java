package com.gensee.rtdemo.vote;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gensee.demo.R;
import com.gensee.rtdemo.adapter.AbstractAdapter;
import com.gensee.vote.VoteGroup;

public class VoteListFragement extends Fragment {
	private ListView voteLv;
	private List<VoteGroup> voteList;
	private VoteAdapter voteAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		voteList = new ArrayList<VoteGroup>();
		super.onCreate(savedInstanceState);
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.vote_list_layout, null);
		voteLv = (ListView) view.findViewById(R.id.vote_lv);
		voteAdapter = new VoteAdapter(getActivity());
		voteAdapter.notifyData(voteList);
		voteLv.setAdapter(voteAdapter);
		return view;
	}

	private class VoteAdapter extends AbstractAdapter<VoteGroup> {

		public VoteAdapter(Context context) {
			super(context);
		}

		@Override
		protected View createView() {
			return LayoutInflater.from(getActivity()).inflate(R.layout.vote_list_item_layout, null);
		}

		@Override
		protected AbstractViewHolder createViewHolder(View view) {
			return new VoteViewHolder(view);
		}

		class VoteViewHolder extends AbstractViewHolder implements
				OnClickListener {
			private LinearLayout voteItemLy;
			private TextView voteNameTv;
			private Button btnVoteQuery;
			private Button btnVoteEdit;
			private Button btnVoteDelete;
			private Button btnVotePublish;
			private Button btnVoteResult;
			private Button btnVoteDeadline;

			public VoteViewHolder(View view) {
				voteItemLy = (LinearLayout) view
						.findViewById(R.id.vote_item_ly);
				voteNameTv = (TextView) view.findViewById(R.id.vote_name_tv);
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
				VoteGroup voteGroup = (VoteGroup) voteList.get(positon);
				voteNameTv.setText(voteGroup.getM_strText());
				btnVoteQuery.setOnClickListener(this);
				btnVoteDelete.setOnClickListener(this);
				btnVotePublish.setOnClickListener(this);
				btnVoteResult.setOnClickListener(this);
				btnVoteDeadline.setOnClickListener(this);
			}

			private void voteQuery() {

			}

			private void voteEdit() {

			}

			private void voteDelete() {

			}

			private void votePublish() {

			}

			private void voteResult() {

			}

			private void voteDeadline() {

			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.vote_query_btn:
					voteQuery();
					break;
				case R.id.vote_edit_btn:
					voteEdit();
					break;
				case R.id.vote_delete_btn:
					voteDelete();
					break;
				case R.id.vote_publish_btn:
					votePublish();
					break;
				case R.id.vote_result_btn:
					voteResult();
					break;
				case R.id.vote_deadline_btn:
					voteDeadline();
					break;
				}
			}

		}

	}
}
