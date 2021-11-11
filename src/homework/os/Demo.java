package homework.os;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Process {
    String id;
    int array_size ;
    int page_size = 5;
    int arriveTime ;
    int execTime ;
    int startTime ;
    int[] array = null;
}

public class Demo {
    HashMap<String, Process> map;  //进程名,进程对象
    Process process = null;
    private String algorithm;
    private static final int memory = 30;
    ArrayList<Process> processA = null;  //正在运行的进程
    ArrayList<Process> processB = null;  //就绪进程
    ArrayList<Process> processFinish = null;  //完成队列
    private static int time = 0; // 模拟时间

    public Demo(){
        this.map = new HashMap<>();
        this.processA = new ArrayList<>();
        this.processB = new ArrayList<>();
        this.processFinish = new ArrayList<>();
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.GetFileInformaion();
        System.out.println("收到数据:");
        demo.test(demo.map);
        System.out.println("");

        System.out.println("请选择算法");
        System.out.println("  1.FIFO");
        System.out.println("  2.LRU");
        System.out.println("  3.LFU");
        System.out.println("");

        Scanner sc = new Scanner(System.in);
        int choose = sc.nextInt();
        if(choose == 1){
            demo.algorithm = "FIFO";
        }else if(choose == 2){
            demo.algorithm = "LRU";
        }else if(choose == 3){
            demo.algorithm = "LFU";
        }else{
            System.out.println("error");
            System.exit(0);
        }

        for(String i:demo.map.keySet()){
            demo.processB.add(demo.map.get(i));  //将进程对象装载到就绪队列
        }

        while (true) {
            //进行判断进程是否已经执行完成
            if (demo.processA.size() > 0) {
                Process p = demo.processA.get(0);
                if (p.startTime + p.execTime <= time) {
                    System.out.println(" ");
                    System.out.println("进程 " + p.id +" 已经执行完成");
                    Algorithm c = Choosealgorithm.getInstantiate(demo.algorithm, p.page_size, p.array);
                    c.PageReplace();
                    demo.processA.remove(0);
                    demo.processFinish.add(p);
                }
            }

            //进程调度
            if (demo.processA.size() == 0){
                if(demo.processB.size() != 0){
                    Process p = demo.processB.get(0);
                    demo.processA.add(p);
                    demo.processB.remove(0);
                    p.startTime = time;

                    System.out.println(" ");
                    System.out.println("time>>>" + time);
                    System.out.println("正在执行的程序>>>" + " " +demo.processA.get(0).id);
                    System.out.print("等待执行的程序>>>" + " ");
                    for (int i = 0; i < demo.processB.size(); i++) {
                        System.out.print(demo.processB.get(i).id + " ");
                    }
                    System.out.println("");
                    System.out.print("已经执行完成的程序>>>" + " ");
                    for (int i = 0; i < demo.processFinish.size(); i++) {
                        System.out.print(demo.processFinish.get(i).id + " ");
                    }
                    System.out.println("");
                    System.out.println("--------------------------------------------");
                }
            }
            time++;
            if (demo.processA.size() < 1 && demo.processB.size() < 1) {
                break;
            }
        }
        System.out.println(" ");
        System.out.println("time>>>" + time);
        System.out.println("正在执行的程序>>>");
        System.out.println("等待执行的程序>>>" + " ");
        for (int i = 0; i < demo.processB.size(); i++) {
            System.out.print(demo.processB.get(i).id + " ");
        }
        System.out.print("已经执行完成的程序>>>" + " ");
        for (int i = 0; i < demo.processFinish.size(); i++) {
            System.out.print(demo.processFinish.get(i).id + " ");
        }
    }

    private HashMap<String, Process> GetFileInformaion(){
        try(InputStreamReader isr = new InputStreamReader(new FileInputStream(".\\1.txt"));
            BufferedReader bfr = new BufferedReader(isr)){
            String s;

            while((s = bfr.readLine())!=null){
                String reg = "([A-Z]),(\\d+),(\\d+),(\\d+),(\\d+)";
                Matcher matcher = Pattern.compile(reg).matcher(s);
                while(matcher.find()){
                    process = new Process();
                    process.id = matcher.group(1);
                    process.arriveTime = Integer.parseInt(matcher.group(3));
                    process.execTime = Integer.parseInt(matcher.group(4));
                    process.array_size = Integer.parseInt(matcher.group(5));
                    process.array = new int[process.array_size];
                }
                StringBuilder sb = new StringBuilder();
                sb.append(reg);
                for(int i=0;i<process.array_size;i++){
                    sb.append(",(\\d+)");
                }
                Matcher matcher1 = Pattern.compile(sb.toString()).matcher(s);
                while(matcher1.find()){
                    for(int j=0;j< process.array_size;j++){
                        process.array[j] = Integer.parseInt(matcher1.group(6+j));
                    }
                }
                map.put(process.id, process);
            }
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
        return map;
    }

    void test(HashMap<String, Process> map){
        for(String i:map.keySet()){
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append("  ");
            sb.append(map.get(i).array_size);
            sb.append("  ");
            for(int j=0;j<map.get(i).array_size;j++){
                sb.append(map.get(i).array[j]);
                sb.append(" ");
            }
            System.out.println(sb);
        }
    }
}
