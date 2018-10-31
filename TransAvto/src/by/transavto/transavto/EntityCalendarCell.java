package by.transavto.transavto;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

public class EntityCalendarCell extends TextView {

	Calendar day = Calendar.getInstance();
	Integer cellResourceId;
	Integer textColor = Color.BLACK;
	Integer bgrColor = Color.WHITE;

	public EntityCalendarCell(Context context) {
		super(context);
		initCell();
		// TODO Auto-generated constructor stub
	}

	public EntityCalendarCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		initCell();
		// TODO Auto-generated constructor stub
	}

	public EntityCalendarCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initCell();
		// TODO Auto-generated constructor stub
	}

	private void initCell() {
		this.setPadding(0, 5, 0, 5);
		this.setGravity(Gravity.CENTER_HORIZONTAL);
		this.setLayoutParams(new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));

	}

	public void setDate(Calendar c) {
		this.day.setTime(c.getTime());
		this.day.set(Calendar.HOUR_OF_DAY, 0);
		this.day.set(Calendar.MINUTE, 0);
		this.day.set(Calendar.SECOND, 0);
		this.day.set(Calendar.MILLISECOND, 0);
	}

	public void draw(Canvas canvas) {
		// this.setTextColor(this.textColor);
		// this.setBackgroundColor(this.bgrColor);
		super.draw(canvas);
	}

}


class CalendarRedCell extends EntityCalendarCell {
	public CalendarRedCell(Context context, Calendar c) {
		super(context);
		this.setTextColor(Color.RED);
		this.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
		this.setDate(c);
	}
}

class CalendarBlackCell extends EntityCalendarCell {
	public CalendarBlackCell(Context context, Calendar c) {
		super(context);
		this.setTextColor(Color.BLACK);
		this.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
		this.setDate(c);
	}
}

class CalendarGrayCell extends EntityCalendarCell {
	public CalendarGrayCell(Context context, Calendar c) {
		super(context);
		this.setTextColor(Color.LTGRAY);
		this.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
		this.setDate(c);
	}
}

class CalendarDarkGrayCell extends EntityCalendarCell {
	public CalendarDarkGrayCell(Context context, Calendar c) {
		super(context);
		this.setTextColor(Color.GRAY);
		this.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
		this.setDate(c);
	}
}