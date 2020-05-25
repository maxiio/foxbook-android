# FoxBook(狐狸的小说下载阅读及转换工具) Android版

**名称:** FoxBook

**功能:** 狐狸的小说下载阅读及转换工具(下载小说站小说，制作为umd,txt格式)

**作者:** 爱尔兰之狐(linpinger)

**邮箱:** <mailto:linpinger@gmail.com>

**主页:** <http://linpinger.github.io?s=FoxBook-Android_MD>

**缘起:** 用别人写的工具，总感觉不能随心所欲，于是自己写个下载管理工具，基本能用，基本满意

**原理:** 下载网页，分析网页，文本保存在数据库中，转换为其他需要的格式

**亮点:** 通用小说网站规则能覆盖大部分文字站的目录及正文内容分析，不需要针对每个网站的规则

**源码及下载:**

-   [源码工程](https://github.com.linpinger.foxbook-android)

-   [APK文件下载点1:sf.net](http://master.dl.sourceforge.net/project/foxtestphp/prj/FoxBook.apk)
-   [APK文件下载点2:七牛](http://linpinger.qiniudn.com/prj/FoxBook.apk)

**工程中包含的其他文件:**
- 生成UMD格式电子书调用umd-builder JAVA版 <http://code.google.com/p/umd-builder/>
- 阅读页面的羊皮纸背景来自: iReaderV1.5.0.1.apk
- 程序图标来自: <http://findicons.com/icon/93370/emblem_library?id=274277> Icon Pack: Human-O2 Designer: schollidesign
- HTTP服务器采用 [NanoHttpd](https://github.com/NanoHttpd/nanohttpd)，在文件服务器那里添加了meta文件编码，还有一点小修改

**HTTP服务器的玩法:**
- 要求**版本**在 2016-01-30 及以上
- 在应用中右键菜单，选择启用HTTP服务器，然后该菜单名会显示HTTP服务器地址，例如 192.168.1.100:8888 已开
- 现在可以在桌面，手机，Kindle浏览器中输入地址 http://192.168.1.100:8888
- 看到的页面是小说的网页列表，在这里，可以直接看小说（可以在浏览器中使用中键点击翻页哦，K3/K4测试），也可以点击下载txt到Kindle上直接看，这个功能专为kindle设计
- kindle下可以写个简单的网页，然后用kindlegen将网页转成mobi，这个网页可以将上面的网页地址或你最常访问的网址放在里面，方便使用，当然也可以用浏览器本身的书签功能
- **另外**在地址后面加入L，例如 http://192.168.1.100:8888/L 可以查看手机上 /sdcard/上的内容，可以下载手机上的文件
- **另外**在地址后面加入F，例如 http://192.168.1.100:8888/F 可以将浏览器所在平台上的文件上传到该手机/sdcard/上

**新增加的用法:**
- 2016-8-16: 在阅读页中，内容右上角(范围: 高度三分之一横线上部，宽度三分之二竖线右部)点击可显示/隐藏 动作栏
- 2016-8-24: 在/sdcard/fonts/目录下，可放上 foxfont.ttf，然后在阅读页中使用字体，可在设置中选择新字体路径
- 2016-8-31: 新阅读页，屏幕上三分之一从左到右分为三份，下面为向下翻页，左上为返回，中上为向上翻页，右上为上下文菜单，长按阅读页也可显示上下文菜单

**2018-05-30: 添加 onyx sdk 实现全刷**
- 通过: https://github.com/onyx-intl/public-wiki/blob/master/README.md 发现需要 `com.onyx.android.sdk:onyxsdk-base`
- 搜索得到: https://bintray.com/onyx-intl/maven/onyxsdk-base  ，下载得到: `onyxsdk-base-1.4.3.13.aar`
- 解压，将.jar命名为 `onyxsdk-base-1.4.3.13.jar`
- 将这个.jar包含进工程就可以调用 `EpdController.invalidate(myView, UpdateMode.GC);` 来全刷
- 发现apk中包含.jar，以及.dex太大了，得改进将.jar包含的方式
- 解压.jar到lib文件夹中，然后Eclipse项目的Properties->Java Build Path->Libraries标签下Add Class Folder，然后将Order And Export标签下的lib文件夹选中
- 这次没有.jar了，小了一些，但是还是太大，因为感觉这个class用得比较少，应该可以精简
- 搜索`class 依赖分析` 找到: `Class Dependency Analyzer (CDA) 2.2.0` http://www.dependency-analyzer.org/
- 按照它的操作，分析jar包，找到EpdController这个类依赖的所有类，然后将其他删除，就得到最小的依赖，果然编译通过，运行正常，整个APK包大了40K左右，可以接受
- 如果能获取它的源码包，理论上应该还可以减得更小

##2018-09-03: 作死版##
- 这个版本将所有activity更换为fragment，只留个入口activity
- 切换会更快，然而不可避免的会有大量bug产生，所以不放到升级里面去，要测试的在release标签里面下载

**更新日志:**
- 2020-05-25: 修改: 同步代码，阅读页添加鼠标滚轮支持
- 2020-05-17: 修改: 同步代码，html检测
- 2020-05-11: 修改: 同步代码，更新方式修改
- 2020-05-09: 修改: 同步代码
- 2020-04-29: 添加: 书籍信息界面加入搜索几个站点的菜单
- 2020-04-27: 修改: 书架
- 2020-04-24: 添加: ymxxs书架
- 2020-04-22: 修改: 同步代码，书架更新
- 2020-04-01: 添加: 书籍信息添加导入阅读url菜单
- 2019-11-17: 修改: 同步代码
- 2019-11-12: 添加: meegoq快速搜索
- 2019-10-31: 修改: 同步代码，添加meegoq
- 2019-09-10: 修改: 搜索书籍崩溃的bug
- 2019-09-04: 修改: 比较得到新章节机制从顺序比较 改为 倒序查找
- 2019-02-12: 修改: 完善onBackPressed，精简并同步一些相同的代码，软件升级线路修改，改进搜索，需进一步优化
- 2018-09-03: 测试: activity 基本都换成了 fragment，解决一些修改造成的bug
- 2018-06-24: 修改: 修改为自写界面，不使用ListActivity，不使用Actionbar，删除http及其他部分较少用到的代码
- 2018-06-15: 修改: 分离更新代码，慢慢规范分类
- 2018-06-13: 添加: 搜索页添加菜单：直接将剪贴板中的新增为新书，ebook阅读页，添加菜单复制本书信息到剪贴板，清理缓存移到设置末尾
- 2018-06-03: 添加: 搜索页，搜索按钮单击先检测剪贴板中有没有`FoxBook>`字符串，判断得到书名并搜索，或开启起点排行页，长按按钮复制本fml的site字符串，例如:` site:qidian.com`，便于在搜索引擎中定向搜索
- 2018-05-31: 修改: 反编译.class得到实际全刷代码 RK3026Device.class
- 2018-05-30: 添加: onyx全刷，在翻页的时候
- 2018-05-23: 修改: 阅读页，方便其他调用
- 2018-05-22: 修改: eink长按退出改单击
- 2018-05-21: 修改: 项目文件编码为UTF-8，长按不同位置有惊喜，使用popupWindow替换optionsMenu，使用alertDialog替换contextMenu，避免7.x中出现问题
- 2018-05-13: 添加: 选项: 为e-ink设备刷新，将contextMenu替换为optionsMenu，并长按不弹出菜单以popupWindow替代
- 2018-05-01: 修改: 书架使用rawcookie，部分站点修正为https，添加: 书籍信息复制粘贴全部菜单，便于多fml的书籍复制新书
- 2018-02-07: 添加: 编辑章节，编辑书按钮To菜单，同步代码
- 2018-02-05: 修改: 阅读页添加选项，正文是否添加两中文空格，方便特制fml文件阅读
- 2018-01-16: 修改: 一个导致无法升级的bug，如果无法升级，在这个页面下载apk下载
- 2018-01-15: 同步: epub生成库，修改切换fml方式，加入切换选项
- 2017-10-20: 同步: 生成epub的库
- 2017-10-17: 添加: 书架: wutuxs.com
- 2017-08-07: 修改: 起点epub内容解析问题
- 2017-06-25: 修改: 阅读页菜单调整，便于E-ink设备刷新，啊，不知道半透明菜单怎么弄啊
- 2017-06-13: 修改: 当更新后有小于1K的短章就显示，选择显示短章，如果为0就返回，起点epub内容格式修改
- 2017-06-05: 修改: 起点旧接口停用，修改以适应，现在qidian的Android应用好像开始使用https了，以后会越来越蛋疼
- 2017-05-22: 添加: 13xxs书架
- 2017-05-19: 修改: 提示，当作者信息为空时，表示为新书，不会只更新目录的后55章
- 2017-05-10: 修正: 书架多线程更新目录
- 2017-04-14: 添加: 书架: xqqxs.com xxbiquge.com
- 2017-04-11: 添加: BookInfo的UI中添加作者，清空DelList菜单，当delList长度大于3且作者名不为空才截取目录最后55个条目
- 2017-04-02: 添加: 飘天书架
- 2017-01-19: 修改: 和Java版同步公共代码
- 2017-01-17: 修改: 起点 API，搜索，httpd
- 2017-01-13: 修改: 显示全部章节时，第一章显示书名
- 2017-01-11: 修改: 起点手机接口地址变化
- 2017-01-06: 修改: 将后缀名从xml修改为fml，便于调试
- 2017-01-03: 删除: 多余的bookloc和pageloc，需要的字段以后需要的时候再加
- 2017-01-01: 修复: 在线查看站点为起点手机时无法处理，少于1K删除无效，更新目录无效
- 2016-12-30: 重写: 重写内部数据结构，外部存储格式部件化，便于以后扩充，增强可移植性，现以XML为默认数据存储格式，摆脱Android下蛋疼的SQLite3的API，移除UMD生成，删除旧的阅读页，重复的Activity，还有很多就不一一列出
- ***** 重大版本分割线 *****
- 2016-12-16: 添加: ShowPage4Eink 添加菜单项：显示/隐藏字体设置菜单 commit 3e88df6c398644aad387981d56d3194eae5dc450
- 2016-12-14: 整理: 各函数，放到单独包中
- 2016-12-13: 整理: 一言不合就 重新分类／重命名各函数，添加读取本应用生成的epub
- 2016-12-12: 修改: 导入起点epub时，将pageurl也添加进去，方便更新 合并: 将txt_view 合并进 zip_viewer 并更名为 ebook_viewer
- 2016-12-11: 添加: Activity_Zip_Viewer添加起点epub支持，因起点改版后txt已不提供下载，故改成epub
- 2016-12-09: 添加: 导入导出阅读页配置，方便调试APK
- 2016-12-08: 添加: Activity_Zip_Viewer，处理zip中的 html 文件
- 2016-12-07: 添加: 选项: 当有超长文本行: 字符与换行比大于200时，替换。为。换行符以增加行数，防止处理这种文本时耗时过长而无响应
- 2016-12-05: 修改: 计算txt内容及maxwidth是否和上次相同，若有不同则重新生成lines，也就是说只在第一次生成，避免每次绘制都生成一次，这可是个耗时大户
- 2016-11-13: 修改: HTTP 超时时间减为 5 秒
- 2016-11-07: 修改: DB不是memdb时，关闭db时删除表: android_metadata ，这货是android自己加上去的
- 2016-10-24: 修改: DB关闭及切换方式当不是memdb时
- 2016-10-11: 添加: 所有ListActivity添加按键处理来翻屏，不够完美待完善
- 2016-09-18: 修正: 当章节为空，程序出错退出
- 2016-09-15: 添加: 菜单选择字体文件，设置结构变化
- 2016-09-13: 添加: 返回菜单，当标题太长时截断底部标题，精简菜单
- 2016-09-11: 添加: 选项: 是否加粗正文字体，将所有SwitchPreference 改成 CheckBoxPreference，修改: 字体间距等调整方式
- 2016-09-08: 修正: 导入起点txt功能，新版，存在章节尾两行链接的状况，处理一下
- 2016-09-07: 修改: 导入起点txt功能，分为新旧版，新版更快，旧版适应性更好
- 2016-09-04: 修改: 更新空白章节改名为填空，pagelist中检查data.size，为0就返回
  - 修正: 导入非起点txt未创建Book信息，导致在新阅读页中崩溃
  - 修正: 导入起点txt时，如果存在同名的db3，重命名之
  - 修正: 阅读页接收时间变动广播，发现阅读器竟然木有反应，手机倒是能正常收到时间变化广播，蛋疼的阅读器
  - 添加: 底部加入电量广播接收，看能否检测电量
- 2016-09-03: 修改: startActivityForResult 检查，返回键使用覆盖onBackPressed，不使用onkeydown
- 2016-09-02: 添加: 首页信息里加入进度
- 2016-09-01: 添加: 选项: 更新所有前先检查是否有空白页，有则更新
- 2016-08-31: 修改: 新阅读页默认使用无ActionBar主题，去除相关菜单项
- 2016-08-30: 修改: 将背景设置放到Activity中，节省ondraw时间，优化绘制时间，分离View，默认使用新View，添加选项:右上区域是返回功能还是显隐ActionBar
- 2016-08-29: 修改: 默认背景图片的绘制方式，原方式会绘制两次，bug修复，删除文本尾部空行以减小页数
- 2016-08-28: 修改: 底部字体大小及位置，上翻到上本书的最后一章的最后一页，修复设置背景，Bug修复
- 2016-08-27: 添加: Activity_ShowPage4Eink，和ShowPage功能一样，只是使用自定义View，专为E-ink设备设计，通过选项来控制跳转到旧的还是新的，以后如果功能有更新，可能需要更新两份
- 2016-08-26: 添加: 设置项: ActionBar图标点击反应，信息页加入菜单:复制/粘贴各编辑框，调整布局
  - 按钮预及菜单 加 复制网址，下载起点txt
  - 当书名为空，搜索按钮打开起点排行
  - BookList: 显示/隐藏图标，OptionMenu 腾空间
  - AllPageList: 添加返回菜单，E-ink设备的一个bug
- 2016-08-25: 添加: 设置项: 先更新书架，显示隐藏Actionbar，搜索添加起点排行地址，编辑页添加菜单
- 2016-08-24: 添加: 设置项: 切换前排序数据库，选择升级线路，字体选择
- 2016-08-19: 添加: 行距调整
- 2016-08-19: 修正: 一个隐藏的bug:当打开nnn.txt时，如果存在nnn.db3会读入db3里的内容到内存数据库，然后在内存数据库中追加.txt的内容
- 2016-08-18: 修改: E-ink设备，阅读时可设置为一直显示滚动条
- 2016-08-17: 修改: 隐藏/显示动作栏时，在动作栏上显示当前时间，便于E-ink查看当前时间
- 2016-08-16: 修改: 针对E-ink设备修改阅读页: 修改显示字体，全屏，翻页键响应有bug，如果是E-ink设置为android.R.style.Theme_DeviceDefault_Light
- 2016-08-15: 修改: 针对E-ink设备修改阅读页: 背景白色，不平滑滚动（翻页），翻页键响应
- 2016-08-06: 删除: 无效的三个站点: 追书神器，快读，宜搜
- 2016-08-01: 修正: 一个隐藏很久的bug，在更新功能中，计算FoxBook.apk的SHA1值时，如果第一位为0，得到的字符串会少了该位
- 2016-07-29: 修改: 起点txt保存为DB3时，目录页地址换成移动端地址并写入起点ID，方便后期更新公众章节
- 2016-07-09: 修改: 所有章节第一章标题加入★以突出
- 2016-06-08: 修改: txt查看 检测编码方式，可靠性大大增强，仅限GBK和UTF8两种编码
- 2016-06-05: 添加: 查看内容页保存背景设置，更新后提示字数小于1K的章节数
- 2016-06-04: 添加: 菜单:显示所有字数小于1K的章节
- 2016-05-29: 添加: 更新所有先更新书架
- 2016-03-23: 添加: 所有章节添加更新菜单, 修改: 调整菜单顺序，要是能根据使用频率自动调整菜单顺序就好了，YY一下，还是很有可能的嘛
- 2016-03-15: 添加: 背景色绿灰
- 2016-03-13: 
  - 切换后，标题显示数据库名
  - 所有列表: 隐藏跳转尾部按钮; 显示总数; 删除所有后精简列表
  - 章节列表: 隐藏删除并不写入菜单
  - 阅读页: 到头部点1/3屏翻上一章，到尾部点2/3屏翻下一章
- 2016-03-12: 变更: 已删除列表机制修改，不再使用起止，直接取第一行之后的对比
- 2016-03-11: 修正: 备份数据库加入空白字符导致每次切换数据库时增大一个字节的问题，添加: 排序时精简DelURL字段
- 2016-03-09: 整理: Swing-Android部分使用同一文件(文件编码不同):FoxBookLib/FoxEpub/site_*
- 2016-03-08: 添加: 查看起点txt增加转utf8编码txt的菜单项
  - 非起点文本，按大小分段，实际是从读取时一行行的读，比较每行大小
- 2016-02-21: 修改: 切换数据库时加入一个标志位判断切换完毕否，避免多次切换而崩溃
- 2016-02-18: 使用dev分支来迅速添加特性，merge前rebase -i head~n 合并fr多个修改成一个
  - 所有列表添加删除按钮菜单
  - 列表修改删除不记录按钮图标
  - 网络查看章节列表隐藏删除按钮
  - 删除选项是否显示列表所有条目，代码优化
- 2016-02-16: 修改: 删除打开db3的界面，使用主界面打开，各界面排版修整，搜索添加使用说明，各标题栏添加返回按钮
- 2016-02-15: 添加: 阅读页设置菜单:音量键是翻章还是翻屏，添加: 打开db3文件，地址加入F可上传文件到/sdcard/目录
- 2016-01-30: 添加: HTTP服务器(注意不要在URL参数里面乱输入，会让程序崩溃的，么么哒)，根地址后加L列出/sdcard/目录，应该可以加入上传功能，后期再说吧
- 2016-01-29: 修改: 统一常量
- 2016-01-27: 添加: 通过文件管理器调用查看起点txt
- 2016-01-22: 添加: 起点手机站地址支持
- 2016-01-19: 调整: 因追书神器改版后的地址有加密，暂无法获取正文内容，故调整菜单等顺序，淡化该站点，另外搜索界面的默认搜索引擎更换为Bing，原因是搜狗返回的结果不新，另外改了一下提示里面的错字，呵呵呵
- 2016-01-08: 修正: 起点目录处理错误
- 2015-12-24: 修正: 新函数处理起点分卷错误
- 2015-12-23: 修改 tocHref函数，改进目录分析策略，qqxs域名修改成13xs
- 2015-11-17: 修正: 新起点网页地址规则
- 2015-10-27: 修改: User-Agent
- 2015-04-16: 添加:下载过滤qidian的txt地址，默认下载.gz会造成使用cdn，然后出现故障
- 2015-03-13: 修正: 消除阅读页音量键翻页声音，使用str.length替换str.isempty
- 2015-03-09: 修正: 起点内容页尾部多余内容
- 2015-03-02: 添加: 快搜菜单
- 2014-10-16: 修改: 阅读页正文末尾添加标题，便于知道当前阅读进度
- 2014-10-14: 修正: 搜索sogou时显示的多余链接
- 2014-10-10: 修正: 比较新章节时未小写网址可能造成的无新章节状况，例如起点的网址
- 2014-09-25: 调整菜单顺序避免误按
- 2014-09-05: 修改提示文字及菜单显示以适应特殊尺寸的手机，修复当无记录排序导致的程序崩溃，修改所有章节删除时结束本Activity
- 2014-08-29: 修改章节列表界面按钮显示，添加 SD卡数据库和内部存储数据库之间的切换及导入导出(默认使用SD卡上的数据库)
- 2014-08-28: 调整弹出菜单顺序，添加搜索起点，并整理起点相关函数到site_qidian.java中
- 2014-08-25: 修改打开数据库方式，修改为将整个数据库文件读入内存以加快速度(可设置取消)，添加txt转epub功能，如果有android版的kindlegen的话，还可以转为mobi格式，可惜木有
- 2014-07-28: 修改多线程更新所有，使用join等待所有线程完毕
- 2014-07-28: 修改切换数据库功能，默认FoxBook.db3，可切换到SD卡跟目录下的其他db3文件
- 2014-07-23: 修复通用链接提取的一个小问题
- 2014-07-21: 将比较新章节放入函数中，条理清晰些，修改tocHref函数中，过滤调包含javascript:的链接
- 2014-07-09: 添加: 快读 更新及搜索支持(包含downhtml的POST功能添加)，修正在线查看未处理zssq和kuaidu的功能
- 2014-07-03: 添加: DelList中包含 起止=-5,5 功能，和桌面版同步添加该功能
- 2014-06-06: 修正: zssq新增User-Agent验证造成的不能使用，在线查看的bug
- 2014-05-31: 修正: zssq和qidian的顺序可能造成的空指针，以及获取数据库cell内容空指针问题
- 2014-05-30: 修正: zhuishushenqi page url 地址转义造成的地址错误
- 2014-05-21: 添加: 在线更新菜单，程序放到 七牛 上
- 2014-05-19: 添加: 9线程多任务下载空白章节(当数量大于25时) 修正: 一些小问题
- 2014-05-16: 修正: 显示页面上一页，下一页和所有章节的连贯性修正
- 2014-05-13: 修正: qidian 章节txt 的地址算法: Author N = 1 + bookid % 8
- 2014-05-12: 添加 easou zhuishushenqi 的在线预览支持和zhuishushenqi的下载，后期准备完善换源功能
- 2014-04-25: 看书界面添加调整字体大小菜单，默认值19
- 2014-04-21: 界面细微调整，在书籍信息里添加修改isEnd字段
- 2014-04-18: 添加: Agent头部来获取yahoo正确搜索结果链接 注:原生头部和IE8都会导致追踪链接
- 2014-04-17: 添加: 在搜索界面的菜单中添加快速搜索功能，以后可以加入更多搜索引擎
- 2014-04-14: 小修复: 每章节第一个段落头部添加空白
- 2014-04-13: 菜单添加生成txt功能，路径 /sdcard/fox.txt
- 2014-04-07: 发布Android版，和FoxBook共用同一数据库文件，放在sdcard根目录
- ...: 懒得写了，就这样吧
