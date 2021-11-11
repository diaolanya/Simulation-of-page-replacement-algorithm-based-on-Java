package homework.os;

import homework.os.algorithm.FIFO;
import homework.os.algorithm.LFU;
import homework.os.algorithm.LRU;

public class Choosealgorithm {
    public static Algorithm getInstantiate(String id,int page_size, int[] array){
        Algorithm alg = null;
        if(id == "FIFO"){
            return alg = new FIFO(page_size,array);
        }else if(id == "LRU"){
            return alg = new LRU(page_size,array);
        }else if(id == "LFU"){
            return alg = new LFU(page_size,array);
        }else{
            return null;
        }
    }
}
