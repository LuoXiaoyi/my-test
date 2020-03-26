package com.myself;

/**
 * @author xiluo
 * @createTime 2019/2/10 13:31
 **/
public class Running {

    public static void main(String[] args) {
        for(int i = 0; i<8; ++i){
            new Thread(){
                private long j = 0;
                @Override
                public void run() {
                    while(true){
                        j++;
                    }
                }
            }.start();
        }
    }
}
