package com.victor.homelaunchvic.utils;

/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

/**
 * A ListAdapter that manages a ListView backed by an array of arbitrary
 * objects. By default this class expects that the provided resource id
 * references a single TextView. If you want to use a more complex layout, use
 * the constructors that also takes a field id. That field id should reference a
 * TextView in the larger layout resource.
 * 
 * However the TextView is referenced, it will be filled with the toString() of
 * each object in the array. You can add lists or arrays of custom objects.
 * Override the toString() method of your objects to determine what text will be
 * displayed for the item in the list.
 * 
 * To use something other than TextViews for the array display, for instance,
 * ImageViews, or to have some of data besides toString() results fill the
 * views, override {@link #getView(int, View, ViewGroup)} to return the type of
 * view you want.
 */
public abstract class ArrayListAdapter<T> extends BaseAdapter implements
		Filterable {
	/**
	 * Contains the list of objects that represent the data of this
	 * ArrayAdapter. The content of this list is referred to as "the array" in
	 * the documentation.
	 */
	private List<T> mObjects;

	/**
	 * Lock used to modify the content of {@link #mObjects}. Any write operation
	 * performed on the array should be synchronized on this lock. This lock is
	 * also used by the filter (see {@link #getFilter()} to make a synchronized
	 * copy of the original array of data.
	 */
	private final Object mLock = new Object();

	/**
	 * If the inflated resource is not a TextView, {@link #mFieldId} is used to
	 * find a TextView inside the inflated views hierarchy. This field must
	 * contain the identifier that matches the one defined in the resource file.
	 */
	// private int mFieldId = 0;

	/**
	 * Indicates whether or not {@link #notifyDataSetChanged()} must be called
	 * whenever {@link #mObjects} is modified.
	 */
	private boolean mNotifyOnChange = true;

	private Context mContext;

	protected ArrayList<T> mOriginalValues;
	private ArrayFilter mFilter;
	protected LayoutInflater mInflater;

	private SeeyonNotifyDataChange seeyonNotifyDataChange;

	public SeeyonNotifyDataChange getSeeyonNotifyDataChange() {
		return seeyonNotifyDataChange;
	}

	/**
	 * ���� ��ݸı�ļ���
	 * 
	 * @param seeyonNotifyDataChange
	 */
	public void setSeeyonNotifyDataChange(
			SeeyonNotifyDataChange seeyonNotifyDataChange) {
		this.seeyonNotifyDataChange = seeyonNotifyDataChange;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The current context.
	 */
	public ArrayListAdapter(Context context) {
		init(context, new ArrayList<T>());
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The current context.
	 * @param objects
	 *            The objects to represent in the ListView.
	 */
	public ArrayListAdapter(Context context, T[] objects) {
		init(context, Arrays.asList(objects));
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The current context.
	 * @param objects
	 *            The objects to represent in the ListView.
	 */
	public ArrayListAdapter(Context context, List<T> objects) {
		init(context, objects);
	}

	/**
	 * Adds the specified object at the end of the array.
	 * 
	 * @param object
	 *            The object to add at the end of the array.
	 */
	public void add(T object) {
		if (mOriginalValues != null) {
			synchronized (mLock) {
				mOriginalValues.add(object);
				if (mNotifyOnChange)
					notifyDataSetChanged();
			}
		} else {
			mObjects.add(object);
			if (mNotifyOnChange)
				notifyDataSetChanged();
		}
	}
	
	public void add(int index,T object) {
		if (mOriginalValues != null) {
			synchronized (mLock) {
				mOriginalValues.add(index,object);
				if (mNotifyOnChange)
					notifyDataSetChanged();
			}
		} else {
			mObjects.add(index,object);
			if (mNotifyOnChange)
				notifyDataSetChanged();
		}
	}

	public void addListData(List<T> lsit) {
		if (lsit != null) {
			for (T n : lsit) {
				add(n);
			}
		}
	}

	public void refreshListData(List<T> list) {
		if (list != null) {
			for (T n : list) {
				add(0,n);
			}
		}
	}

	/**
	 * Inserts the specified object at the specified index in the array.
	 * 
	 * @param object
	 *            The object to insert into the array.
	 * @param index
	 *            The index at which the object must be inserted.
	 */
	public void insert(T object, int index) {
		if (mOriginalValues != null) {
			synchronized (mLock) {
				mOriginalValues.add(index, object);
				if (mNotifyOnChange)
					notifyDataSetChanged();
			}
		} else {
			mObjects.add(index, object);
			if (mNotifyOnChange)
				notifyDataSetChanged();
		}
	}

	/**
	 * Removes the specified object from the array.
	 * 
	 * @param object
	 *            The object to remove.
	 */
	public void remove(T object) {
		if (mOriginalValues != null) {
			synchronized (mLock) {
				mOriginalValues.remove(object);
			}
		} else {
			mObjects.remove(object);
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}

	/**
	 * Remove all elements from the list.
	 */
	public void clear() {
		if (mOriginalValues != null) {
			synchronized (mLock) {
				mOriginalValues.clear();
			}
		} else {
			mObjects.clear();
		}
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}

	/**
	 * Sorts the content of this adapter using the specified comparator.
	 * 
	 * @param comparator
	 *            The comparator used to sort the objects contained in this
	 *            adapter.
	 */
	public void sort(Comparator<? super T> comparator) {
		Collections.sort(mObjects, comparator);
		if (mNotifyOnChange)
			notifyDataSetChanged();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		mNotifyOnChange = true;
		if (seeyonNotifyDataChange != null) {
			seeyonNotifyDataChange.NotifyDataChange();
		}
	}

	/**
	 * Control whether methods that change the list ({@link #add},
	 * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
	 * {@link #notifyDataSetChanged}. If set to false, caller must manually call
	 * notifyDataSetChanged() to have the changes reflected in the attached
	 * view.
	 * 
	 * The default is true, and calling notifyDataSetChanged() resets the flag
	 * to true.
	 * 
	 * @param notifyOnChange
	 *            if true, modifications to the list will automatically call
	 *            {@link #notifyDataSetChanged}
	 */
	public void setNotifyOnChange(boolean notifyOnChange) {
		mNotifyOnChange = notifyOnChange;
	}

	private void init(Context context, List<T> objects) {
		mContext = context;
		mObjects = objects;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Returns the context associated with this array adapter. The context is
	 * used to create views from the resource passed to the constructor.
	 * 
	 * @return The Context associated with this adapter.
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		return mObjects.size();
	}

	public List<T> getListData() {
		return mObjects;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getItem(int position) {
		return mObjects.get(position);
	}

	/**
	 * Returns the position of the specified item in the array.
	 * 
	 * @param item
	 *            The item to retrieve the position of.
	 * 
	 * @return The position of the specified item.
	 */
	public int getPosition(T item) {
		return mObjects.indexOf(item);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	abstract public View getView(int position, View convertView,
			ViewGroup parent);

	/**
	 * <p>
	 * Sets the layout resource to create the drop down views.
	 * </p>
	 * 
	 * @param resource
	 *            the layout resource defining the drop down views
	 * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
	 */
	// public void setDropDownViewResource(int resource) {
	// this.mDropDownResource = resource;
	// }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent);
	}

	// {
	// return createViewFromResource(position, convertView, parent,
	// mDropDownResource);
	// }

	/**
	 * Creates a new ArrayAdapter from external resources. The content of the
	 * array is obtained through
	 * {@link android.content.res.Resources#getTextArray(int)}.
	 * 
	 * @param context
	 *            The application's environment.
	 * @param textArrayResId
	 *            The identifier of the array to use as the data source.
	 * @param textViewResId
	 *            The identifier of the layout used to create views.
	 * 
	 * @return An ArrayAdapter<CharSequence>.
	 */
	public static ArrayAdapter<CharSequence> createFromResource(
			Context context, int textArrayResId, int textViewResId) {
		CharSequence[] strings = context.getResources().getTextArray(
				textArrayResId);
		return new ArrayAdapter<CharSequence>(context, textViewResId, strings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	/**
	 * <p>
	 * An array filter constrains the content of the array adapter with a
	 * prefix. Each item that does not start with the supplied prefix is removed
	 * from the list.
	 * </p>
	 */
	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				synchronized (mLock) {
					mOriginalValues = new ArrayList<T>(mObjects);
				}
			}

			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					ArrayList<T> list = new ArrayList<T>(mOriginalValues);
					results.values = list;
					results.count = list.size();
				}
			} else {
				String prefixString = prefix.toString().toLowerCase();

				final ArrayList<T> values = mOriginalValues;
				final int count = values.size();

				final ArrayList<T> newValues = new ArrayList<T>(count);

				for (int i = 0; i < count; i++) {
					final T value = values.get(i);
					final String valueText = value.toString().toLowerCase();

					// First match against the whole, non-splitted value
					if (valueText.startsWith(prefixString)) {
						newValues.add(value);
					} else {
						final String[] words = valueText.split(" ");
						final int wordCount = words.length;

						for (int k = 0; k < wordCount; k++) {
							if (words[k].startsWith(prefixString)) {
								newValues.add(value);
								break;
							}
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// noinspection unchecked
			mObjects = (List<T>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

	public interface SeeyonNotifyDataChange {
		public void NotifyDataChange();
	}
}
