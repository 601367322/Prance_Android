package com.gensee.rtdemo.vote;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gensee.room.RtSdk;
import com.gensee.demo.R;
import com.gensee.taskret.OnTaskRet;
import com.gensee.vote.VoteAnswer;
import com.gensee.vote.VoteGroup;
import com.gensee.vote.VoteQuestion;
import com.gensee.vote.VoteQuestion.VoteQuestionType;

@SuppressLint("ValidFragment")
public class VoteCommitFragement extends Fragment implements OnClickListener {
	private RtSdk rtSdk;
	private Button btnCommit;
	private Button btnClose;
	private VoteGroup voteGroup;
	private TextView tvVoteName;
	private ExpandableListView expandQuestionLv;
	private List<VoteQuestion> questionList;
	private QuestionAdapter questionAdapter;

	public VoteCommitFragement(RtSdk rtSdk, VoteGroup voteGroup) {
		this.rtSdk = rtSdk;
		this.voteGroup = voteGroup;
		questionList = new ArrayList<VoteQuestion>();
		questionList.addAll(voteGroup.getM_questions());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.vote_commit_layout, null);
		tvVoteName = (TextView) view.findViewById(R.id.vote_query_votename_tv);
		tvVoteName.setText(voteGroup.getM_strText());
		expandQuestionLv = (ExpandableListView) view
				.findViewById(R.id.vote_query_expandlv);
		expandQuestionLv.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
		questionAdapter = new QuestionAdapter();
		expandQuestionLv.setAdapter(questionAdapter);
		btnCommit = (Button) view.findViewById(R.id.vote_commit_btn);
		btnCommit.setOnClickListener(this);
		btnClose = (Button)view.findViewById(R.id.vote_commit_close_btn);
		btnClose.setOnClickListener(this);
		return view;
	}

	@Override
	public void onResume() {
		if (null != questionAdapter) {
			questionAdapter.notifyDataSetChanged();
			for (int i = 0; i < questionAdapter.getGroupCount(); i++) {
				expandQuestionLv.expandGroup(i);
			}
		}
		super.onResume();
	}

	private class QuestionAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			return questionList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return questionList.get(groupPosition).getM_answers().size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return questionList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return questionList.get(groupPosition).getM_answers()
					.get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			QuestionViewHolder questionViewHolder = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.vote_commit_group_layout, null);
				questionViewHolder = new QuestionViewHolder(convertView);
				convertView.setTag(questionViewHolder);
			} else {
				questionViewHolder = (QuestionViewHolder) convertView.getTag();
			}
			questionViewHolder.init(questionList.get(groupPosition),
					groupPosition);
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			AnswerViewHolder answerViewHolder = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.vote_commit_child_layout, null);
				answerViewHolder = new AnswerViewHolder(convertView);
				convertView.setTag(answerViewHolder);
			} else {
				answerViewHolder = (AnswerViewHolder) convertView.getTag();
			}
			answerViewHolder.init((VoteQuestion) getGroup(groupPosition),
					(VoteAnswer) getChild(groupPosition, childPosition),
					childPosition);
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

		private class QuestionViewHolder {
			private TextView tvQuestionName;

			public QuestionViewHolder(View view) {
				tvQuestionName = (TextView) view
						.findViewById(R.id.question_name_tv);
			}

			public void init(final VoteQuestion voteQuestion, int nPosition) {
				String type = "";
				if (voteQuestion.getM_strType() == VoteQuestionType.SINGLE_TYPE) {
					type = getString(R.string.single_choice);
				} else {
					type = getString(R.string.multi_choice);
				}
				String sName = (nPosition + 1) + ".  "
						+ voteQuestion.getM_strText() + "  " + type;
				tvQuestionName.setText(sName);
			}
		}

		private class AnswerViewHolder {
			private TextView tvAnswerName;
			private ImageView ivAnswerSuccess;
			private RadioButton rbSingle;
			private CheckBox cbMulti;

			public AnswerViewHolder(View view) {
				tvAnswerName = (TextView) view
						.findViewById(R.id.answer_name_tv);
				rbSingle = (RadioButton) view.findViewById(R.id.single_rb);
				cbMulti = (CheckBox) view.findViewById(R.id.multi_cb);
				ivAnswerSuccess = (ImageView) view
						.findViewById(R.id.query_answer_success_iv);
			}

			public void init(final VoteQuestion voteQuestion,
					final VoteAnswer voteAnswer, int nPosition) {
				int nValue = 65;
				tvAnswerName.setText((char) (nValue + nPosition) + ".  "
						+ voteAnswer.getM_strText());

				if (voteAnswer.isM_bCorrect()) {
					ivAnswerSuccess.setVisibility(View.VISIBLE);
				} else {
					ivAnswerSuccess.setVisibility(View.GONE);
				}

				if (voteQuestion.getM_strType().equals(
						VoteQuestionType.SINGLE_TYPE)) {
					rbSingle.setVisibility(View.VISIBLE);
					cbMulti.setVisibility(View.GONE);
					rbSingle.setChecked(voteAnswer.isM_bChoose());
				} else if (voteQuestion.getM_strType().equals(
						VoteQuestionType.MULTI_TYPE)) {
					rbSingle.setVisibility(View.GONE);
					cbMulti.setVisibility(View.VISIBLE);
					cbMulti.setChecked(voteAnswer.isM_bChoose());
				}
				rbSingle.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						for (VoteAnswer tmpAnswer : voteQuestion
								.getM_answers()) {
							if (tmpAnswer.isM_bChoose()) {
								tmpAnswer.setM_bChoose(false);
							}
						}
						voteAnswer.setM_bChoose(true);
						notifyDataSetChanged();
					}
				});

				cbMulti.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						voteAnswer.setM_bChoose(cbMulti.isChecked());
					}
				});
			}
		}

	}

	private void commit() {
		rtSdk.voteSubmit(voteGroup, new OnTaskRet() {

			@Override
			public void onTaskRet(boolean ret, int id, String desc) {
				if (ret) {
					getFragmentManager().popBackStack();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vote_commit_btn:
			commit();
			break;
		case R.id.vote_commit_close_btn:
			getFragmentManager().popBackStack();
			break;
		}
	}
}
