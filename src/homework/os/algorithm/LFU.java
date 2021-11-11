package homework.os.algorithm;

import homework.os.Algorithm;

import java.util.*;

class LFUNode{
    Integer key;
    Integer value;
    public LFUNode(Integer key,Integer value){
        this.key = key;
        this.value = value;
    }
}

public class LFU implements Algorithm {
    private HashMap<Integer, LFUNode> map;
    private final int[] array;
    private static int count = 0;
    private int page_size;
    private final int[][] timeCount;  //保存key-count键值对

    public LFU(int page_size,int[] array){
        this.page_size = page_size;
        this.array = array;
        this.map = new HashMap<>();
        this.timeCount = new int[page_size][2];
    }

    public void PageReplace(){
        count = 0;
        if(page_size <= 0 || array == null || array.length == 0){
            throw new IllegalArgumentException("The parameter is invalid!");
        }
        for (int element:array){
            if(map.containsKey(element)){
                //已经存在的
                int loc = -1;
                for(int i = 0;i<page_size;i++){
                    if(timeCount[i][0] == element){
                        loc = i;
                    }
                }
                if(loc == -1){
                    throw new IllegalArgumentException("error");
                }
                timeCount[loc][1]++;  //count++
                print(map);
            }else if(map.size()<page_size){
                //元素个数少于page_size,直接放入
                count++;
                for(int i = 0;i<page_size;i++){
                    timeCount[i][1] = 0;
                }
                int empty = findEmptyMemory();
                LFUNode node = new LFUNode(element,empty);
                timeCount[empty][0] = element;
                timeCount[empty][1] = 1;
                map.put(element,node);
                print(map);
            }else{
                //元素不存在,并且超出页面大小,需要进行页面调度
                count++;
                int min = timeCount[0][1];
                int minKey = 0;
                int x;
                for(int i = 0;i<page_size;i++){
                    x = timeCount[i][1];
                    if(x<min){
                        min = x;
                        minKey = i;
                    }
                }
                LFUNode node = new LFUNode(element,map.get(timeCount[minKey][0]).value);
                map.remove(timeCount[minKey][0]);
                map.put(element,node);
                for(int i = 0;i<page_size;i++){
                    timeCount[i][1] = 0;
                }
                timeCount[minKey][0] = element;
                timeCount[minKey][1] = 1;
                print(map);
            }
        }
        System.out.println("缺页次数>>>" + count);
        System.out.println("缺页率>>>" + (double)count/array.length);
    }

    int findEmptyMemory(){
        if(map.size()<page_size){
            return map.size();
        }
        return -1;
    }

    private void print(HashMap<Integer, LFUNode> map){

        StringBuilder sb1 = new StringBuilder();
        for(int i : map.keySet()){
            sb1.append(i);
            sb1.append(" ");
            sb1.append(map.get(i).value);
            sb1.append("    ");
        }
        System.out.println(sb1.toString());
    }
}
