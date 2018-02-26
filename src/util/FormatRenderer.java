package util;

import java.text.Format;
import java.text.DateFormat;
import javax.swing.table.DefaultTableCellRenderer;

public class FormatRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 9079104672214588271L;
	private Format formatter;

	public FormatRenderer(Format formatter)
	{
		this.formatter = formatter;
	}

	public void setValue(Object value)
	{
		try
		{
			if (value != null)
				value = formatter.format(value);
		}
		catch(IllegalArgumentException e) {}

		super.setValue(value);
	}

	public static FormatRenderer getDateTimeRenderer()
	{
		return new FormatRenderer( DateFormat.getDateTimeInstance() );
	}

	public static FormatRenderer getTimeRenderer()
	{
		return new FormatRenderer( DateFormat.getTimeInstance() );
	}
}