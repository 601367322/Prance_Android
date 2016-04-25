package com.gensee.rtdemo.vote;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.gensee.demo.R;
import com.gensee.vote.VoteAnswer;
import com.gensee.vote.VoteQuestion;
import com.gensee.vote.VoteQuestion.VoteQuestionType;

public class VoteAnswerFragement extends Fragment implements OnClickListener,
		OnCheckedChangeListener {
	private final int DEFAULT_ANSWER_COUNT = 4;
	private EditText edtQuestionName;
	private EditText edtQuestionScore;
	private RadioButton rbSingleType;
	private RadioButton rbMultiType;
	private LinearLayout lyAnswerList;
	private Button btnAddMore;
	private Button btnSave, btnCancel;
	private VoteQuestion voteQuestion;
	private List<VoteAnswer> answerList;
	private List<VoteQuestion> questionList;
	private boolean bEdit = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		answerList = new ArrayList<VoteAnswer>();
		super.onCreate(savedInstanceState);
	}

	public List<VoteQuestion> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<VoteQuestion> questionList) {
		this.questionList = questionList;
	}

	public VoteQuestion getVoteQuestion() {
		return voteQuestion;
	}

	/*
	 * bEdit : false 新建问题 true : 编辑问题
	 */
	public void setVoteQuestion(VoteQuestion voteQuestion, boolean bEdit) {
		this.voteQuestion = voteQuestion;
		this.bEdit = bEdit;
	}

	private void initEditValue() {
		answerList.clear();
		answerList.addAll(voteQuestion.getM_answers());
		for (VoteAnswer voteAnswer : answerList) {
			LinearLayout childLy = (LinearLayout) LayoutInflater.from(
					getActivity()).inflate(R.layout.vote_answer_item, null);

			for (int j = 0; j < childLy.getChildCount(); j++) {
				final View view = childLy.getChildAt(j);
				if (view instanceof EditText) {
					((EditText) view).setText(voteAnswer.getM_strText());
				} else if (view instanceof RadioButton) {
					if (voteQuestion.getM_strType().equals(
							VoteQuestionType.SINGLE_TYPE)) {
						((RadioButton) view).setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								singleChoice(view);
							}
						});
						((RadioButton) view).setVisibility(View.VISIBLE);
						((RadioButton) view).setChecked(voteAnswer
								.isM_bCorrect() ? true : false);
					}
					else
					{
						((RadioButton) view).setVisibility(View.GONE);
					}
				} else if (view instanceof CheckBox) {
					if (voteQuestion.getM_strType().equals(
							VoteQuestionType.MULTI_TYPE)) {
						((CheckBox) view).setVisibility(View.VISIBLE);
						((CheckBox) view)
								.setChecked(voteAnswer.isM_bCorrect());
					}
					else
					{
						((CheckBox) view).setVisibility(View.GONE);
					}
				}
			}
			lyAnswerList.addView(childLy);
		}

		edtQuestionName.setText(voteQuestion.getM_strText());
		if (voteQuestion.getM_nScore() >= 0) {
			edtQuestionScore.setText(voteQuestion.getM_nScore() + "");
		}

		rbSingleType.setChecked(voteQuestion.getM_strType().equals(
				VoteQuestionType.SINGLE_TYPE) ? true : false);
		rbMultiType.setChecked(voteQuestion.getM_strType().equals(
				VoteQuestionType.MULTI_TYPE) ? true : false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.vote_answer_edit, null);
		edtQuestionName = (EditText) view.findViewById(R.id.edt_question_name);
		edtQuestionScore = (EditText) view
				.findViewById(R.id.edt_question_scorce);
		rbSingleType = (RadioButton) view
				.findViewById(R.id.vote_question_single_rb);
		rbMultiType = (RadioButton) view
				.findViewById(R.id.vote_question_multi_rb);
		lyAnswerList = (LinearLayout) view.findViewById(R.id.vote_answer_ly);
		btnAddMore = (Button) view.findViewById(R.id.vote_answer_add_more_btn);
		btnAddMore.setOnClickListener(this);
		btnSave = (Button) view.findViewById(R.id.vote_answer_save);
		btnSave.setOnClickListener(this);
		btnCancel = (Button) view.findViewById(R.id.answer_cancel);
		btnCancel.setOnClickListener(this);
		if (bEdit) {
			initEditValue();
			rbSingleType.setOnCheckedChangeListener(this);
			rbMultiType.setOnCheckedChangeListener(this);
		} else {
			rbSingleType.setOnCheckedChangeListener(this);
			rbMultiType.setOnCheckedChangeListener(this);
			answerAddMore();
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vote_answer_add_more_btn:
			answerAddMore();
			break;
		case R.id.vote_answer_save:
			answerSave();
			break;
		case R.id.answer_cancel:
			answerCancel();
			break;
		}
	}

	/*
	 * 增加答案选项，每项跟answer对应
	 */
	private void answerAddMore() {
		for (int i = 0; i < DEFAULT_ANSWER_COUNT; i++) {
			LinearLayout childLy = (LinearLayout) LayoutInflater.from(
					getActivity()).inflate(R.layout.vote_answer_item, null);
			lyAnswerList.addView(childLy);
			answerItem(childLy);
			VoteAnswer voteAnswer = new VoteAnswer();
			answerList.add(voteAnswer);
		}
	}

	/*
	 * 只在保存的时候去修改对象的内容
	 */
	private void answerSave() {
		String sQuestionName = edtQuestionName.getText().toString();
		if ("".equals(sQuestionName)) {
			Toast.makeText(getActivity(), R.string.vote_question_name_empty,
					3000).show();
			return;
		}

		for (int i = 0; i < lyAnswerList.getChildCount(); i++) {
			VoteAnswer voteAnswer = answerList.get(i);
			LinearLayout childLy = (LinearLayout) lyAnswerList.getChildAt(i);
			for (int j = 0; j < childLy.getChildCount(); j++) {
				View view = childLy.getChildAt(j);
				if (view instanceof EditText) {
					String sAnswer = ((EditText) view).getText().toString();
					voteAnswer.setM_strText(sAnswer);
				} else if (view instanceof RadioButton) {
					if (rbSingleType.isChecked()) {
						voteAnswer.setM_bCorrect(((RadioButton) view)
								.isChecked());
					}
				} else if (view instanceof CheckBox) {
					if (rbMultiType.isChecked()) {
						voteAnswer.setM_bCorrect(((CheckBox) view).isChecked());
					}
				}
			}
		}

		boolean bTrue = false;
		for (VoteAnswer voteAnswer : answerList) {
			if (!"".equals(voteAnswer.getM_strText())) {
				bTrue = true;
			}
		}
		if (!bTrue) {
			Toast.makeText(getActivity(), R.string.vote_answer_please_set, 3000)
					.show();
			return;
		}

		voteQuestion.setM_strText(sQuestionName);
		String sScore = edtQuestionScore.getText().toString();
		if (!"".equals(sScore)) {
			int nScore = Integer.valueOf(sScore);
			voteQuestion.setM_nScore(nScore);
		}

		if (rbMultiType.isChecked()) {
			voteQuestion.setM_strType(VoteQuestion.VoteQuestionType.MULTI_TYPE);
		} else if (rbSingleType.isChecked()) {
			voteQuestion
					.setM_strType(VoteQuestion.VoteQuestionType.SINGLE_TYPE);
		}

		voteQuestion.getM_answers().clear();
		for (VoteAnswer voteAnswer : answerList) {
			if (!"".equals(voteAnswer.getM_strText())) {
				voteQuestion.getM_answers().add(voteAnswer);
			}
		}

		if (!questionList.contains(voteQuestion)) {
			questionList.add(voteQuestion);
		}
		getFragmentManager().popBackStack();
	}

	private void answerCancel() {
		getFragmentManager().popBackStack();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			if (buttonView.getId() == R.id.vote_question_single_rb) {
				rbMultiType.setChecked(false);
			} else if (buttonView.getId() == R.id.vote_question_multi_rb) {
				rbSingleType.setChecked(false);
			}
			answerSelectType();
		}
	}

	/*
	 * 单项选择，需去除其他的选择项
	 */
	private void singleChoice(View view) {
		for (int i = 0; i < lyAnswerList.getChildCount(); i++) {
			LinearLayout childLy = (LinearLayout) lyAnswerList.getChildAt(i);
			for (int j = 0; j < childLy.getChildCount(); j++) {
				View tmp = childLy.getChildAt(j);
				if (tmp instanceof RadioButton && (tmp != view)) {
					if (((RadioButton) tmp).isChecked()) {
						((RadioButton) tmp).setChecked(false);
					}
				}
			}
		}
	}

	private void answerItem(LinearLayout childLy) {
		for (int j = 0; j < childLy.getChildCount(); j++) {
			final View view = childLy.getChildAt(j);
			if (view instanceof RadioButton) {
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						singleChoice(view);
					}
				});
				if (rbSingleType.isChecked()) {
					view.setVisibility(View.VISIBLE);
					((RadioButton) view).setChecked(false);
				} else {
					view.setVisibility(View.GONE);
				}
			}
			if (view instanceof CheckBox) {
				if (rbSingleType.isChecked()) {
					view.setVisibility(View.GONE);
				} else {
					view.setVisibility(View.VISIBLE);
					((CheckBox) view).setChecked(false);
				}
			}
		}
	}

	/*
	 * 切换单选，多选
	 */
	private void answerSelectType() {
		for (int i = 0; i < lyAnswerList.getChildCount(); i++) {
			LinearLayout childLy = (LinearLayout) lyAnswerList.getChildAt(i);
			answerItem(childLy);
		}
	}

}
