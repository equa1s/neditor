package com.java.texteditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FindDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;
	private Display display;


	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public FindDialog(Shell parent, int style) {
		super(parent, style);
		setText("Find");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		display = getParent().getDisplay();
		Point pt = display.getCursorLocation();
		shell.setLocation(pt.x, pt.y);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(405, 200);
		shell.setText(getText());
		shell.setLayout(null);
		shell.setLocation(100, 100);
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(10, 10, 264, 26);
		
		Button btnFind = new Button(shell, SWT.NONE);
		btnFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				find();
			}
		});
		
		btnFind.setBounds(298, 8, 90, 30);
		btnFind.setText("Find");

		
		Group grpDirection = new Group(shell, SWT.NONE);
		grpDirection.setText("Direction");
		grpDirection.setBounds(10, 67, 186, 87);
		
		Button btnUp = new Button(grpDirection, SWT.RADIO);
		btnUp.setBounds(10, 42, 51, 20);
		btnUp.setText("Up");
		
		Button btnDown = new Button(grpDirection, SWT.RADIO);
		btnDown.setBounds(100, 42, 69, 20);
		btnDown.setText("Down");

	}
	
    /**
     *  Find
     */
    public void find() {
    	
    	int count = 0;
    	String keyword = text.getText().toLowerCase();
    	String gotText = TextEditor.textField.getText().toLowerCase();
    	String pattern = "[,;:.!?\\s]+";
    	String[] list = gotText.split(pattern);
		for(String i : list) {
			if(i.equals(keyword)) {
				count++;
			}	
		}
	System.out.println("All: "+count);
    	
    }
}
