package com.gensee.rtdemo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AbstractAdapter<T> extends BaseAdapter {
	protected Context context;
	protected List<T> objectList;

	public AbstractAdapter(Context context) {
		this.context = context;
		objectList = new ArrayList<T>();
	}

	@Override
	public int getCount() {
		return objectList.size();
	}

	@Override
	public Object getItem(int position) {
		return objectList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void notifyData(List<T> objectList) {
		this.objectList.clear();
		this.objectList.addAll(objectList);
		notifyDataSetChanged();
	}

	// public <T> void addData(T object, int index) {
	// this.objectList.add(index, object);
	// notifyDataSetChanged();
	// }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AbstractViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = createView();
			viewHolder = createViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (AbstractViewHolder) convertView.getTag();
		}
		viewHolder.init(position);
		return convertView;
	}

	protected abstract class AbstractViewHolder {
		public abstract void init(final int positon);
	}

	protected abstract View createView();

	protected abstract AbstractViewHolder createViewHolder(View view);
}
