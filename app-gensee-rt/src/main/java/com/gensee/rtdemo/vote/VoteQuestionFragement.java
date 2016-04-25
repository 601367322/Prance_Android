package com.gensee.rtdemo.vote;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.gensee.room.RtSdk;
import com.gensee.room.RtSdk.VoteResult;
import com.gensee.demo.R;
import com.gensee.utils.GenseeLog;
import com.gensee.vote.VoteAnswer;
import com.gensee.vote.VoteGroup;
import com.gensee.vote.VoteQuestion;
import com.gensee.vote.VoteQuestion.VoteQuestionType;

@SuppressLint("ValidFragment")
public class VoteQuestionFragement extends Fragment implements OnClickListener {
	private EditText edtVoteName;
	private Button btnQuestionAdd;
	private Button btnSave;
	private Button btnCancel;
	private ExpandableListView expandQuestionLv;
	private List<VoteQuestion> questionList;
	private QuestionAdapter questionAdapter;
	private VoteGroup voteGroup;
	private RtSdk rtSdk;
	private boolean bEdit = false;

	public VoteQuestionFragement(RtSdk rtSdk) {
		this.rtSdk = rtSdk;
	}

	public RtSdk getRtSdk() {
		return rtSdk;
	}

	public void setRtSdk(RtSdk rtSdk) {
		this.rtSdk = rtSdk;
	}

	public VoteGroup getVoteGroup() {
		return voteGroup;
	}

	public void setVoteGroup(VoteGroup voteGroup, boolean bEdit) {
		this.voteGroup = voteGroup;
		this.bEdit = bEdit;
	}
	
	private void initEditValue()
	{
		edtVoteName.setText(voteGroup.getM_strText());
		questionList.clear();
		questionList.addAll(voteGroup.getM_questions());
		if(null != questionAdapter)
		{
			questionAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		questionList = new ArrayList<VoteQuestion>();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.vote_question_edit, null);
		edtVoteName = (EditText) view.findViewById(R.id.edt_vote_name);
		btnQuestionAdd = (Button) view.findViewById(R.id.vote_question_add_btn);
		btnQuestionAdd.setOnClickListener(this);
		btnSave = (Button) view.findViewById(R.id.question_save);
		btnSave.setOnClickListener(this);
		btnCancel = (Button) view.findViewById(R.id.question_cancel);
		btnCancel.setOnClickListener(this);
		expandQuestionLv = (ExpandableListView) view
				.findViewById(R.id.question_expand_lv);
		expandQuestionLv.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});
		questionAdapter = new QuestionAdapter();
		expandQuestionLv.setAdapter(questionAdapter);
		if(bEdit)
		{
			initEditValue();
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vote_question_add_btn:
			questionAdd();
			break;
		case R.id.question_save:
			questionSave();
			break;
		case R.id.question_cancel:
			questionCancel();
			break;
		}
	}

	private void questionAdd() {
		enterAnswerFragement(new VoteQuestion(), false);
	}
	

  private void enterAnswerFragement(VoteQuestion voteQuestion, boolean bEdit)
  {
	    VoteAnswerFragement answerFragement = new VoteAnswerFragement();
		answerFragement.setVoteQuestion(voteQuestion, bEdit);
		answerFragement.setQuestionList(questionList);
		
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction
				.replace(R.id.vote_holder_container, answerFragement);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
  }

	/*
	 * 保存试卷，各项的ID不用填写，底层库会自动生成
	 */
	private void questionSave() {
		String voteName = edtVoteName.getText().toString();
		if ("".equals(voteName)) {
			Toast.makeText(getActivity(), R.string.vote_name_empty, 3000)
					.show();
			return;
		}
		if(questionList.size() <= 0)
		{
			Toast.makeText(getActivity(), R.string.vote_question_please_set, 3000)
			.show();
			return;
		}
		voteGroup.setM_strText(voteName);
		voteGroup.getM_questions().clear();
		voteGroup.getM_questions().addAll(questionList);
		if (null != rtSdk) {
			if (VoteResult.SUCCESS == rtSdk.voteAdd(voteGroup, null)) {
				getFragmentManager().popBackStack();
			} else {
				Toast.makeText(getActivity(), R.string.vote_save_failure, 3000)
				.show();
			}
		}
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

	private void questionCancel() {
		getFragmentManager().popBackStack();
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
						R.layout.vote_question_group_layout, null);
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
						R.layout.vote_question_child_layout, null);
				answerViewHolder = new AnswerViewHolder(convertView);
				convertView.setTag(answerViewHolder);
			} else {
				answerViewHolder = (AnswerViewHolder) convertView.getTag();
			}
			answerViewHolder.init(
					(VoteQuestion)getGroup(groupPosition), (VoteAnswer) getChild(groupPosition, childPosition),
					childPosition);
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

		private class QuestionViewHolder {
			private TextView tvQuestionName;
			private LinearLayout lyQuestion;

			public QuestionViewHolder(View view) {
				tvQuestionName = (TextView) view
						.findViewById(R.id.question_name_tv);
				lyQuestion = (LinearLayout) view
						.findViewById(R.id.vote_guestion_group_ly);
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
				lyQuestion.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						enterAnswerFragement(voteQuestion, true);
					}
				});
			}
		}

		private class AnswerViewHolder {
			private TextView tvAnswerName;
			private LinearLayout lyAnswer;

			public AnswerViewHolder(View view) {
				tvAnswerName = (TextView) view
						.findViewById(R.id.answer_name_tv);
				lyAnswer = (LinearLayout) view
						.findViewById(R.id.vote_guestion_child_ly);
			}

			public void init(final VoteQuestion voteQuestion, VoteAnswer voteAnswer, int nPosition) {
				int nValue = 65;
				tvAnswerName.setText((char) (nValue + nPosition) + ".  "
						+ voteAnswer.getM_strText());
				lyAnswer.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						enterAnswerFragement(voteQuestion, true);
					}
				});
			}
		}

	}

}
