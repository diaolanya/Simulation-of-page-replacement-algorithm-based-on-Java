package homework.os.algorithm;

import homework.os.Algorithm;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

class Page{
    int id;
    int memory;

    public Page(int id,int memory){
        this.id = id;
        this.memory = memory;
    }

    public Page(){

    }

    public int getInfo(){
        return id;
    }
}

public class FIFO implements Algorithm{

    private final Queue<Page> queue = new LinkedList<>();
    private final int[] array;
    private Page[] a;
    private static int count = 0;
    private int page_size;

    public FIFO(int page_size, int[] array) {
        this.page_size = page_size;
        this.array = array;
    }

    public void PageReplace() {
        count = 0;
        if (page_size <= 0 || array == null || array.length == 0) {
            throw new IllegalArgumentException("The parameter is invalid!");
        }
        for (int element : array) {
            //页面中已经包含此元素
            if (queue.contains(element)) {
//                System.out.println(queue.toString());
                toArray();
            }
            //页面大小未达到页框数目，直接放入元素
            else if (queue.size() < page_size) {
                count++;
                int empty = findEmptyMemory();
                Page p = new Page(element,empty);
                queue.offer(p);
//                System.out.println(queue.toString());
                toArray();
            } else {
                //移出最早的元素,并加入新元素
                Page p = new Page();
                p = queue.poll();
                p.id = element;
                queue.offer(p);
                count++;
//                System.out.println(queue.toString());
                toArray();
            }
        }
        System.out.println("缺页次数>>>" + count);
        System.out.println("缺页率>>>" + (double)count/array.length);
    }

    private void toArray() {
        a = new Page[queue.size()];
        if(queue.size() > 0){
            Page[] p = queue.toArray(a);
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for(Page o : p){
                sb.append(o.id);
                sb.append(", ");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.deleteCharAt(sb.length()-1);
            sb.append("]");
            System.out.println(sb);
            StringBuilder sb1 = new StringBuilder();
            for(Page o : p){
                sb1.append(o.memory);
                sb1.append(", ");
            }
            sb1.deleteCharAt(sb1.length()-1);
            sb1.deleteCharAt(sb1.length()-1);
            System.out.println(sb1);
        }
    }

    int findEmptyMemory(){
        if(queue.size() < 5){
            return queue.size();
        }
        return -1;
    }
}
