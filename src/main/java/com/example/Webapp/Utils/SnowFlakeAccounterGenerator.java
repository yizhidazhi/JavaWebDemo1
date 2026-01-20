package com.example.Webapp.Utils;

public class SnowFlakeAccounterGenerator {
    private  static  final long START_TIME = 1672531200000L;
    private static long sequence = 0;
    private static long lastTimestamp = -1;

    private static final long MIN_ACCOUNT = 1000000000L;
    private static final long MAX_ACCOUNT = 9999999999L;
    private  static final long RANGE = MIN_ACCOUNT + MAX_ACCOUNT - 1;

    public static synchronized String generateAccount(){
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("系统时钟异常");
        }
        if (timestamp == lastTimestamp) {
            sequence ++;
        }else{
            sequence = 0;
        }

        lastTimestamp = timestamp;

        long baseId = (timestamp - START_TIME)<<12|sequence;

        long accountNumber = (baseId % RANGE) + MIN_ACCOUNT;

        return String.valueOf(accountNumber);

    }

    public static void main(String[] args){
        System.out.println(generateAccount());
    }


}
