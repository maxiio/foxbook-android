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

-   [源码工程](https://github.com/linpinger/foxbook-android)

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


**更新日志:**

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
