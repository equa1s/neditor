package com.java.texteditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;


public class TextEditor {
	
	private Display display;
	protected Shell shell;
	public static Text textField;
	private Font font;
	private Color color;
	private int RESULT_OK; 
	private BufferedWriter bwriter = null;
	private FileWriter fwriter = null;
	private File newFile = null;
	private String filePath = null;
	private String fileName = null;
	private Monitor primary = null;
    private Rectangle bounds = null;
    private Rectangle rect = null;
    private boolean modified = false;
    private final static Logger LOGGER = Logger.getLogger(TextEditor.class.getName());


	/**
	 * Launch the application
	 */
	public static void main(String[] args) {
			try {
				TextEditor window = new TextEditor();
					   window.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
				
	}

	/**
	 * Run the window
	 */
	public void run() {
		display = Display.getDefault();
			createContents();
			shell.open();
			shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}	
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		    shell = new Shell();
			shell.setImage(SWTResourceManager.getImage("C:\\Eclipse Projects\\TextEditor\\notepad.png"));
			shell.setSize(900, 600);
			shell.setLocation(getWidth(primary, bounds, rect, shell), getHeight(primary, bounds, rect, shell));
			shell.setText("Notepad");
			updateTitle();
			shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Menu menu = new Menu(shell, SWT.BAR);
			shell.setMenuBar(menu);
			shell.addShellListener(new ShellListener() {

				@Override
				public void shellActivated(ShellEvent arg0) {
				}
				@Override
				public void shellClosed(ShellEvent arg0) {
					close();
				}
				@Override
				public void shellDeactivated(ShellEvent arg0) {	
				}
				@Override
				public void shellDeiconified(ShellEvent arg0) {
				}
				@Override
				public void shellIconified(ShellEvent arg0) {
				}
				
			});
			
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
			mntmFile.setText("File");
		
			Menu menu_1 = new Menu(mntmFile);
				mntmFile.setMenu(menu_1);
		
			MenuItem mntmNew = new MenuItem(menu_1, SWT.PUSH);
				mntmNew.setText("New\tCtrl+N");
				mntmNew.setAccelerator(SWT.CTRL + 'N');
				mntmNew.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
						newTxt();
					}
					
				});
		
			MenuItem mntmOpen = new MenuItem(menu_1, SWT.PUSH);
				mntmOpen.setText("Open...\tCtrl+O");
				mntmOpen.setAccelerator(SWT.CTRL + 'O');
				mntmOpen.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
							try {
								open();
							} catch (IOException e) {
								e.printStackTrace();
							}
					}
					
				});
		
			MenuItem mntmSave = new MenuItem(menu_1, SWT.SAVE);
				mntmSave.setText("Save\tCtrl+S");
				mntmSave.setAccelerator(SWT.CTRL + 'S');
				mntmSave.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						try {
							save();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}	
			});
				
				 
				
		
			MenuItem mntmSaveAs = new MenuItem(menu_1, SWT.SAVE);
					 mntmSaveAs.setText("Save as...");
					
					 mntmSaveAs.addSelectionListener(new SelectionAdapter() {

						 @Override
						 public void widgetSelected(SelectionEvent arg0) {
							 try {
								saveAs();
							} catch (IOException e) {
								e.printStackTrace();
							}
						 }
					
					 });
					 
					
		
				new MenuItem(menu_1, SWT.SEPARATOR);
		
			MenuItem mntmExit = new MenuItem(menu_1, SWT.PUSH);
				mntmExit.setText("Exit");
				
				mntmExit.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
						close();
						shell.dispose();
					}

				});
		
		// EDIT BAR
		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
			mntmEdit.setText("Edit");
		
			Menu menu_2 = new Menu(mntmEdit);
				mntmEdit.setMenu(menu_2);
		
			// Crtl + Z 
			MenuItem mntmUndo = new MenuItem(menu_2, SWT.PUSH);
				mntmUndo.setText("Undo\tCtrl+Z");
				mntmUndo.addListener(SWT.Selection, new Listener() {

					@Override
					public void handleEvent(Event arg0) {
					}
			
				});
				
		
				//Edit menu separator
				new MenuItem(menu_2, SWT.SEPARATOR);
		
		// Ctrl + X
		MenuItem mntmCut = new MenuItem(menu_2, SWT.PUSH);
		mntmCut.setText("Cut\tCtrl+X");
		mntmCut.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				textField.cut();
			}
			
		});
		mntmCut.setAccelerator(SWT.CTRL + 'X');
		
		// Ctrl + C
		MenuItem mntmCopy = new MenuItem(menu_2, SWT.PUSH);
		mntmCopy.setText("Copy\tCtrl+C");
		mntmCopy.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				textField.copy();
			}

		});
		mntmCopy.setAccelerator(SWT.CTRL + 'C');
		
		// Ctrl + V
		MenuItem mntmPaste = new MenuItem(menu_2, SWT.NONE);
		mntmPaste.setText("Paste\tCtrl+V");
		mntmPaste.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				textField.paste();	
			}
			
		});
		mntmPaste.setAccelerator(SWT.CTRL + 'V');
		
		// Del Delete selected text
		MenuItem mntmDelete = new MenuItem(menu_2, SWT.PUSH);
				 mntmDelete.setText("Delete\tDel");
				 
				 mntmDelete.addListener(SWT.Selection, new Listener(){

					 @Override
					 public void handleEvent(Event arg0) {
						 textField.setText(textField.getText().replace(textField.getSelectionText(),""));
					 }
			
				 });
				 
				 mntmDelete.setAccelerator(SWT.DEL);
		
		new MenuItem(menu_2, SWT.SEPARATOR);
		
		//Find
		MenuItem mntmFind = new MenuItem(menu_2, SWT.PUSH);
		mntmFind.setText("Find...\tCtrl+F");
		mntmFind.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				FindDialog fd = new FindDialog(shell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
					fd.open();
					// FIXME Need add highlight text fields
			}
			
		});
		mntmFind.setAccelerator(SWT.CTRL + 'F');
		
		
		//Replace
		MenuItem mntmReplace = new MenuItem(menu_2, SWT.NONE);
		mntmReplace.setText("Replace...");
			mntmReplace.addListener(SWT.Selection, new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					//FIXME Write replace method
				}
				
			});	
			
		new MenuItem(menu_2, SWT.SEPARATOR);
		
		MenuItem mntmSelectAll = new MenuItem(menu_2, SWT.PUSH);
				 mntmSelectAll.setText("Select All\tCtrl+A");
		
				 mntmSelectAll.addListener(SWT.Selection, new Listener() {
					 
					 @Override
					 public void handleEvent(Event e) {
						 textField.selectAll();
					 }
				 });
				 
				 mntmSelectAll.setAccelerator(SWT.CTRL + 'A');
		
		// F5 Date & Time
		MenuItem mntmTimedate = new MenuItem(menu_2, SWT.PUSH);
			     mntmTimedate.setText("Time/Date\tF5");
			     
			     mntmTimedate.addListener(SWT.Selection, new Listener() {

			    	 @Override
			    	 public void handleEvent(Event arg0) {
			    		 textField.append(new Date().toString());
			    	 }
			
			     });
			     
			     mntmTimedate.setAccelerator(SWT.F5);
		
		MenuItem mntmView = new MenuItem(menu, SWT.CASCADE);
				mntmView.setText("View");
		Menu menu_4 = new Menu(mntmView);
				mntmView.setMenu(menu_4);
				
		
		// Set font for styled text field
		MenuItem mntmFont = new MenuItem(menu_4, SWT.PUSH);
				 mntmFont.setText("Font...");
				 
				 mntmFont.addSelectionListener(new SelectionAdapter() {
				
					 @Override
					 public void widgetSelected(SelectionEvent arg0) {
						setFont();
					 }
				 });

				
		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
				 mntmHelp.setText("Help");
		Menu menu_3 = new Menu(mntmHelp);
				 mntmHelp.setMenu(menu_3);
		
		MenuItem mntmAbout = new MenuItem(menu_3, SWT.NONE);
				 mntmAbout.addSelectionListener(new SelectionAdapter() {
					 @Override
					 public void widgetSelected(SelectionEvent arg0) {
						about()	;
					 }
				 });
				 mntmAbout.setText("About");
				 
		
		textField = new Text(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP );
			textField.setFont(new Font(shell.getDisplay(), "Calibri", 12, SWT.NORMAL));
			textField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					setModified(true);
				}
			});
		
	}
		///////////////////////////////////////////////////////////////////////////////////
	
	/**	
	 * Print error message
	 */
	public void errMessage(String s) {
    	MessageBox error = new MessageBox(shell, SWT.ICON_ERROR | SWT.YES | SWT.NO);
    		error.setMessage(filePath+": "+s);
    		error.setText("Confirm Save As");
    		RESULT_OK = error.open(); 		
    		LOGGER.warning("Error: > " + filePath);
	}
	
	/**
	 * Set font for text area 
	 **/
	public void setFont() {
		
		FontDialog	fontDialog = new FontDialog(shell);
		
		if (font != null) {
			fontDialog.setFontList(textField.getFont().getFontData());
		}
        if (color != null) {
        	fontDialog.setRGB(color.getRGB());
        }

        if (fontDialog.open() != null) {
          // Dispose of any fonts or colors we have created
          if (font != null) {
        	  font.dispose();
          }
          if (color != null) {
        	  color.dispose();
          }
          
          // Create the new font and set it into the label
          font = new Font(shell.getDisplay(), fontDialog.getFontList());
          textField.setFont(font);

          // Create the new color and set it
          color = new Color(shell.getDisplay(), fontDialog.getRGB());
          textField.setForeground(color);

        }
	}
        
        /**
         * Get monitor width  
         **/
        public int getWidth(Monitor monitor, Rectangle bounds, Rectangle rect, Shell shell) {
        	monitor = display.getPrimaryMonitor();
    	    bounds = monitor.getBounds();
    	    rect = shell.getBounds();
        	return bounds.x + (bounds.width - rect.width) / 2;
        }
        
        /**
         * Get monitor height 
         **/
        public int getHeight(Monitor monitor, Rectangle bounds, Rectangle rect, Shell shell) {
        	monitor = display.getPrimaryMonitor();
    	    bounds = monitor.getBounds();
    	    rect = shell.getBounds();
        	return bounds.y + (bounds.height - rect.height) / 2;
        }
        
        /**
         * Updates the title bar
         */
        private void updateTitle() {
        	if(filePath == null) {
        		shell.setText("Untitled - Text Editor");
        	} else {
        		shell.setText(filePath + " - Text Editor");
        	}
        	LOGGER.info("Title updated: > " + filePath);
        }
        
        /**
         * Set modified file
         */
        private void setModified(boolean modified) {
    		this.modified = modified;
    		if(modified && !shell.getText().endsWith("*")) {
    			shell.setText(shell.getText() + "*");
    		} else if(!modified && shell.getText().endsWith("*")) {
    			shell.setText(shell.getText().substring(0, shell.getText().length() - 1));
    		}
    	}
        
        /**
         * Close text editor
         */
        private void close() {
    		if(!"".equals(textField.getText()) && isModified()) {
    			MessageBox alert = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
    			alert.setMessage("File is modified. Do you want to save?");
    			int result = alert.open();
    			if(result == SWT.YES) {
    				try {
						save();
					} catch (IOException e) {
						e.printStackTrace();
					}
    			} 
    		}
    		LOGGER.info("close(): > " + filePath);
    		textField.setText("");
    		setModified(false);
    	}
        
        /**
         * Is file the modified? 
         **/
        private boolean isModified() {
        	return this.modified;
        }
        
        /**
         * Save file 
         **/
    	private void save() throws IOException {
    		if(filePath == null) {
    			saveAs();
    		} else {
    			saveFile(filePath, textField.getText());
    		    updateTitle();
    		}
    		LOGGER.info("save(): > " + filePath);
    	}
    	
    	/**
    	 *  Save as file 
    	 */
    	
    	private void saveAs() throws IOException {
    		
			String text = textField.getText();
			FileDialog fd = new FileDialog(shell, SWT.SAVE);
        	String[] filterExt = { "*.txt", "*.*" };
        	String[] filterName = {"*.txt"};
        		fd.setFilterNames(filterName);
        		fd.setFilterExtensions(filterExt);
        		fd.open();
        	fileName = fd.getFileName();
        filePath = fd.getFilterPath() + File.separator + fileName;
        	newFile = new File(filePath);
        	if(filePath.equals("\\")) {
        		filePath = null;
        	}
        	if(!fileName.isEmpty()) {
        		if(!newFile.exists()) {
    	 				newFile.createNewFile();
    	 				fwriter =  new FileWriter(newFile);
    	 				bwriter = new BufferedWriter(fwriter);
    	 				bwriter.write(text);
    	 				LOGGER.info("saveAs(): > " + filePath);
    	 				updateTitle();
    	 				bwriter.close();
    	 				fwriter.close();
    	 			
    	 			setModified(false);
    	 		
        		} else {
    	 		  errMessage("File already exists! Do you want to replace it?");
    	 				if(RESULT_OK == SWT.YES) {
    	 					newFile.delete();
    	 					newFile.createNewFile();
    	 						fwriter =  new FileWriter(newFile);
    	 						bwriter = new BufferedWriter(fwriter);
    	 					bwriter.write(text);
    	 					LOGGER.info("File replaced: > " + filePath);
    	 					setModified(false);
    	 					bwriter.close();
    	 					fwriter.close();
    	 				} else {
    	 					saveFile(filePath, text);
    	 					
    	 				}
        		}
        	}  
    	} 
    	
    /**
     * Open the file
     */
    private void open() throws IOException {
    	if(isModified()) {
    		MessageBox alert = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
			alert.setMessage("File is modified. Do you want to save?");
			int result = alert.open();
			if(result == SWT.YES) {
				try {
					save();
					openFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
		    	openFile();
			}
    	} else {
	    	openFile();
    	}
    }
    
    /**
     * Save changes to current file 
     */
    public void saveFile(String filename, String data) throws IOException {
    	
        File outputFile = new File(filename);
        FileWriter out;
        BufferedWriter br;
		
			out = new FileWriter(outputFile);
	        br = new BufferedWriter(out);
       		br.write(data);
       		setModified(false);
       		br.close();
       		out.close();
       		LOGGER.info("saveFile(): > " + filePath);
    }
    
    /**
     * Create a new .txt 
     */
    public void newTxt() {
    	close();
    	shell.setText("Untitled - Text Editor");
    	filePath = null;
    	LOGGER.info("newTxt(): > " + filePath);
    }
    
    /**
     * About menu 
     */
    public void about() {
    	MessageBox about = new MessageBox(shell, SWT.ICON_INFORMATION);
    			   about.setMessage("Author: Razuvaev E.I.\nText Editor v1.0\n(c) 2014");
    			   about.setText("About");
    			   about.open();
    }
    
    /**
     * Open 
     */
    public void openFile() throws IOException {
    	FileDialog dlg = new FileDialog(shell, SWT.OPEN);
				   dlg.open();
				   fileName = dlg.getFileName();
				   filePath = dlg.getFilterPath() + File.separator + fileName;
		File f = new File(filePath);
			if(f.isFile() && fileName.length() > 0) {
				BufferedReader br;
							   br = new BufferedReader(new FileReader(f));
				StringBuffer buf = new StringBuffer();
				String line = null;
					while((line = br.readLine()) != null) {
						buf.append(line + '\n');
					}
							   br.close();
					textField.setText(buf.toString());
					LOGGER.info("open(): > " + filePath);
					updateTitle();
			}
			
    }
    
    
}