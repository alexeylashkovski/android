package by.transavto.transavto;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TableRow;

public class ViewDatePicker extends FrameLayout implements OnClickListener {

	private Calendar fromDate = Calendar.getInstance();
	private Calendar toDate = Calendar.getInstance();
	private int numClicksMade = 0;
	Boolean selectedFrom = false, selectedTo = false;
	final String TAG = "DatePickerControl";
	private Calendar today;
	EntityCalendarCell[] cells = new EntityCalendarCell[35];

	public ViewDatePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (!isInEditMode())
			initControl();
	}

	public ViewDatePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode())
			initControl();
	}

	public ViewDatePicker(Context context) {
		super(context);
		if (!isInEditMode())
			initControl();
	}

	private void initControl() {
		//Log.d(TAG,"initContorl");
		today = Calendar.getInstance();
		today.setTime(new Date());
		// получаем экземпляр класса LayoutInflater, нужен чтобы загрузить
		// разметку из xml файла
		LayoutInflater li = ((Activity) getContext()).getLayoutInflater();
		// загружаем в себя (мы унаследовались от FrameLayout) содержимое
		// файла-разметки like_unlike_control
		li.inflate(R.layout.date_picker_view_layout, this);
		fillArray();
	}

	private void fillArray() {
		//Log.d(TAG,"fillArray");
		int cell = 0;
		TableRow ll;
		Calendar c = Calendar.getInstance();
		Calendar mx = Calendar.getInstance();
		int max;
		c.setTime(today.getTime());
		mx.setTime(today.getTime());

		max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		mx.add(Calendar.DAY_OF_MONTH, max - c.get(Calendar.DAY_OF_MONTH));

		int startDif = Math.max(c.get(Calendar.DAY_OF_WEEK) - 2, 2);

		c.add(Calendar.DAY_OF_MONTH, -startDif);

		for (int week = 0; week < 5; week++) {
			ll = (TableRow) findViewById(getResources().getIdentifier(
					"llWeek" + String.valueOf(week + 1), "id",
					"by.transavto.transavto"));

			for (int day = 0; day < 7; day++) {
				if (c.getTimeInMillis() < today.getTimeInMillis()) {
					cells[cell] = new CalendarGrayCell(this.getContext(), c);
				} else {
					if (c.getTimeInMillis() > mx.getTimeInMillis())
						cells[cell] = new CalendarDarkGrayCell(this.getContext(), c);
					else if (day == 5 || day == 6)
						cells[cell] = new CalendarRedCell(this.getContext(), c);
					else
						cells[cell] = new CalendarBlackCell(this.getContext(),	c);
					cells[cell].setOnClickListener(this);
					// cells[cell].setOnTouchListener(this);
				}

				if (c.getTimeInMillis() == today.getTimeInMillis()) {
					cells[cell].setTypeface(null, Typeface.BOLD);
				}
				ll.addView(cells[cell]);
				cell++;
				c.add(Calendar.DAY_OF_MONTH, 1);

			}
		}
		// Log.d(TAG, String.valueOf(startDif));
		// Log.d(TAG, c.toString());
	}

	@Override
	public void onClick(View v) {
		if (numClicksMade == 2) {
			selectedFrom = selectedTo = false;
			clearAllMarks();
			numClicksMade = 0;
		}
		if (numClicksMade == 1) {
			selectedTo = true;
			numClicksMade++;
			toDate.setTime(((EntityCalendarCell) v).day.getTime());
			markDates();
		}
		if (numClicksMade == 0) {
			clearAllMarks();
			selectedFrom = true;
			numClicksMade++;
			fromDate.setTime(((EntityCalendarCell) v).day.getTime());
		}

		v.setBackgroundColor(Color.GREEN);

	}

	public void clearAllMarks() {
		fromDate.clear();
		toDate.clear();
		selectedFrom = selectedTo = false;
		for (int i = 0; i < 35; i++) {
			cells[i].setBackgroundColor(Color.WHITE);
		}

	}
	
	public void refreshMarks() {
		//Log.d(TAG,"refresh marks");
		//Log.d(TAG,fromDate.toString());
		//Log.d(TAG,toDate.toString());
		for (int i = 0; i < 35; i++) {
			if (cells[i].day.getTimeInMillis() >= fromDate.getTimeInMillis()
					&& cells[i].day.getTimeInMillis() <= toDate
							.getTimeInMillis()) {
				cells[i].setBackgroundColor(Color.GREEN);
			}
			else
				cells[i].setBackgroundColor(Color.WHITE);
		}		
	}

	public void markDates() {
		if (selectedFrom && !selectedTo) 
			setDateTo(fromDate);
		if (selectedTo && !selectedFrom)
			setDateFrom(toDate);
		if (fromDate.getTimeInMillis() > toDate.getTimeInMillis()) {
			Calendar tmp = Calendar.getInstance();
			tmp.setTime(fromDate.getTime());
			fromDate.setTime(toDate.getTime());
			toDate.setTime(tmp.getTime());
		}
	//	Log.d(TAG, "fromDate: " + fromDate.toString());
	//	Log.d(TAG, "toDate: " + toDate.toString());
		refreshMarks();
	}

	public void setDateFrom(Long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		setDateFrom(c);
	}
	public void setDateFrom(Calendar c) {
		fromDate.setTime(c.getTime());
		fromDate.set(Calendar.HOUR_OF_DAY, 0);
		fromDate.set(Calendar.MINUTE, 0);
		fromDate.set(Calendar.SECOND, 0);
		fromDate.set(Calendar.MILLISECOND, 0);

		selectedFrom = true;
//		Log.d(TAG,"SET df: "+fromDate.toString());
		//numClicksMade = 1;
		markDates();
//		Log.d(TAG,"SET df: "+fromDate.toString());
		
	}
	public Calendar getDateFrom() {
		return this.fromDate;
	}
	public Calendar getDateTo() {
		return this.toDate;
	}
	
	public void setDateTo(Long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		setDateTo(c);
	}

	public void setDateTo(Calendar c) {
		toDate.setTime(c.getTime());
		toDate.set(Calendar.HOUR_OF_DAY, 0);
		toDate.set(Calendar.MINUTE, 0);
		toDate.set(Calendar.SECOND, 0);
		toDate.set(Calendar.MILLISECOND, 0);

		selectedTo = true;
//		Log.d(TAG,"SET dt: "+toDate.toString());
		//numClicksMade = 2;
		markDates();
	}
	
	public Calendar getToday() {
		return this.today;
	}

	public String getRusRange() {
			String month[] = { "Янв.", "Фев.", "Мар.", "Апр.", "Май", "Июн", "Июл",
					"Авг.", "Сен.", "Окт.", "Ноя.", "Дек." };
			String ret = "";

			ret += month[getDateFrom().get(Calendar.MONTH)] + " "
					+ getDateFrom().get(Calendar.DAY_OF_MONTH);
			if (getDateFrom().get(Calendar.YEAR) != getDateTo().get(Calendar.YEAR) || !selectedTo)
				ret += ", " + getDateFrom().get(Calendar.YEAR);

			if (selectedTo) {
				if (getDateFrom().getTimeInMillis() != getDateTo()
						.getTimeInMillis()) {
					ret += " - ";
					if (getDateFrom().get(Calendar.MONTH) != getDateTo().get(
							Calendar.MONTH))
						ret += month[getDateTo().get(Calendar.MONTH)] + " ";
					ret += getDateTo().get(Calendar.DAY_OF_MONTH);
				}
				ret += ", " + getDateTo().get(Calendar.YEAR);
			}
			return ret;
	}
}
