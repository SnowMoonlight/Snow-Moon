// package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class APPv2 extends JFrame {
    static LocalDate today = LocalDate.now();// 将初始日期设置为今天

    JPanel calendarPanel;
    JPanel todoPanel;
    static JPanel todoEventListPanel;
    public APPv2(){
        super("Calendar App"); // 设置窗口标题
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置关闭操作
        setSize(800, 500); // 设置窗口大小
        setLocationRelativeTo(null); // 将窗口居中显示
        setVisible(true); // 显示窗口
        initMenuBar(); // 初始化菜单栏
        initCalendarPanel(); // 初始化日历界面组件
        inittodoPanel(); // 初始化待办事件面板
        setLayout(new GridLayout(1, 2));
        add(calendarPanel); // 添加日历面板到窗口
        add(todoPanel); // 添加待办事件面板到窗口
        revalidate();// 重新绘制面板
    }
    public static void main(String[] args) {
        new APPv2();
    }
    //初始化菜单栏
    private void initMenuBar() {
        // 菜单栏
        JMenuBar menuBar = new JMenuBar();// 设置菜单栏
        JMenu menu = new JMenu("视图");// 设置菜单
        JButton showDayButton = new JButton("日");//菜单项
        JButton showWeekButton = new JButton("周");
        JButton showMonthsButton = new JButton("月");
        menu.add(showDayButton);
        menu.add(showWeekButton);
        menu.add(showMonthsButton);
        menuBar.add(menu);
        setJMenuBar(menuBar);
        showDayButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) (calendarPanel.getLayout());
            cl.show(calendarPanel, "Panel 1");
        });
        showWeekButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) (calendarPanel.getLayout());
            cl.show(calendarPanel, "Panel 2");
        });
        showMonthsButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) (calendarPanel.getLayout());
            cl.show(calendarPanel, "Panel 3");
        });
    }
    //初始化日历面板
    private void initCalendarPanel() {
        calendarPanel = new JPanel(new CardLayout());
        DayPanel dayPanel = new DayPanel();
        WeekPanel weekPanel = new WeekPanel();
        MonthPanel monthsPanel = new MonthPanel();
        // 日历面板添加月，周，日面板
        calendarPanel.add(dayPanel, "Panel 1");
        calendarPanel.add(weekPanel, "Panel 2");
        calendarPanel.add(monthsPanel, "Panel 3");
    }
    //初始化待办事件面板
    private void inittodoPanel() { 
        JPanel todoTitlePanel = new JPanel(new FlowLayout());
        JButton addTodoEventButton = new JButton("添加");
        todoTitlePanel.add(new JLabel("待办事项"));
        todoTitlePanel.add(new JLabel("                                  "));
        todoTitlePanel.add(addTodoEventButton);

        todoEventListPanel = new JPanel(new GridLayout(30, 1));
        JScrollPane tempTodoEventListPanel = new JScrollPane(todoEventListPanel);
        tempTodoEventListPanel.setPreferredSize(new Dimension(400, 200));

        JPanel schedulePanel = new JPanel();
        schedulePanel.add(new Label("日程详情"));
        todoPanel = new JPanel(new BorderLayout());
        todoPanel.add(todoTitlePanel, BorderLayout.NORTH);
        todoPanel.add(tempTodoEventListPanel, BorderLayout.CENTER);
        todoPanel.add(schedulePanel, BorderLayout.SOUTH);//

        // 添加待办事件按钮事件
        addTodoEventButton.addActionListener(e -> {new TodoEventFrame();});
    }
    public static void addButten(Data data){
        JButton button = new JButton(data.getName()+ " " + data.getStartTime());
        todoEventListPanel.add(button);
        todoEventListPanel.revalidate();
    }
}
//日面板
class DayPanel extends JPanel {
    JPanel hourPanel;
    double lineH;
    DayPanel() {
        setLayout(new BorderLayout());
        JPanel timeTitlePanel = new JPanel(new FlowLayout());
        JLabel time = new JLabel();
        setTimer(time);
        timeTitlePanel.add(time);
        add(timeTitlePanel, BorderLayout.NORTH);
        hourPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                graphics.drawLine(getWidth()/4, 0, getWidth()/4, getHeight());// 纵线
                for (int i = 0; i <= 24; i++) {
                    int y = i * getHeight() / 24;
                    graphics.drawLine(getWidth()/4, y, getWidth()/8*7, y);// 横线1/4 -> 7/8
                    String label = String.format("%02d:00", i);
                    graphics.drawString(label, getWidth()/8, y + 5);
                }
                graphics.setColor(Color.BLUE);
                Timer timer = new Timer(1000, e -> {
                    LocalTime todayhMf = LocalTime.now();// 将初始日期设置为今天
                    lineH=getHeight()/24*todayhMf.getHour() + getHeight()/(24*60*1.0)*todayhMf.getMinute();
                    hourPanel.revalidate();// 重新绘制面板
                    hourPanel.repaint();// 重绘面板
                });
                timer.start();
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.drawLine(getWidth()/4,(int)lineH, getWidth()/8*7, (int)lineH);
            }
        };
        hourPanel.setPreferredSize(new Dimension(0, 1200));
        JScrollPane scrollPane = new JScrollPane(hourPanel);
        Timer timer = new Timer(1000, e -> {
            if(lineH>300){
                scrollPane.getVerticalScrollBar().setValue((int)lineH-150);// 设置垂直滚动条初始位置
            }else{
                scrollPane.getVerticalScrollBar().setValue(0);// 设置垂直滚动条初始位置
            }
            hourPanel.revalidate();// 重新绘制面板
            hourPanel.repaint();// 重绘面板
        });
        timer.start();
        add(scrollPane, BorderLayout.CENTER);
    }
    // 设置时间标签显示
    private void setTimer(JLabel time) {
		final JLabel varTime = time;
		Timer timeAction = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				long timemillis = System.currentTimeMillis();
				// 转换日期显示格式
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				varTime.setText(df.format(new Date(timemillis)));
			}
		});
		timeAction.start();
	}
}
//周面板
class WeekPanel extends JPanel {
    WeekPanel() {
        setLayout(new BorderLayout());
        JPanel weekHourPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                for (int i = 0; i <7; i++) {
                    int x = (i+1) * getWidth()/8;
                    graphics.drawLine(x, 50, x, getHeight());// 纵线
                    String label = String.format(MonthPanel.daysOfWeek[i], i);
                    if(i==APPv2.today.getDayOfWeek().getValue()){
                        graphics.setColor(Color.RED);
                        graphics.drawString(label, x+getWidth()/16-10, 25);
                        // graphics.fillRect(x, 50,getWidth()/8, getHeight());// 绘制当前日期的背景色
                        graphics.setColor(Color.BLACK);
                    }else{
                        graphics.drawString(label, x+getWidth()/16-10, 25);
                    }
                }
                for (int i = 0; i <= 24; i++) {
                    int y = i * getHeight() / 24+50;//向下偏移50
                    graphics.drawLine(getWidth()/8, y, getWidth(), y);// 横线
                    String label = String.format("%02d:00", i);
                    graphics.drawString(label, getWidth()/32, y + 5);
                }
            }
        };
        weekHourPanel.setPreferredSize(new Dimension(500, 1200));
        JScrollPane scrollPane1 = new JScrollPane(weekHourPanel);
        add(scrollPane1, BorderLayout.CENTER);
    }
}
//月面板
class MonthPanel extends JPanel implements ActionListener {
    private String[] months = { "January", "February", "March", "April", "May", "June","July", "August", "September", "October", "November", "December" }; // 月份数组
    final static String[] daysOfWeek = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" }; // 星期数组
    private JPanel monthYearPanel; // 月份和年份控件面板
    private static JPanel monthsDayPanel;// 月份日历面板
    private static JLabel currentMonthLabel; // 当前月份标签
    private static JComboBox<String> yearComboBox; // 年份下拉框
    private static JComboBox<String> monthComboBox; // 月份下拉框
    private JButton previousMonthButton; // 上一个月按钮
    private JButton nextMonthButton; // 下一个月按钮
    private static JButton[] dayButton = new JButton[32];
    public MonthPanel() {
        // 月份和年份面板
        monthYearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        currentMonthLabel = new JLabel("Month Year"); // 当前月份标签
        previousMonthButton = new JButton("<"); // 上一个月按钮
        nextMonthButton = new JButton(">"); // 下一个月按钮
        monthComboBox = new JComboBox<>(months); // 月份下拉框
        yearComboBox = new JComboBox<>(); // 年份下拉框
        for (int i = LocalDate.now().getYear(); i <= LocalDate.now().getYear() + 10; i++) {
            yearComboBox.addItem(String.valueOf(i));
        }
        monthYearPanel.add(yearComboBox);
        monthYearPanel.add(monthComboBox);
        monthYearPanel.add(previousMonthButton);
        monthYearPanel.add(currentMonthLabel);
        monthYearPanel.add(nextMonthButton);
        previousMonthButton.addActionListener(this); // 监听按钮点击事件
        nextMonthButton.addActionListener(this); // 监听按钮点击事件
        yearComboBox.addActionListener(this); // 监听下拉框选择事件
        monthComboBox.addActionListener(this); // 监听下拉框选择事件
        // 月份日历面板
        monthsDayPanel = new JPanel(new GridLayout(7, 7));
        monthsDayPanel.setBackground(Color.WHITE);
        MonthPanel.updateCalendar(APPv2.today.getYear(), APPv2.today.getMonthValue() - 1,APPv2.today.getDayOfMonth()); // 月份在Calendar类中是从0开始的
        
        // 月面板
        setLayout(new BorderLayout());
        add(monthYearPanel, BorderLayout.NORTH);
        add(monthsDayPanel, BorderLayout.CENTER);
    }
    // 更新日历面板
    static void updateCalendar(int year, int month,int day) {
        // 清空日历面板
        monthsDayPanel.removeAll();
        // 添加星期标签
        for (String dayOfWeekLabel : daysOfWeek) {
            JLabel label = new JLabel(dayOfWeekLabel);
            label.setHorizontalAlignment(JLabel.CENTER);
            monthsDayPanel.add(label);
        }
        // 获取本月的第一天
        LocalDate firstDayOfMonth = LocalDate.of(year, month + 1, 1);// 月份在Calendar类中是从0开始的
        // 计算本月第一天是星期几
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();
        // 在本月第一天之前添加空标签
        if(dayOfWeek==7) dayOfWeek=0;
        for (int i = 0; i < dayOfWeek; i++) {
            monthsDayPanel.add(new JLabel(""));
        }
        // 添加本月的日期标签
        for (int tempDay = 1; tempDay <= firstDayOfMonth.lengthOfMonth(); tempDay++) {
            dayButton[tempDay] = new JButton(String.valueOf(tempDay));
            dayButton[tempDay].setHorizontalAlignment(JLabel.CENTER);
            monthsDayPanel.add(dayButton[tempDay]);
            if(tempDay==day){dayButton[tempDay].setForeground(Color.RED);} 
            else dayButton[tempDay].setEnabled(false);
        }
        // 在本月最后一天之后添加空标签
        for(int i = 0; i < 42 - firstDayOfMonth.lengthOfMonth() - dayOfWeek; i++) {
            monthsDayPanel.add(new JLabel(""));
        }
        // 更新当前月份的标签
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM");
        currentMonthLabel.setText(firstDayOfMonth.format(formatter));
        // 更新年份下拉框，月份下拉框
        yearComboBox.setSelectedItem(String.valueOf(year));
        monthComboBox.setSelectedIndex(month);
        // 刷新日历面板
        monthsDayPanel.revalidate();// 重新绘制面板
        monthsDayPanel.repaint();// 重绘面板
    }
    // 按钮点击事件处理
    @Override
    public void actionPerformed(ActionEvent e) {
         if (e.getSource() == previousMonthButton) {
             onPreviousMonth(); // 处理点击上一个月按钮事件
         } else if (e.getSource() == nextMonthButton) {
             onNextMonth(); // 处理点击下一个月按钮事件
         } else if (e.getSource() == yearComboBox || e.getSource() == monthComboBox) {
            int selectedYear = Integer.parseInt((String) yearComboBox.getSelectedItem());
            int selectedMonth = monthComboBox.getSelectedIndex();
            int DayMonth=0;
            if(selectedYear==APPv2.today.getYear() && selectedMonth==APPv2.today.getMonthValue()-1)
                DayMonth=APPv2.today.getDayOfMonth();
            updateCalendar(selectedYear, selectedMonth,DayMonth); // 处理年份或月份选择事件
         }
     }
    // 处理点击上一个月按钮事件
    private void onPreviousMonth() {
        int selectedYear = Integer.parseInt((String) yearComboBox.getSelectedItem());
        int selectedMonth = monthComboBox.getSelectedIndex();
        if (selectedMonth == 0) { // 如果是一月，上一个月要跳到去年的十二月
            selectedMonth = 11;
            selectedYear--;
        } else {
            selectedMonth--;
        }
        int DayMonth=0;
        if(selectedYear==APPv2.today.getYear() && selectedMonth==APPv2.today.getMonthValue()-1)
            DayMonth=APPv2.today.getDayOfMonth();
        updateCalendar(selectedYear, selectedMonth,DayMonth); // 处理年份或月份选择事件
    }
    // 处理点击下一个月按钮事件
    private void onNextMonth() {
        int selectedYear = Integer.parseInt((String) yearComboBox.getSelectedItem());
        int selectedMonth = monthComboBox.getSelectedIndex();
        if (selectedMonth == 11) { // 如果是十二月，下一个月要跳到明年的一月
            selectedMonth = 0; // 一月
            selectedYear++;
        } else {
            selectedMonth++;
        }
        int DayMonth=0;
        if(selectedYear==APPv2.today.getYear() && selectedMonth==APPv2.today.getMonthValue()-1)
            DayMonth=APPv2.today.getDayOfMonth();
        updateCalendar(selectedYear, selectedMonth,DayMonth); // 处理年份或月份选择事件
    }
    public static void addButton(Data data){
        int day=Integer.parseInt(data.getStartData());
        dayButton[day].setBackground(Color.RED);
        dayButton[day].setEnabled(true);
        monthsDayPanel.revalidate();// 重新绘制面板
        monthsDayPanel.repaint();// 重绘面板
    }

}
// 事件面板
class TodoEventFrame extends JFrame {
    String[] laberString = {"待办事项名称：","开始时间:","结束时间:","开始时期:","结束时期","内容:","备注:","地点:","颜色:","状态:"};
    JLabel[] laber =new JLabel[laberString.length];
    JTextField[] jTextField=new JTextField[laberString.length];
    public TodoEventFrame() {
        super("待办事项");
        setSize(300, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 设置关闭操作
        setVisible(true);
        JPanel laberPanel = new JPanel(new GridLayout(laberString.length+1,1));
        JPanel TextFieldPanel = new JPanel(new GridLayout(laberString.length+1,1,0,10));
        for (int i = 0; i < laberString.length; i++) { 
			laber[i] = new JLabel(laberString[i]);
            laberPanel.add(laber[i]);
            if(i==5){
            // }else if(i==5){
                jTextField[i] = new JTextField(20);
                TextFieldPanel.add(jTextField[i]);
            }else{
                jTextField[i] = new JTextField();
                TextFieldPanel.add(jTextField[i]);
            }
		}
        JButton noButton =new JButton("取消");
        JButton okButton =new JButton("保存");
        JButton moreButton =new JButton("更多设置");
        laberPanel.add(noButton);
        TextFieldPanel.add(okButton);
        // 按钮监视器
        okButton.addActionListener(e->{okButten();});
        noButton.addActionListener(e->{dispose();});
        moreButton.addActionListener(e->{moreSetting();});
        //添加面板
        setLayout(new BorderLayout());
        add(laberPanel,BorderLayout.WEST);
        add(TextFieldPanel,BorderLayout.CENTER);
        add(moreButton,BorderLayout.SOUTH);
    }
    public void moreSetting(){
    }
    //保存后续：日周月面板创建按钮，待办列表面板出现按钮
    public void okButten(){
        String name=jTextField[0].getText();
        String startTime=jTextField[1].getText();
        String endTime=jTextField[2].getText();
        String startdate=jTextField[3].getText();
        String endated=jTextField[4].getText();
        String content=jTextField[5].getText();
        String note=jTextField[6].getText();
        String location=jTextField[7].getText();
        Data data=new Data(name,startTime,endTime,startdate,endated,content,note,location);
        dispose();
        APPv2.addButten(data);
        // DayPanel.addButton(data);
        // WeekPanel.addButton(data);
        MonthPanel.addButton(data);
    }

}
//存储待办事件数据
class Data{
    private  String name;
    private  String startTime;//开始时间
    private  String endTime;//结束时间
    private  String startData;//起始日期
    private  String endData;//结束日期
    private  String content;//内容
    private  String note;//备注
    private  String location;//地点
    // private  Class color;//颜色
    // private  boolean status;//状态
    // , Class color, boolean status
    public Data(String name, String startTime, String endTime, String startData, String endData, String content, String note, String location) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startData = startData;
        this.endData = endData;
        this.content = content;
        this.note = note;
        this.location = location;
        // this.color = color;
        // this.status = status;
    }
    public void setName(String name) {this.name = name;}
    public void setStartTime(String startTime) {this.startTime = startTime;}
    public void setEndTime(String endTime) {this.endTime = endTime;}
    public void setStartData(String startData) {this.startData = startData;}
    public void setEndData(String endData) {this.endData = endData;}
    public void setContent(String content) {this.content = content;}
    public void setNote(String note) {this.note = note;}
    public void setLocation(String location) {this.location = location;}
    // public void setColor(Class color) {this.color = color;}
    // public void setStatus(boolean status) {this.status = status;}
    public String getName() {return name;}
    public String getStartTime() {return startTime;}
    public String getEndTime() {return endTime;}
    public String getStartData() {return startData;}
    public String getEndData() {return endData;}
    public String getContent() {return content;}
    public String getNote() {return note;}
    public String getLocation() {return location;}
    // public Class getColor() {return color;}
    // public boolean isStatus() {return status;}
}
