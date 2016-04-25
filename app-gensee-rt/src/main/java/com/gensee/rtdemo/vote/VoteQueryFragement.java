package com.gensee.rtdemo.vote;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gensee.demo.R;
import com.gensee.vote.VoteAnswer;
import com.gensee.vote.VoteGroup;
import com.gensee.vote.VoteQuestion;
import com.gensee.vote.VoteQuestion.VoteQuestionType;

public class VoteQueryFragement extends Fragment implements OnClickListener{
	private VoteGroup voteGroup;
	private TextView tvVoteName;
	private Button btnClose;
	private ExpandableListView expandQuestionLv;
	private List<VoteQuestion> questionList;
	private QuestionAdapter questionAdapter;

	public VoteQueryFragement(VoteGroup voteGroup) {
		this.voteGroup = voteGroup;
		questionList = new ArrayList<VoteQuestion>();
		questionList.addAll(voteGroup.getM_questions());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.vote_query_layout, null);
		tvVoteName = (TextView) view.findViewById(R.id.vote_query_votename_tv);
		tvVoteName.setText(voteGroup.getM_strText());
		expandQuestionLv = (ExpandableListView) view
				.findViewById(R.id.vote_query_expandlv);
		btnClose = (Button)view.findViewById(R.id.vote_query_close_btn);
		btnClose.setOnClickListener(this);
		expandQuestionLv.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
		questionAdapter = new QuestionAdapter();
		expandQuestionLv.setAdapter(questionAdapter);
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
						R.layout.vote_query_group, null);
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
						R.layout.vote_query_child, null);
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
			private TextView tvQuestionTotal;

			public QuestionViewHolder(View view) {
				tvQuestionName = (TextView) view
						.findViewById(R.id.query_question_name_tv);
				tvQuestionTotal = (TextView) view
						.findViewById(R.id.query_question_tatal_tv);
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
				int nValue = voteQuestion.getUsersSize();
				String sValue = String.format(
						getString(R.string.vote_query_question_all_person),
						nValue);
				int nScore = voteQuestion.getM_nScore();
				if (nScore > 0) {
					sValue += ", "
							+ String.format(
									getString(R.string.vote_query_question_score),
									nScore);
				}
				tvQuestionTotal.setText(sValue);
			}
		}

		private class AnswerViewHolder {
			private TextView tvAnswerName;
			private TextView tvAnswerTotal;
			private ImageView ivAnswerSuccess;

			public AnswerViewHolder(View view) {
				tvAnswerName = (TextView) view
						.findViewById(R.id.query_answer_name_tv);
				tvAnswerTotal = (TextView) view
						.findViewById(R.id.query_answer_total_tv);
				ivAnswerSuccess = (ImageView) view
						.findViewById(R.id.query_answer_success_iv);
			}

			public void init(final VoteQuestion voteQuestion,
					VoteAnswer voteAnswer, int nPosition) {
				int nValue = 65;
				tvAnswerName.setText((char) (nValue + nPosition) + ".  "
						+ voteAnswer.getM_strText());

				int nMax = 0;

				if (voteQuestion.getM_strType().equals(
						VoteQuestion.VoteQuestionType.MULTI_TYPE)) {
					for (VoteAnswer voteAnswer1 : voteQuestion.getM_answers()) {
						nMax += voteAnswer1.getUsersSize();
					}
				} else {
					nMax = voteQuestion.getUsersSize();
				}

				if(nMax <= 0)
				{
					nMax = 100;
				}
				int nSize = voteAnswer.getUsersSize();
				float result = (float) nSize / nMax;
				java.text.DecimalFormat format = (java.text.DecimalFormat) java.text.DecimalFormat
						.getInstance();
				format.applyPattern("##.##");
				String fr = (int)(Float.parseFloat(format.format(result)) * 100) + "%";
				String sValue = String.format(
						getString(R.string.vote_query_answer_total), nSize)
						+ "(" + fr + ")";
				tvAnswerTotal.setText(sValue);

				if (voteAnswer.isM_bCorrect()) {
					ivAnswerSuccess.setVisibility(View.VISIBLE);
				} else {
					ivAnswerSuccess.setVisibility(View.GONE);
				}
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.vote_query_close_btn:
			getFragmentManager().popBackStack();
			break;
		}
	}
}
