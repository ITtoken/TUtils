package com.tianjj.tutils.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.tianjj.tutils.base.PinyinUtil;

import android.R.array;

public class QuickIndexHelper {

	/**
	 * get the ordered {@link List} by first letter of names
	 * 
	 * @param names
	 *            the {@link String} {@link array} will to be show whitch
	 *            includes all names
	 * @return the ordered {@link List}
	 */
	public static List<String> getOrderedList(String[] names) {
		List<String> users = new ArrayList<String>();
		String focusLetter = "";

		// order the array
		Arrays.sort(names, new Comparator<String>() {

			@Override
			public int compare(String lhs, String rhs) {

				char lhsFirstChar = PinyinUtil.getInstance().getFirstChar(lhs.trim());
				char rhsFirstChar = PinyinUtil.getInstance().getFirstChar(rhs.trim());
				return lhsFirstChar - rhsFirstChar;
			}
		});

		// add the group info by first letter of names
		for (int i = 0; i < names.length; i++) {
			String name_i = names[i].trim();
			String nameFirstLetter = String.valueOf(PinyinUtil.getInstance().getFirstChar(name_i));

			if (focusLetter != null && !focusLetter.equals(nameFirstLetter)) {
				users.add(nameFirstLetter);
			}
			users.add(name_i);

			focusLetter = nameFirstLetter;
		}

		return users;
	}

	/**
	 * get the position int the {@link String} {@link List}
	 * 
	 * @param letter
	 * @param strings
	 * @return
	 */
	public static int getLetterPosition(String letter, List<String> strings) {
		for (int i = 0; i < strings.size(); i++) {
			if (letter.equals(strings.get(i))) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * get the position int the {@link String} {@link array}
	 * 
	 * @param letter
	 * @param strings
	 * @return
	 */
	public static int getLetterPosition(String letter, String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			if (letter.equals(strings[i])) {
				return i;
			}
		}
		return -1;
	}

}
