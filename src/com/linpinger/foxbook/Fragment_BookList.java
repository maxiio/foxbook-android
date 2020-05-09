package com.linpinger.foxbook;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linpinger.misc.BackHandledFragment;
import com.linpinger.novel.Action_UpdateNovel;
import com.linpinger.novel.Action_UpdateNovel.OnStatuChangeListener;
import com.linpinger.novel.NV;
import com.linpinger.novel.NovelManager;
import com.linpinger.tool.ToolAndroid;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class Fragment_BookList extends BackHandledFragment {

	private float clickX = 0 ; // 用来确定点击横坐标以实现不同LV不同区域点击效果
	private NovelManager nm ;
	File wDir ;			// 工作目录
	File cookiesFile ;	// 保存有cookie的文件名: FoxBook.cookie

	// 设置: 
	SharedPreferences settings;
	private String beforeSwitchShelf = "orderby_count_desc" ; // 和 arrays.xml中的beforeSwitchShelf_Values 对应
	private boolean isUpdateBlankPagesFirst = true; // 更新前先检测是否有空白章节
	private boolean isCompareShelf = true ;		// 更新前比较书架

	ListView lv;
	TextView tv;
	List<Map<String, Object>> data;

	private static Handler handler;
	private final int DO_SETTITLE = 1;
	private final int DO_REFRESHLIST = 3;
	private final int DO_REFRESH_TIP = 4;
	private final int IS_NEWVER = 5;
	private final int DO_REFRESH_SETTITLE = 6 ;
//	private final int DO_UPDATEFINISH = 7;

	private boolean switchShelfLock = false;
//	private long mExitTime;

	Action_UpdateNovel aun = new Action_UpdateNovel();

	public static Fragment_BookList newInstance(String inArg) {
		Fragment_BookList fc = new Fragment_BookList();
		Bundle bd = new Bundle();
		bd.putString("ebookPath", inArg);
		fc.setArguments(bd);
		return fc;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ctx = container.getContext();
		View v = inflater.inflate(R.layout.fragment_booklist, container, false); // 这个false很重要，不然会崩溃

		settings = PreferenceManager.getDefaultSharedPreferences(ctx);

		lv = (ListView)v.findViewById(R.id.testLV); // 获取LV
		tv = (TextView)v.findViewById(R.id.testTV);
		btnOther = (Button)v.findViewById(R.id.btnOther);

		if ( ! ToolAndroid.isEink() ) {
			lv.getRootView().setBackgroundColor(Color.parseColor("#EEFAEE"));
			//lv.setBackgroundColor(Color.parseColor("#EEFAEE"));
		}
		init_LV_Touch() ;
		init_ToolBar_Button_LongClick(v);

		initQuickButton(v);

// GUI 布局显示完毕
//		mExitTime = System.currentTimeMillis(); // 当前时间，便于两次退出
		this.wDir = ToolAndroid.getDefaultDir(settings);
		this.cookiesFile = new File(wDir, "FoxBook.cookie");

		File inShelfFile; // 传入的路径(db3/fml文件)
		if ( null == getArguments() ) { // 木有传入文件
			inShelfFile = new File(this.wDir, "FoxBook.fml");
			if ( ! inShelfFile.exists() ) {
				inShelfFile = new File(this.wDir, "FoxBook.db3");
				if ( ! inShelfFile.exists() )
					inShelfFile = new File(this.wDir, "FoxBook.fml");
			}
		 } else {
			inShelfFile = new File( getArguments().getString("ebookPath", "fox.fml") );
		}

		this.nm = new NovelManager(inShelfFile);
		foxtipL( inShelfFile.getName() + " : " + nm.getBookCount() );

		init_handler(); // 初始化一个handler 用于处理后台线程的消息
		refresh_BookList();

		aun.setOnStatuChangeListener(new OnStatuChangeListener(){
			@Override
			public void OnStatuChange(int threadIDX, String msgOBJ) {
				if ( threadIDX >= 0 ) { // 多线程更新FMLs
					Message msg = Message.obtain();
					msg.what = DO_SETTITLE;
					msg.obj = msgOBJ;
					handler.sendMessage(msg);
				} else { // 刷新LV
					handler.sendEmptyMessage(DO_REFRESHLIST); // 更新完毕，通知刷新
				}
			}
		});

		if ( 0 == data.size() ) { // 没有书，跳转到搜索页面
			foxtip("貌似没有书哦，那偶自己打开搜索好了");
			startFragment( Fragment_SearchBook.newInstance(nm).setOnFinishListener(oflsn) );
		}

		return v;
	} // onCreate end

	void showListLess1KPages() {
		int xCount = nm.getLess1KCount();
		if ( xCount == 0 ) {
			foxtip("木有字数少于1K的章节");
		} else {
			startFragment( Fragment_PageList.newInstance(nm, AC.aListLess1KPages).setOnFinishListener(oflsn) );
			foxtip("有 " + xCount + " 章节短于1K");
		}
	}

	void init_LV_Touch() {
		lv.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent arg1) {
				clickX = arg1.getX(); // 点击横坐标
				return false;
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position, long id) {
				Map<String, Object> book = (HashMap<String, Object>) av.getItemAtPosition(position);
				foxtipL(book.get(NV.BookName)
					+ " : " + book.get(NV.BookAuthor)
//					+ " @ " + book.get(NV.QDID)
//					+ " = " + book.get(NV.PagesCount)
					+ "\n" + book.get(NV.BookURL));

				if ( settings.getBoolean("isClickItemRightAct", true) ) {
					if ( clickX > lv.getWidth() * 0.8 ) { // 右边1/5处弹出菜单
						lvItemLongClickDialog(book);
						return ;
					}
				}

				if ( (Integer)book.get(NV.PagesCount) > 0) { // 章节数大于0
					startFragment( Fragment_PageList.newInstance(nm, AC.aListBookPages, (Integer)book.get(NV.BookIDX)).setOnFinishListener(oflsn) );
				}
			}
		}); // LV item click end
		// 初始化 长击 条目 的行为
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> av, View view, int position, long id) {
				@SuppressWarnings("unchecked")
				final Map<String, Object> book = (HashMap<String, Object>) av.getItemAtPosition(position);
				foxtipL(book.get(NV.BookName) + " : " + book.get(NV.PagesCount));
				lvItemLongClickDialog(book);
				return true;
			}
		}); // long click end
	}

	void init_ToolBar_Button_LongClick( View v ) {
		onViewClickListener cl = new onViewClickListener();
		tv.setOnClickListener(cl);
		v.findViewById(R.id.btnSeeAll).setOnClickListener(cl);
		v.findViewById(R.id.btnSwitch).setOnClickListener(cl);
		btnOther.setOnClickListener(cl);

		tv.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				isSaveBeforeExit = false;
				onDestroy();
				return true;
			}
		});
		v.findViewById(R.id.btnSeeAll).setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				showListLess1KPages();
				return true;
			}
		});
		v.findViewById(R.id.btnSwitch).setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				refresh_BookList(); // 刷新LV中的数据
				foxtip("ListView已刷新");
				return true;
			}
		});
		btnOther.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				startFragment( Fragment_SearchBook.newInstance(nm).setOnFinishListener(oflsn) );
				return true;
			}
		});
	}

	private class onViewClickListener implements View.OnClickListener { // 单击
		@Override
		public void onClick(View v) {
			switch ( v.getId() ) {
			case R.id.testTV:
				onDestroy();
				break;
			case R.id.btnOther:
				createPopupMenu();
				foxtipL("其他菜单或按钮");
				break;
			case R.id.btnSeeAll: // 所有章节
				if ( nm.getPageCount() == 0 ) {
					foxtip("一章都木有哟");
				} else {
					startFragment( Fragment_PageList.newInstance(nm, AC.aListAllPages).setOnFinishListener(oflsn) );
				}
				break;
			case R.id.btnSwitch:
				foxtipL("切换书架");
				if ( switchShelfLock ) {
					foxtip("还在切换中...");
				} else {
					(new Thread(){
						public void run(){
							switchShelfLock = true;
							beforeSwitchShelf = settings.getString("beforeSwitchShelf", beforeSwitchShelf);
							if ( ! beforeSwitchShelf.equalsIgnoreCase("none") ) { // 切换前先排序
								if ( beforeSwitchShelf.equalsIgnoreCase("orderby_count_desc") )
									nm.sortBooks(true);
								if ( beforeSwitchShelf.equalsIgnoreCase("orderby_count_asc") )
									nm.sortBooks(false);
								nm.simplifyAllDelList();
							}
							String nowPath = nm.switchShelf(true).getName();
							switchShelfLock = false;
							Message msg = Message.obtain();
							msg.what = DO_REFRESH_SETTITLE;
							msg.obj = nowPath + " : " + nm.getBookCount();
							handler.sendMessage(msg);
						}
					}).start();
				}
				break;
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	void createPopupMenu() { // 弹出菜单 btnOther
		PopupMenu popW = new PopupMenu(ctx, btnOther);
		Menu m = popW.getMenu();

		m.add("设置");
		m.add("更新本软件");
		m.add("全部转为TXT");
		m.add("全部转为EPUB");
		m.add("刷新列表");
		m.add("显示字数少于1K的章节");
		m.add("不保存退出");
		m.add("按页数顺序排列");
		m.add("按页数倒序排列");
		m.add("搜索/添加小说");

		popW.show();

		popW.setOnMenuItemClickListener(new OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem mi) {
				String mt = mi.getTitle().toString();
				if ( mt.equalsIgnoreCase("设置") ) {
					startFragment( new Fragment_Setting() );
				} else if ( mt.equalsIgnoreCase("刷新列表") ) {
					refresh_BookList(); // 刷新LV中的数据
					foxtip("ListView已刷新");
				} else if ( mt.equalsIgnoreCase("搜索/添加小说") ) {
					startFragment( Fragment_SearchBook.newInstance(nm).setOnFinishListener(oflsn) );
				} else if ( mt.equalsIgnoreCase("显示字数少于1K的章节") ) {
					showListLess1KPages();
				} else if ( mt.equalsIgnoreCase("更新本软件") ) {
					foxtipL("开始更新版本...");
					(new Thread(){
						public void run(){
							int newver = new FoxUpdatePkg(ctx).FoxCheckUpdate() ;
							Message msg = Message.obtain();
							if ( newver > 0 ) {
								msg.what = IS_NEWVER;
								msg.obj = newver + ":新版本" ;
							} else {
								msg.what = DO_SETTITLE;
								msg.obj = "无新版本" ;
							}
							handler.sendMessage(msg);
						}
					}).start();
				} else if ( mt.equalsIgnoreCase("不保存退出") ) {
					isSaveBeforeExit = false;
					onDestroy();
				} else if ( mt.equalsIgnoreCase("按页数倒序排列") ) {
					foxtipL("倒序排序");
					(new Thread(){ public void run(){
						nm.sortBooks(true);
						nm.simplifyAllDelList();
						Message msg = Message.obtain();
						msg.what = DO_REFRESH_TIP;
						msg.obj = "已按页面页数倒序重排好书籍";
						handler.sendMessage(msg);
					} }).start();
				} else if ( mt.equalsIgnoreCase("按页数顺序排列") ) {
					foxtipL("顺序排序");
					(new Thread(){ public void run(){
							nm.sortBooks(false);
							nm.simplifyAllDelList();
							Message msg = Message.obtain();
							msg.what = DO_REFRESH_TIP;
							msg.obj = "已按页面页数顺序重排好书籍";
							handler.sendMessage(msg);
						} }).start();
				} else if ( mt.equalsIgnoreCase("全部转为EPUB") ) {
					foxtipL("开始转换成EPUB...");
					(new Thread(){
						public void run(){
							File oFile = new File(wDir, "fox.epub");
							nm.exportAsEpub(oFile);
							Message msg = Message.obtain();
							msg.what = DO_SETTITLE;
							msg.obj = "全部转换完毕: " + oFile.getPath();
							handler.sendMessage(msg);
						}
					}).start();
				} else if ( mt.equalsIgnoreCase("全部转为TXT") ) {
					foxtipL("开始转换成TXT...");
					(new Thread(){
						public void run(){
							File oFile = new File(wDir, "fox.txt");
							nm.exportAsTxt(oFile);
							Message msg = Message.obtain();
							msg.what = DO_SETTITLE;
							msg.obj = "全部转换完毕: " + oFile.getPath();
							handler.sendMessage(msg);
						}
					}).start();
				} else {
					foxtipL(mt);
				}
				return true;
			}
		});
	}

	void initQuickButton(View v) {
			// 不要再设置onclick和onLongClick了，会冲突
			v.findViewById(R.id.btnRefreshQuick).setOnTouchListener(new OnTouchListener(){
				FrameLayout.LayoutParams lp;
				int startX;
				int startY;
				int lastX;
				int lastY;
				long sTime ;
				long eTime ;
	
				@TargetApi(Build.VERSION_CODES.JELLY_BEAN) @Override
				public boolean onTouch(View v, MotionEvent event) {
					lp = (FrameLayout.LayoutParams) v.getLayoutParams();
					int x = (int) event.getRawX();
					int y = (int) event.getRawY();
	
					switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						sTime = System.currentTimeMillis();
						startX = x;
						startY = y;
						lastX = x;
						lastY = y;
						v.getBackground().setAlpha(99);
						// v.getBackground().setColorFilter(new ColorMatrixColorFilter(BUTTON_PRESSED));
						break;
					case MotionEvent.ACTION_MOVE:
						int offX = x - lastX; //计算移动的距离
						int offY = y - lastY;
						lp.rightMargin = lp.rightMargin - offX ;
						lp.bottomMargin = lp.bottomMargin - offY ;
						v.setLayoutParams(lp);
						lastX = x;
						lastY = y;
						break;
					case MotionEvent.ACTION_UP:
						v.getBackground().setAlpha(255);
						//v.getBackground().setColorFilter(new ColorMatrixColorFilter(BUTTON_RELEASED));
						int ofX = lastX - startX;
						int ofY = lastY - startY;
						if ( ( ( ofX >= 0 && ofX < 10 ) || ( ofX <= 0 && ofX > -10 ) ) && ( ( ofY >= 0 && ofY < 10 ) || ( ofY <= 0 && ofY > -10 ) ) ) { // 非移动
							eTime = System.currentTimeMillis() - sTime;
							if ( eTime > 3000L ) { // 长按 3s
								v.playSoundEffect(0);
								foxtipL("长按3s了");
							} else if ( eTime > 300L ) { // 长按 300ms / 500ms
								v.playSoundEffect(0);
								//foxtipL("已长按更新按钮,操作放在这里");
								createPopupMenu();
							} else { // 单击
								v.playSoundEffect(0);
								foxtipL("更新所有");
								isUpdateBlankPagesFirst = settings.getBoolean("isUpdateBlankPagesFirst", isUpdateBlankPagesFirst);
								isCompareShelf = settings.getBoolean("isCompareShelf", isCompareShelf); // 更新前比较书架
								new Thread(aun.new UpdateAllBook(nm, cookiesFile, isUpdateBlankPagesFirst, isCompareShelf)).start();
							}
						}
						break;
					}
					return true;
				}
			});
	//		private final static float[] BUTTON_PRESSED = new float[] {      
	//		2.0f, 0, 0, 0, -50,      
	//		0, 2.0f, 0, 0, -50,      
	//		0, 0, 2.0f, 0, -50,      
	//		0, 0, 0, 5, 0 };
	//	private final static float[] BUTTON_RELEASED = new float[] {      
	//		1, 0, 0, 0, 0,      
	//		0, 1, 0, 0, 0,      
	//		0, 0, 1, 0, 0,      
	//		0, 0, 0, 1, 0 };
		} // initQuickButton end

	private void lvItemLongClickDialog(final Map<String, Object> book) { // 长击LV条目弹出的对话框
		final String lcURL = book.get(NV.BookURL).toString();
		final String lcName = book.get(NV.BookName).toString();
		final int bookIDX = (Integer)book.get(NV.BookIDX);

		new AlertDialog.Builder(ctx) //.setIcon(R.drawable.ic_launcher);
		.setTitle("操作:" + lcName)
		.setItems(new String[] { "更新本书",
				"更新本书目录",
				"在线查看",
				"搜索:bing",
				"复制书名",
				"编辑本书信息",
				"删除本书" },
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: // 更新本书
					new Thread(aun.new UpdateBook(nm, bookIDX, lcURL, lcName, true)).start();
					foxtip("正在更新: " + lcName);
					break;
				case 1: // 更新本书目录
					new Thread(aun.new UpdateBook(nm, bookIDX, lcURL, lcName, false)).start();
					foxtip("正在更新目录: " + lcName);
					break;
				case 2: // 在线查看
					startFragment( Fragment_PageList.newInstance(nm, AC.aListSitePages, bookIDX).setOnFinishListener(oflsn) );
					break;
				case 3: // 搜索:bing
//					startFragment( Fragment_QuickSearch.newInstance(nm, AC.SE_BING, lcName).setOnFinishListener(oflsn) );
					ToolAndroid.setClipText(lcName, ctx);
					foxtipL("搜索: " + lcName);
					startFragment( Fragment_SearchBook.newInstance(nm).setOnFinishListener(oflsn) );
					break;
				case 4: // 复制书名
					ToolAndroid.setClipText(lcName, ctx);
					foxtip("已复制到剪贴板: " + lcName);
					break;
				case 5: // 编辑本书信息
					startFragment( Fragment_BookInfo.newInstance(nm, bookIDX).setOnFinishListener(oflsn) );
					break;
				case 6: // 删除本书
					nm.deleteBook(bookIDX);
					refresh_BookList();
					foxtip("已删除: " + lcName);
					break;
				}
			}
		})
		.create().show();
	}

	private void refresh_BookList() { // 刷新LV中的数据
		data = nm.getBookList(); // 获取书籍列表
		lv.setAdapter(new SimpleAdapter(ctx, data, R.layout.lv_item_booklist
			, new String[] { NV.BookName, NV.PagesCount }
			, new int[] { R.id.tvName, R.id.tvCount } )); // 设置listview的Adapter, 当data是原data时才能 adapter.notifyDataSetChanged();
	}

	private void init_handler() { // 初始化一个handler 用于处理后台线程的消息
		handler = new Handler(new Handler.Callback() {
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case DO_SETTITLE:
					foxtipL((String)msg.obj);
					break;
				case DO_REFRESHLIST:
					refresh_BookList();
					break;
				case DO_REFRESH_TIP :
					refresh_BookList();
					foxtip((String) msg.obj);
					break;
				case DO_REFRESH_SETTITLE :
					refresh_BookList();
					foxtipL((String) msg.obj);
					break;
				case IS_NEWVER:
					foxtipL((String)msg.obj);
					try {
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setDataAndType(Uri.fromFile(new File("/sdcard/FoxBook.apk")), "application/vnd.android.package-archive"); 
						startActivity(i);
					} catch(Exception e) {
						e.toString();
					}
					break;
				}
				return false;
			}
		});
	}

	OnFinishListener oflsn = new OnFinishListener(){
		@Override
		public void OnFinish() {
			refresh_BookList(); // 刷新LV中的数据
		}
	};

	@Override
	public void onDestroy() {
		if ( isSaveBeforeExit ) {
			if ( ! settings.getBoolean("isSaveAsFML", true) )
				nm.setSaveFormat(NovelManager.SQLITE3);
			nm.close();
		}
		super.onDestroy();
		System.exit(0);
	}

	private void foxtip(String sinfo) { // Toast消息
		Toast.makeText(ctx, sinfo, Toast.LENGTH_SHORT).show();
	}

	private void foxtipL(String sinfo) {
		tv.setText(sinfo);
	}

	private Context ctx;
	private Button btnOther;
	private boolean isSaveBeforeExit = true ;

} // class end

