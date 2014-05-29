package fr.mrcraftcod.objects;

/* From http://java.sun.com/docs/books/tutorial/index.html */
/*
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */
import java.awt.event.ItemEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import fr.mrcraftcod.objects.AutoComboBox.Java2sAutoTextField.AutoDocument;

@SuppressWarnings("rawtypes")
public class AutoComboBox extends JComboBox
{
	private static final long serialVersionUID = 2903956019272831092L;

	private class AutoTextFieldEditor extends BasicComboBoxEditor
	{
		private Java2sAutoTextField getAutoTextFieldEditor()
		{
			return (Java2sAutoTextField) editor;
		}

		AutoTextFieldEditor(List list, boolean ac)
		{
			editor = new Java2sAutoTextField(list, AutoComboBox.this, ac);
		}
	}

	class Java2sAutoTextField extends JTextField
	{
		private static final long serialVersionUID = 6729817746756783477L;

		class AutoDocument extends PlainDocument
		{
			private static final long serialVersionUID = 5897591998358263784L;
			private boolean autoCompletion;

			public AutoDocument(boolean ac)
			{
				super();
				autoCompletion = ac;
			}

			public void replace(int i, int j, String s, AttributeSet attributeset) throws BadLocationException
			{
				super.remove(i, j);
				insertString(i, s, attributeset);
			}

			public void insertString(int i, String s, AttributeSet attributeset) throws BadLocationException
			{
				if(!autoCompletion)
				{
					super.insertString(i, s, attributeset);
					return;
				}
				if(s == null || "".equals(s))
					return;
				String s1 = getText(0, i);
				if(s1.equals(""))
				{
					super.insertString(i, s, attributeset);
					return;
				}
				String s2 = getMatch(s1 + s);
				int j = (i + s.length()) - 1;
				if(isStrict && s2 == null)
				{
					s2 = getMatch(s1);
					j--;
				}
				else if(!isStrict && s2 == null)
				{
					super.insertString(i, s, attributeset);
					return;
				}
				if(autoComboBox != null && s2 != null)
					autoComboBox.setSelectedValue(s2);
				super.remove(0, getLength());
				super.insertString(0, s2, attributeset);
				setSelectionStart(j + 1);
				setSelectionEnd(getLength());
			}

			public void remove(int i, int j) throws BadLocationException
			{
				if(!autoCompletion)
				{
					super.remove(i, j);
					return;
				}
				int k = getSelectionStart();
				if(k > 0)
					k--;
				if(getText(0, k).equals(""))
				{
					super.remove(0, getLength());
					return;
				}
				String s = getMatch(getText(0, k));
				if(!isStrict && s == null)
					super.remove(i, j);
				else
				{
					super.remove(0, getLength());
					super.insertString(0, s, null);
				}
				if(autoComboBox != null && s != null)
					autoComboBox.setSelectedValue(s);
				try
				{
					setSelectionStart(k);
					setSelectionEnd(getLength());
				}
				catch(Exception exception)
				{}
			}

			public boolean isAutoCompletion()
			{
				return autoCompletion;
			}

			public void setAutoCompletion(boolean autoCompletion)
			{
				this.autoCompletion = autoCompletion;
			}
		}

		public Java2sAutoTextField(List list, boolean ac)
		{
			isCaseSensitive = false;
			isStrict = false;
			autoComboBox = null;
			if(list == null)
				throw new IllegalArgumentException("values can not be null");
			dataList = list;
			init(ac);
			return;
		}

		Java2sAutoTextField(List list, AutoComboBox b, boolean ac)
		{
			isCaseSensitive = false;
			isStrict = false;
			autoComboBox = null;
			if(list == null)
				throw new IllegalArgumentException("values can not be null");
			dataList = list;
			autoComboBox = b;
			init(ac);
			return;
		}

		private void init(boolean ac)
		{
			setDocument(new AutoDocument(ac));
			if(isStrict && dataList.size() > 0)
				setText(dataList.get(0).toString());
		}

		private String getMatch(String s)
		{
			for(int i = 0; i < dataList.size(); i++)
			{
				String s1 = dataList.get(i).toString();
				if(s1 != null)
				{
					if(!isCaseSensitive && s1.toLowerCase().startsWith(s.toLowerCase()))
						return s1;
					if(isCaseSensitive && s1.startsWith(s))
						return s1;
				}
			}
			return null;
		}

		public void replaceSelection(String s)
		{
			AutoDocument _lb = (AutoDocument) getDocument();
			if(_lb != null)
				try
				{
					int i = Math.min(getCaret().getDot(), getCaret().getMark());
					int j = Math.max(getCaret().getDot(), getCaret().getMark());
					_lb.replace(i, j - i, s, null);
				}
				catch(Exception exception)
				{}
		}

		public boolean isCaseSensitive()
		{
			return isCaseSensitive;
		}

		public void setCaseSensitive(boolean flag)
		{
			isCaseSensitive = flag;
		}

		public boolean isStrict()
		{
			return isStrict;
		}

		public void setStrict(boolean flag)
		{
			isStrict = flag;
		}

		public List getDataList()
		{
			return dataList;
		}

		public void setDataList(List list)
		{
			if(list == null)
				throw new IllegalArgumentException("values can not be null");
			dataList = list;
			return;
		}

		private List dataList;
		private boolean isCaseSensitive;
		private boolean isStrict;
		private AutoComboBox autoComboBox;
	}

	@SuppressWarnings({"unchecked"})
	public AutoComboBox(List list, boolean ac)
	{
		isFired = false;
		autoTextFieldEditor = new AutoTextFieldEditor(list, ac);
		setEditable(true);
		setModel(new DefaultComboBoxModel(list.toArray())
		{
			private static final long serialVersionUID = 4490030922878980301L;

			protected void fireContentsChanged(Object obj, int i, int j)
			{
				if(!isFired)
					super.fireContentsChanged(obj, i, j);
			}

			public void addElement(Object anObject)
			{
				super.addElement(anObject);
				addDataList(anObject);
			}

			public void removeElement(Object anObject)
			{
				super.removeElement(anObject);
				removeDataList(anObject);
			}
		});
		setEditor(autoTextFieldEditor);
	}

	public DefaultComboBoxModel getDefModel()
	{
		return (DefaultComboBoxModel) this.getModel();
	}

	public boolean isCaseSensitive()
	{
		return autoTextFieldEditor.getAutoTextFieldEditor().isCaseSensitive();
	}

	public void setCaseSensitive(boolean flag)
	{
		autoTextFieldEditor.getAutoTextFieldEditor().setCaseSensitive(flag);
	}

	public boolean isStrict()
	{
		return autoTextFieldEditor.getAutoTextFieldEditor().isStrict();
	}

	public void setStrict(boolean flag)
	{
		autoTextFieldEditor.getAutoTextFieldEditor().setStrict(flag);
	}

	public List getDataList()
	{
		return autoTextFieldEditor.getAutoTextFieldEditor().getDataList();
	}

	@SuppressWarnings({"unchecked"})
	public void setDataList(List list)
	{
		autoTextFieldEditor.getAutoTextFieldEditor().setDataList(list);
		setModel(new DefaultComboBoxModel(list.toArray()));
	}

	@SuppressWarnings({"unchecked"})
	public void addDataList(Object o)
	{
		List list = autoTextFieldEditor.getAutoTextFieldEditor().getDataList();
		if(list.contains(o))
			return;
		list.add(o);
		autoTextFieldEditor.getAutoTextFieldEditor().setDataList(list);
		setModel(new DefaultComboBoxModel(list.toArray()));
	}

	@SuppressWarnings({"unchecked"})
	public void removeDataList(Object o)
	{
		List list = autoTextFieldEditor.getAutoTextFieldEditor().getDataList();
		list.remove(o);
		autoTextFieldEditor.getAutoTextFieldEditor().setDataList(list);
		setModel(new DefaultComboBoxModel(list.toArray()));
	}

	void setSelectedValue(Object obj)
	{
		if(isFired)
			return;
		isFired = true;
		setSelectedItem(obj);
		fireItemStateChanged(new ItemEvent(this, 701, selectedItemReminder, 1));
		isFired = false;
		return;
	}

	protected void fireActionEvent()
	{
		if(!isFired)
			super.fireActionEvent();
	}

	public void setAutoCompletion(boolean status)
	{
		((AutoDocument) autoTextFieldEditor.getAutoTextFieldEditor().getDocument()).setAutoCompletion(status);
	}

	private AutoTextFieldEditor autoTextFieldEditor;
	private boolean isFired;
}