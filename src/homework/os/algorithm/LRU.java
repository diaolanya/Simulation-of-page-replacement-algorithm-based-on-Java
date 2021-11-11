package homework.os.algorithm;

import homework.os.Algorithm;

import java.util.HashMap;

class LRUNode {
    Integer key;
    Object value;
    LRUNode prev;
    LRUNode next;
    public LRUNode(Integer key, Object value){
        this.key = key;
        this.value = value;
    }
}

public class LRU implements Algorithm {
    private HashMap<Integer, LRUNode> map;
    private LRUNode head;
    private LRUNode tail;

    private final int[] array;
    private static int count = 0;
    private int page_size;

    public LRU(int page_size, int[] array) {
        this.page_size = page_size;
        this.array = array;
        this.map = new HashMap<>();
    }

    public void PageReplace() {
        count = 0;
        if (page_size <= 0 || array == null || array.length == 0) {
            throw new IllegalArgumentException("The parameter is invalid!");
        }
        for(int element:array){
            if(map.containsKey(element)){
                //已经存在的
                LRUNode node = map.get(element);
                remove(node);
                //放在头部
                setHead(node);
                print(map);
            }else if(map.size()<page_size){
                //元素个数少于page_size,直接放入
                int empty = findEmptyMemory();
                LRUNode node = new LRUNode(element, empty);
                map.put(element, node);
                //放在头部
                setHead(node);
                print(map);
                count++;
            }else{
                //元素不存在，并且超出页面的大小，需要移出最久未使用的元素，加入新元素
                int value = (int)map.get(tail.key).value;
                map.remove(tail.key);
                remove(tail);

                LRUNode node = new LRUNode(element, value);
                map.put(element, node);
                setHead(node);
                count++;
                print(map);
            }
        }
        System.out.println("缺页次数>>>" + count);
        System.out.println("缺页率>>>" + (double)count/array.length);
    }

    int findEmptyMemory(){
        if(map.size()<5){
            return map.size();
        }
        return -1;
    }

    private void print(HashMap<Integer, LRUNode> map){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i : map.keySet()){
            sb.append(i+", ");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        System.out.println(sb.toString());

        StringBuilder sb1 = new StringBuilder();
        for(int i : map.keySet()){
            sb1.append(map.get(i).value + ", ");
        }
        sb1.deleteCharAt(sb1.length()-1);
        sb1.deleteCharAt(sb1.length()-1);
        System.out.println(sb1.toString());
    }

    //head指针后移
    private void setHead(LRUNode node){
        if(head!=null){
            node.next = head;
            head.prev = node;
        }
        head = node;
        if(tail==null){
            tail = node;
        }
    }

    //双向链表
    private void remove(LRUNode node){
        if(node.prev!=null){
            node.prev.next = node.next;
        }else{
            head = node.next;
        }
        if(node.next!=null){
            node.next.prev=node.prev;
        }else{
            tail = node.prev;
        }
        node.next = null;
        node.prev = null;
    }
}
